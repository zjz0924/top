package cn.wow.common.utils.operationlog.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * ignore automatic operation log for some special case.
 *
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface OperationLogIgnore {

}
