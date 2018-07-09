package org.iata.bsplink.refund.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.model.entity.TaxMiscellaneousFee;
import org.iata.bsplink.refund.test.validation.CustomConstraintValidatorTestCase;
import org.junit.Before;
import org.junit.Test;

public class TaxAmountsValidatorTest extends CustomConstraintValidatorTestCase {

    private TaxAmountsValidator validator;

    private Refund refund0;
    private Refund refund10;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        refund0 = new Refund();
        refund10 = new Refund();
        refund10.getAmounts().setTax(BigDecimal.TEN);
        validator = new TaxAmountsValidator();
    }

    @Test
    public void testIsValid() {
        assertTrue(validator.isValid(refund0, context));
    }

    @Test
    public void testIsValidNullTaxMiscellaneousFee() {
        refund10.setTaxMiscellaneousFees(null);
        assertTrue(validator.isValid(refund10, context));
    }

    @Test
    public void testIsNotValid() {
        assertFalse(validator.isValid(refund10, context));
        verifyConstraintViolation("amounts.tax",
                TaxAmountsValidator.INCORRECT_TOTAL_MSG);
    }

    @Test
    public void testIsNotValidTotal() {
        refund10.getTaxMiscellaneousFees().add(tax("AA", 5));
        refund10.getTaxMiscellaneousFees().add(tax("BB", 7));
        assertFalse(validator.isValid(refund10, context));
        verifyConstraintViolation("amounts.tax",
                TaxAmountsValidator.INCORRECT_TOTAL_MSG);
    }

    @Test
    public void testIsValidTaxNull() {
        refund10.getTaxMiscellaneousFees().add(tax("AA", 5));
        refund10.getTaxMiscellaneousFees().add(tax("BB", 7));
        refund10.getAmounts().setTax(null);
        assertTrue(validator.isValid(refund10, context));
    }

    @Test
    public void testIsValidAmountsNull() {
        refund10.getTaxMiscellaneousFees().add(tax("AA", 5));
        refund10.getTaxMiscellaneousFees().add(tax("BB", 7));
        refund10.setAmounts(null);
        assertTrue(validator.isValid(refund10, context));
    }

    private TaxMiscellaneousFee tax(String type, int amount) {
        TaxMiscellaneousFee tax = new TaxMiscellaneousFee();
        tax.setType(type);
        tax.setAmount(BigDecimal.valueOf(amount));
        return tax;
    }

}
