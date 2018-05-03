package cn.wow.operationlog;

import java.io.Serializable;

public class OperatorInfo implements Serializable
{
   private static final long serialVersionUID = 1288011234942984617L;

   private String userName;
   
   public OperatorInfo()
   {
   }
   
   public OperatorInfo(String userName)
   {
      super();
      this.userName = userName;
   }
   
   public String getUserName()
   {
      return userName;
   }

   public void setUserName(String userName)
   {
      this.userName = userName;
   }
}
