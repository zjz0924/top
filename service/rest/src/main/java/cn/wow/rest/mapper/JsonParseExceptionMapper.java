package cn.wow.rest.mapper;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParseException;

import cn.wow.rest.exception.ApiError;
import cn.wow.rest.exception.ApiErrorResponse;
import cn.wow.rest.exception.Exceptions.ApiException;

@Provider
@Priority(Priorities.USER)
public class JsonParseExceptionMapper implements ExceptionMapper<JsonParseException>
{
   private static final Log LOGGER = LogFactory.getLog(JsonMappingExceptionMapper.class);

   @Override
   public Response toResponse(JsonParseException exception)
   {
      LOGGER.error("exception.", exception);
      String msg = "Invalid JSON format.";
      Throwable t = exception.getCause();
      if (t instanceof ApiException)
      {
         msg = t.getMessage();
      }
      return Response.status(Response.Status.BAD_REQUEST).entity(new ApiErrorResponse(new ApiError(400, msg))).build();
   }
}
