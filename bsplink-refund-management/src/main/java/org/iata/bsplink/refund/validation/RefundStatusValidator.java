package org.iata.bsplink.refund.validation;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.time.LocalDate;

import lombok.extern.java.Log;

import org.apache.commons.lang3.StringUtils;
import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
import org.iata.bsplink.refund.dto.RefundStatusRequest;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.model.entity.RefundStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Log
@Component
public class RefundStatusValidator {
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

    private static final String STATUS = "status";

    /**
     * Validates that refund's status is correct. Validating that the origin and final status are
     * corrects. Returns the final refund to save.
     *
     * @param refund The origin refund where fields are validated.
     * @param refundStatusRequest The new status, airlineRemark and rejectionReason.
     * @param errors A list with fields unsuccessful validations.
     * @return Refund final refund with new fields
     */
    public Refund validate(Refund refund, RefundStatusRequest refundStatusRequest, Errors errors) {

        log.info(String.format("Validating status from %s to %s", refund.getStatus(),
                refundStatusRequest.getStatus()));

        if (!checkStatus(refund.getStatus(), refundStatusRequest.getStatus())) {
            log.info(String.format("Change of status not allowed from %s to %s", refund.getStatus(),
                    refundStatusRequest.getStatus()));
            errors.rejectValue(STATUS, "", "Change of status not allowed");
            throw new ApplicationValidationException(errors);
        }

        if (refundStatusRequest.getStatus().equals(RefundStatus.PENDING)
                || refundStatusRequest.getStatus().equals(RefundStatus.PENDING_SUPERVISION)) {
            validatePendingRefund(refund, errors);
            if (errors.hasErrors()) {
                throw new ApplicationValidationException(errors);
            }
        }

        if (refundStatusRequest.getStatus().equals(RefundStatus.REJECTED)
                || refundStatusRequest.getStatus().equals(RefundStatus.AUTHORIZED)
                || refundStatusRequest.getStatus().equals(RefundStatus.UNDER_INVESTIGATION)) {
            validateAirlineStatusChange(refund, refundStatusRequest, errors);
        }

        refund.setStatus(refundStatusRequest.getStatus());

        return refund;
    }


    private void validateAirlineStatusChange(
            Refund refund, RefundStatusRequest refundStatusRequest, Errors errors) {

        if (refundStatusRequest.getStatus().equals(RefundStatus.AUTHORIZED)) {
            if (refund.getAmounts().getRefundToPassenger().signum() < 1) {
                log.info("Refund to passenger must be greater than cero");
                errors.rejectValue(STATUS, "",
                        "An authorized refund's total amount is expected to be greater than cero");
                throw new ApplicationValidationException(errors);
            }
            log.info("Setting billing period");
            refund.setBillingPeriod(2018124);
        }

        if (refundStatusRequest.getStatus().equals(RefundStatus.UNDER_INVESTIGATION)) {
            log.info("Setting billing period and rejection reason to null");
            refund.setBillingPeriod(null);
            refund.setRejectionReason(null);
        }

        if (refundStatusRequest.getStatus().equals(RefundStatus.REJECTED)) {

            if (StringUtils.isBlank(refundStatusRequest.getRejectionReason())) {
                log.info("Rejection reason is required");
                errors.rejectValue("rejectionReason", "", "Field required.");
                throw new ApplicationValidationException(errors);
            }

            log.info("Setting rejection reason: " + refundStatusRequest.getRejectionReason());
            refund.setRejectionReason(refundStatusRequest.getRejectionReason());
        }

        refund.setAirlineRemark(refundStatusRequest.getAirlineRemark());
        refund.setDateOfAirlineAction(LocalDate.now());
    }


    /**
    * Validates mandatory fields for refund in pending state.
    */
    public void validatePendingRefund(Refund refund, Errors errors) {
        String missingData = "missing_data";
        if (refund.getAirlineCode() == null) {
            errors.rejectValue(STATUS, missingData, AIRLINE_CODE_REQUIRED);
        }

        if (StringUtils.isBlank(refund.getPassenger())) {
            errors.rejectValue(STATUS, missingData, PASSENGER_REQUIRED);
        }

        if (refund.getAirlineCodeRelatedDocument() == null) {
            errors.rejectValue(STATUS, missingData, AIRLINE_CODE_RELATED_DOCUMENT_REQUIRED);
        }

        if (StringUtils.isBlank(refund.getIssueReason())) {
            errors.rejectValue(STATUS, missingData, REASON_REQUIRED);
        }

        if (refund.getDateOfIssueRelatedDocument() == null) {
            errors.rejectValue(STATUS, missingData, DATE_OF_ISSUE_RELATED_DOCUMENT_REQUIRED);
        }
    }

    /**
     * Returns true if the status flow is correct. Otherwise returns false.
     *
     * @param fromStatus The origin status
     * @param toStatus The final status
     * @return boolean true or false
     */
    public boolean checkStatus(RefundStatus fromStatus, RefundStatus toStatus) {
        Multimap<RefundStatus, RefundStatus> multimap = ArrayListMultimap.create();
        multimap.put(RefundStatus.DRAFT, RefundStatus.PENDING);
        multimap.put(RefundStatus.DRAFT, RefundStatus.PENDING_SUPERVISION);
        multimap.put(RefundStatus.PENDING_SUPERVISION, RefundStatus.PENDING);
        multimap.put(RefundStatus.PENDING, RefundStatus.UNDER_INVESTIGATION);
        multimap.put(RefundStatus.PENDING, RefundStatus.AUTHORIZED);
        multimap.put(RefundStatus.PENDING, RefundStatus.REJECTED);
        multimap.put(RefundStatus.UNDER_INVESTIGATION, RefundStatus.UNDER_INVESTIGATION);
        multimap.put(RefundStatus.UNDER_INVESTIGATION, RefundStatus.REJECTED);
        multimap.put(RefundStatus.UNDER_INVESTIGATION, RefundStatus.AUTHORIZED);
        multimap.put(RefundStatus.REJECTED, RefundStatus.UNDER_INVESTIGATION);
        multimap.put(RefundStatus.AUTHORIZED, RefundStatus.UNDER_INVESTIGATION);
        return multimap.containsEntry(fromStatus, toStatus);
    }
}
