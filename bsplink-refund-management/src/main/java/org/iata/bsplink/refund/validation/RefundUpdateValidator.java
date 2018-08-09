package org.iata.bsplink.refund.validation;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import lombok.extern.java.Log;

import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.model.entity.RefundStatus;
import org.iata.bsplink.refund.model.repository.RefundRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Log
@Component
public class RefundUpdateValidator {

    @Value("${app.refund_update_ignored_fields}")
    private String[] refundUpdateIgnoredFields;

    private static final String CANNOT_BE_MODIFIED_MESSAGE = " cannot be modified";
    private static final String STATUS_MESSAGE = " can only be modified in status: ";
    private static final String DRAFT_PENDING_AND_PENDING_SUPERVISION_MESSAGE =
            " can only be modified in DRAFT, PENDING and PENDING_SUPERVISION status";
    private static final String STATUS_NOT_MODIFIED_MESSAGE =
            "Refund's status cannot be modified: ";
    private static final String STATUS_NOT_ALLOW_MODIFIED_MESSAGE =
            "Refund with status not allowed to be modified: ";
    private static final String REFUND_NOT_FOUND_MESSAGE = "Refund not found";
    private static final String IGNORED_FIELDS_MESSAGE =
            "Copy new field values. Ignored not allow to modify: ";

    private RefundRepository refundRepository;

    public RefundUpdateValidator(RefundRepository refundRepository) {
        this.refundRepository = refundRepository;
    }

    /**
     * Returns refund from database with new passed values ignoring not allow to modify fields.
     *
     * @param newRefund The refund with new values
     * @param errors List of errors
     * @return Refund New refund with new values
     */
    public Refund validate(Long id, Refund newRefund, Errors errors,
            boolean statusValidationEnabled) {

        log.info("Validating refund to update: " + newRefund);

        Optional<Refund> oldRefund = refundRepository.findById(id);

        if (oldRefund.isPresent()) {

            validateFields(oldRefund.get(), newRefund, errors, statusValidationEnabled);

            if (errors.hasErrors()) {
                throw new ApplicationValidationException(errors);
            }

            log.info(IGNORED_FIELDS_MESSAGE + Arrays.toString(refundUpdateIgnoredFields));

            BeanUtils.copyProperties(newRefund, oldRefund.get(), refundUpdateIgnoredFields);

            oldRefund.get().setId(id);

            return oldRefund.get();

        } else {
            log.info(REFUND_NOT_FOUND_MESSAGE);
            errors.rejectValue("", "", REFUND_NOT_FOUND_MESSAGE);
            return null;
        }
    }

    public Refund validate(Long id, Refund newRefund, Errors errors) {
        return validate(id, newRefund, errors, true);
    }

