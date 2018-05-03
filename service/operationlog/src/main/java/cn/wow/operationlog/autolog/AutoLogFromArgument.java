package cn.wow.operationlog.autolog;

import javax.inject.Named;

import cn.wow.operationlog.OperatorInfo;

/**
 * create/update/delete(String clusterId, String userName [, String shortName] , JpaEntity entity)
 * 
 * create/update/delete(String clusterId, String userName [, String shortName] , Collection<? extends JpaEntity> entityList)
 * 
 * update/replace(String clusterId, String userName [, String shortName] , JpaEntity entity, Serializable oldpk)
 * 
 * delete(String clusterId, String userName [, String shortName] , Class<? extends JpaEntity> entityClass, Serializable oldpk)
 * 
 * - if clusterId/userName not appear, default 'UNKNOWN' set.
 * - shortName is optional. 
 * 
 * New feature from 20150303:
 *    - check if not-null "OperatorInfo" object exists in arguments.
 *      if exists, then use the object to provide related log entity.
 * 
 */
@Named
public class AutoLogFromArgument implements AutoLogAdapter
{

   private static final long serialVersionUID = 8436126410771324274L;

   public AutoLogFromArgument()
   {
   }

   /**
    * @param args
    * @return
    */
   @Override
   public String getUserName(Object[] args)
   {
      // the second parameter
      if (args.length > 0 && args[0] instanceof String)
      {
         return String.valueOf(args[0]);
      }

      // try to find operator
      if (args.length > 0)
      {
         for (int i = 0; i < args.length; i++)
         {
            if (args[i] != null && args[i] instanceof OperatorInfo)
            {
               OperatorInfo operator = (OperatorInfo)args[i];
               return operator.getUserName();
            }
         }
      }

      return "Unknown";
   }

}
