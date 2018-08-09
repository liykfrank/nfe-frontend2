package org.iata.bsplink.refund.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.iata.bsplink.refund.model.entity.RefundAmounts;
import org.iata.bsplink.refund.test.validation.CustomConstraintValidatorTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class RefundAmountsValidatorTest extends CustomConstraintValidatorTestCase {

    private RefundAmountsValidator validator;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        validator = new RefundAmountsValidator();
    }

    @Test
    public void testIsValidWithDefaultValues() {
        RefundAmounts amounts = new RefundAmounts();
        assertTrue(validator.isValid(amounts, context));
    }

    @Test
    @Parameters
    public void testIsValid(Integer grossFare, Integer lessGrossFareUsed, Integer tax,
            Integer cancellationPenalty, Integer taxOnCancellationPenalty, Integer miscellaneousFee,
            Integer taxOnMiscellaneousFee, Integer refundToPassenger) {
        RefundAmounts amounts = refundAmounts(grossFare, lessGrossFareUsed, tax,
                cancellationPenalty, taxOnCancellationPenalty, miscellaneousFee,
                taxOnMiscellaneousFee, refundToPassenger);
        assertTrue(validator.isValid(amounts, context));
    }

    @Test
    @Parameters
    public void testIsNotValid(Integer grossFare, Integer lessGrossFareUsed, Integer tax,
            Integer cancellationPenalty, Integer taxOnCancellationPenalty, Integer miscellaneousFee,
            Integer taxOnMiscellaneousFee, Integer refundToPassenger,
            String property, String message) {
        RefundAmounts amounts = refundAmounts(grossFare, lessGrossFareUsed, tax,
                cancellationPenalty, taxOnCancellationPenalty, miscellaneousFee,
                taxOnMiscellaneousFee, refundToPassenger);
        assertFalse(validator.isValid(amounts, context));
        verifyConstraintViolation(property, message);
    }

    private RefundAmounts refundAmounts(Integer grossFare, Integer lessGrossFareUsed, Integer tax,
            Integer cancellationPenalty, Integer taxOnCancellationPenalty, Integer miscellaneousFee,
            Integer taxOnMiscellaneousFee, Integer refundToPassenger) {
        RefundAmounts amounts = new RefundAmounts();
        amounts.setGrossFare(grossFare == null ? null : BigDecimal.valueOf(grossFare));
        amounts.setLessGrossFareUsed(
                lessGrossFareUsed == null ? null : BigDecimal.valueOf(lessGrossFareUsed));
        amounts.setTax(tax == null ? null : BigDecimal.valueOf(tax));
        amounts.setCancellationPenalty(
                cancellationPenalty == null ? null : BigDecimal.valueOf(cancellationPenalty));
        amounts.setTaxOnCancellationPenalty(taxOnCancellationPenalty == null ? null
                : BigDecimal.valueOf(taxOnCancellationPenalty));
        amounts.setMiscellaneousFee(
                miscellaneousFee == null ? null : BigDecimal.valueOf(miscellaneousFee));
        amounts.setTaxOnMiscellaneousFee(
                taxOnMiscellaneousFee == null ? null : BigDecimal.valueOf(taxOnMiscellaneousFee));
        amounts.setRefundToPassenger(
                refundToPassenger == null ? null : BigDecimal.valueOf(refundToPassenger));
        return amounts;
    }

    @Test
    public void testIsValidCommission() {
        RefundAmounts amounts = new RefundAmounts();
        amounts.setCommissionAmount(BigDecimal.valueOf(8));
        amounts.setGrossFare(BigDecimal.TEN);
        amounts.setLessGrossFareUsed(BigDecimal.ONE);
        amounts.setRefundToPassenger(BigDecimal.valueOf(9));
        assertTrue(validator.isValid(amounts, context));
    }

    @Test
    public void testIsInValidCommission() {
        RefundAmounts amounts = new RefundAmounts();
        amounts.setCommissionAmount(BigDecimal.TEN);
        amounts.setGrossFare(BigDecimal.valueOf(5));
        amounts.setLessGrossFareUsed(BigDecimal.ONE);
        amounts.setRefundToPassenger(BigDecimal.valueOf(4));
        assertFalse(validator.isValid(amounts, context));
        verifyConstraintViolation("commissionAmount",
                RefundAmountsValidator.COMMISSION_GREATER_THAN_REFUND_AMOUNT_MSG);
    }


    @Test
    public void testIsValidLessGross() {
        RefundAmounts amounts = new RefundAmounts();
        amounts.setGrossFare(BigDecimal.TEN);
        amounts.setLessGrossFareUsed(BigDecimal.ONE);
        amounts.setRefundToPassenger(BigDecimal.valueOf(9));
        assertTrue(validator.isValid(amounts, context));
    }

    @Test
    public void testIsInValidLessGross() {
        RefundAmounts amounts = new RefundAmounts();
        amounts.setCommissionAmount(null);
        amounts.setSpam(null);
        amounts.setGrossFare(BigDecimal.ONE);
        amounts.setLessGrossFareUsed(BigDecimal.TEN);
        amounts.setTax(BigDecimal.TEN);
        amounts.setRefundToPassenger(BigDecimal.ONE);
        assertFalse(validator.isValid(amounts, context));
        verifyConstraintViolation("lessGrossFareUsed",
                RefundAmountsValidator.LESS_GROSS_GREATER_THAN_GROSS_MSG);
    }

    @Test
    public void testIsValidCpMfCommission() {
        RefundAmounts amounts = new RefundAmounts();
        amounts.setGrossFare(BigDecimal.TEN);
        amounts.setCancellationPenalty(BigDecimal.ONE);
        amounts.setMiscellaneousFee(BigDecimal.ONE);
        amounts.setCommissionOnCpAndMfAmount(BigDecimal.ONE);
        amounts.setRefundToPassenger(BigDecimal.valueOf(8));
        assertTrue(validator.isValid(amounts, context));
    }

    @Test
    public void testIsInValidCpMfCommission() {
        RefundAmounts amounts = new RefundAmounts();
        amounts.setGrossFare(BigDecimal.TEN);
        amounts.setCancellationPenalty(BigDecimal.ONE);
        amounts.setMiscellaneousFee(BigDecimal.ONE);
        amounts.setCommissionOnCpAndMfAmount(BigDecimal.TEN);
        amounts.setRefundToPassenger(BigDecimal.valueOf(8));
        assertFalse(validator.isValid(amounts, context));
        verifyConstraintViolation("commissionOnCpAndMfAmount",
                RefundAmountsValidator.COMMISSION_ON_CPMF_GREATER_THAN_CPMF_MSG);
    }

    /**
     * Parameters for isValid test:
     * grossFare, lessGrossFareUsed, tax, cancellationPenalty, taxOnCancellationPenalty,
     *  miscellaneousFee, taxOnMiscellaneousFee, refundToPassenger.
     */
    public Object[][] parametersForTestIsValid() {
        return new Object[][] {
            { 1, 2, null, 4, 5, 6, 7, 8 },
            { 1, 2, 3, 4, -5, 6, 7, 8 },
            { 1000, 400, 100, 20, 2, 10, 1, 667 }
        };
    }

    /**
     * Parameters for isNotValid test:
     * grossFare, lessGrossFareUsed, tax, cancellationPenalty, taxOnCancellationPenalty,
     *  miscellaneousFee, taxOnMiscellaneousFee, refundToPassenger.
     */
    public Object[][] parametersForTestIsNotValid() {
        return new Object[][] {
            { 30, 2, 3, 5, 4, 7, 6, 8,
                "refundToPassenger", RefundAmountsValidator.INCORRECT_TOTAL_MSG },

            { 30, 2, 3, 4, 5, 7, 6, 9,
                "taxOnCancellationPenalty", RefundAmountsValidator.TAX_ON_CP_GREATER_THAN_CP_MSG },

            { 30, 2, 3, 5, 4, 6, 7, 9,
                "taxOnMiscellaneousFee", RefundAmountsValidator.TAX_ON_MF_GREATER_THAN_MF_MSG }

        };
    }
}
