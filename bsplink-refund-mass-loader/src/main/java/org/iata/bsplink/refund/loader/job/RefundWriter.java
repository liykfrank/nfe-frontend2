package org.iata.bsplink.refund.loader.job;

import static org.iata.bsplink.refund.loader.job.RefundJobParametersConverter.JOBID_PARAMETER_NAME;

import java.util.List;

import lombok.extern.java.Log;

import org.iata.bsplink.refund.loader.dto.Refund;
import org.iata.bsplink.refund.loader.dto.RefundEditable;
import org.iata.bsplink.refund.loader.dto.RefundStatus;
import org.iata.bsplink.refund.loader.dto.RefundStatusRequest;
import org.iata.bsplink.refund.loader.restclient.RefundClient;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Does the update request to the refund service.
 */
@Component
@Log
public class RefundWriter implements ItemWriter<Refund> {

    private RefundClient client;
    private String fileName;


    /**
     * Before the first step is executed, jobid is read.
     */
    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {

        fileName = stepExecution.getJobExecution().getJobParameters()
                .getString(JOBID_PARAMETER_NAME);
    }


    public RefundWriter(RefundClient client) {

        this.client = client;
    }

    @Override
    public void write(List<? extends Refund> items) throws Exception {

        for (Refund item : items) {

            log.info(String.format("writing refund: %s", item.toString()));

            update(item, findRefund(item).getBody());
        }
    }

    private ResponseEntity<Refund> findRefund(Refund refundFromFile) {

        ResponseEntity<Refund> refundToUpdate = client.findRefund(
                refundFromFile.getIsoCountryCode(),
                refundFromFile.getAirlineCode(),
                refundFromFile.getTicketDocumentNumber());

        log.info(String.format("read refund: %s, refund to update: %s", refundFromFile,
                refundToUpdate));

        return refundToUpdate;
    }



    private void update(Refund refundFromFile, Refund refundToUpdate) {

        RefundStatus newStatus = refundFromFile.getStatus();

        if (RefundStatus.AUTHORIZED.equals(newStatus)) {

            BeanUtils.copyProperties(refundFromFile, refundToUpdate, RefundEditable.class);
            client.updateRefund(refundToUpdate.getId(), fileName, refundToUpdate);
            log.info(String.format("updated refund (%s): %s", fileName, refundToUpdate));
        } else {

            RefundStatusRequest statusRequest = new RefundStatusRequest();
            if (RefundStatus.REJECTED.equals(newStatus)) {

                statusRequest.setStatus(newStatus);
                statusRequest.setRejectionReason(refundFromFile.getAirlineRemark());
            } else {

                statusRequest.setStatus(newStatus);
                statusRequest.setAirlineRemark(refundFromFile.getAirlineRemark());
            }

            client.updateStatus(refundToUpdate.getId(), fileName, statusRequest);
            log.info(String.format("refund status request (%s): %s", fileName, statusRequest));
        }

    }

}
