package org.iata.bsplink.refund.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.iata.bsplink.refund.validation.FormOfPaymentConsistentDataValidator;

/**
 * Checks that the form of payment data is consistent.
 *
 * <p>
 *  - form of payments with type cash must not have values related to credit.
 *  - form of payments on credit must have credit card an vendor code.
 *  </p>
 */
@Constraint(validatedBy = FormOfPaymentConsistentDataValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FormOfPaymentConsistentDataConstraint {

    /**
     * Default message.
     */
    String message() default "invalid values in form of payment";

    /**
     * For user to customize the targeted groups.
     */
    Class<?>[] groups() default {};

    /**
     * For extensibility purposes.
     */
    Class<? extends Payload>[] payload() default {};
}
