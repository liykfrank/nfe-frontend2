package org.iata.bsplink.refund.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.validation.constraints.DateOfIssueRelatedDocumentConstraint;
import org.springframework.stereotype.Component;

@Component
public class DateOfIssueRelatedDocumentValidator
        implements ConstraintValidator<DateOfIssueRelatedDocumentConstraint, Refund> {

    public static final String INCORRECT_DATE_MSG =
            "The Date of Issue Date of the related document cannot be a future date.";

    @Override
    public boolean isValid(Refund refund, ConstraintValidatorContext context) {
        if (refund.getDateOfIssueRelatedDocument() == null || refund.getDateOfIssue() == null) {
            return true;
        }

        if (refund.getDateOfIssue().isBefore(refund.getDateOfIssueRelatedDocument())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(INCORRECT_DATE_MSG)
                    .addPropertyNode("dateOfIssueRelatedDocument")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}
