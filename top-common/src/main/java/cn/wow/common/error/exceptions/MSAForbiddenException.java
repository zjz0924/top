package cn.wow.common.error.exceptions;

import cn.wow.common.error.FaultCodeToMessage;

/**
 * Class thrown from the persisnt (Db) layer when en error occurs.
 * 
 * @author Ericsson MIEP Team
 * @since Nov 13, 2008
 */
@SuppressWarnings("serial")
public class MSAForbiddenException extends MSARuntimeException
{
   public MSAForbiddenException(int faultcode, String message)
   {
      super(FaultCodeToMessage.FORBIDDEN_ERROR, message);
   }

}
