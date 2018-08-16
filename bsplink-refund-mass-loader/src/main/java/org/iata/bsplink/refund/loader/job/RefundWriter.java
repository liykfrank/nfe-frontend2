package org.iata.bsplink.refund.loader.job;

import static org.iata.bsplink.refund.loader.job.RefundJobParametersConverter.JOBID_PARAMETER_NAME;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Response;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import lombok.extern.apachecommons.CommonsLog;

import org.iata.bsplink.refund.loader.dto.Refund;
import org.iata.bsplink.refund.loader.dto.RefundEditable;
import org.iata.bsplink.refund.loader.dto.RefundStatus;
import org.iata.bsplink.refund.loader.dto.RefundStatusRequest;
import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.error.ValidationPhase;
import org.iata.bsplink.refund.loader.exception.RefundLoaderException;
import org.iata.bsplink.refund.loader.response.ValidationErrorResponse;
import org.iata.bsplink.refund.loader.restclient.RefundClient;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Does the update request to the refund service.
 */
@Component
@CommonsLog
public class RefundWriter extends RefundLoaderErrorsAware implements ItemWriter<Refund> {

    private RefundClient client;
    private String fileName;
    private ObjectMapper objectMapper;

    /**
     * Creates the writer.
     */
    public RefundWriter(RefundClient client, ObjectMapper objectMapper) {

        this.client = client;
        this.objectMapper = objectMapper;
    }

    /**
     * Before the first step is executed, jobid is read.
     */
    @Override
    public void beforeStep(StepExecution stepExecution) {

        fileName = stepExecution.getJobExecution().getJobParameters()
                .getString(JOBID_PARAMETER_NAME);

        super.beforeStep(stepExecution);
    }

    @Override
    public void write(List<? extends Refund> items) throws Exception {

        for (Refund item : items) {

            Optional<Refund> optionalRefund = findRefund(item);

            if (!optionalRefund.isPresent()) {

                addRefundError(item, "ticketDocumentNumber", "The Refund does not exist.");
                continue;
            }

            log.info(String.format("read refund: %s, refund to update: %s", item,
                    optionalRefund.get()));

            update(item, optionalRefund.get());
        }
    }

    private Optional<Refund> findRefund(Refund refundFromFile) {

        log.info(getLogMessageForRefund("searching refund", refundFromFile));

        ResponseEntity<Refund> response = client.findRefund(
                refundFromFile.getIsoCountryCode(),
                refundFromFile.getAirlineCode(),
                refundFromFile.getTicketDocumentNumber());

        if (HttpStatus.NOT_FOUND.equals(response.getStatusCode())) {

            log.info(getLogMessageForRefund("refund does not exist", refundFromFile));

            return Optional.empty();
        }

        throwExceptionIfResponseIsNotSuccessful(response);

        return Optional.ofNullable(response.getBody());
    }

    private String getLogMessageForRefund(String message, Refund refund) {

        return String.format(
                message + ": isoCountryCode=%s, airlineCode=%s, ticketDocumentNumber=%s",
                refund.getIsoCountryCode(), refund.getAirlineCode(),
                refund.getTicketDocumentNumber());
    }

    private void addRefundError(Refund refund, String fieldName, String message) {

        RefundLoaderError error = new RefundLoaderError();

        error.setField(fieldName);
        error.setMessage(message);
        error.setTransactionNumber(refund.getTransactionNumber());
        error.setValidationPhase(ValidationPhase.UPDATE);

        refundLoaderErrors.add(error);

        log.error(error);
    }

    private void update(Refund refundFromFile, Refund refundToUpdate) throws IOException {

        RefundStatus newStatus = refundFromFile.getStatus();

        log.info("writing refund: " + refundFromFile);

        if (RefundStatus.AUTHORIZED.equals(newStatus)) {

            BeanUtils.copyProperties(refundFromFile, refundToUpdate, RefundEditable.class);

            manageUpdateResponse(refundFromFile,
                    client.updateRefund(refundToUpdate.getId(), fileName, refundToUpdate));

            log.info(String.format("updated refund (%s): %s", fileName, refundToUpdate));

        } else {

            RefundStatusRequest statusRequest = new RefundStatusRequest();
            statusRequest.setStatus(newStatus);

            if (RefundStatus.REJECTED.equals(newStatus)) {

                statusRequest.setRejectionReason(refundFromFile.getRejectionReason());

            } else {

                statusRequest.setAirlineRemark(refundFromFile.getAirlineRemark());
            }

            manageUpdateResponse(refundFromFile,
                    client.updateStatus(refundToUpdate.getId(), fileName, statusRequest));

            log.info(String.format("refund status request (%s): %s", fileName, statusRequest));
        }

    }

    private void manageUpdateResponse(Refund refundFromFile, Response response)
            throws IOException {

        if (HttpStatus.valueOf(response.status()).equals(HttpStatus.BAD_REQUEST)) {

            addValidationErrors(refundFromFile, objectMapper
                    .readValue(response.body().asInputStream(), ValidationErrorResponse.class));

        } else {

            throwExceptionIfResponseIsNotSuccessful(response);
        }
    }

    private void addValidationErrors(Refund refund,
            ValidationErrorResponse validationErrorResponse) {

        validationErrorResponse.getValidationErrors().stream().forEach(x ->
            addRefundError(refund, x.getFieldName(), x.getMessage())
        );
    }

    private void throwExceptionIfResponseIsNotSuccessful(Response response) {

        if (!HttpStatus.valueOf(response.status()).is2xxSuccessful()) {

            throw new RefundLoaderException(response.toString());
        }
    }

    private void throwExceptionIfResponseIsNotSuccessful(ResponseEntity<?> response) {

        if (!response.getStatusCode().is2xxSuccessful()) {

            throw new RefundLoaderException(response.toString());
        }
    }

}
