package org.iata.bsplink.refund.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.test.validation.CustomConstraintValidatorTestCase;
import org.junit.Before;
import org.junit.Test;


public class NetReportingValidatorTest extends CustomConstraintValidatorTestCase {

    private NetReportingValidator validator;
    private Refund refund;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        refund = new Refund();
        validator = new NetReportingValidator();
    }

    @Test
    public void testIsValidForNullAmounts() throws Exception {
        refund.setAmounts(null);
        assertTrue(validator.isValid(refund, context));
    }

    @Test
    public void testIsValidForNullSpam() throws Exception {
        refund.getAmounts().setSpam(null);
        assertTrue(validator.isValid(refund, context));
    }

    @Test
    public void testIsValidForZeroSpam() throws Exception {
        refund.getAmounts().setSpam(BigDecimal.ZERO);
        assertTrue(validator.isValid(refund, context));
    }

    @Test
    public void testIsValidForNetReporting() throws Exception {
        refund.setNetReporting(true);
        refund.getAmounts().setSpam(BigDecimal.TEN);
        assertTrue(validator.isValid(refund, context));
    }

    @Test
    public void testIsNotValidForNetReportingNull() throws Exception {
        refund.setNetReporting(null);
        refund.getAmounts().setSpam(BigDecimal.TEN);
        assertFalse(validator.isValid(refund, context));
        verifyConstraintViolation("amounts.spam",
                NetReportingValidator.NET_REPORTING_MSG);
    }

    @Test
    public void testIsNotValidForNetReportingFalse() throws Exception {
        refund.setNetReporting(false);
        refund.getAmounts().setSpam(BigDecimal.TEN);
        assertFalse(validator.isValid(refund, context));
        verifyConstraintViolation("amounts.spam",
                NetReportingValidator.NET_REPORTING_MSG);
    }
}
