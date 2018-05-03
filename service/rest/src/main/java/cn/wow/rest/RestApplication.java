package cn.wow.rest;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import cn.wow.rest.mapper.ApiExceptionMapper;
import cn.wow.rest.mapper.ConstraintViolationExceptionMapper;
import cn.wow.rest.mapper.JsonMappingExceptionMapper;
import cn.wow.rest.mapper.JsonParseExceptionMapper;
import cn.wow.rest.mapper.JsonValidationFilter;
import cn.wow.rest.mapper.ObjectMapperProvider;

/**
 * 用来注册功能类
 * @author ejunzzh
 * 2017-07-12
 */
public class RestApplication extends ResourceConfig
{
   public RestApplication()
   {
      // 异常处理
      register(JsonParseExceptionMapper.class);
      register(JsonMappingExceptionMapper.class);
      register(ApiExceptionMapper.class);
      
      // 格式化处理
      register(ObjectMapperProvider.class);
      // 约束违返处理
      register(ConstraintViolationExceptionMapper.class);
      
      // Json格式检查（数据是否为空等）
      register(JsonValidationFilter.class);

      register(JacksonFeature.class);
      register(MultiPartFeature.class);

      property(ServerProperties.FEATURE_AUTO_DISCOVERY_DISABLE, false);
   }

}
