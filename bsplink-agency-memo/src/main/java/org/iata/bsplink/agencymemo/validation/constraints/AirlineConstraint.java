package org.iata.bsplink.agencymemo.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.iata.bsplink.agencymemo.validation.AirlineValidator;

/**
 * Checks the Agent.
 */
@Constraint(validatedBy = AirlineValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AirlineConstraint {

    /**
     * Default message.
     */
    String message() default "Invalid Airline Code";

    /**
     * For user to customize the targeted groups.
     */
    Class<?>[] groups() default {};

    /**
     * For extensibility purposes.
     */
    Class<? extends Payload>[] payload() default {};
}