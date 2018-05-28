package org.iata.bsplink.sftpaccountmanager.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.iata.bsplink.sftpaccountmanager.validation.PublicKeyValidator;

/**
 * Validates the given public key.
 */
@Constraint(validatedBy = PublicKeyValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PublicKeyConstraint {

    /**
     * Whether an empty public key is considered valid or not.
     */
    boolean emptyIsValid() default false;

    /**
     * Default message.
     */
    String message() default "public key is not valid";

    /**
     * For user to customize the targeted groups.
     */
    Class<?>[] groups() default {};

    /**
     * For extensibility purposes.
     */
    Class<? extends Payload>[] payload() default {};
}
