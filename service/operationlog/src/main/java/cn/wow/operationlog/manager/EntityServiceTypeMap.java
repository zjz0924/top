package cn.wow.operationlog.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.wow.common.domain.Account;
import cn.wow.common.domain.Area;
import cn.wow.common.domain.Dictionary;
import cn.wow.common.domain.Material;
import cn.wow.common.domain.Menu;
import cn.wow.common.domain.Org;
import cn.wow.common.domain.Parts;
import cn.wow.common.domain.Role;
import cn.wow.common.domain.RoleGroup;
import cn.wow.common.domain.Vehicle;
import cn.wow.common.utils.operationlog.ServiceType;

public final class EntityServiceTypeMap {
	private EntityServiceTypeMap() {
		// not called
	}

	private static Map<String, ServiceType> typeMap = new HashMap<String, ServiceType>();
	// 实体类查询的方法
	private static Map<String, String> daoMap = new HashMap<String, String>();
	
	static {
		initMap();
	}

	static void initMap() {
		typeMap.clear();
		typeMap.put(Account.class.getName(), ServiceType.ACCOUNT);
		typeMap.put(Role.class.getName(), ServiceType.SYSTEM);
		typeMap.put(RoleGroup.class.getName(), ServiceType.SYSTEM);
		typeMap.put(Area.class.getName(), ServiceType.SYSTEM);
		typeMap.put(Org.class.getName(), ServiceType.SYSTEM);
		typeMap.put(Menu.class.getName(), ServiceType.SYSTEM);
		typeMap.put(Dictionary.class.getName(), ServiceType.SYSTEM);
		
		//DAO 类型
		daoMap.clear();
		daoMap.put(Account.class.getName(), "cn.wow.common.dao.AccountDao.selectOne");
		daoMap.put(Role.class.getName(), "cn.wow.common.dao.RoleDao.selectOne");
		daoMap.put(Area.class.getName(), "cn.wow.common.dao.AreaDao.selectOne");
		daoMap.put(Org.class.getName(), "cn.wow.common.dao.OrgDao.selectOne");
		daoMap.put(Menu.class.getName(), "cn.wow.common.dao.MenuDao.selectOne");
		daoMap.put(RoleGroup.class.getName(), "cn.wow.common.dao.RoleGroupDao.selectOne");
		daoMap.put(Dictionary.class.getName(), "cn.wow.common.dao.DictionaryDao.selectOne");
	}

	public static ServiceType getServiceType(Class<?> clazz) {
		if (clazz != null) {
			String className = clazz.getName();
			return getServiceType(className);
		}
		return null;
	}

	public static ServiceType getServiceType(String className) {
		return typeMap.get(className);
	}
	
	public static String getDaoType(Class<?> clazz) {
		if (clazz != null) {
			String className = clazz.getName();
			return daoMap.get(className);
		}
		return null;
	}

	public static boolean contains(ServiceType type) {
		for (ServiceType t : typeMap.values()) {
			if (t.equals(type)) {
				return true;
			}
		}
		return false;
	}

	public static List<String> getAllType() {
		List<String> typeList = new ArrayList<String>();
		for (ServiceType type : ServiceType.values()) {
			typeList.add(type.getDisplayName());
		}
		return typeList;
	}

}
