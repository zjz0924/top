package cn.wow.operationlog;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import cn.wow.common.utils.JsonUtil;
import cn.wow.common.utils.PropertyUtil;
import cn.wow.common.utils.operationlog.OperationType;

public class OpLogDetailCoder
{
   private static final String COLLECTION = "collection";

   private static final String TYPE_SEPERATOR = "|";

   public final static String KEY_ENTITY = "ENTITY";

   public final static String KEY_OLDENTITY = "OLDENTITY";

   public final static String KEY_ERROR = "ERROR";

   public final static String KEY_ENTITYTYPE = "ENTITYTYPE";

   public final static String KEY_OPERATION = "OPERATION";
   
   public final static String KEY_FROM = "from";
   
   public final static String KEY_TO = "to";
   
   public final static String KEY_NAME = "name";

   /**
    * Encode the job detail information for a creation operation
    * 
    * @param obj
    *           : The entity to be created.
    * @param error
    *           : error information in case of error
    * @return JSON string characters
    */
   public static <T> String encodeCreationJobDetail(final T obj, final String error)
   {
      return getJobDetailString(obj, null, error, OperationType.CREATE);
   }

   /**
    * Encode the job detail information for a update operation
    * 
    * @param obj
    *           : The entity which will replace the old one.
    * @param oldobj
    *           : The entity which will be replaced.
    * @param error
    *           : error information in case of error
    * @return JSON string characters
    */
   public static <T> String encodeUpdateJobDetail(final T obj, final T oldObj, final String error)
   {
      return getJobDetailString(obj, oldObj, error, OperationType.UPDATE);
   }

   /**
    * Encode the job detail information for a update operation
    * 
    * @param obj
    *           : The entity which will replace the old one.
    * @param oldobj
    *           : The entity which will be replaced.
    * @param objectType
    *           : The specified object type.
    * @param error
    *           : error information in case of error
    * @return JSON string characters
    */
   public static <T> String encodeUpdateJobDetail(final T obj, final T oldObj, final String objectType, final String error)
   {
      return getJobDetailString(obj, oldObj, objectType, error, OperationType.UPDATE);
   }

   /**
    * Encode the job detail information for a deletion operation
    * 
    * @param obj
    *           : The entity to be deleted.
    * @param error
    *           : error information in case of error
    * @return JSON string characters
    */
   public static <T> String encodeDeletionJobDetail(final T obj, final String error)
   {
      return getJobDetailString(obj, null, error, OperationType.DELETE);
   }

   /**
    * Decode the job detail string characters to a map
    * 
    * @param detailStr
    *           : the job detail string characters
    * @param clazz
    *           : the class of the target entity.
    * @return
    */
   public static <T> Map<String, Object> decodeWholeMap(final String detailStr, Class<T> clazz)
   {
      Map<String, String> strMap = null;
      strMap = JsonUtil.fromJson(detailStr);
      return decodeWholeMap(strMap, clazz);
   }

   public static <T> Map<String, Object> decodeWholeMap(Map<String, String> strMap, Class<T> clazz)
   {
      Map<String, Object> objMap = new TreeMap<String, Object>();

      if (strMap != null)
      {
         String objType = decodeObjType(strMap);
         boolean isCollection = isCollection(objType);
         if (isCollection)
         {
            String className = getEntityClassName(objType);
            Class<?> clazz1;
            try
            {
               clazz1 = Class.forName(className);
               objMap.put(KEY_ENTITY, decodeEntity(strMap, clazz1, isCollection));
               objMap.put(KEY_OLDENTITY, decodeOldEntity(strMap, clazz1, isCollection));
            }
            catch (Exception e)
            {
               throw new RuntimeException(e);
            }
         }
         else
         {
            objMap.put(KEY_ENTITY, decodeEntity(strMap, clazz, isCollection));
            objMap.put(KEY_OLDENTITY, decodeOldEntity(strMap, clazz, isCollection));
         }
         objMap.put(KEY_ERROR, decodeError(strMap));
         objMap.put(KEY_ENTITYTYPE, objType);
         objMap.put(KEY_OPERATION, decodeOperation(strMap));
      }
      return objMap;
   }

   /**
    * Get the target entity from map
    * 
    * @param map
    * @return the target entity
    */
   public static Object getEntityFromMap(Map<String, Object> map)
   {
      return getValueFromMap(map, KEY_ENTITY);
   }

   /**
    * Get the old target entity from map
    * 
    * @param map
    * @return the old target entity
    */
   public static Object getOldEntityFromMap(Map<String, Object> map)
   {
      return getValueFromMap(map, KEY_OLDENTITY);
   }

   /**
    * Get the error information from map
    * 
    * @param map
    * @return the error information
    */
   public static String getErrorFromMap(Map<String, Object> map)
   {
      Object errObj = getValueFromMap(map, KEY_ERROR);
      return errObj == null ? "" : errObj.toString();
   }

   /**
    * Get the entity type from map
    * 
    * @param map
    * @return the entity type
    */
   public static String getEntityTypeFromMap(Map<String, Object> map)
   {
      Object entity = getValueFromMap(map, KEY_ENTITYTYPE);
      return entity == null ? "" : entity.toString();
   }

   /**
    * Get the operation type from map
    * 
    * @param map
    * @return the operation type
    */
   public static OperationType getOperationFromMap(Map<String, Object> map)
   {
      OperationType ret = OperationType.UNKNOWN;
      Object optObj = getValueFromMap(map, KEY_OPERATION);
      if (optObj != null)
      {
         ret = (OperationType)optObj;
      }
      return ret;
   }

