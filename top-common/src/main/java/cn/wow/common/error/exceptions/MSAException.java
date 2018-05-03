package cn.wow.common.error.exceptions;

/**
 * TopLayer exception, this exception is used by the representation layer. 
 * This exception is mapped from any other exception thrown in MSA.
 */
public class MSAException extends Exception{
   private static final long serialVersionUID = 3425394396394024217L;
   private String service;
   private String errorMessage;
 
   public MSAException(String errorMessage, String message, String service)
   {
      super(message);
      this.errorMessage = errorMessage; 
      this.service = service;
   }
   
   /**
    * Constructor to create a {@link MSAException} with a cause.
    * 
    * @param message The message. This also represents the errorMessage.
    * @param service The service where it happend.
    * @param cause The real cause of the exception.
    */
   public MSAException(String message, String service, Throwable cause) {
      super(message, cause);
      
      this.errorMessage = message;
      this.service = service;
   }
   
   public String getErrorMessage()
   {
      return errorMessage;
   }
    
   public String getService()
   {
      return service;
   }
}

