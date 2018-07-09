package org.iata.bsplink.refund.validation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.model.entity.TaxMiscellaneousFee;
import org.iata.bsplink.refund.validation.constraints.XfTaxesConstraint;


public class XfTaxesValidator
        implements ConstraintValidator<XfTaxesConstraint, Refund> {
    public static final String ONLY_ONE_XF_MSG = "Only one XF tax permitted.";
    public static final String XF_FIRST_MSG = "'XF' tax should be reported first.";
    public static final String CONSECUTIVE_MSG = "XF-taxes should be reported one after the other.";
    public static final String XF_MISSING_MSG = "XF-taxes without amount to be reported.";
    public static final String ZERO_AMOUNT_MSG =
            "When 'XF' tax is reported, then XF-amount expected to be zero.";


    @Override
    public boolean isValid(Refund refund,
            ConstraintValidatorContext context) {

        List<TaxMiscellaneousFee> taxes = refund.getTaxMiscellaneousFees();
        if (taxes == null) {
            return true;
        }

        if (taxes.stream().anyMatch(Objects::isNull)) {
            return true;
        }

        if (taxes.stream().anyMatch(tax -> tax.getType() == null || tax.getAmount() == null)) {
            return true;
        }

        context.disableDefaultConstraintViolation();

        return isValidXfTaxes(taxes, context);
    }


    private String property(int i) {
        return String.format("taxMiscellaneousFees[%d].type", i);
    }

    private boolean isValidXfTaxes(List<TaxMiscellaneousFee> taxes,
            ConstraintValidatorContext context) {

        List<String> taxTypes = taxes.stream().map(TaxMiscellaneousFee::getType)
                .collect(Collectors.toList());

        int xfIndex = taxTypes.indexOf("XF");

        if (xfIndex != taxTypes.lastIndexOf("XF")) {
            addMessage(context, property(taxTypes.lastIndexOf("XF")), ONLY_ONE_XF_MSG);
            return false;
        }

        if (xfIndex >= 0) {
            if (taxTypes.stream().limit(xfIndex).anyMatch(taxType -> taxType.startsWith("XF"))) {
                addMessage(context, property(xfIndex), XF_FIRST_MSG);
                return false;
            }
            if (!isValidXfTaxesWithoutAmount(taxes, xfIndex, context)) {
                return false;
            }
        }

        int i = -1;
        boolean firstXfFound = false;
        for (String taxType : taxTypes) {
            i++;
            if (taxType.startsWith("XF")) {
                if (firstXfFound && !taxTypes.get(i - 1).startsWith("XF")) {
                    addMessage(context, property(i), CONSECUTIVE_MSG);
                    return false;
                }
                firstXfFound = true;
            }
        }

        return true;
    }

    private boolean isValidXfTaxesWithoutAmount(List<TaxMiscellaneousFee> taxes, int xfIndex,
            ConstraintValidatorContext context) {
        int i = -1;
        boolean isXfReported = false;
        for (TaxMiscellaneousFee tax : taxes) {
            i++;
            if (tax.getType().matches("^XF.+$")) {
                if (tax.getAmount().compareTo(BigDecimal.ZERO) != 0) {
                    addMessage(context, property(i), ZERO_AMOUNT_MSG);
                    return false;
                }
                isXfReported = true;
            }
        }

        if (!isXfReported) {
            addMessage(context, property(xfIndex), XF_MISSING_MSG);
            return false;
        }

        return true;
    }

    private void addMessage(ConstraintValidatorContext context, String property, String message) {
        context.disableDefaultConstraintViolation();
        context
            .buildConstraintViolationWithTemplate(message)
            .addPropertyNode(property)
            .addConstraintViolation();
    }
}
