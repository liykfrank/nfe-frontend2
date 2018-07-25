package org.iata.bsplink.agencymemo.validation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.dto.CalculationsRequest;
import org.iata.bsplink.agencymemo.dto.TaxMiscellaneousFeeRequest;
import org.iata.bsplink.agencymemo.validation.constraints.DecimalsConstraint;


public class DecimalsValidator implements ConstraintValidator<DecimalsConstraint, AcdmRequest> {

    public static final String DECIMALS_MSG =
            "Incorrect number of amount's decimals for the specified currency.";

    @Override
    public boolean isValid(AcdmRequest acdm, ConstraintValidatorContext context) {

        if (acdm.getCurrency() == null) {
            return true;
        }

        Integer decimals = acdm.getCurrency().getDecimals();
        if (decimals == null) {
            return true;
        }

        return isValidDecimals(acdm, decimals, context);
    }


    private boolean isValidDecimals(AcdmRequest acdm, int decimals,
            ConstraintValidatorContext context) {
        boolean result = true;

        if (!isValidAmountDecimals(decimals, acdm.getAgentCalculations(), "agentCalculations",
                context)) {
            result = false;
        }

        if (!isValidAmountDecimals(decimals, acdm.getAirlineCalculations(), "airlineCalculations",
                context)) {
            result = false;
        }

        if (!isValidAmount(acdm.getAmountPaidByCustomer(), decimals, "amountPaidByCustomer",
                context)) {
            result = false;
        }

        if (acdm.getTaxMiscellaneousFees() != null
                && !isValidTaxAmount(acdm.getTaxMiscellaneousFees(), decimals, context)) {
            result = false;
        }

        return result;
    }


    private boolean isValidTaxAmount(List<TaxMiscellaneousFeeRequest> taxMiscellaneousFees,
            int decimals, ConstraintValidatorContext context) {
        boolean result = true;
        int i = -1;
        for (TaxMiscellaneousFeeRequest tax: taxMiscellaneousFees) {
            i++;
            if (tax != null) {
                if (!isValidAmount(tax.getAgentAmount(), decimals,
                        "taxMiscellaneousFees[" + i + "].agentAmount", context)) {
                    result = false;
                }
                if (!isValidAmount(tax.getAirlineAmount(), decimals,
                        "taxMiscellaneousFees[" + i + "].airlineAmount", context)) {
                    result = false;
                }
            }
        }
        return result;
    }

    private boolean isValidAmountDecimals(int decimals, CalculationsRequest calculations,
            String field, ConstraintValidatorContext context) {
        boolean result = true;
        result = isValidAmount(calculations.getCommission(), decimals, field + ".commission",
                context) && result;
        result = isValidAmount(calculations.getFare(), decimals, field + ".fare", context)
                && result;
        result = isValidAmount(calculations.getSpam(), decimals, field + ".spam", context)
                && result;
        result = isValidAmount(calculations.getTax(), decimals, field + ".tax", context)
                && result;
        result = isValidAmount(calculations.getTaxOnCommission(), decimals,
                field + ".taxOnCommission", context) && result;
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
