package cn.wow.common.utils.operationlog;

import java.io.Serializable;

/**
 * 
 * An enum for the different ServiceType types that exists in the MSA
 * application.
 * 
 * @author MIEP/Ericsson
 *
 */
public enum ServiceType implements Serializable {
	ACCOUNT("用户管理"), 
	SYSTEM("系统管理"),
	APPLY("申请管理"),
	LAB("实验管理"),
	COST("费用管理"),
	TASK("任务管理");

	private String displayName;

	ServiceType(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public static ServiceType valueOfString(String type) {
		ServiceType st = null;
		for (ServiceType t : ServiceType.values()) {
			if (t.name().equalsIgnoreCase(type)) {
				st = t;
				break;
			}
		}
		return st;
	}

}
