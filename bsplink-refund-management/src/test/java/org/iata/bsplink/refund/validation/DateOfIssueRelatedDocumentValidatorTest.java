package org.iata.bsplink.refund.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.test.validation.CustomConstraintValidatorTestCase;
import org.junit.Before;
import org.junit.Test;

public class DateOfIssueRelatedDocumentValidatorTest extends CustomConstraintValidatorTestCase {

    private DateOfIssueRelatedDocumentValidator validator;
    private Refund refund;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        refund = new Refund();
        validator = new DateOfIssueRelatedDocumentValidator();
    }

    @Test
    public void testIsValid() throws Exception {
        refund.setDateOfIssue(LocalDate.of(2000, 10, 2));
        refund.setDateOfIssueRelatedDocument(refund.getDateOfIssue());
        assertTrue(validator.isValid(refund, context));
    }

    @Test
    public void testIsValidDateOfIssueIsNull() throws Exception {
        refund.setDateOfIssue(null);
        refund.setDateOfIssueRelatedDocument(LocalDate.of(2000, 10, 2));
        assertTrue(validator.isValid(refund, context));
    }

    @Test
    public void testIsValidDateOfIssueRelatedDocumentIsNull() throws Exception {
        refund.setDateOfIssue(LocalDate.of(2000, 10, 2));
        refund.setDateOfIssueRelatedDocument(null);
        assertTrue(validator.isValid(refund, context));
    }

    @Test
    public void testIsNotValid() throws Exception {
        refund.setDateOfIssue(LocalDate.of(2000, 10, 2));
        refund.setDateOfIssueRelatedDocument(refund.getDateOfIssue().plusDays(1));
        assertFalse(validator.isValid(refund, context));
        verifyConstraintViolation("dateOfIssueRelatedDocument",
                DateOfIssueRelatedDocumentValidator.INCORRECT_DATE_MSG);
    }
}
