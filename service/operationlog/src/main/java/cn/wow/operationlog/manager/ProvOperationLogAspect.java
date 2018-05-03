package cn.wow.operationlog.manager;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.wow.common.domain.JpaEntity;
import cn.wow.common.service.OperationLogService;
import cn.wow.common.utils.JsonUtil;
import cn.wow.common.utils.PropertyUtil;
import cn.wow.common.utils.operationlog.OperationType;
import cn.wow.common.utils.operationlog.ServiceType;
import cn.wow.common.utils.operationlog.annotation.OperationLogIgnore;
import cn.wow.common.utils.operationlog.annotation.OperationLogNeeded;
import cn.wow.operationlog.OpLogDetailCoder;
import cn.wow.operationlog.autolog.AutoLogAdapter;

@Aspect
@Component
public class ProvOperationLogAspect {
	private static Logger logger = LoggerFactory.getLogger("ProvOperationLogAspect");

	private ThreadLocal<LogItem> threadLocal = new ThreadLocal<LogItem>();

	@Autowired
	private OperationLogService operLogService;

	@Autowired
	private AutoLogAdapter autoLogAdapter;

	private LogItemParser logItemParser;

	@Resource
	public SqlSessionTemplate sqlSessionTemplate;

	public ProvOperationLogAspect() {
		logItemParser = new LogItemParser();
	}

	@Pointcut("execution(* cn.wow.common.service.impl.*.persist*(..)) || execution(* cn.wow.common.service.impl.*.save*(..)) || execution(* cn.wow.common.service.impl.*.create*(..)) || execution(* cn.wow.common.service.impl.*.update*(..)) || execution(* cn.wow.common.service.impl.*.delete*(..)) || execution(* cn.wow.common.service.impl.*.remove*(..))")
	public void pointcut() {

	}

	@Pointcut("@annotation(cn.wow.common.utils.operationlog.annotation.OperationLogNeeded)")
	public void annotationPointcut() {

	}

	@Before("pointcut() || annotationPointcut()")
	public void doBefore(JoinPoint joinPoint) {
		try {
			OperationLogIgnore oli = getIgnoreAnnotation(joinPoint);
			if (oli != null) {
				// ignore these
				return;
			}
			OperationLogNeeded osn = getMethodAnnotation(joinPoint);
			LogItem logItem = logItemParser.parseBefore(joinPoint, osn);
			threadLocal.set(logItem);
		} catch (Exception e) {
			logger.error("can not create an operation log", e);
		}
	}

	@After("pointcut() || annotationPointcut()")
	public void doAfter(JoinPoint joinPoint) {
		try {
			OperationLogIgnore oli = getIgnoreAnnotation(joinPoint);
			if (oli != null) {
				// ignore these
				return;
			}
			Object[] args = joinPoint.getArgs();
			if (args == null || args.length == 0) {
				return;
			}

			LogItem logItem = threadLocal.get();
			if (logItem != null && logItem.logable()) {
				logItem.newJson = logItemParser.getNewJson(args, logItem);
				createOperationLog(logItem);
			}
		} catch (Exception e) {
			logger.error("can not create an operation log", e);
		} finally {
			threadLocal.remove();
		}
	}

