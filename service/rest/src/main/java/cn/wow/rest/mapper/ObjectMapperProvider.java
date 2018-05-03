package cn.wow.rest.mapper;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.fasterxml.jackson.databind.util.NameTransformer;

import cn.wow.rest.exception.Exceptions.InvalidParameterException;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ObjectMapperProvider implements ContextResolver<ObjectMapper>
{
   private static final Log LOGGER = LogFactory.getLog(JsonMappingExceptionMapper.class);

   private static ObjectMapper mapper = null;

   @Override
   public ObjectMapper getContext(Class<?> arg0)
   {
      return createDefaultMapper();
   }

   public static ObjectMapper getObjectMapper()
   {
      return mapper;
   }

   private synchronized ObjectMapper createDefaultMapper()
   {
      if (mapper == null)
      {
         mapper = new ObjectMapper();

         SerializerFactory factory = new NullStringSerializerFactory(null);
         mapper.setSerializerFactory(factory);
         mapper.setDefaultPrettyPrinter(MsaPrettyPrinter.instance);
         mapper.setDateFormat(new SimpleDateFormat("yyyyMMddHHmmss"));
         mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
         mapper.enable(MapperFeature.USE_ANNOTATIONS);
         mapper.enable(SerializationFeature.INDENT_OUTPUT);
         mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
         mapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);

         // customized
         SimpleModule myModule = new SimpleModule("MyModule", new Version(1, 0, 0, null, null, null));
         myModule.addDeserializer(String.class, new StringDeserializer());
         myModule.addDeserializer(Long.class, new LongDeserializer());
         myModule.addDeserializer(Integer.class, new IntegerDeserializer());
         mapper.registerModule(myModule);
      }
      return mapper;
   }

   private static class MsaPrettyPrinter extends DefaultPrettyPrinter
   {
      private static final long serialVersionUID = 4502648246434539255L;

      public static final MsaPrettyPrinter instance = new MsaPrettyPrinter();

      public MsaPrettyPrinter()
      {
         LOGGER.debug("Create arrayIndenter with SYSTEM_LINEFEED_INSTANCE.");
         _arrayIndenter = DefaultIndenter.SYSTEM_LINEFEED_INSTANCE;
      }
   }

   public class LongDeserializer extends StdDeserializer<Long>
   {

      private static final long serialVersionUID = -1625900970374960824L;

      public LongDeserializer()
      {
         super(Long.TYPE);
      }

      @Override
      public Long deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException
      {
         Long v = null;
         try
         {
            v = _parseLong(jp, ctxt);
         }
         catch (Exception e)// NOSONAR
         {
            LOGGER.error(e.getMessage());
            StringBuilder msg = new StringBuilder("Invalid Long value [" + jp.getText() + "]");
            msg.append(" in line " + jp.getTokenLocation().getLineNr());
            msg.append(" column " + jp.getTokenLocation().getColumnNr());
            msg.append(".");
            LOGGER.error(msg.toString());
            throw new InvalidParameterException(msg.toString());
         }
         return v;
      }
   }

   public class IntegerDeserializer extends StdDeserializer<Integer>
   {

      private static final long serialVersionUID = -1939439035060641225L;

      public IntegerDeserializer()
      {
         super(Integer.TYPE);
      }

      @Override
      public Integer deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException
      {
         Integer v = null;
         try
         {
            v = _parseInteger(jp, ctxt);
         }
         catch (Exception e)// NOSONAR
         {
            LOGGER.error(e.getMessage());
            StringBuilder msg = new StringBuilder("Invalid Integer value [" + jp.getText() + "]");
            msg.append(" in line " + jp.getTokenLocation().getLineNr());
            msg.append(" column " + jp.getTokenLocation().getColumnNr());
            msg.append(".");
            throw new InvalidParameterException(msg.toString());
         }
         return v;
      }
   }

   public class StringDeserializer extends JsonDeserializer<String>
   {
      @Override
      public String deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException
      {
         String s = parser.getText();
         return s != null ? s.trim() : s;
      }
   }

   public class NullStringBeanPropertyWriter extends BeanPropertyWriter
   {

      private static final long serialVersionUID = -615888533045746133L;

      public NullStringBeanPropertyWriter()
      {
         super();
      }

      public NullStringBeanPropertyWriter(BeanPropertyDefinition propDef, AnnotatedMember member, Annotations contextAnnotations,
            JavaType declaredType, JsonSerializer<?> ser, TypeSerializer typeSer, JavaType serType, boolean suppressNulls, Object suppressableValue)
      {
         super(propDef, member, contextAnnotations, declaredType, ser, typeSer, serType, suppressNulls, suppressableValue);
      }

      protected NullStringBeanPropertyWriter(BeanPropertyWriter base)
      {
         super(base);
      }

      protected NullStringBeanPropertyWriter(BeanPropertyWriter base, PropertyName name)
      {
         super(base, name);
      }

      protected NullStringBeanPropertyWriter(BeanPropertyWriter base, SerializedString name)
      {
         super(base, name);
      }

      @Override
      public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception
      {
         final Object value = (_accessorMethod == null) ? _field.get(bean) : _accessorMethod.invoke(bean);

         // Null handling is bit different, check that first
         if (value == null)
         {

            if (_declaredType == null || !String.class.equals(_declaredType.getRawClass()))
            {
               if (_nullSerializer != null)
               {
                  gen.writeFieldName(_name);
                  _nullSerializer.serialize(null, gen, prov);
               }
            }
            else
            {
               // 2016-02-04 rewrite null string method
               gen.writeFieldName(_name);
               gen.writeString("");
            }
            return;
         }
         // 2016-02-04 apply new pretty printer.
         if (!MsaPrettyPrinter.instance.equals(gen.getPrettyPrinter()))
         {
            gen.setPrettyPrinter(MsaPrettyPrinter.instance);
         }
         super.serializeAsField(bean, gen, prov);
      }
   }

   public class NullStringPropertyBuilder extends PropertyBuilder
   {

      public NullStringPropertyBuilder(SerializationConfig config, BeanDescription beanDesc)
      {
         super(config, beanDesc);
      }

      @Override
      protected BeanPropertyWriter buildWriter(SerializerProvider prov, BeanPropertyDefinition propDef, JavaType declaredType, JsonSerializer<?> ser,
            TypeSerializer typeSer, TypeSerializer contentTypeSer, AnnotatedMember am, boolean defaultUseStaticTyping) throws JsonMappingException
      {
         // do we have annotation that forces type to use (to declared type or its super type)?
         JavaType serializationType = findSerializationType(am, defaultUseStaticTyping, declaredType);

         // Container types can have separate type serializers for content (value / element) type
         if (contentTypeSer != null)
         {
            /*
             * 04-Feb-2010, tatu: Let's force static typing for collection, if there is type information for contents. Should work well (for JAXB
             * case); can be revisited if this causes problems.
             */
            if (serializationType == null)
            {
               serializationType = declaredType;
            }
            JavaType ct = serializationType.getContentType();
            // Not exactly sure why, but this used to occur; better check explicitly:
            if (ct == null)
            {
               throw new IllegalStateException("Problem trying to create BeanPropertyWriter for property '" + propDef.getName() + "' (of type "
                     + _beanDesc.getType() + "); serialization type " + serializationType + " has no content");
            }
            serializationType = serializationType.withContentTypeHandler(contentTypeSer);
            ct = serializationType.getContentType();
         }

         Object valueToSuppress = null;
         boolean suppressNulls = false;

         JsonInclude.Value inclV = _defaultInclusion.withOverrides(propDef.findInclusion());
         JsonInclude.Include inclusion = inclV.getValueInclusion();
         if (inclusion == JsonInclude.Include.USE_DEFAULTS)
         { // should not occur but...
            inclusion = JsonInclude.Include.ALWAYS;
         }

         switch (inclusion)
         {
         case NON_DEFAULT:
            // 11-Nov-2015, tatu: This is tricky because semantics differ between cases,
            // so that if enclosing class has this, we may need to values of property,
            // whereas for global defaults OR per-property overrides, we have more
            // static definition. Sigh.
            // First: case of class specifying it; try to find POJO property defaults
            JavaType t = (serializationType == null) ? declaredType : serializationType;
            if (_defaultInclusion.getValueInclusion() == JsonInclude.Include.NON_DEFAULT)
            {
               valueToSuppress = getPropertyDefaultValue(propDef.getName(), am, t);
            }
            else
            {
               valueToSuppress = getDefaultValue(t);
            }
            if (valueToSuppress == null)
            {
               suppressNulls = true;
            }
            else
            {
               if (valueToSuppress.getClass().isArray())
               {
                  valueToSuppress = ArrayBuilders.getArrayComparator(valueToSuppress);
               }
            }

            break;
         case NON_ABSENT: // new with 2.6, to support Guava/JDK8 Optionals
            // always suppress nulls
            suppressNulls = true;
            // and for referential types, also "empty", which in their case means "absent"
            if (declaredType.isReferenceType())
            {
               valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
            }
            break;
         case NON_EMPTY:
            // always suppress nulls
            suppressNulls = true;
            // but possibly also 'empty' values:
            valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
            break;
         case NON_NULL:// NOSONAR
            suppressNulls = true;
            // fall through
         case ALWAYS: // NOSONAR // default
         default:
            // we may still want to suppress empty collections, as per [JACKSON-254]:
            if (declaredType.isContainerType() && !_config.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS))
            {
               valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
            }
            break;
         }
         // 2016-02-04, override this writer.
         BeanPropertyWriter bpw = new NullStringBeanPropertyWriter(propDef, am, _beanDesc.getClassAnnotations(), declaredType, ser, typeSer,
               serializationType, suppressNulls, valueToSuppress);

         // How about custom null serializer?
         Object serDef = _annotationIntrospector.findNullSerializer(am);
         if (serDef != null)
         {
            bpw.assignNullSerializer(prov.serializerInstance(am, serDef));
         }
         // And then, handling of unwrapping
         NameTransformer unwrapper = _annotationIntrospector.findUnwrappingNameTransformer(am);
         if (unwrapper != null)
         {
            bpw = bpw.unwrappingWriter(unwrapper);
         }
         return bpw;
      }

   }

   public class NullStringSerializerFactory extends BeanSerializerFactory
   {

      private static final long serialVersionUID = -2132748751673509827L;

      public NullStringSerializerFactory(SerializerFactoryConfig config)
      {
         super(config);
      }

      @Override
      protected PropertyBuilder constructPropertyBuilder(SerializationConfig config, BeanDescription beanDesc)
      {
         return new NullStringPropertyBuilder(config, beanDesc);
      }

      @Override
      public SerializerFactory withConfig(SerializerFactoryConfig config)
      {
         if (_factoryConfig == config)
         {
            return this;
         }
         return new NullStringSerializerFactory(config);
      }
   }

}
