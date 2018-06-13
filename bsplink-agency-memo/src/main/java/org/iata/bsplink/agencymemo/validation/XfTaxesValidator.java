package org.iata.bsplink.agencymemo.validation;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.dto.TaxMiscellaneousFeeRequest;
import org.iata.bsplink.agencymemo.validation.constraints.AgentConstraint;


public class XfTaxesValidator
        implements ConstraintValidator<AgentConstraint, AcdmRequest> {
    public static final String ONLY_ONE_XF_MSG = "Only one XF tax permitted.";
    public static final String XF_FIRST_MSG = "'XF' tax should be reported first.";
    public static final String CONSECUTIVE_MSG = "XF-taxes should be reported one after the other.";
    public static final String XF_MISSING_MSG = "XF-taxes without amount to be reported.";
    public static final String ZERO_AMOUNT_MSG =
            "When 'XF' tax is reported, then XF-amount difference expected to be zero.";


    @Override
    public boolean isValid(AcdmRequest acdm,
            ConstraintValidatorContext context) {

        List<TaxMiscellaneousFeeRequest> taxes = acdm.getTaxMiscellaneousFees();
        if (taxes == null) {
            return true;
        }

        if (taxes.stream().anyMatch(Objects::isNull)) {
            return true;
        }

        if (taxes.stream().anyMatch(tax -> tax.getType() == null || tax.getAgentAmount() == null
                || tax.getAirlineAmount() == null)) {
            return true;
        }

        context.disableDefaultConstraintViolation();

        return isValidXfTaxes(taxes, context);
    }

    private int taxDiffSignum(TaxMiscellaneousFeeRequest tax) {
        return tax.getAirlineAmount().subtract(tax.getAgentAmount()).signum();
    }

    private String property(int i) {
        return String.format("taxMiscellaneousFees[%d].type", i);
    }

    private boolean isValidXfTaxes(List<TaxMiscellaneousFeeRequest> taxes,
            ConstraintValidatorContext context) {

        List<String> taxTypes = taxes.stream().map(TaxMiscellaneousFeeRequest::getType)
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

    private boolean isValidXfTaxesWithoutAmount(List<TaxMiscellaneousFeeRequest> taxes, int xfIndex,
            ConstraintValidatorContext context) {
        int i = -1;
        boolean isXfReported = false;
        for (TaxMiscellaneousFeeRequest tax : taxes) {
            i++;
            if (tax.getType().matches("^XF.+$")) {
                if (taxDiffSignum(tax) != 0) {
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
        context
            .buildConstraintViolationWithTemplate(message)
            .addPropertyNode(property)
            .addConstraintViolation();
    }
}
