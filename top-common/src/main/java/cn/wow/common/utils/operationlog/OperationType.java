package cn.wow.common.utils.operationlog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public enum OperationType implements Serializable {

   CREATE("新建", LogLevel.ONLYNEW), UPDATE("编辑", LogLevel.BOTH),
   DELETE("删除", LogLevel.ONLYOLD), 
   LOGIN("登录"), LOGOUT("登出"), MOVE("移动"), EXAMINE("审核"), APPLY("申请"), TRANSMIT("下达任务"), APPROVE("审批任务"),  APPLY_INFO("信息修改申请"),
   APPLY_RESULT("试验结果修改申请"), UPLOAD_ATL("上传图谱结果"), UPLOAD_PF("上传型式结果"),SEND("结果发送"), CONFIRM("结果确认"), COMPARE("结果对比"),END("中止申请"),
   SEND_COST("费用单发送"), EXPORT("导出"), IMPORT("导入"),
   // will not use in code
   UNKNOWN("Unknown");

   private String displayName;
   
   private LogLevel logLevel;

   OperationType(String displayName)
   {
      this.displayName = displayName;
      this.logLevel = LogLevel.NONE;
   }

   OperationType(String displayName, LogLevel logLevel)
   {
      this.displayName = displayName;
      this.logLevel = logLevel;
   }

   public String getDisplayName()
   {
      return displayName;
   }
   
   public boolean logBoth()
   {
      return logLevel == LogLevel.BOTH;
   }

   public boolean onlyLogOld()
   {
      return logLevel == LogLevel.ONLYOLD;
   }

   public boolean onlyLogNew()
   {
      return logLevel == LogLevel.ONLYNEW;
   }

   public static OperationType valueOfString(String name)
   {
      OperationType opt = UNKNOWN;
      for (OperationType item : OperationType.values())
      {
         String n = name != null ? name.trim() : "";
         if (n.equalsIgnoreCase(item.getDisplayName()) || n.equalsIgnoreCase(item.name()))
         {
            opt = item;
            break;
         }
      }
      return opt;
   }
   
   private static enum LogLevel
   {      
      NONE, ONLYOLD, ONLYNEW, BOTH
   }
   
	public static List<String> getAllType() {
		List<String> typeList = new ArrayList<String>();
		for (OperationType item : OperationType.values()) {
			typeList.add(item.getDisplayName());
		}
		return typeList;
	}

}
