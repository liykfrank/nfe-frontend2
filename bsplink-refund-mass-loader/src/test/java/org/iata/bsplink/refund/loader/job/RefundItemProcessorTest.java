package org.iata.bsplink.refund.loader.job;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.AIRLINE_CODE;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.ISO_COUNTRY_CODE;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.TICKET_DOCUMENT_NUMBER_1;
import static org.junit.Assert.assertThat;

import org.iata.bsplink.refund.loader.dto.Refund;
import org.iata.bsplink.refund.loader.model.RefundDocument;
import org.iata.bsplink.refund.loader.model.record.RecordIt02;
import org.junit.Test;
import org.springframework.batch.item.ItemProcessor;

public class RefundItemProcessorTest {

    @Test
    public void testCopiesValues() throws Exception {

        RecordIt02 recordIt02 = new RecordIt02();
        recordIt02.setIsoCountryCode(ISO_COUNTRY_CODE);
        recordIt02.setTicketingAirlineCodeNumber(AIRLINE_CODE);
        recordIt02.setTicketDocumentNumber(TICKET_DOCUMENT_NUMBER_1);

        RefundDocument refundDocument = new RefundDocument();
        refundDocument.setRecordIt02(recordIt02);

        ItemProcessor<RefundDocument, Refund> processor = new RefundItemProcessor();

        Refund refund = processor.process(refundDocument);

        // TODO: it's pending the addition of the fields that don't exist yet
        assertThat(refund.getIsoCountryCode(), equalTo(ISO_COUNTRY_CODE));
        assertThat(refund.getAirlineCode(), equalTo(AIRLINE_CODE));
        assertThat(refund.getTicketDocumentNumber(), equalTo(TICKET_DOCUMENT_NUMBER_1));
    }

}
