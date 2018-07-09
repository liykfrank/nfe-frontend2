package org.iata.bsplink.refund.utils;

import org.iata.bsplink.refund.exception.CustomValidationException;
import org.iata.bsplink.refund.validation.ValidationMessages;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AgentCodeUtilityTest {
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testValidAgentCode() throws CustomValidationException {
        AgentCodeUtility.validateAgentCode("78200102");
    }

    @Test
    public void testIncorrectFormat() throws CustomValidationException {
        expectedEx.expect(CustomValidationException.class);
        expectedEx.expectMessage(ValidationMessages.INCORRECT_FORMAT);
        AgentCodeUtility.validateAgentCode("782XX102");
    }

    @Test
    public void testInvalidCheckDigit() throws CustomValidationException {
        expectedEx.expect(CustomValidationException.class);
        expectedEx.expectMessage(AgentCodeUtility.INVALID_CHECK_DIGIT_MSG);
        AgentCodeUtility.validateAgentCode("78200107");
    }

    @Test
    public void testIncorrectCheckDigit() throws CustomValidationException {
        expectedEx.expect(CustomValidationException.class);
        expectedEx.expectMessage(AgentCodeUtility.INCORRECT_CHECK_DIGIT_MSG);
        AgentCodeUtility.validateAgentCode("78200105");
    }
}
