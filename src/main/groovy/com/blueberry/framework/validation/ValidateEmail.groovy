package com.blueberry.framework.validation

import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size
import java.lang.annotation.Retention
import java.lang.annotation.Target

import static java.lang.annotation.ElementType.*
import static java.lang.annotation.RetentionPolicy.*

/**
 * Validation constraint for email
 *
 * @author Ganeshji Marwaha
 * @since 9/8/14
 */
@NotNull
@Size(min = 7)
@Pattern(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}")
@Constraint(validatedBy = [])
@Target([METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER])
@Retention(RUNTIME)
public @interface ValidateEmail {

    String message() default "Email address doesn't look good"
    Class<?>[] groups() default []
    Class<? extends Payload>[] payload() default []
}