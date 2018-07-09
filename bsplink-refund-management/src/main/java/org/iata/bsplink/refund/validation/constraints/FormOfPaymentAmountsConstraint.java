package org.iata.bsplink.refund.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.iata.bsplink.refund.validation.FormOfPaymentAmountsValidator;

/**
 * Checks the Form of Payment Amounts.
 */
@Constraint(validatedBy = FormOfPaymentAmountsValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FormOfPaymentAmountsConstraint {

    /**
     * Default message.
     */
    String message() default "Incorrect Form of Payment Amounts";

    /**
     * For user to customize the targeted groups.
     */
    Class<?>[] groups() default {};

    /**
     * For extensibility purposes.
     */
    Class<? extends Payload>[] payload() default {};
}
