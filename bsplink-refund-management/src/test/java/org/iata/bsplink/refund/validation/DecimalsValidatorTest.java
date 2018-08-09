package org.iata.bsplink.refund.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.apache.commons.beanutils.BeanUtils;
import org.iata.bsplink.refund.model.entity.FormOfPaymentAmount;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.model.entity.RefundAmounts;
import org.iata.bsplink.refund.model.entity.RefundCurrency;
import org.iata.bsplink.refund.model.entity.TaxMiscellaneousFee;
import org.iata.bsplink.refund.test.validation.CustomConstraintValidatorTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class DecimalsValidatorTest extends CustomConstraintValidatorTestCase {

    private DecimalsValidator validator;
    private Refund refund;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        refund = new Refund();
        RefundCurrency refundCurrency = new RefundCurrency();
        refund.setCurrency(refundCurrency);
        validator = new DecimalsValidator();
    }

    @Test
    public void testIsValidForNullCurrency() throws Exception {
        refund.setCurrency(null);
        assertTrue(validator.isValid(refund, context));
    }

    @Test
    public void testIsValidForNullCurrencyDecimals() throws Exception {
        refund.getCurrency().setDecimals(null);
        assertTrue(validator.isValid(refund, context));
    }

    @Test
    public void testIsValidTaxAmount() throws Exception {
        refund.getCurrency().setDecimals(2);
        TaxMiscellaneousFee tax = new TaxMiscellaneousFee();
        tax.setAmount(BigDecimal.valueOf(2.23));
        refund.getTaxMiscellaneousFees().add(tax);
        assertTrue(validator.isValid(refund, context));
    }

    @Test
    public void testIsValidNullAmount() throws Exception {
        refund.getCurrency().setDecimals(2);
        refund.getAmounts().setGrossFare(null);
        assertTrue(validator.isValid(refund, context));
    }

    @Test
    public void testIsNotValidTaxAmount() throws Exception {
        refund.getCurrency().setDecimals(1);
        TaxMiscellaneousFee tax = new TaxMiscellaneousFee();
        tax.setAmount(BigDecimal.valueOf(2.23));
        refund.getTaxMiscellaneousFees().add(tax);
        assertFalse(validator.isValid(refund, context));
        verifyConstraintViolation("taxMiscellaneousFees[0].amount",
                DecimalsValidator.DECIMALS_MSG);
    }

    @Test
    public void testIsValidFormOfPaymentAmount() throws Exception {
        refund.getCurrency().setDecimals(2);
        FormOfPaymentAmount fop = new FormOfPaymentAmount();
        fop.setAmount(BigDecimal.valueOf(2.23));
        refund.getFormOfPaymentAmounts().add(fop);
        assertTrue(validator.isValid(refund, context));
    }

    @Test
    public void testIsValidFormOfPaymentAmountIsNull() throws Exception {
        refund.getFormOfPaymentAmounts().add(null);
        assertTrue(validator.isValid(refund, context));
    }

    @Test
    public void testIsValidTaxIsNull() throws Exception {
        refund.getTaxMiscellaneousFees().add(null);
        assertTrue(validator.isValid(refund, context));
    }

    @Test
    public void testIsValidFormOfPaymentAmountsIsNull() throws Exception {
        refund.setFormOfPaymentAmounts(null);
        assertTrue(validator.isValid(refund, context));
    }

    @Test
    public void testIsValidTaxesIsNull() throws Exception {
        refund.setTaxMiscellaneousFees(null);
        assertTrue(validator.isValid(refund, context));
    }

    @Test
    public void testIsNotValidFormOfPaymentAmount() throws Exception {
        refund.getCurrency().setDecimals(1);
        FormOfPaymentAmount fop = new FormOfPaymentAmount();
        fop.setAmount(BigDecimal.valueOf(2.23));
        refund.getFormOfPaymentAmounts().add(fop);
        assertFalse(validator.isValid(refund, context));
        verifyConstraintViolation("formOfPaymentAmounts[0].amount",
                DecimalsValidator.DECIMALS_MSG);
    }



    @Test
    @Parameters
    public void testIsValidAmounts(String field, double amountValue, int decimals,
            Boolean expected, String message) throws Exception {
        BigDecimal amount = BigDecimal.valueOf(amountValue);
        refund.getCurrency().setDecimals(decimals);

        refund.setAmounts(new RefundAmounts());
        BeanUtils.setProperty(refund.getAmounts(), field, amount);
        assertThat(validator.isValid(refund, context), equalTo(expected));
        if (!expected) {
            verifyConstraintViolation("amounts." + field, message);
        }
    }


    /**
     * Test Parameters.
     */
    public Object[][] parametersForTestIsValidAmounts() {
        return new Object[][] {
            { "grossFare", 0.23D, 2, Boolean.TRUE, null },
            { "cancellationPenalty",  0.23D, 3, Boolean.TRUE, null },
            { "commissionAmount",  0.23D, 0, Boolean.FALSE, DecimalsValidator.DECIMALS_MSG },
            { "commissionOnCpAndMfAmount", 1.234D, 3, Boolean.TRUE, null },
            { "lessGrossFareUsed", 1.234D, 0, Boolean.FALSE, DecimalsValidator.DECIMALS_MSG },
            { "miscellaneousFee", 1234D, 0, Boolean.TRUE, null },
            { "refundToPassenger", 1234D, 0, Boolean.TRUE, null },
            { "spam",  1.234D, 2, Boolean.FALSE, DecimalsValidator.DECIMALS_MSG },
            { "tax", 1234D, 7, Boolean.TRUE, null },
            { "taxOnCancellationPenalty", 1234D, 0, Boolean.TRUE, null },
            { "taxOnMiscellaneousFee", 1234D, 0, Boolean.TRUE, null }
        };
    }
}
