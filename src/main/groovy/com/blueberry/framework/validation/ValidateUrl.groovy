package com.blueberry.framework.validation

import javax.validation.Constraint
import javax.validation.Payload
import java.lang.annotation.Retention
import java.lang.annotation.Target

import static java.lang.annotation.ElementType.*
import static java.lang.annotation.RetentionPolicy.*

/**
 * Validation constraint for Url
 *
 * @author Ganeshji Marwaha
 * @since 9/8/14
 */
@Constraint(validatedBy = [UrlValidator.class])
@Target([METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
@Retention(RUNTIME)
public @interface ValidateUrl {

    String message() default "Malformed URL";
    Class<?>[] groups() default [];
    Class<? extends Payload>[] payload() default [];

    String protocol() default "";
    String host() default "";
    int port() default -1;

}