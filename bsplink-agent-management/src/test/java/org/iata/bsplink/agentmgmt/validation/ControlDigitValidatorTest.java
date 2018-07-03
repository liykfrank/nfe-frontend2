package org.iata.bsplink.agentmgmt.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import javax.validation.ConstraintValidatorContext;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class ControlDigitValidatorTest {

    private ControlDigitValidator validator;
    private ConstraintValidatorContext validatorContext;

    @Before
    public void setUp() {

        validator = new ControlDigitValidator();
        validatorContext = mock(ConstraintValidatorContext.class);
    }

    @Test
    @Parameters
    public void testValidatesNumberEndingWithControlDigit(String value, boolean expected) {

        assertThat(validator.isValid(value, validatorContext), equalTo(expected));
    }

    @SuppressWarnings("unused")
    private Object[][] parametersForTestValidatesNumberEndingWithControlDigit() {

        return new Object[][] {
            { "", true },
            { null, true },
            { "1", false },
            { "11111111", true },
            { "12345675", true },
            { "12345677", false },
            { "1234567A", false }
        };
    }

}
