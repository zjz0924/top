package cn.wow.rest.exception;

public class Exceptions
{

   private Exceptions()
   {

   }

   public static class ApiException extends RuntimeException
   {

      private static final long serialVersionUID = 1049984396058367302L;

      protected int code;

      public ApiException(int code, String msg)
      {
         super(msg);
         this.code = code;
      }

      public ApiException(String msg)
      {
         this(ApiError.ERROR, msg);
      }
   }

   public static class BadRequestException extends ApiException
   {

      private static final long serialVersionUID = -6191601426637999845L;

      public BadRequestException(int code, String msg)
      {
         super(code, msg);
      }

      public BadRequestException(String msg)
      {
         super(msg);
      }
   }

   public static class NotFoundException extends ApiException
   {

      private static final long serialVersionUID = -8599392153194991030L;

      public NotFoundException(int code, String msg)
      {
         super(code, msg);
      }

      public NotFoundException(String msg)
      {
         super(msg);
      }
   }

   public static class ConflictsException extends ApiException
   {

      private static final long serialVersionUID = -2245287743257521L;

      public ConflictsException(int code, String msg)
      {
         super(code, msg);
      }

      public ConflictsException(String msg)
      {
         super(msg);
      }
   }

   public static class DuplicatedException extends ApiException
   {

      private static final long serialVersionUID = -224528779434277521L;

      public DuplicatedException(int code, String msg)
      {
         super(code, msg);
      }

      public DuplicatedException(String msg)
      {
         super(msg);
      }
   }

   public static class NotAllowedException extends ApiException
   {

      private static final long serialVersionUID = -224528779434277521L;

      public NotAllowedException(int code, String msg)
      {
         super(code, msg);
      }

      public NotAllowedException(String msg)
      {
         super(msg);
      }
   }

   public static class DuplicatedFieldsException extends ApiException
   {

      private static final long serialVersionUID = -224528779434277521L;

      public DuplicatedFieldsException(int code, String msg)
      {
         super(code, msg);
      }

      public DuplicatedFieldsException(String msg)
      {
         super(msg);
      }
   }

   public static class InvalidParameterException extends ApiException
   {

      private static final long serialVersionUID = 4799487727088958739L;

      public InvalidParameterException(int code, String msg)
      {
         super(code, msg);
      }

      public InvalidParameterException(String msg)
      {
         super(msg);
      }
   }
}
