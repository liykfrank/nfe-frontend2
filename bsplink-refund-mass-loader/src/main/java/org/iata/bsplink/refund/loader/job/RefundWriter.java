package org.iata.bsplink.refund.loader.job;

import java.util.List;

import lombok.extern.java.Log;

import org.iata.bsplink.refund.loader.dto.Refund;
import org.iata.bsplink.refund.loader.dto.RefundEditable;
import org.iata.bsplink.refund.loader.restclient.RefundClient;
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

        // TODO: copy properties from refundFromFile to refundToUpdate

        BeanUtils.copyProperties(refundFromFile, refundToUpdate, RefundEditable.class);

        client.updateRefund(refundToUpdate.getId(), refundToUpdate);

        log.info(String.format("updated refund: %s", refundToUpdate));
    }

}
