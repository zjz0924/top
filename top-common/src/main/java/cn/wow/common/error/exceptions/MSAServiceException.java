package cn.wow.common.error.exceptions;

/**
 * Business exception: Not-Allow, Not-Found, Conflicts, Invalid-Parameters, Internal-Error etc.
 *
 */
public class MSAServiceException extends RuntimeException
{

   private static final long serialVersionUID = -7932477648752671995L;

   public static final int NOT_ALLOW = 403;

   public static final int CONFLICTS = 409;

   public static final int NOT_FOUND = 404;

   public static final int INVALID_PARAMETER = 400;

   public static final int INTERNAL_ERROR = 500;

   private int faultCode = 0;

   public MSAServiceException(String message)
   {
      super(message);
   }

   public MSAServiceException(int faultCode, String message)
   {
      super(message);
      this.faultCode = faultCode;
   }

   public int getFaultCode()
   {
      return faultCode;
   }

}