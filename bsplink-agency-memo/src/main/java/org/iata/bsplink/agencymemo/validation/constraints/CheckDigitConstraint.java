package org.iata.bsplink.agencymemo.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.iata.bsplink.agencymemo.validation.CheckDigitValidator;

/**
 * Checks Mod-7 Check-Digit.
 */
@Constraint(validatedBy = CheckDigitValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckDigitConstraint {

    /**
     * Default message.
     */
    String message() default "Invalid Check-Digit";

    /**
     * For user to customize the targeted groups.
     */
    Class<?>[] groups() default {};

    /**
     * For extensibility purposes.
     */
    Class<? extends Payload>[] payload() default {};
}