   static <T> T getValueFromMap(Map<String, T> map, String key)
   {
      T v = null;
      if (map != null)
      {
         v = map.get(key);
      }
      return v;
   }

   /**
    * Get the entity by picking out from map and decoding the JSON string characters
    * 
    * @param map
    *           : the Map<String, String> map, in this map, both entity and old entity are stored as JSON string characters
    * @param clazz
    *           : the class of the target entity.
    * @param isCollection
    *           whether the JSON string is a collection's value
    * @return the target entity, null will be returned if exception occurred while JSON conversion
    */
   private static Object decodeEntity(final Map<String, String> map, Class<?> clazz, boolean isCollection)
   {
      final String objStr = getValueFromMap(map, KEY_ENTITY);
      return decodeObject(objStr, clazz, isCollection);
   }

   private static Object decodeObject(String json, Class<?> clazz, boolean isCollection)
   {
      Object ret = null;
      if (json != null && json.trim().length() > 0)
      {
         if (isCollection)
         {
            ret = JsonUtil.fromJson(json, ArrayList.class, clazz);
         }
         else
         {
            ret = JsonUtil.fromJson(json, clazz);
         }
      }
      return ret;
   }

   /**
    * Get the old entity by picking out from map and decoding the JSON string characters
    * 
    * @param map
    *           : the Map<String, String> map, in this map, both entity and old entity are stored as JSON string characters
    * @param clazz
    *           : the class of the target entity.
    * @param isCollection
    *           whether the JSON string is a collection's value
    * @return the old target entity, null will be returned if exception occurred while JSON conversion
    */
   private static Object decodeOldEntity(final Map<String, String> map, Class<?> clazz, boolean isCollection)
   {
      final String objStr = getValueFromMap(map, KEY_OLDENTITY);
      return decodeObject(objStr, clazz, isCollection);
   }

   /**
    * Get the error info from map
    * 
    * @param map
    *           : the Map<String, String> map, in this map, both entity and old entity are stored as JSON string characters
    * @return the error information, null will be returned if exception occurred while JSON conversion
    */
   private static String decodeError(final Map<String, String> map)
   {
      final String str = getValueFromMap(map, KEY_ERROR);
      return str;
   }

   /**
    * Get the Option info from map
    * 
    * @param map
    *           : the Map<String, String> map, in this map, both entity and old entity are stored as JSON string characters
    * @return the option information
    */
   private static OperationType decodeOperation(final Map<String, String> map)
   {
      final String str = getValueFromMap(map, KEY_OPERATION);
      return OperationType.valueOfString(str);
   }

   /**
    * Get the entity type from map
    * 
    * @param map
    *           : the Map<String, String> map, in this map, both entity and old entity are stored as JSON string characters
    * @return the entity type information
    */
   private static String decodeObjType(final Map<String, String> map)
   {
      final String str = getValueFromMap(map, KEY_ENTITYTYPE);
      return str;
   }

   private static <T> String getJobDetailString(final T newObj, final T oldObj, final String errInfo, final OperationType opt)
   {
      return getJobDetailString(newObj, oldObj, null, errInfo, opt);
   }

   private static <T> String getJobDetailString(final T newObj, final T oldObj, final String objectType, final String errInfo,
         final OperationType opt)
   {
      final Map<String, String> map = new TreeMap<String, String>();
      map.put(KEY_ENTITY, JsonUtil.toJson(newObj));
      map.put(KEY_OLDENTITY, JsonUtil.toJson(oldObj));
      map.put(KEY_ERROR, errInfo);
      String objType = objectType == null ? getObjType(newObj) : objectType;
      map.put(KEY_ENTITYTYPE, objType);
      map.put(KEY_OPERATION, opt.getDisplayName());
      final String str = JsonUtil.toJson(map);
      return str;
   }

   public static String getObjType(Object value)
   {
      if (value != null)
      {
         if (PropertyUtil.isCollectionField(value) && PropertyUtil.getValueAsCollection(value) != null)
         {
            return COLLECTION + TYPE_SEPERATOR + PropertyUtil.getFirstElement(PropertyUtil.getValueAsCollection(value)).getClass().getName();
         }
         else
         {
            return value.getClass().getName();
         }
      }
      return null;
   }

   public static String getEntityClassName(String entityType)
   {
      if (entityType != null && entityType.indexOf(TYPE_SEPERATOR) > 0)
      {
         return entityType.substring(entityType.indexOf(TYPE_SEPERATOR) + 1);
      }
      return entityType;
   }

   public static boolean isCollection(String entityType)
   {
      return entityType != null && entityType.startsWith(COLLECTION + TYPE_SEPERATOR);
   }

   public static String getJobDetailString(String newJson, String oldJson, String objType, OperationType opt)
   {
      Map<String, String> map = new TreeMap<String, String>();
      map.put(KEY_ENTITY, newJson);
      map.put(KEY_OLDENTITY, oldJson);
      map.put(KEY_ENTITYTYPE, objType);
      map.put(KEY_OPERATION, opt.getDisplayName());
      String str = JsonUtil.toJson(map);
      return str;
   }

   public static String getObjType(Class<?> entityClass, boolean isCollection)
   {
      if (entityClass != null)
      {
         if (isCollection)
         {
            return COLLECTION + TYPE_SEPERATOR + entityClass.getName();
         }
         else
         {
            return entityClass.getName();
         }
      }
      return null;
   }

}
