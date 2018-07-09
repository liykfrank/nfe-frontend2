package org.iata.bsplink.refund.validation;

import org.iata.bsplink.refund.dto.RefundStatusRequest;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.model.entity.RefundStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class MassloadValidator {
    public static final String INCORRECT_STATUS = "The refund status is incorrect.";
    public static final String INCORRECT_FILENAME =
            "The fileName parameter value has incorrect format.";
    public static final String INCORRECT_COUNTRY =
            "The fileName and refund's ISO country code do not match.";
    public static final String INCORRECT_AIRLINE =
            "The fileName and refund's airline code do not match.";

    @Autowired
    RefundUpdateValidator refundUpdateValidator;

    @Autowired
    RefundStatusValidator refundStatusValidator;

    /**
     * Validates refund request received from Massload File.
     */
    public Refund validate(Long id, Refund refund, String fileName, Errors errors) {
        if (fileName == null
                || !fileName.matches("^[A-Z]{2}e9EARS_\\d{8}_[A-Z0-9]{3}[0-69]_\\d{3}")) {
            errors.reject("param.incorrect_format", INCORRECT_FILENAME);
        }

        if (fileName != null && refund.getIsoCountryCode() != null
                && !fileName.startsWith(refund.getIsoCountryCode())) {
            errors.reject("param.incorrect_country", INCORRECT_COUNTRY);
        }

        if (fileName != null && refund.getAirlineCode() != null
                && fileName.indexOf(refund.getAirlineCode(), 18) != 18) {
            errors.reject("param.incorrect_airline", INCORRECT_AIRLINE);
        }

        if (refund.getStatus().equals(RefundStatus.PENDING)
                || refund.getStatus().equals(RefundStatus.DRAFT)
                || refund.getStatus().equals(RefundStatus.PENDING_SUPERVISION)) {
            errors.rejectValue("status", "incorrect_status", INCORRECT_STATUS);
        }

        Refund refundToSave = refundUpdateValidator.validate(id, refund, errors, false);

        RefundStatusRequest refundStatusRequest = new RefundStatusRequest(refund.getStatus(),
                refund.getAirlineRemark(), refund.getRejectionReason());

        refundStatusValidator.validate(refundToSave, refundStatusRequest, errors);

        return refundToSave;
    }

}
