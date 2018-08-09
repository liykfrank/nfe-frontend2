package org.iata.bsplink.refund.validation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.iata.bsplink.refund.model.entity.FormOfPaymentAmount;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.model.entity.RefundAmounts;
import org.iata.bsplink.refund.model.entity.TaxMiscellaneousFee;
import org.iata.bsplink.refund.validation.constraints.DecimalsConstraint;



public class DecimalsValidator implements ConstraintValidator<DecimalsConstraint, Refund> {

    public static final String DECIMALS_MSG =
            "Incorrect number of amount's decimals for the specified currency.";

    @Override
    public boolean isValid(Refund refund, ConstraintValidatorContext context) {

        if (refund.getCurrency() == null) {
            return true;
        }

        Integer decimals = refund.getCurrency().getDecimals();
        if (decimals == null) {
            return true;
        }

        return isValidDecimals(refund, decimals, context);
    }


    private boolean isValidDecimals(Refund refund, int decimals,
            ConstraintValidatorContext context) {
        boolean result = true;

        if (!isValidAmountDecimals(decimals, refund.getAmounts(), context)) {
            result = false;
        }

        if (!isValidFopDecimals(decimals, refund.getFormOfPaymentAmounts(), context)) {
            result = false;
        }


        if (refund.getTaxMiscellaneousFees() != null
                && !isValidTaxAmount(refund.getTaxMiscellaneousFees(), decimals, context)) {
            result = false;
        }

        return result;
    }


    private boolean isValidFopDecimals(int decimals, List<FormOfPaymentAmount> formOfPaymentAmounts,
            ConstraintValidatorContext context) {
        boolean result = true;
        int i = -1;
        for (FormOfPaymentAmount fopa: formOfPaymentAmounts) {
            i++;
            if (fopa != null && !isValidAmount(fopa.getAmount(), decimals,
                        "formOfPaymentAmounts[" + i + "].amount", context)) {
                result = false;
            }
        }
        return result;
    }


    private boolean isValidTaxAmount(List<TaxMiscellaneousFee> taxMiscellaneousFees,
            int decimals, ConstraintValidatorContext context) {
        boolean result = true;
        int i = -1;
        for (TaxMiscellaneousFee tax: taxMiscellaneousFees) {
            i++;
            if (tax != null && !isValidAmount(tax.getAmount(), decimals,
                        "taxMiscellaneousFees[" + i + "].amount", context)) {
                result = false;
            }
        }
        return result;
    }

    private boolean isValidAmountDecimals(int decimals, RefundAmounts amounts,
            ConstraintValidatorContext context) {
        boolean result = true;
        result = isValidAmount(amounts.getCancellationPenalty(), decimals,
                "amounts.cancellationPenalty", context) && result;
        result = isValidAmount(amounts.getCommissionAmount(), decimals,
                "amounts.commissionAmount", context) && result;
        result = isValidAmount(amounts.getCommissionOnCpAndMfAmount(), decimals,
                "amounts.commissionOnCpAndMfAmount", context) && result;
        result = isValidAmount(amounts.getGrossFare(), decimals,
                "amounts.grossFare", context) && result;
        result = isValidAmount(amounts.getLessGrossFareUsed(), decimals,
                "amounts.lessGrossFareUsed", context) && result;
        result = isValidAmount(amounts.getMiscellaneousFee(), decimals,
                "amounts.miscellaneousFee", context) && result;
        result = isValidAmount(amounts.getRefundToPassenger(), decimals,
                "amounts.refundToPassenger", context) && result;
        result = isValidAmount(amounts.getSpam(), decimals,
                "amounts.spam", context) && result;
        result = isValidAmount(amounts.getTax(), decimals,
                "amounts.tax", context) && result;
        result = isValidAmount(amounts.getTaxOnCancellationPenalty(), decimals,
                "amounts.taxOnCancellationPenalty", context) && result;
        result = isValidAmount(amounts.getTaxOnMiscellaneousFee(), decimals,
                "amounts.taxOnMiscellaneousFee", context) && result;
        return result;
    }

    private boolean isValidAmount(BigDecimal amount, int decimals, String field,
            ConstraintValidatorContext context) {
        if (amount == null) {
            return true;
        }
        BigDecimal roundedAmount = amount.abs().setScale(decimals, RoundingMode.HALF_UP);
        if (roundedAmount.compareTo(amount.abs()) != 0) {
            addMessage(context, field, DECIMALS_MSG);
            return false;
        }
        return true;
    }

    private void addMessage(ConstraintValidatorContext context, String property, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addPropertyNode(property)
                .addConstraintViolation();
    }
}
