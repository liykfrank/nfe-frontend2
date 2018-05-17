package org.iata.bsplink.sftpaccountmanager.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.iata.bsplink.sftpaccountmanager.dto.AccountPasswordRequest;
import org.iata.bsplink.sftpaccountmanager.validation.constraints.PasswordMatchConstraint;

public class PasswordMatchValidator
        implements ConstraintValidator<PasswordMatchConstraint, AccountPasswordRequest> {

    @Override
    public boolean isValid(AccountPasswordRequest password,
            ConstraintValidatorContext context) {

        return  password.getPassword() != null
                && password.getPassword().equals(password.getConfirmPassword());
    }
}
