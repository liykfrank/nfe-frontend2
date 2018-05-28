package org.iata.bsplink.sftpaccountmanager.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.iata.bsplink.sftpaccountmanager.service.SftpServerAccountManager;
import org.iata.bsplink.sftpaccountmanager.validation.constraints.PublicKeyConstraint;

public class PublicKeyValidator implements ConstraintValidator<PublicKeyConstraint, String> {

    private SftpServerAccountManager sftpServerAccountManager;
    private boolean emptyIsValid;

    public PublicKeyValidator(SftpServerAccountManager sftpServerAccountManager) {

        this.sftpServerAccountManager = sftpServerAccountManager;
    }

    @Override
    public void initialize(PublicKeyConstraint constraintAnnotation) {

        this.emptyIsValid = constraintAnnotation.emptyIsValid();
    }

    @Override
    public boolean isValid(String publicKey, ConstraintValidatorContext context) {


        if (publicKey == null || publicKey.isEmpty()) {

            return emptyIsValid ? true : false;
        }

        return sftpServerAccountManager.publicKeyIsValid(publicKey);
    }

}
