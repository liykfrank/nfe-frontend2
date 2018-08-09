package org.iata.bsplink.refund.validation;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.iata.bsplink.refund.model.entity.FormOfPaymentAmount;
import org.iata.bsplink.refund.model.entity.FormOfPaymentType;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.validation.constraints.FormOfPaymentAmountsConstraint;


public class FormOfPaymentAmountsValidator
    implements ConstraintValidator<FormOfPaymentAmountsConstraint, Refund> {

    public static final String MAX_2_CC_MSG =
            "Only a maximum of two Credit Form of Payment Amounts are permitted.";
    public static final String MAX_1_MSG =
            "The Form of Payment is only permitted to be reported once.";

    public static final String INCORRECT_TOTAL_MSG =
            "The Refund to Passenger Amount expected to be equal"
            + " to the sum of Form of Payment Amounts.";


    @Override
    public boolean isValid(Refund refund, ConstraintValidatorContext context) {

        if (refund.getFormOfPaymentAmounts() == null) {
            return true;
        }

        boolean result = true;

        if (!isValidFormOfPayment(refund.getFormOfPaymentAmounts(), context)) {
            result = false;
        }

        if (!isValidTotal(refund)) {
            addToContext(context, "amounts.refundToPassenger", INCORRECT_TOTAL_MSG);
            result = false;
        }

        return result;
    }


    private boolean isValidTotal(Refund refund) {
        if (refund.getAmounts() == null || refund.getAmounts().getRefundToPassenger() == null) {
            return true;
        }

        BigDecimal total = refund.getFormOfPaymentAmounts().stream().filter(Objects::nonNull)
            .map(FormOfPaymentAmount::getAmount).filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return refund.getAmounts().getRefundToPassenger().compareTo(total) == 0;
    }


    private boolean isValidFormOfPayment(List<FormOfPaymentAmount> formOfPaymentAmounts,
            ConstraintValidatorContext context) {

        boolean result = true;
        int ccCount = 0;
        int i = -1;
        Set<FormOfPaymentType> fopTypes = new HashSet<>();
        for (FormOfPaymentAmount fop : formOfPaymentAmounts) {
            i++;
            if (fop == null || fop.getType() == null) {
                continue;
            }
            if (fop.getType().equals(FormOfPaymentType.CC)) {
                if (++ccCount > 2) {
                    result = false;
                    addToContext(context, "formOfPaymentAmounts[" + i + "].type", MAX_2_CC_MSG);
                }
            } else if (fopTypes.contains(fop.getType())) {
                result = false;
                addToContext(context, "formOfPaymentAmounts[" + i + "].type", MAX_1_MSG);
            }
            fopTypes.add(fop.getType());
        }
        return result;
    }




    private void addToContext(ConstraintValidatorContext context, String property, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
            .addPropertyNode(property)
            .addConstraintViolation();
    }
}