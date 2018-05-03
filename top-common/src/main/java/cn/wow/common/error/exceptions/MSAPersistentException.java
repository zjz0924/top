package cn.wow.common.error.exceptions;

import cn.wow.common.error.FaultCodeToMessage;

/**
 * Class thrown from the persisnt (Db) layer when en error occurs.
 * 
 * @author Ericsson MIEP Team
 * @since Nov 13, 2008
 */
@SuppressWarnings("serial")
public class MSAPersistentException extends MSARuntimeException
{
   /**
    * Constructor inhereted from the {@link MSARuntimeException}.
    * 
    * @param message What has gone wrong.
    * @param service What service did this happen in.
    */
   public MSAPersistentException(String message, String service)
   {
      super(FaultCodeToMessage.DATABASE_FAILURE, message, service);
   }

   /**
    * Constructor inhereted from the {@link MSARuntimeException}.
    * 
    * @param message What has gone wrong.
    * @param service What service did this happen in.
    * @param cause The real cause of the error.
    */
   public MSAPersistentException(String message, String service, Throwable cause)
   {
      super(FaultCodeToMessage.DATABASE_FAILURE, message, service, cause);
   }
}
