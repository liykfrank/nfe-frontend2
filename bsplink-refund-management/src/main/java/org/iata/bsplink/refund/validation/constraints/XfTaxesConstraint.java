package org.iata.bsplink.refund.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.iata.bsplink.refund.validation.XfTaxesValidator;


/**
 * Checks the XF Taxes.
 */
@Constraint(validatedBy = XfTaxesValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface XfTaxesConstraint {

    /**
     * Default message.
     */
    String message() default "Taxes KO";

    /**
     * For user to customize the targeted groups.
     */
    Class<?>[] groups() default {};

    /**
     * For extensibility purposes.
     */
    Class<? extends Payload>[] payload() default {};
}
