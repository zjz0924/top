package cn.wow.common.error;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 
 * This class converts fault code ids to readable Strings.
 * 
 * @author MIEP/Ericsson
 * 
 */
public class FaultCodeToMessage
{
   private static final String DEFAULT_ERROR = "Application Error.";

   private static volatile FaultCodeToMessage fcr = null;

   public static final int DATABASE_FAILURE = 1000;

   public static final int CONFIGURATION_FAULT = 1001;

   public static final int INVALID_INPUT_PARAMETER = 1002;

   public static final int TRANSACTION_FAILED_ROLLBACK = 1003;

   public static final int INTERNAL_COMMUNICATION_FAILED = 1004;

   public static final int RUNTIME_ERROR = 1005;

   public static final int XML_EXPORT_ERROR = 1006;

   public static final int XML_IMPORT_ERROR = 1007;

   public static final int ORCHESTRATOR_FAILURE = 1008;
   
   public static final int FORBIDDEN_ERROR = 1009;

   /**
    * Configuration related detail error
    */

   public static final int CONF_JOB_EXPORT_GENERAL_ERROR = 20001;

   public static final int CONF_JOB_EXPORT_WRITE_FILE_ERROR = 20002;

   public static final int CONF_STRUCTURE_EXPORT_ERROR = 20003;

   public static final int CONF_VERIFY_VALUES_ERROR = 20004;

   public static final int CONF_XML_DATA_IMPORT_ERROR = 20005;

   public static final int CONF_XML_SCHEMA_NOT_FOUND = 20006;

   public static final int CONF_XML_VALIDATION_ERROR = 20007;

   public static final int CONF_VERIFY_SERVER_SIZE_NOT_MATCH_ERROR = 20008;

   public static final int CONF_VERIFY_SERVER_NAME_NOT_MATCH_ERROR = 20009;

   public static final int CONF_VERIFY_PARAMETER_NAME_NOT_MATCH_ERROR = 20010;

   public static final int CONF_VERIFY_DATA_ERROR = 20011;

   public static final int CONF_VERIFY_PARAMETER_STRUCTURE_ERROR = 20012;

   public static final int CONF_XML_STRUCTURE_IMPORT_ERROR = 20013;

   public static final int CONF_LEGACY_XML_STRUCTURE_IMPORT_ERROR = 20014;

   public static final int CONF_SERVER_JOB_NOT_FOUND = 20015;

   public static final int CONFIG_PARAMETER_NOT_FOUND = 20016;

   public static final int CONFIG_PARAMETER_NOT_FOUND_FOR_SERVER = 20017;

   public static final int CONFIG_GROUP_NOT_FOUND = 20018;

   public static final int GONFIG_GROUP_NOT_DYNAMIC = 20019;

   public static final int CONFIG_SERVER_NOT_EXISTS = 20020;

   public static final int ADD_CONFIG_GROUP_SUBGROUP_NOT_MATCH_TYPE_ERROR = 20021;

   public static final int ADD_CONFIG_PARAMETER_NOT_MATCH_TYPE_ERROR = 20022;

   public static final int CONFIG_SERVER_NOT_FOUND = 20023;

   public static final int CHANGE_CONFIG_SERVER_DISPLAY_NAME_ERROR = 20024;

   public static final int MOVE_PARAMTER_OLD_GROUP_NAME_EMPTY_ERROR = 20025;

   public static final int MOVE_PARAMTER_NEW_GROUP_NAME_EMPTY_ERROR = 20026;

   public static final int CONFIG_PARAMTER_NOT_FOUND_ERROR = 20027;

   /**
    * Default constructor initializing everyhting needed.
    */
   private FaultCodeToMessage()
   {
      loadProperties();
   }

   /**
    * 
    * Singleton pattern.
    * 
    * @return FaultCodeToMessage the uniuqe global instance
    * 
    **/
   public static FaultCodeToMessage getInstance()
   {
      if (fcr == null)
      {
         synchronized (FaultCodeToMessage.class)
         {
            if (fcr == null)
            {
               fcr = new FaultCodeToMessage();
            }
         }

      }
      return fcr;
   }

   /**
    * Loads properties from a file with the same name as this class simple name.
    */
   private void loadProperties()
   {
   }

   /**
    * The method accepts a fault code and returns the corresponding error message.
    * 
    * @param faultCode
    *           the fault code id
    * @return String the fault code message
    */
   public String getFaultCodeMessage(int faultCode)
   {
      return DEFAULT_ERROR;
   }
   
}
