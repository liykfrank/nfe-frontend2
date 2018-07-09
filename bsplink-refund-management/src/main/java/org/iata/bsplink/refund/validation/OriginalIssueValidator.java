package org.iata.bsplink.refund.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.validation.constraints.OriginalIssueConstraint;
import org.springframework.stereotype.Component;

@Component
public class OriginalIssueValidator
        implements ConstraintValidator<OriginalIssueConstraint, Refund> {

    public static final String MISSING_ORIGINAL_ISSUE_MSG =
            "In case of an Exchange the Original Issue Data is expected ot be reported.";
    public static final String ONLY_EXCHANGE_MSG =
            "Original Issue Data only to be reported for an Exchange";
    public static final String INCORRECT_DATE_MSG =
            "The Original Issue Date cannot be a future date.";


    @Override
    public boolean isValid(Refund refund, ConstraintValidatorContext context) {
        boolean result = true;

        if (refund.getExchange() != null && refund.getExchange()
                && refund.getOriginalIssue() == null) {
            addMessage(context, "originalIssue", MISSING_ORIGINAL_ISSUE_MSG);
            result = false;
        }

        if ((refund.getExchange() == null || !refund.getExchange())
                && refund.getOriginalIssue() != null) {
            addMessage(context, "originalIssue", ONLY_EXCHANGE_MSG);
            result = false;
        }

        if ((refund.getExchange() != null && refund.getExchange())
                && refund.getOriginalIssue() != null
                && refund.getOriginalIssue().getOriginalDateOfIssue() != null
                && refund.getDateOfIssue() != null
                && refund.getDateOfIssue()
                        .isBefore(refund.getOriginalIssue().getOriginalDateOfIssue())) {
            addMessage(context, "originalIssue.originalDateOfIssue", INCORRECT_DATE_MSG);
            result = false;
        }

        return result;
    }


    private void addMessage(ConstraintValidatorContext context, String property, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addPropertyNode(property)
                .addConstraintViolation();
    }
}
