package org.iata.bsplink.refund.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.iata.bsplink.refund.model.entity.FormOfPaymentAmount;
import org.iata.bsplink.refund.validation.constraints.FormOfPaymentConsistentDataConstraint;

public class FormOfPaymentConsistentDataValidator
        implements ConstraintValidator<FormOfPaymentConsistentDataConstraint, FormOfPaymentAmount> {

    @Override
    public boolean isValid(FormOfPaymentAmount value, ConstraintValidatorContext context) {

        return !(isOnCreditButLackesOfCreditData(value) || isCashButHasCreditData(value));
    }

    private boolean isOnCreditButLackesOfCreditData(FormOfPaymentAmount value) {

        if (value.getType().isCash()) {

            return false;
        }

        return StringUtils.isEmpty(value.getNumber())
                || StringUtils.isEmpty(value.getVendorCode());
    }

    private boolean isCashButHasCreditData(FormOfPaymentAmount value) {

        if (!value.getType().isCash()) {

            return false;
        }

        return ! (StringUtils.isEmpty(value.getNumber())
                &&  StringUtils.isEmpty(value.getVendorCode()));
    }
}
