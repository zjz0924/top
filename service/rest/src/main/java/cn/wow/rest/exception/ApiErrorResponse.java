package cn.wow.rest.exception;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ApiErrorResponse extends ApiResponse
{
   @JsonInclude(Include.NON_EMPTY)
   private List<ApiError> errors = new ArrayList<ApiError>();

   public ApiErrorResponse(ApiError error)
   {
      errors.add(error);
      this.setResult(null);
   }

   public ApiErrorResponse(List<ApiError> errors)
   {
      super();
      this.errors = errors;
      this.setResult(null);
   }

   public List<ApiError> getErrors()
   {
      return errors;
   }

   public void setErrors(List<ApiError> errors)
   {
      this.errors = errors;
   }

   public void addError(ApiError error)
   {
      this.errors.add(error);
   }

}
