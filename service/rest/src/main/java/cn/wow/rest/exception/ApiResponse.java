package cn.wow.rest.exception;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"result", "created", "updated", "deleted", "message", "additionals"})
public class ApiResponse
{
   public enum RESULT {
      SUCCESSFUL("Successful"), FAILED("Failed");

      private String displayName;

      private RESULT(String displayName)
      {
         this.displayName = displayName;
      }

      public String getDisplayName()
      {
         return this.displayName;
      }

      @Override
      public String toString()
      {
         return getDisplayName();
      }
   }

   // result
   private RESULT result = null;

   private Map<String, String> additionals = null;

   private Integer created = null;

   private Integer updated = null;

   private Integer deleted = null;

   private Long id = null;

   private Integer existed = null;

   // message
   @JsonInclude(Include.NON_EMPTY)
   private String message = "";

   private List<ApiResultItem> results = null;

   public ApiResponse()
   {
      result = RESULT.SUCCESSFUL;
   }

   public ApiResponse(String message)
   {
      this();
      this.message = message;
   }

   public String getMessage()
   {
      return message;
   }

   public void setMessage(String message)
   {
      this.message = message;
   }

   public RESULT getResult()
   {
      return result;
   }

   public List<ApiResultItem> getResults()
   {
      return results;
   }

   public void setResults(List<ApiResultItem> results)
   {
      this.results = results;
   }

   public void setResult(RESULT result)
   {
      this.result = result;
   }

   public Map<String, String> getAdditionals()
   {
      return additionals;
   }

   public void setAdditionals(Map<String, String> additionals)
   {
      this.additionals = additionals;
   }

   public Integer getCreated()
   {
      return created;
   }

   public void setCreated(Integer created)
   {
      this.created = created;
   }

   public Integer getUpdated()
   {
      return updated;
   }

   public void setUpdated(Integer updated)
   {
      this.updated = updated;
   }

   public Integer getDeleted()
   {
      return deleted;
   }

   public void setDeleted(Integer deleted)
   {
      this.deleted = deleted;
   }

   public Long getId()
   {
      return id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   public Integer getExisted()
   {
      return existed;
   }

   public void setExisted(Integer existed)
   {
      this.existed = existed;
   }

   @JsonInclude(Include.NON_NULL)
   public static class ApiResultItem
   {
      private String type;

      private Integer created = null;

      private Integer updated = null;

      private Integer existed = null;

      private Integer ignored = null;

      @JsonInclude(Include.NON_EMPTY)
      private String message = "";

      public ApiResultItem(String type)
      {
         this.type = type;
      }

      public ApiResultItem()
      {
      }

      public String getType()
      {
         return type;
      }

      public void setType(String type)
      {
         this.type = type;
      }

      public Integer getCreated()
      {
         return created;
      }

      public void setCreated(Integer created)
      {
         this.created = created;
      }

      public Integer getUpdated()
      {
         return updated;
      }

      public void setUpdated(Integer updated)
      {
         this.updated = updated;
      }

      public Integer getExisted()
      {
         return existed;
      }

      public void setExisted(Integer existed)
      {
         this.existed = existed;
      }

      public Integer getIgnored()
      {
         return ignored;
      }

      public void setIgnored(Integer ignored)
      {
         this.ignored = ignored;
      }

      public String getMessage()
      {
         return message;
      }

      public void setMessage(String message)
      {
         this.message = message;
      }

   }

}
