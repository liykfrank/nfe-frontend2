package org.iata.bsplink.agencymemo.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.iata.bsplink.agencymemo.validation.CountryValidator;

/**
 * Checks the Agent.
 */
@Constraint(validatedBy = CountryValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CountryConstraint {

    /**
     * Default message.
     */
    String message() default "Invalid Country Code";

    /**
     * For user to customize the targeted groups.
     */
    Class<?>[] groups() default {};

    /**
     * For extensibility purposes.
     */
    Class<? extends Payload>[] payload() default {};
}