    /**
     * Adds errors to passed errors list if changes are made in new refund.
     *
     * @param oldRefund Old values
     * @param newRefund New values
     * @param errors List of errors
     */
    public void validateFields(Refund oldRefund, Refund newRefund, Errors errors,
            boolean statusValidationEnabled) {

        if (statusValidationEnabled && (newRefund.getStatus() == null
                || !newRefund.getStatus().equals(oldRefund.getStatus()))) {
            log.info(STATUS_NOT_MODIFIED_MESSAGE + newRefund.getStatus());
            errors.rejectValue("status", "", STATUS_NOT_MODIFIED_MESSAGE);
            throw new ApplicationValidationException(errors);
        }

        if (oldRefund.getStatus().equals(RefundStatus.AUTHORIZED)
                || oldRefund.getStatus().equals(RefundStatus.REJECTED)
                || oldRefund.getStatus().equals(RefundStatus.RESUBMITTED)) {
            log.info(STATUS_NOT_ALLOW_MODIFIED_MESSAGE + newRefund.getStatus());
            errors.rejectValue("status", "",
                    STATUS_NOT_ALLOW_MODIFIED_MESSAGE + oldRefund.getStatus());
            throw new ApplicationValidationException(errors);
        }

        validateFileWithStatus("airlineCode", newRefund.getAirlineCode(),
                oldRefund.getAirlineCode(), oldRefund.getStatus(), RefundStatus.DRAFT, errors);

        validateField("agentCode", newRefund.getAgentCode(), oldRefund.getAgentCode(), errors);

        validateField("isoCountryCode", newRefund.getIsoCountryCode(),
                oldRefund.getIsoCountryCode(), errors);

        validateDraftPendingAndPendingSupervisionStatus("currency",
                newRefund.getCurrency().getCode(), oldRefund.getCurrency().getCode(),
                oldRefund.getStatus(), errors);

        validateDraftPendingAndPendingSupervisionStatus("passenger", newRefund.getPassenger(),
                oldRefund.getPassenger(), oldRefund.getStatus(), errors);

        validateDraftPendingAndPendingSupervisionStatus("dateOfIssue", newRefund.getDateOfIssue(),
                oldRefund.getDateOfIssue(), oldRefund.getStatus(), errors);

        validateDraftPendingAndPendingSupervisionStatus("netReporting", newRefund.getNetReporting(),
                oldRefund.getNetReporting(), oldRefund.getStatus(), errors);

        validateDraftPendingAndPendingSupervisionStatus("tourCode", newRefund.getTourCode(),
                oldRefund.getTourCode(), oldRefund.getStatus(), errors);

        validateDraftPendingAndPendingSupervisionStatus("dateOfIssueRelatedDocument",
                newRefund.getDateOfIssueRelatedDocument(),
                oldRefund.getDateOfIssueRelatedDocument(), oldRefund.getStatus(), errors);

        validateField("dateOfAirlineAction", newRefund.getDateOfAirlineAction(),
                oldRefund.getDateOfAirlineAction(), errors);

        validateDraftPendingAndPendingSupervisionStatus("exchange", newRefund.getExchange(),
                oldRefund.getExchange(), oldRefund.getStatus(), errors);

        validateDraftPendingAndPendingSupervisionStatus("agentContact", newRefund.getAgentCode(),
                oldRefund.getAgentCode(), oldRefund.getStatus(), errors);

        validateFileWithStatus("airlineContact", newRefund.getAirlineContact(),
                oldRefund.getAirlineContact(), oldRefund.getStatus(),
                RefundStatus.UNDER_INVESTIGATION, errors);

        validateDraftPendingAndPendingSupervisionStatus("settlementAuthorisationCode",
                newRefund.getSettlementAuthorisationCode(),
                oldRefund.getSettlementAuthorisationCode(), oldRefund.getStatus(), errors);

        validateDraftPendingAndPendingSupervisionStatus("agentVatNumber",
                newRefund.getAgentVatNumber(), oldRefund.getAgentVatNumber(), oldRefund.getStatus(),
                errors);

        validateFileWithStatus("airlineVatNumber", newRefund.getAirlineVatNumber(),
                oldRefund.getAirlineVatNumber(), oldRefund.getStatus(),
                RefundStatus.UNDER_INVESTIGATION, errors);

        validateFileWithStatus("airlineRegistrationNumber",
                newRefund.getAirlineRegistrationNumber(), oldRefund.getAirlineRegistrationNumber(),
                oldRefund.getStatus(), RefundStatus.UNDER_INVESTIGATION, errors);

        validateFileWithStatus("airlineRemark", newRefund.getAirlineRemark(),
                oldRefund.getAirlineRemark(), oldRefund.getStatus(),
                RefundStatus.UNDER_INVESTIGATION, errors);

        validateDraftPendingAndPendingSupervisionStatus("issueReason", newRefund.getIssueReason(),
                oldRefund.getIssueReason(), oldRefund.getStatus(), errors);

        validateDraftPendingAndPendingSupervisionStatus("airlineCodeRelatedDocument",
                newRefund.getAirlineCodeRelatedDocument(),
                oldRefund.getAirlineCodeRelatedDocument(), oldRefund.getStatus(), errors);

        validateDraftPendingAndPendingSupervisionStatus(
                "relatedDocument.relatedTicketDocumentNumber",
                newRefund.getRelatedDocument().getRelatedTicketDocumentNumber(),
                oldRefund.getRelatedDocument().getRelatedTicketDocumentNumber(),
                oldRefund.getStatus(), errors);

        validateDraftPendingAndPendingSupervisionStatus("conjunctions",
                newRefund.getConjunctions().size(), oldRefund.getConjunctions().size(),
                oldRefund.getStatus(), errors);


        validateDraftPendingAndPendingSupervisionStatus("agentRegistrationNumber",
                newRefund.getAgentRegistrationNumber(), oldRefund.getAgentRegistrationNumber(),
                oldRefund.getStatus(), errors);


        validateCollection("history", newRefund.getHistory(), errors);
        validateCollection("attachedFiles", newRefund.getAttachedFiles(), errors);
        validateCollection("comments", newRefund.getComments(), errors);
    }

