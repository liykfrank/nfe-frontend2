package org.iata.bsplink.refund.validation;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.validation.constraints.NetReportingConstraint;

public class NetReportingValidator implements ConstraintValidator<NetReportingConstraint, Refund> {

    public static final String NET_REPORTING_MSG =
            "Spam is only to be reported for Net Reporting.";

    @Override
    public boolean isValid(Refund refund, ConstraintValidatorContext context) {

        if (refund.getAmounts() == null
                || refund.getAmounts().getSpam() == null
                || refund.getAmounts().getSpam().compareTo(BigDecimal.ZERO) == 0) {
            return true;
        }

        if (refund.getNetReporting() == null || !refund.getNetReporting()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(NET_REPORTING_MSG)
                .addPropertyNode("amounts.spam")
                .addConstraintViolation();
            return false;
        }

        return true;
    }
}
