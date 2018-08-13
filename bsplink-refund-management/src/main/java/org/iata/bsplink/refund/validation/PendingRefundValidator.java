package org.iata.bsplink.refund.validation;

import org.apache.commons.lang3.StringUtils;
import org.iata.bsplink.refund.model.entity.Refund;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PendingRefundValidator implements Validator {
    public static final String AIRLINE_CODE_REQUIRED =
            "For a pending refund the Airline Code is required.";
    public static final String PASSENGER_REQUIRED =
            "For a pending refund the Passenger is required.";
    public static final String REASON_REQUIRED =
            "For a pending refund the Issue Reason is required.";
    public static final String DATE_OF_ISSUE_RELATED_DOCUMENT_REQUIRED =
            "For a pending refund the Issue Date of the related document is required.";
    public static final String AIRLINE_CODE_RELATED_DOCUMENT_REQUIRED =
            "For a pending refund the Airline Code of the related document is required.";


    @Override
    public boolean supports(Class<?> clazz) {
        return Refund.class.isAssignableFrom(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {
        Refund refund = (Refund) target;
        String notFoundErrorCode = "field.not_found";
        if (refund.getAirlineCode() == null) {
            errors.rejectValue("airlineCode", notFoundErrorCode, AIRLINE_CODE_REQUIRED);
        }

        if (StringUtils.isBlank(refund.getPassenger())) {
            errors.rejectValue("passenger", notFoundErrorCode, PASSENGER_REQUIRED);
        }

        if (refund.getAirlineCodeRelatedDocument() == null) {
            errors.rejectValue("airlineCodeRelatedDocument", notFoundErrorCode,
                    AIRLINE_CODE_RELATED_DOCUMENT_REQUIRED);
        }

        if (StringUtils.isBlank(refund.getIssueReason())) {
            errors.rejectValue("issueReason", notFoundErrorCode, REASON_REQUIRED);
        }

        if (refund.getDateOfIssueRelatedDocument() == null) {
            errors.rejectValue("dateOfIssueRelatedDocument", notFoundErrorCode,
                    DATE_OF_ISSUE_RELATED_DOCUMENT_REQUIRED);
        }
    }
}