    /**
     * Returns false if the old and new value are not equals. Otherwise returns true.
     *
     * @param field The name
     * @param newValue The new value to validate
     * @param oldValue The old value to validate
     * @param errors List of errors
     * @return boolean
     */
    public boolean validateField(String field, Object newValue, Object oldValue, Errors errors) {
        if (newValue != null && oldValue != null && !newValue.equals(oldValue)) {
            log.info(field + CANNOT_BE_MODIFIED_MESSAGE);
            errors.rejectValue(field, "", field + CANNOT_BE_MODIFIED_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Returns false if the status is not DRAFT. Otherwise returns true.
     *
     * @param field The name of the field
     * @param newValue The new value to validate
     * @param oldValue The old value to validate
     * @param errors Lists of errors
     * @return boolean
     */
    public boolean validateFileWithStatus(String field, Object newValue, Object oldValue,
            RefundStatus refundStatus, RefundStatus statusAllowed, Errors errors) {
        if (newValue != null && oldValue != null && !newValue.equals(oldValue)
                && !refundStatus.equals(statusAllowed)) {
            log.info(field + STATUS_MESSAGE + statusAllowed);
            errors.rejectValue(field, "", field + STATUS_MESSAGE + statusAllowed);
            return false;
        }
        return true;
    }

    /**
     * Returns false if the status is not DRAFT, PENDING or PENDING_SUPERVISION. Otherwise returns
     * true.
     *
     * @param field The name of the field
     * @param newValue The new value to validate.
     * @param oldValue The old value to validate.
     * @param status The Refund status
     * @param errors Lists of errors
     * @return boolean
     */
    public boolean validateDraftPendingAndPendingSupervisionStatus(String field, Object newValue,
            Object oldValue, RefundStatus status, Errors errors) {
        if (newValue != null && oldValue != null && !newValue.equals(oldValue)
                && (!status.equals(RefundStatus.DRAFT) && !status.equals(RefundStatus.PENDING)
                        && !status.equals(RefundStatus.PENDING_SUPERVISION))) {
            log.info(field + DRAFT_PENDING_AND_PENDING_SUPERVISION_MESSAGE);
            errors.rejectValue(field, "", field + DRAFT_PENDING_AND_PENDING_SUPERVISION_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Returns false if list is not null or is not empty. Otherwise returns true.
     *
     * @param list The list to evaluate
     * @param field Name of the list
     * @param errors List of errors
     * @return boolean
     */
    public boolean validateCollection(String field, List<?> list, Errors errors) {
        if (null != list && !list.isEmpty()) {
            log.info(field + CANNOT_BE_MODIFIED_MESSAGE);
            errors.rejectValue(field, "", field + CANNOT_BE_MODIFIED_MESSAGE);
            return false;
        }
        return true;
    }
}
