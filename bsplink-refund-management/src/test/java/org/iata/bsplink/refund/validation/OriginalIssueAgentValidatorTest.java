package org.iata.bsplink.refund.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.iata.bsplink.refund.test.validation.CustomConstraintValidatorTestCase;
import org.iata.bsplink.refund.utils.AgentCodeUtility;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class OriginalIssueAgentValidatorTest extends CustomConstraintValidatorTestCase {

    private OriginalIssueAgentValidator validator;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        validator = new OriginalIssueAgentValidator();
    }

    @Test
    @Parameters
    public void testIsValid(String agentCode) throws Exception {
        assertTrue(validator.isValid(agentCode, context));
    }

    @Test
    @Parameters
    public void testIsNotValid(String agentCode, String message) throws Exception {
        assertFalse(validator.isValid(agentCode, context));
        verifyConstraintViolation("agentCode", message);
    }

    /**
     * Test Parameters.
     */
    public Object[][] parametersForTestIsValid() {
        return new Object[][] {
            { null },
            { "78200102" },
        };
    }

    /**
     * Test Parameters.
     */
    public Object[][] parametersForTestIsNotValid() {
        return new Object[][] {
            { "ABCDEFGH", ValidationMessages.INCORRECT_FORMAT },
            { "123", ValidationMessages.INCORRECT_FORMAT },
            { "78200007", AgentCodeUtility.INVALID_CHECK_DIGIT_MSG },
            { "78200103", AgentCodeUtility.INCORRECT_CHECK_DIGIT_MSG }
        };
    }
}
