package org.iata.bsplink.refund.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.iata.bsplink.refund.model.entity.OriginalIssue;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.test.validation.CustomConstraintValidatorTestCase;
import org.junit.Before;
import org.junit.Test;

public class OriginalIssueValidatorTest extends CustomConstraintValidatorTestCase {

    private OriginalIssueValidator validator;
    private Refund refund;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        refund = new Refund();
        validator = new OriginalIssueValidator();
    }

    @Test
    public void testIsValidIfNotExchange() throws Exception {
        refund.setExchange(false);
        refund.setOriginalIssue(null);
        assertTrue(validator.isValid(refund, context));
    }

    @Test
    public void testIsValidIfExchange() throws Exception {
        refund.setExchange(true);
        refund.setOriginalIssue(new OriginalIssue());
        assertTrue(validator.isValid(refund, context));
    }

    @Test
    public void testIsNotValidExchangeWithoutOriginalIssue() throws Exception {
        refund.setExchange(true);
        refund.setOriginalIssue(null);
        assertFalse(validator.isValid(refund, context));
        verifyConstraintViolation("originalIssue",
                OriginalIssueValidator.MISSING_ORIGINAL_ISSUE_MSG);
    }

    @Test
    public void testIsNotValidNoExchangeWithOriginalIssue() throws Exception {
        refund.setExchange(false);
        refund.setOriginalIssue(new OriginalIssue());
        assertFalse(validator.isValid(refund, context));
        verifyConstraintViolation("originalIssue",
                OriginalIssueValidator.ONLY_EXCHANGE_MSG);
    }

    @Test
    public void testIsNotValidOriginalIssueDate() throws Exception {
        refund.setDateOfIssue(LocalDate.of(2000, 10, 2));
        refund.setExchange(true);
        refund.setOriginalIssue(new OriginalIssue());
        refund.getOriginalIssue().setOriginalDateOfIssue(refund.getDateOfIssue().plusDays(1));
        assertFalse(validator.isValid(refund, context));
        verifyConstraintViolation("originalIssue.originalDateOfIssue",
                OriginalIssueValidator.INCORRECT_DATE_MSG);
    }

    @Test
    public void testIsValidOriginalIssueDate() throws Exception {
        refund.setDateOfIssue(LocalDate.of(2000, 10, 2));
        refund.setExchange(true);
        refund.setOriginalIssue(new OriginalIssue());
        refund.getOriginalIssue().setOriginalDateOfIssue(refund.getDateOfIssue());
        assertTrue(validator.isValid(refund, context));
    }
}
