package org.iata.bsplink.refund.loader.job;

import org.iata.bsplink.refund.loader.dto.Refund;
import org.iata.bsplink.refund.loader.model.RefundDocument;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Converts a read refund in the DTO that will be used to send the update to the refund service.
 */
@Component
public class RefundItemProcessor implements ItemProcessor<RefundDocument, Refund> {

    @Override
    public Refund process(RefundDocument item) throws Exception {

        Refund refund = new Refund();

        refund.setAirlineCode(item.getRecordIt02().getTicketingAirlineCodeNumber());
        refund.setIsoCountryCode(item.getRecordIt02().getIsoCountryCode());
        refund.setTicketDocumentNumber(item.getRecordIt02().getTicketDocumentNumber());

        return refund;
    }

}
