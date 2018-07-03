package org.iata.bsplink.agentmgmt.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.iata.bsplink.agentmgmt.validation.ControlDigitValidator;

/**
 * Validates that the given string is numeric and ends in a calculated control digit.
 */
@Constraint(validatedBy = ControlDigitValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ControlDigitConstraint {

    /**
     * Default message.
     */
    String message() default "must be a numeric value ending with a control digit";

    /**
     * For user to customize the targeted groups.
     */
    Class<?>[] groups() default {};

    /**
     * For extensibility purposes.
     */
    Class<? extends Payload>[] payload() default {};
}
