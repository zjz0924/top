package cn.wow.rest.mapper;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Priority;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.wow.rest.exception.ApiError;
import cn.wow.rest.exception.ApiErrorResponse;

@Provider
@Priority(Priorities.USER)
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException>
{
   public static final String PROPERTY_SEPERATOR = ".";

   private static final Log LOGGER = LogFactory.getLog(JsonMappingExceptionMapper.class);

   @Override
   public Response toResponse(ConstraintViolationException exception)
   {
      List<ApiError> errors = new ArrayList<ApiError>();
      if (exception.getConstraintViolations() != null)
      {
         for (ConstraintViolation<?> cv : exception.getConstraintViolations())
         {
            String property = cv.getPropertyPath().toString();
            Object v = cv.getInvalidValue();
            String s = String.valueOf(v);
            if (s != null && s.length() == 0)
            {
               s = "<EMPTY>";
            }
            if (property.indexOf(PROPERTY_SEPERATOR) > 0)
            {
               property = property.substring(property.lastIndexOf(PROPERTY_SEPERATOR) + 1);
            }
            errors.add(new ApiError(property, s, cv.getMessage()));
         }

      }
      LOGGER.warn("validation error.", exception);
      return Response.status(Status.BAD_REQUEST).entity(new ApiErrorResponse(errors)).build();
   }

}
