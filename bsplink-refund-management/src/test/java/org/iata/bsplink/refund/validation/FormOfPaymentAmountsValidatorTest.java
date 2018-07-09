package org.iata.bsplink.refund.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.iata.bsplink.refund.model.entity.FormOfPaymentAmount;
import org.iata.bsplink.refund.model.entity.FormOfPaymentType;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.test.validation.CustomConstraintValidatorTestCase;
import org.junit.Before;
import org.junit.Test;

public class FormOfPaymentAmountsValidatorTest extends CustomConstraintValidatorTestCase {

    private FormOfPaymentAmountsValidator validator;

    private Refund refund0;
    private Refund refund10;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        refund0 = new Refund();
        refund10 = new Refund();
        refund10.getAmounts().setRefundToPassenger(BigDecimal.TEN);
        validator = new FormOfPaymentAmountsValidator();
    }

    @Test
    public void testIsValid() {
        assertTrue(validator.isValid(refund0, context));
    }

    @Test
    public void testIsValidNullFormOfPayments() {
        refund10.setFormOfPaymentAmounts(null);
        assertTrue(validator.isValid(refund10, context));
    }

    @Test
    public void testIsNotValid() {
        assertFalse(validator.isValid(refund10, context));
        verifyConstraintViolation("amounts.refundToPassenger",
                FormOfPaymentAmountsValidator.INCORRECT_TOTAL_MSG);
    }

    @Test
    public void testIsNotValidMoreThanOneCa() {
        for (int i = 0; i < 2; i++) {
            refund10.getFormOfPaymentAmounts().add(fop(FormOfPaymentType.CA, 5));
        }
        assertFalse(validator.isValid(refund10, context));
        verifyConstraintViolation("formOfPaymentAmounts[1].type",
                FormOfPaymentAmountsValidator.MAX_1_MSG);
    }

    @Test
    public void testIsValidOneMscaAnOneCa() {
        refund10.getFormOfPaymentAmounts().add(fop(FormOfPaymentType.MSCA, 5));
        refund10.getFormOfPaymentAmounts().add(fop(FormOfPaymentType.CA, 5));
        assertTrue(validator.isValid(refund10, context));
    }

    @Test
    public void testIsNotValidMoreThanTwoCc() {
        int total = 0;
        for (int i = 0; i < 3; i++) {
            refund10.getFormOfPaymentAmounts().add(fop(FormOfPaymentType.CC, 5 + i));
            total += 5 + i;
        }
        refund10.getAmounts().setRefundToPassenger(BigDecimal.valueOf(total));
        assertFalse(validator.isValid(refund10, context));
        verifyConstraintViolation("formOfPaymentAmounts[2].type",
                FormOfPaymentAmountsValidator.MAX_2_CC_MSG);
    }

    @Test
    public void testIsValidTwoCc() {
        for (int i = 0; i < 2; i++) {
            refund10.getFormOfPaymentAmounts().add(fop(FormOfPaymentType.CC, 5));
        }
        assertTrue(validator.isValid(refund10, context));
    }

    @Test
    public void testIsNotValidOneMscaAndOneCaTotal() {
        refund10.getFormOfPaymentAmounts().add(fop(FormOfPaymentType.MSCA, 5));
        refund10.getFormOfPaymentAmounts().add(fop(FormOfPaymentType.CA, 7));
        assertFalse(validator.isValid(refund10, context));
        verifyConstraintViolation("amounts.refundToPassenger",
                FormOfPaymentAmountsValidator.INCORRECT_TOTAL_MSG);
    }

    @Test
    public void testIsValidRefundtoPassengerNull() {
        refund10.getFormOfPaymentAmounts().add(fop(FormOfPaymentType.MSCC, 5));
        refund10.getFormOfPaymentAmounts().add(fop(FormOfPaymentType.MSCA, 7));
        refund10.getAmounts().setRefundToPassenger(null);
        assertTrue(validator.isValid(refund10, context));
    }

    @Test
    public void testIsValidAmountsNull() {
        refund10.getFormOfPaymentAmounts().add(fop(FormOfPaymentType.MSCC, 5));
        refund10.getFormOfPaymentAmounts().add(fop(FormOfPaymentType.MSCA, 7));
        refund10.setAmounts(null);
        assertTrue(validator.isValid(refund10, context));
    }

    @Test
    public void testIsValidFopTypeNull() {
        refund10.getFormOfPaymentAmounts().add(fop(FormOfPaymentType.MSCC, 3));
        refund10.getFormOfPaymentAmounts().add(fop(null, 7));
        refund10.setAmounts(null);
        assertTrue(validator.isValid(refund10, context));
    }

    @Test
    public void testIsValidFopNull() {
        refund10.getFormOfPaymentAmounts().add(fop(FormOfPaymentType.CC, 10));
        refund10.getFormOfPaymentAmounts().add(null);
        refund10.setAmounts(null);
        assertTrue(validator.isValid(refund10, context));
    }

    private FormOfPaymentAmount fop(FormOfPaymentType type, int amount) {
        FormOfPaymentAmount fop = new FormOfPaymentAmount();
        fop.setType(type);
        fop.setAmount(BigDecimal.valueOf(amount));
        return fop;
    }

}
