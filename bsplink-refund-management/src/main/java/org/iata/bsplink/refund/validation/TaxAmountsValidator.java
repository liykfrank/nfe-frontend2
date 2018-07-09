package org.iata.bsplink.refund.validation;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.model.entity.TaxMiscellaneousFee;
import org.iata.bsplink.refund.validation.constraints.TaxAmountsConstraint;


public class TaxAmountsValidator
    implements ConstraintValidator<TaxAmountsConstraint, Refund> {

    public static final String INCORRECT_TOTAL_MSG = "The sum of taxes is incorrect.";


    @Override
    public boolean isValid(Refund refund, ConstraintValidatorContext context) {

        if (refund.getAmounts() == null || refund.getAmounts().getTax() == null) {
            return true;
        }

        if (refund.getTaxMiscellaneousFees() == null) {
            return true;
        }

        BigDecimal total = refund.getTaxMiscellaneousFees().stream().filter(Objects::nonNull)
                .map(TaxMiscellaneousFee::getAmount).filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (refund.getAmounts().getTax().compareTo(total) != 0) {
            addToContext(context, "amounts.tax", INCORRECT_TOTAL_MSG);
            return false;
        }

        return true;
    }

    private void addToContext(ConstraintValidatorContext context, String property, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
            .addPropertyNode(property)
            .addConstraintViolation();
    }
}