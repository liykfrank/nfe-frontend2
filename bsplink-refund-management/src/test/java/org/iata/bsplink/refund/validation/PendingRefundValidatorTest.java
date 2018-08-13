package org.iata.bsplink.refund.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.iata.bsplink.refund.test.fixtures.RefundFixtures.getRefunds;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.apache.commons.beanutils.BeanUtils;
import org.iata.bsplink.refund.model.entity.Refund;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.validation.BeanPropertyBindingResult;

@RunWith(JUnitParamsRunner.class)
public class PendingRefundValidatorTest {

    private PendingRefundValidator validator;
    private BeanPropertyBindingResult errors;
    private Refund refund;

    @Before
    public void setUp() {
        refund = getRefunds().get(0);
        errors = new BeanPropertyBindingResult(refund, "refund");
        validator = new PendingRefundValidator();
    }


    @Test
    public void testSupports() {
        assertTrue(validator.supports(refund.getClass()));
        assertFalse(validator.supports(Object.class));
    }


    @Test
    public void testIsValid() {
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    @Parameters
    public void testIsNotValid(String field, String message) throws Exception {

        BeanUtils.setProperty(refund, field, null);
        validator.validate(refund, errors);
        assertTrue(errors.hasFieldErrors());
        assertThat(errors.getFieldError().getField(), equalTo(field));
        assertThat(errors.getFieldError().getDefaultMessage(), equalTo(message));
    }


    /**
     * Test Parameters.
     */
    public Object[][] parametersForTestIsNotValid() {

        return new Object[][] {
            { "airlineCode", PendingRefundValidator.AIRLINE_CODE_REQUIRED },
            { "passenger", PendingRefundValidator.PASSENGER_REQUIRED },
            { "airlineCodeRelatedDocument",
                PendingRefundValidator.AIRLINE_CODE_RELATED_DOCUMENT_REQUIRED },
            { "dateOfIssueRelatedDocument",
                PendingRefundValidator.DATE_OF_ISSUE_RELATED_DOCUMENT_REQUIRED },
            { "issueReason", PendingRefundValidator.REASON_REQUIRED }
        };
    }
}


