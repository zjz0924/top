package cn.wow.rest.mapper;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.ParamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.wow.rest.exception.ApiError;
import cn.wow.rest.exception.ApiErrorResponse;
import cn.wow.rest.exception.Exceptions.ApiException;
import cn.wow.rest.exception.Exceptions.BadRequestException;
import cn.wow.rest.exception.Exceptions.ConflictsException;
import cn.wow.rest.exception.Exceptions.DuplicatedException;
import cn.wow.rest.exception.Exceptions.DuplicatedFieldsException;
import cn.wow.rest.exception.Exceptions.InvalidParameterException;
import cn.wow.rest.exception.Exceptions.NotAllowedException;
import cn.wow.rest.exception.Exceptions.NotFoundException;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<Exception>
{
   private static final Logger logger = LoggerFactory.getLogger(ApiExceptionMapper.class);

   @Override
   public Response toResponse(Exception exception)
   {
      logger.error("exception.", exception);
      Response result = null;
      if (exception instanceof NotFoundException)
      {
         result = Response.status(Status.NOT_FOUND).entity(new ApiErrorResponse(new ApiError(exception.getMessage()))).build();
      }
      else if (exception instanceof ParamException)
      {
         String msg = "Invalid Parameter(s).";
         result = Response.status(Status.BAD_REQUEST).entity(new ApiErrorResponse(new ApiError(msg))).build();
      }
      else if (exception instanceof BadRequestException)
      {
         result = Response.status(Status.BAD_REQUEST).entity(new ApiErrorResponse(new ApiError(exception.getMessage()))).build();
      }
      else if (exception instanceof InvalidParameterException)
      {
         result = Response.status(Status.BAD_REQUEST).entity(new ApiErrorResponse(new ApiError(exception.getMessage()))).build();
      }
      else if (exception instanceof ConflictsException)
      {
         result = Response.status(Status.CONFLICT).entity(new ApiErrorResponse(new ApiError(exception.getMessage()))).build();
      }
      else if (exception instanceof DuplicatedException)
      {
         result = Response.status(Status.BAD_REQUEST).entity(new ApiErrorResponse(new ApiError(exception.getMessage()))).build();
      }
      else if (exception instanceof NotAllowedException)
      {
         result = Response.status(Status.FORBIDDEN).entity(new ApiErrorResponse(new ApiError(exception.getMessage()))).build();
      }
      else if (exception instanceof DuplicatedFieldsException)
      {
         result = Response.status(Status.BAD_REQUEST).entity(new ApiErrorResponse(new ApiError(exception.getMessage()))).build();
      }
      else if (exception instanceof ApiException)
      {
         result = Response.status(Status.BAD_REQUEST).entity(new ApiErrorResponse(new ApiError(exception.getMessage()))).build();
      }
      else if (exception instanceof WebApplicationException)
      {
         WebApplicationException e = (WebApplicationException)exception;
         result = Response.status(e.getResponse().getStatus())
               .entity(new ApiErrorResponse(new ApiError(e.getResponse().getStatus(), exception.getMessage()))).build();
      }
      else
      {
         String msg = exception.getMessage();
         result = Response.status(500).entity(new ApiErrorResponse(new ApiError(msg))).build();
      }
      return result;
   }
}
