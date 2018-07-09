package org.iata.bsplink.refund.validation;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.iata.bsplink.refund.model.entity.RefundAmounts;
import org.iata.bsplink.refund.validation.constraints.RefundAmountsConstraint;

public class RefundAmountsValidator
        implements ConstraintValidator<RefundAmountsConstraint, RefundAmounts> {

    public static final String INCORRECT_TOTAL_MSG =
            "The amount for 'Refund to Passenger' is incorrect.";
    public static final String COMMISSION_GREATER_THAN_REFUND_AMOUNT_MSG =
            "The commission has to be inferior to the refund amount.";
    public static final String LESS_GROSS_GREATER_THAN_GROSS_MSG =
            "The Less Gross Fare Used amount has to be inferior to the Gross Fare amount.";
    public static final String COMMISSION_ON_CPMF_GREATER_THAN_CPMF_MSG =
            "The commission on CP and MF has to be inferior to the sum of CP and MF amounts.";
    public static final String TAX_ON_CP_GREATER_THAN_CP_MSG = "The tax on cancellation penalty"
            + " has to be inferior to cancellation penalty amount";
    public static final String TAX_ON_MF_GREATER_THAN_MF_MSG = "The tax on miscellaneous fee"
            + " has to be inferior to miscellaneous fee amount";


    @Override
    public boolean isValid(RefundAmounts amounts, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        boolean result = true;

        if (hasNullValues(amounts)) {
            return true;
        }

        if (hasNegativeValues(amounts)) {
            return true;
        }

        if (totalAmount(amounts).compareTo(amounts.getRefundToPassenger()) != 0) {
            result = false;
            addToContext(context, "refundToPassenger", INCORRECT_TOTAL_MSG);
        }

        if (commissionGreaterThanRefundAmount(amounts)) {
            addToContext(context, "commissionAmount", COMMISSION_GREATER_THAN_REFUND_AMOUNT_MSG);
            result = false;
        }

        if (lessGrossGreaterThanGrossFare(amounts)) {
            addToContext(context, "lessGrossFareUsed", LESS_GROSS_GREATER_THAN_GROSS_MSG);
            result = false;
        }

        if (commissionOnCpAndMfGreaterThanMfAndCp(amounts)) {
            addToContext(context, "commissionOnCpAndMfAmount",
                    COMMISSION_ON_CPMF_GREATER_THAN_CPMF_MSG);
            result = false;
        }

        if (commissionCpTaxGreaterThanCp(amounts)) {
            addToContext(context, "taxOnCancellationPenalty", TAX_ON_CP_GREATER_THAN_CP_MSG);
            result = false;
        }

        if (commissionMfTaxGreaterThanMf(amounts)) {
            addToContext(context, "taxOnMiscellaneousFee", TAX_ON_MF_GREATER_THAN_MF_MSG);
            result = false;
        }

        return result;
    }

    private BigDecimal totalAmount(RefundAmounts amounts) {
        return amounts.getGrossFare()
                .subtract(amounts.getLessGrossFareUsed())
                .add(amounts.getTax())
                .subtract(amounts.getMiscellaneousFee())
                .subtract(amounts.getTaxOnMiscellaneousFee())
                .subtract(amounts.getCancellationPenalty())
                .subtract(amounts.getTaxOnCancellationPenalty());
    }

    private boolean hasNullValues(RefundAmounts amounts) {
        return amounts.getCancellationPenalty() == null
                || amounts.getGrossFare() == null
                || amounts.getLessGrossFareUsed() == null
                || amounts.getMiscellaneousFee() == null
                || amounts.getRefundToPassenger() == null
                || amounts.getTax() == null
                || amounts.getTaxOnCancellationPenalty() == null
                || amounts.getTaxOnMiscellaneousFee() == null;
    }

    private boolean hasNegativeValues(RefundAmounts amounts) {
        return amounts.getCancellationPenalty().compareTo(BigDecimal.ZERO) < 0
                || amounts.getGrossFare().compareTo(BigDecimal.ZERO) < 0
                || amounts.getLessGrossFareUsed().compareTo(BigDecimal.ZERO) < 0
                || amounts.getMiscellaneousFee().compareTo(BigDecimal.ZERO) < 0
                || amounts.getRefundToPassenger().compareTo(BigDecimal.ZERO) < 0
                || amounts.getTax().compareTo(BigDecimal.ZERO) < 0
                || amounts.getTaxOnCancellationPenalty().compareTo(BigDecimal.ZERO) < 0
                || amounts.getTaxOnMiscellaneousFee().compareTo(BigDecimal.ZERO) < 0;
    }

    private boolean commissionCpTaxGreaterThanCp(RefundAmounts amounts) {
        if (amounts.getTaxOnCancellationPenalty() == null
                || amounts.getTaxOnCancellationPenalty().compareTo(BigDecimal.ZERO) == 0) {
            return false;
        }

        BigDecimal cp;
        if (amounts.getCancellationPenalty() == null) {
            cp = BigDecimal.ZERO;
        } else {
            cp = amounts.getCancellationPenalty();
        }

        return amounts.getTaxOnCancellationPenalty().compareTo(cp) > 0;
    }

    private boolean commissionMfTaxGreaterThanMf(RefundAmounts amounts) {
        if (amounts.getTaxOnMiscellaneousFee() == null
                || amounts.getTaxOnMiscellaneousFee().compareTo(BigDecimal.ZERO) == 0) {
            return false;
        }

        BigDecimal cp;
        if (amounts.getMiscellaneousFee() == null) {
            cp = BigDecimal.ZERO;
        } else {
            cp = amounts.getMiscellaneousFee();
        }

        return amounts.getTaxOnMiscellaneousFee().compareTo(cp) > 0;
    }

    private boolean commissionOnCpAndMfGreaterThanMfAndCp(RefundAmounts amounts) {
        if (amounts.getCommissionOnCpAndMfAmount() == null) {
            return false;
        }

        if (amounts.getCommissionOnCpAndMfAmount().compareTo(BigDecimal.ZERO) == 0) {
            return false;
        }

        BigDecimal cpAndMf;

        if (amounts.getCancellationPenalty() != null) {
            cpAndMf = amounts.getCancellationPenalty();
        } else {
            cpAndMf = BigDecimal.ZERO;
        }

        if (amounts.getMiscellaneousFee() != null) {
            cpAndMf = cpAndMf.add(amounts.getMiscellaneousFee());
        }

        return amounts.getCommissionOnCpAndMfAmount().compareTo(cpAndMf) > 0;
    }

    private boolean commissionGreaterThanRefundAmount(RefundAmounts amounts) {
        if (amounts.getCommissionAmount() == null && amounts.getSpam() == null) {
            return false;
        }

        if (amounts.getGrossFare() == null) {
            return false;
        }

        BigDecimal commission;

        if (amounts.getCommissionAmount() != null) {
            commission = amounts.getCommissionAmount();
        } else {
            commission = BigDecimal.ZERO;
        }

        if (amounts.getSpam() != null) {
            commission = commission.add(amounts.getSpam());
        }

        if (commission.compareTo(BigDecimal.ZERO) == 0) {
            return false;
        }

        BigDecimal refundAmount;

        BigDecimal lessGross = amounts.getLessGrossFareUsed();
        if (lessGross == null) {
            refundAmount = amounts.getGrossFare();
        } else {
            refundAmount = amounts.getGrossFare().subtract(lessGross);
        }

        return commission.compareTo(refundAmount) >= 0;
    }

    private boolean lessGrossGreaterThanGrossFare(RefundAmounts amounts) {
        if (amounts.getGrossFare() == null) {
            return false;
        }

        if (amounts.getLessGrossFareUsed() == null) {
            return false;
        }

        return amounts.getLessGrossFareUsed().compareTo(amounts.getGrossFare()) > 0;
    }

    private void addToContext(ConstraintValidatorContext context, String property, String message) {
        context.buildConstraintViolationWithTemplate(message)
            .addPropertyNode(property)
            .addConstraintViolation();
    }
}
