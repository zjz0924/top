package cn.wow.rest.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ApiError
{
   public static final int ERROR = 1;

   public static final int WARNING = 2;

   public static final int INFO = 3;

   public static final int OK = 4;

   public static final int TOO_BUSY = 5;

   // internal use only
   private Integer code;

   // for validation
   @JsonInclude(Include.NON_EMPTY)
   private String field = "";

   // for validation
   @JsonInclude(Include.NON_EMPTY)
   private String currentValue = "";

   @JsonInclude(Include.NON_EMPTY)
   private String message = "";

   // not necessary field.
   @JsonInclude(Include.NON_EMPTY)
   private String solution = "";

   public ApiError(String message)
   {
      super();
      this.message = message;
   }

   public ApiError(String field, String currentValue, String message)
   {
      super();
      this.field = field;
      this.currentValue = currentValue;
      this.message = message;
   }

   public ApiError(Integer code, String message)
   {
      super();
      this.code = code;
      this.message = message;
   }

   public ApiError(Integer code, String field, String message, String solution)
   {
      super();
      this.code = code;
      this.field = field;
      this.message = message;
      this.solution = solution;
   }

   @JsonIgnore
   public Integer getCode()
   {
      return code;
   }

   public void setCode(Integer code)
   {
      this.code = code;
   }

   public String getCurrentValue()
   {
      return currentValue;
   }

   public void setCurrentValue(String currentValue)
   {
      this.currentValue = currentValue;
   }

   public String getField()
   {
      return field;
   }

   public void setField(String field)
   {
      this.field = field;
   }

   public String getMessage()
   {
      return message;
   }

   public void setMessage(String message)
   {
      this.message = message;
   }

   public String getSolution()
   {
      return solution;
   }

   public void setSolution(String solution)
   {
      this.solution = solution;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((code == null) ? 0 : code.hashCode());
      result = prime * result + ((field == null) ? 0 : field.hashCode());
      result = prime * result + ((message == null) ? 0 : message.hashCode());
      result = prime * result + ((solution == null) ? 0 : solution.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      ApiError other = (ApiError)obj;
      if (code == null)
      {
         if (other.code != null)
            return false;
      }
      else if (!code.equals(other.code))
         return false;
      if (field == null)
      {
         if (other.field != null)
            return false;
      }
      else if (!field.equals(other.field))
         return false;
      if (message == null)
      {
         if (other.message != null)
            return false;
      }
      else if (!message.equals(other.message))
         return false;
      if (solution == null)
      {
         if (other.solution != null)
            return false;
      }
      else if (!solution.equals(other.solution))
         return false;
      return true;
   }

}
