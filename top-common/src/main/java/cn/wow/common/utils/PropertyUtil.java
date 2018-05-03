package cn.wow.common.utils;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import org.apache.commons.beanutils.PropertyUtils;
import cn.wow.common.error.FaultCodeToMessage;
import cn.wow.common.error.exceptions.MSARuntimeException;

public class PropertyUtil
{
   public static final String SERVICE = "PropertyUtil";

   public static Object getProperty(Object o, String propertyName)
   {
      try
      {
         return PropertyUtils.getProperty(o, propertyName);
      }
      catch (Exception e)
      {
         throw new MSARuntimeException(FaultCodeToMessage.RUNTIME_ERROR, "can not get property[" + propertyName + "]'s value from Object["
               + o.getClass().getName() + "].", SERVICE, e);
      }
   }

   public static void setProperty(Object o, String propertyName, Object value)
   {
      try
      {
         PropertyUtils.setProperty(o, propertyName, value);
      }
      catch (Exception e)
      {
         throw new MSARuntimeException(FaultCodeToMessage.RUNTIME_ERROR, "can not set property[" + propertyName + "]'s value to Object["
               + o.getClass().getName() + "].", SERVICE, e);
      }
   }

   public static Object getPropertyValue(Object o, String propertyName)
   {
      try
      {
         return PropertyUtils.getSimpleProperty(o, propertyName);
      }
      catch (Exception e)
      {
         throw new MSARuntimeException(FaultCodeToMessage.RUNTIME_ERROR, "can not get property[" + propertyName + "]'s value from Object["
               + o.getClass().getName() + "].", SERVICE, e);
      }
   }

   public static String getValueAsString(Object o, String propertyName)
   {
      try
      {
         Object v = PropertyUtils.getSimpleProperty(o, propertyName);
         return String.valueOf(v);
      }
      catch (Exception e)
      {
         throw new MSARuntimeException(FaultCodeToMessage.RUNTIME_ERROR, "can not get property[" + propertyName + "]'s value from Object["
               + o.getClass().getName() + "].", SERVICE, e);
      }
   }

   public static void setPropertyValue(Object o, String propertyName, Object value)
   {
      try
      {
         PropertyUtils.setSimpleProperty(o, propertyName, value);
      }
      catch (Exception e)
      {
         throw new MSARuntimeException(FaultCodeToMessage.RUNTIME_ERROR, "can not set property[" + propertyName + "]'s value to Object["
               + o.getClass().getName() + "].", SERVICE, e);
      }
   }

   // copy spec property.
   public static void copyProperty(Object dest, Object src, String name)
   {
      try
      {
         PropertyUtils.setSimpleProperty(dest, name, PropertyUtils.getSimpleProperty(src, name));
      }
      catch (Exception e)
      {
         throw new MSARuntimeException(FaultCodeToMessage.RUNTIME_ERROR, "copy property[" + name + "] error.", SERVICE, e);
      }
   }

   // copy all properties
   public static void copyProperties(Object dest, Object src)
   {
      try
      {
         PropertyUtils.copyProperties(dest, src);
      }
      catch (Exception e)
      {
         throw new MSARuntimeException(FaultCodeToMessage.RUNTIME_ERROR, "copy all properties error.", SERVICE, e);
      }
   }

   // copy all properties except type is Collections
   public static void copyPropertiesExceptCollections(Object dest, Object src)
   {
      copyPropertiesExceptCollections(dest, src, false);
   }

   /**
    * copy all properties except collections.
    * 
    * @param dest
    * @param src
    * @param copyExistOnly
    *           copy the properties only the dest object contains.
    */
   public static void copyPropertiesExceptCollections(Object dest, Object src, boolean copyExistOnly)
   {
      try
      {
         PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(src);
         for (PropertyDescriptor pd : descriptors)
         {
            if (pd.getReadMethod() == null || pd.getWriteMethod() == null)
            {
               continue;
            }
            String propertyName = pd.getName();
            Object value = PropertyUtils.getSimpleProperty(src, propertyName);
            if (value != null && value instanceof Collection)
            {
               continue;
            }
            if (copyExistOnly)
            {
               if (isPropertyExist(dest, propertyName))
               {
                  PropertyUtils.setSimpleProperty(dest, propertyName, value);
               }

            }
            else
            {
               PropertyUtils.setSimpleProperty(dest, propertyName, value);
            }
         }
      }
      catch (Exception e)
      {
         throw new MSARuntimeException(FaultCodeToMessage.RUNTIME_ERROR, "copy all properties error.", SERVICE, e);
      }
   }

