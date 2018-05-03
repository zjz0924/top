package cn.wow.common.error.exceptions;

import java.lang.reflect.Method;
import java.sql.SQLException;

import javax.xml.ws.soap.SOAPFaultException;

public class ExceptionUtil
{
   
   // extract the real error message from MSAException_Exception which is
   // throwed by webService client.
   public static String getRealErrorMessage(Throwable e)
   {
      String errorMessage = "";
      try
      {
         if (isSqlException(e))
         {
            return getSqlErrorMessage(e);
         }
         else if (isMSARuntimeException(e))
         {
            return getMSAErrorMessage(e);
         }

         errorMessage = e.getMessage();
         if (e instanceof SOAPFaultException)
         {
         }
         else if (e instanceof MSAException)
         {
            Method m = e.getClass().getMethod("getFaultInfo", null);
            Object t = m.invoke(e, null);
            Method m1 = t.getClass().getMethod("getErrorMessage", null);
            String error1 = (String)m1.invoke(t, null);
            errorMessage = error1;
         }
         return errorMessage;
      }
      catch (Exception ex)//NOSONAR
      {

      }
      return errorMessage;
   }
   
   private static boolean isSqlException(Throwable e)
   {
      Throwable tmp = e;
      for (int i = 0; i <= 10; i++)
      {
         if (null == tmp)
         {
            break;
         }
         else if (tmp instanceof SQLException)
         {
            return true;
         }
         tmp = tmp.getCause();
      }
      return false;
   }

   private static String getSqlErrorMessage(Throwable e)
   {
      Throwable tmp = e;
      for (int i = 0; i <= 10; i++)
      {
         if (null == tmp)
         {
            break;
         }
         else if (tmp instanceof SQLException)
         {
            return tmp.getMessage();
         }
         tmp = tmp.getCause();
      }
      return "Unknown error.";
   }

   private static boolean isMSARuntimeException(Throwable e)
   {
      Throwable tmp = e;
      for (int i = 0; i <= 10; i++)
      {
         if (null == tmp)
         {
            break;
         }
         else if (tmp instanceof MSARuntimeException)
         {
            return true;
         }
         tmp = tmp.getCause();
      }
      return false;
   }

   private static String getMSAErrorMessage(Throwable e)
   {
      Throwable tmp = e;
      for (int i = 0; i <= 10; i++)
      {
         if (null == tmp)
         {
            break;
         }
         else if (tmp instanceof MSARuntimeException)
         {
            return tmp.getMessage();
         }
         tmp = tmp.getCause();
      }
      return "Unknown error.";
   }


}
