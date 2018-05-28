package org.iata.bsplink.sftpaccountmanager.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.validation.ConstraintValidatorContext;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.iata.bsplink.sftpaccountmanager.service.SftpServerAccountManager;
import org.iata.bsplink.sftpaccountmanager.validation.constraints.PublicKeyConstraint;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class PublicKeyValidatorTest {

    @Test
    @Parameters
    public void testValidatesPublicKey(String publicKey, boolean emptyIsValid,
            boolean accountManagerResult, boolean expected) {

        SftpServerAccountManager sftpServerAccountManager = mock(SftpServerAccountManager.class);
        when(sftpServerAccountManager.publicKeyIsValid(publicKey)).thenReturn(accountManagerResult);

        PublicKeyConstraint constraintAnnotation = mock(PublicKeyConstraint.class);
        when(constraintAnnotation.emptyIsValid()).thenReturn(emptyIsValid);

        PublicKeyValidator validator = new PublicKeyValidator(sftpServerAccountManager);
        validator.initialize(constraintAnnotation);

        ConstraintValidatorContext validatorContext = mock(ConstraintValidatorContext.class);

        assertThat(validator.isValid(publicKey, validatorContext), equalTo(expected));
    }

    @SuppressWarnings("unused")
    private Object[][] parametersForTestValidatesPublicKey() {

        return new Object[][] {

            { null, true, false, true },
            { "", true, false, true },
            { null, false, false, false },
            { "", false, false, false },
            { "any public key", false, false, false },
            { "any public key", false, true, true }
        };
    }

}