	@AfterThrowing(value = "pointcut() || annotationPointcut()", throwing = "ex")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
	}

	private OperationLogNeeded getMethodAnnotation(JoinPoint joinPoint) {
		try {
			Method proxyMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
			Method soruceMethod = joinPoint.getTarget().getClass().getMethod(proxyMethod.getName(),
					proxyMethod.getParameterTypes());
			if (soruceMethod.isAnnotationPresent(OperationLogNeeded.class)) {
				OperationLogNeeded osn = soruceMethod.getAnnotation(OperationLogNeeded.class);
				return osn;
			}
		} catch (Exception e) {
			logger.error("error", e);
		}
		return null;
	}

	private OperationLogIgnore getIgnoreAnnotation(JoinPoint joinPoint) {
		try {
			Method proxyMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
			Method soruceMethod = joinPoint.getTarget().getClass().getMethod(proxyMethod.getName(),
					proxyMethod.getParameterTypes());
			if (soruceMethod.isAnnotationPresent(OperationLogIgnore.class)) {
				OperationLogIgnore oli = soruceMethod.getAnnotation(OperationLogIgnore.class);
				return oli;
			}
		} catch (Exception e) {
			logger.error("error", e);
		}
		return null;
	}

	private void createOperationLog(LogItem logItem) {
		String jsonDetail = logItem.jsonDetail();
		if (!StringUtils.isEmpty(jsonDetail)) {
			operLogService.save(logItem.userName, logItem.operationType, logItem.serviceType, jsonDetail);
		}
	}

	public static enum EntityFieldType {
		ENTITY, ENTITY_LIST, PRIMARY_KEY
	}

	/**
	 * recode all log items from arguments, annotation, mehod signature, etc.
	 */
	private class LogItem {
		private String userName;

		private String oldJson;

		private String newJson;

		private ServiceType serviceType;

		private OperationType operationType;

		private EntityFieldType entityFieldType;

		private Class<?> entityClass;

		private int entityArgumentPos = -1;

		boolean logable() {
			return serviceType != null && operationType != null && operationType != OperationType.UNKNOWN;
		}

		String jsonDetail() {
			String jsonDetail = null;
			String jobType = OpLogDetailCoder.getObjType(entityClass, isCollection());

			if (operationType.logBoth()) {
				jsonDetail = OpLogDetailCoder.getJobDetailString(newJson, oldJson, jobType, operationType);
			} else if (operationType.onlyLogNew()) {
				jsonDetail = OpLogDetailCoder.getJobDetailString(newJson, null, jobType, operationType);
			} else if (operationType.onlyLogOld()) {
				jsonDetail = OpLogDetailCoder.getJobDetailString(null, oldJson, jobType, operationType);
			}

			return jsonDetail;
		}

		boolean isCollection() {
			return entityFieldType == EntityFieldType.ENTITY_LIST;
		}
	}

	/**
	 * parse log items from methods which signature meets some specification
	 */
	private class LogItemParser {
		/**
		 * parse logitem from arguments before actual action
		 * 
		 * @param joinPoint
		 * @param osn
		 * @return
		 */
		public LogItem parseBefore(JoinPoint joinPoint, OperationLogNeeded osn) {
			LogItem logItem = new LogItem();

			if (joinPoint == null) {
				return logItem;
			}
			Object[] args = joinPoint.getArgs();
			if (args == null || args.length == 0) {
				return logItem;
			}

			if (!findByEntity(args, logItem) && !findByEntities(args, logItem) && !findByPK(args, logItem)
					&& !findByAnnoPK(args, osn, logItem)) {
				return logItem;
			}

			ServiceType serviceType = EntityServiceTypeMap.getServiceType(logItem.entityClass);
			if (serviceType == null) {
				logger.warn("can not find a suitable service type for " + logItem.entityClass.getName());
				return logItem;
			}
			logItem.serviceType = serviceType;

			if (osn != null) {
				// obtain operationType from method's annotation
				logItem.operationType = osn.optType();
			} else {
				// obtain operationType from method's signature
				logItem.operationType = getOperation(joinPoint, logItem.oldJson);
			}

			logItem.userName = autoLogAdapter.getUserName(args);

			return logItem;
		}

		/**
		 * build new object's json
		 * 
		 * @param args
		 * @return
		 */
		public String getNewJson(Object[] args, LogItem logItem) {
			int pos = logItem.entityArgumentPos;
			String result = null;
			if (pos >= 0) {
				if (logItem.entityFieldType == EntityFieldType.ENTITY_LIST) {
					Collection<?> entities = (Collection<?>) args[pos];
					if (entities != null && !entities.isEmpty()) {
						result = JsonUtil.toJson(entities);
					}
				} else if (logItem.entityFieldType == EntityFieldType.ENTITY) {
					Object entity = args[pos];
					if (entity != null) {
						result = JsonUtil.toJson(entity);
					}
				} else if (logItem.entityFieldType == EntityFieldType.PRIMARY_KEY) {
					Serializable pk = (Serializable) args[pos];
					Object entity = findEntity(logItem.entityClass, pk);

					if (entity != null) {
						result = JsonUtil.toJson(entity);
					}
				}
			}
			return result;
		}

		/**
		 * create/update/delete(JpaEntity firstJpaEntity)
		 * 
		 * update/replace(JpaEntity firstJpaEntity, Serializable followsOldPk)
		 * 
		 * - replace means delete then recreate, another case of update, usually
		 * used in pk-changing.
		 * 
		 * @param args
		 * @param result
		 * @return
		 */
		private boolean findByEntity(Object[] args, LogItem result) {
			JpaEntity entity = null;
			Serializable oldpk = null;
			int pos = getEntityPosition(args);
			if (pos >= 0) {
				entity = (JpaEntity) args[pos];
				result.entityClass = entity.getClass();
				result.entityFieldType = EntityFieldType.ENTITY;
				result.entityArgumentPos = pos;

				// first find oldpk in arguments
				if (pos < (args.length - 1)) {
					Object target = args[pos + 1];
					if (target instanceof Serializable && !(target instanceof JpaEntity)
							&& !PropertyUtil.isCollectionField(target)) {
						oldpk = (Serializable) target;
					}
				}

				// if not found before, use entity's pk
				if (oldpk == null) {
					oldpk = entity.getPrimaryKey();
				}

				if (oldpk != null) {
					// old entity may be null, like in creation
					Object oldEntity = findEntity(result.entityClass, oldpk);

					if (oldEntity != null) {
						result.oldJson = JsonUtil.toJson(oldEntity);
					}
				}
				return true;
			}
			return false;
		}

		/**
		 * create/update/delete(Collection<? extends JpaEntity>
		 * firstJpaEntityList)
		 * 
		 * @param args
		 * @param result
		 * @return
		 */
		@SuppressWarnings("unchecked")
		private boolean findByEntities(Object[] args, LogItem result) {
			Collection<? extends JpaEntity> entities = null;
			for (int i = 0; i < args.length; i++) {
				Object o = args[i];
				if (isJpaEntityCollection(o)) {
					entities = (Collection<? extends JpaEntity>) o;
					result.entityArgumentPos = i;
					break;
				}
			}

			if (entities != null && !entities.isEmpty()) {
				result.entityClass = PropertyUtil.getFirstElement(entities).getClass();
				result.entityFieldType = EntityFieldType.ENTITY_LIST;

				List<Object> entityList = new ArrayList<Object>();
				for (Object obj : entities) {
					Serializable id = ((JpaEntity) obj).getPrimaryKey();
					if (id != null) {
						Object oldEntity = findEntity(obj.getClass(), id);

						if (oldEntity != null) {
							entityList.add(oldEntity);
						}
					}
				}

				if (!entityList.isEmpty()) {
					result.oldJson = JsonUtil.toJson(entityList);
				}

				return true;
			}

			return false;
		}

		/**
		 * delete([SomeParam... aa, ] Class<? extends JpaEntity>
		 * firstJpaEntityClass, Serializable followsPk)
		 * 
		 * @param args
		 * @param result
		 * @return
		 */
		private boolean findByPK(Object[] args, LogItem result) {
			int pos = getClassPosition(args);
			if (pos >= 0 && pos < (args.length - 1)) {
				result.entityClass = (Class<?>) args[pos];

				Object target = args[pos + 1];
				Serializable id = null;
				if (target instanceof Serializable && !(target instanceof JpaEntity)
						&& !PropertyUtil.isCollectionField(target)) {
					id = (Serializable) target;
					result.entityFieldType = EntityFieldType.PRIMARY_KEY;
					result.entityArgumentPos = pos + 1;
				}

				if (result.entityClass != null && id != null) {
					Object oldEntity = findEntity(result.entityClass, id);

					if (oldEntity != null) {
						result.oldJson = JsonUtil.toJson(oldEntity);
					}
					return true;
				}
			}
			return false;
		}

		/**
		 * find log items from annotation info
		 * 
		 * @param args
		 * @param osn
		 * @param result
		 * @return
		 */
		private boolean findByAnnoPK(Object[] args, OperationLogNeeded osn, LogItem result) {
			if (osn == null) {
				return false;
			}
			if (!osn.entityClass().equals(Object.class) && osn.pkpos() >= 0) {
				result.entityClass = osn.entityClass();

				Serializable id = (Serializable) args[osn.pkpos()];

				result.entityFieldType = EntityFieldType.PRIMARY_KEY;
				result.entityArgumentPos = osn.pkpos();

				Object oldEntity = findEntity(result.entityClass, id);
				if (oldEntity != null) {
					result.oldJson = JsonUtil.toJson(oldEntity);
				}
				return true;
			}
			return false;
		}

		private int getEntityPosition(Object[] args) {
			for (int i = 0; i < args.length; i++) {
				Object o = args[i];
				if (o instanceof JpaEntity) {
					return i;
				}
			}
			return -1;
		}

		private boolean isJpaEntityCollection(Object obj) {
			if (PropertyUtil.isCollectionField(obj)) {
				Collection<?> c = (Collection<?>) obj;
				if (!c.isEmpty()) {
					for (Object item : c) {
						if (!(item instanceof JpaEntity)) {
							return false;
						}
					}
					return true;
				}
			}
			return false;
		}

		private int getClassPosition(Object[] args) {
			for (int i = 0; i < args.length; i++) {
				Object o = args[i];
				if (o instanceof Class) {
					return i;
				}
			}
			return -1;
		}

		private OperationType getOperation(JoinPoint joinPoint, String oldJson) {
			String targetMethod = joinPoint.getSignature().getName().toLowerCase();
			OperationType result = OperationType.UNKNOWN;
			
			if (targetMethod.contains("create") || targetMethod.contains("persist")) {
				result = OperationType.CREATE;
			} else if (targetMethod.contains("update") || targetMethod.contains("replace")) {
				result = OperationType.UPDATE;
			} else if (targetMethod.contains("delete") || targetMethod.contains("remove")) {
				result = OperationType.DELETE;
			} else if (targetMethod.contains("save")) {
				result = oldJson == null ? OperationType.CREATE : OperationType.UPDATE;
			}
			return result;
		}

		
		private Object findEntity(Class<?> clazz, Serializable pk) {
			String findMethod = "";
			try {
				findMethod = EntityServiceTypeMap.getDaoType(clazz);
				
				Object obj = sqlSessionTemplate.selectOne(findMethod, pk);
				return obj;
			} catch (Exception ex) {
				logger.error("can't find the entity, the method [" + findMethod + " ] does not exist.");
				return null;
			}
		}
	}

}
