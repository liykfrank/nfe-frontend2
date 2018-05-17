package org.iata.bsplink.sftpaccountmanager.validation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import javax.validation.ConstraintValidatorContext;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.iata.bsplink.sftpaccountmanager.dto.AccountPasswordRequest;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class PasswordMatchValidatorTest {

    @Test
    @Parameters
    public void testIsValid(String password, String confirmPassword, boolean expected) {

        PasswordMatchValidator validator = new PasswordMatchValidator();

        ConstraintValidatorContext validatorContext = mock(ConstraintValidatorContext.class);
        AccountPasswordRequest accountPassword =
                new AccountPasswordRequest("old", password, confirmPassword);

        assertEquals(expected, validator.isValid(accountPassword, validatorContext));
    }

    @SuppressWarnings("unused")
    private Object[][] parametersForTestIsValid() {

        return new Object[][] {
            { "foo", "bar", false },
            { "foo", "foo", true },
            { null, "bar", false },
            { "foo", null, false },
        };
    }

}
