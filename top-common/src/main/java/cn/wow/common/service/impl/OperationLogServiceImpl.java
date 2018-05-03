package cn.wow.common.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.wow.common.dao.OperationLogDao;
import cn.wow.common.domain.OperationLog;
import cn.wow.common.service.OperationLogService;
import cn.wow.common.utils.operationlog.ClientInfo;
import cn.wow.common.utils.operationlog.OperationType;
import cn.wow.common.utils.operationlog.ServiceType;
import cn.wow.common.utils.operationlog.annotation.OperationLogIgnore;
import cn.wow.common.utils.pagination.PageHelperExt;

@Service
@Transactional
public class OperationLogServiceImpl implements OperationLogService {

	@Autowired
	private OperationLogDao operationLogDao;

	// make it thread-safe
	private static final Map<String, ClientInfo> clientInfoMap = new ConcurrentHashMap<String, ClientInfo>();

	public OperationLog selectOne(Long id) {
		return operationLogDao.selectOne(id);
	}

	@OperationLogIgnore
	public Long save(String theUserName, OperationType operationType, ServiceType serviceType, String jsonDetail) {
		OperationLog operationLog = null;
		try{
			String clientIp = null;
			String userAgent = null;
			String theUser1 = theUserName;
			ClientInfo clientInfo = getClientInfoByUserName(theUserName);
			clientIp = clientInfo.getClientIp();
			userAgent = clientInfo.getUserAgent();

			operationLog = new OperationLog();
			operationLog.setUserName(theUser1);
			operationLog.setType(serviceType.getDisplayName());
			operationLog.setTime(new Date());
			operationLog.setClientIp(clientIp);
			operationLog.setUserAgent(userAgent);
			operationLog.setOperation(operationType.getDisplayName());
			if (StringUtils.isNotBlank(jsonDetail)) {
				operationLog.setDetail(jsonDetail);
			}
			
			operationLogDao.insert(operationLog);
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
		
		return operationLog.getId();
	}

	@OperationLogIgnore
	public void createOrUpdateUserClientInfo(String userName, ClientInfo clientInfo) {
		if (userName != null && userName.trim().length() > 0 && clientInfo != null) {
			// HashMap will overwrite the clientInfo record if the userName already exists
			clientInfoMap.put(userName, clientInfo);
		}
	}

	public ClientInfo getClientInfoByUserName(String userName) {
		if (userName == null) {
			return null;
		}
		return clientInfoMap.get(userName);
	}

	public List<OperationLog> selectAllList(Map<String, Object> map) {
		PageHelperExt.startPage(map);
		return operationLogDao.selectAllList(map);
	}

}
