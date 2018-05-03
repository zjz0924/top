package cn.wow.common.error.exceptions;

import cn.wow.common.error.FaultCodeToMessage;

/**
 * MSARuntimeException, 
 * this exception is used when an exception should be thrown to the top layer,
 * and don't have to be caught in any other class.
 *  
 * @param service, the name of the web service
 * @param faultcode the fault as an integer, to mapped to a string.
 */
public class MSARuntimeException extends RuntimeException {
   private static final long serialVersionUID = 87732409240806187L;
   private String service;
   private int faultcode;
    
   public MSARuntimeException(int faultcode, String message, String service)
   {
      super(message);
      this.faultcode = faultcode;
      this.service = service;
   }
   
   public MSARuntimeException(int faultcode, String service)
   {
      this(faultcode,FaultCodeToMessage.getInstance().getFaultCodeMessage(faultcode),service);
   }
   /**
    * For message with place holder like {0} {1}...
    * Change service to the first parameter to avoid method overwrite issue.
    * @param service
    * @param faultcode
    * @param values used to fill in the message format place holder.
    */
   public MSARuntimeException(String service,int faultcode,Object... values )
   {
      this(faultcode,String.format(FaultCodeToMessage.getInstance().getFaultCodeMessage(faultcode),values),service);
   }
   
   public MSARuntimeException(String service,Throwable cause,int faultcode,Object... values )
   {
      this(faultcode,String.format(FaultCodeToMessage.getInstance().getFaultCodeMessage(faultcode),values),service,cause);
   }
   
	public MSARuntimeException(int faultcode, String service, Throwable cause) {
		this(faultcode, FaultCodeToMessage.getInstance().getFaultCodeMessage(
				faultcode), service,cause);
	}
	
	
   /**
    * Constructor to create a {@link MSAPersistentException}.
    * 
    * @param faultcode Code of what has gone wrong.
    * @param message Description of what has gone wrong.
    * @param service Where did it go wrong.
    * @param cause The real cause of the error.
    */
   public MSARuntimeException(int faultcode, String message, String service,
         Throwable cause)
   {
      super(message, cause);
      this.faultcode = faultcode;
      this.service = service;
   }
    
   public int getFaultCode()
   {
      return faultcode;
   }
    
   public String getService()
   {
      return service;
   }
}