   // clear all collection fields.
   public static Object clearAllCollectionFields(Object o)
   {
      try
      {
         PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(o);
         for (PropertyDescriptor pd : descriptors)
         {
            if (pd.getReadMethod() == null || pd.getWriteMethod() == null)
            {
               continue;
            }
            String propertyName = pd.getName();
            Object value = PropertyUtils.getSimpleProperty(o, propertyName);
            if (isCollectionField(value))
            {
               PropertyUtils.setSimpleProperty(o, propertyName, null);
            }
         }
         return o;
      }
      catch (Exception e)
      {
         throw new MSARuntimeException(FaultCodeToMessage.RUNTIME_ERROR, "clear all Collection fields error.", SERVICE, e);
      }
   }

   // whether the value is a collection
   public static boolean isCollectionField(Object value)
   {
      return value != null && value instanceof Collection;
   }

   // whether the object value is a entity or entity collection
   private static boolean isEntityField(Object value)
   {
      boolean flag = true;
      // auto box
      if (value instanceof Byte || value instanceof Short || value instanceof Character || value instanceof Boolean || value instanceof Long
            || value instanceof Integer || value instanceof String || value instanceof Float || value instanceof Double || value instanceof Date
            || value instanceof Enum || value.getClass().isArray())
      {
         flag = false;
      }
      return flag;
   }

   // clear all Entity fields.
   public static Object clearAllEntityFields(Object o)
   {
      try
      {
         PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(o);
         for (PropertyDescriptor pd : descriptors)
         {
            if (pd.getReadMethod() == null || pd.getWriteMethod() == null)
            {
               continue;
            }
            String propertyName = pd.getName();
            Object value = PropertyUtils.getSimpleProperty(o, propertyName);
            if (value != null && isEntityField(value))
            {
               PropertyUtils.setSimpleProperty(o, propertyName, null);
            }
         }
         return o;
      }
      catch (Exception e)
      {
         throw new MSARuntimeException(FaultCodeToMessage.RUNTIME_ERROR, "clear all Entity fields error.", SERVICE, e);
      }
   }

   private static PropertyDescriptor getPropertyDescriptor(Object o, String propertyName)
   {
      PropertyDescriptor p = null;
      PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(o);
      for (PropertyDescriptor pd : descriptors)
      {
         String pName = pd.getName();
         if (pName.equals(propertyName))
         {
            p = pd;
            break;
         }
      }
      return p;
   }

   // check whether the property is exist.
   public static boolean isPropertyExist(Object o, String propertyName)
   {
      return getPropertyDescriptor(o, propertyName) != null;
   }

   // check whether the property has read method.
   public static boolean isPropertyCanRead(Object o, String propertyName)
   {
      return getPropertyDescriptor(o, propertyName) != null && getPropertyDescriptor(o, propertyName).getReadMethod() != null;
   }

   // check whether the property has write method.
   public static boolean isPropertyCanWrite(Object o, String propertyName)
   {
      return getPropertyDescriptor(o, propertyName) != null && getPropertyDescriptor(o, propertyName).getWriteMethod() != null;
   }

   public static Object getFirstElement(Collection<?> c)
   {
      if (c != null && !c.isEmpty())
      {
         return c.iterator().next();
      }
      return null;
   }

   public static Collection<?> getValueAsCollection(Object value)
   {
      if (isCollectionField(value))
      {
         Collection<?> c = (Collection<?>)value;
         return c;
      }
      return null;
   }

   public static boolean isBaseDataType(Class<?> clazz)
   {
      return (clazz.equals(String.class) || clazz.equals(Integer.class) || clazz.equals(Byte.class) || clazz.equals(Long.class)
            || clazz.equals(Double.class) || clazz.equals(Float.class) || clazz.equals(Character.class) || clazz.equals(Short.class)
            || clazz.equals(BigDecimal.class) || clazz.equals(BigInteger.class) || clazz.equals(Boolean.class) || clazz.equals(Date.class)
            || clazz.equals(java.sql.Date.class) || clazz.isPrimitive());
   }

}
