package org.iata.bsplink.refund.validation;

import org.iata.bsplink.refund.dto.RefundStatusRequest;
import org.iata.bsplink.refund.model.entity.Refund;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class MassloadValidator {
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

    @Autowired
    MassloadFileNameValidator fileNameValidator;

    /**
     * Validates refund request received from massload file.
     */
    public Refund validate(Long id, Refund refund, String fileName, Errors errors) {

        fileNameValidator.validate(refund, fileName, errors);

        Refund refundToSave = refundUpdateValidator.validate(id, refund, errors, false);

        RefundStatusRequest refundStatusRequest = new RefundStatusRequest(refund.getStatus(),
                refund.getAirlineRemark(), refund.getRejectionReason());

        refundStatusValidator.validate(refundToSave, refundStatusRequest, errors);

        return refundToSave;
    }

}
