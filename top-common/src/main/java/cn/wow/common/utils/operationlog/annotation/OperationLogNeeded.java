package cn.wow.common.utils.operationlog.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import cn.wow.common.utils.operationlog.OperationType;

/**
 * Used to mention operation type in operationlog
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface OperationLogNeeded {

   OperationType optType() default OperationType.UNKNOWN;

   Class<?> entityClass() default Object.class;

   int pkpos() default 0;

}
