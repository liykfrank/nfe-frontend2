package org.iata.bsplink.agencymemo.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class CheckDigitValidatorTest {

    @Test
    @Parameters
    public void testIsValid(Integer checkDigit, Boolean isValid) {
        CheckDigitValidator checkDigitValidator = new CheckDigitValidator();
        assertThat(checkDigitValidator.isValid(checkDigit, null), equalTo(isValid));
    }

    /**
     * Test Parameters.
     */
    public Object[][] parametersForTestIsValid() {
        return new Object[][] {
            { 0, true },
            { 1, true },
            { 2, true },
            { 3, true },
            { 4, true },
            { 5, true },
            { 6, true },
            { 7, false },
            { 8, false },
            { 9, true },
            { 10, false }
        };
    }

}
