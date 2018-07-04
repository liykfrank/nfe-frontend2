package org.iata.bsplink.refund.loader.job.writer;

import static org.hamcrest.CoreMatchers.containsString;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.AIRLINE_CODE;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.ID;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.ISO_COUNTRY_CODE;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.TICKET_DOCUMENT_NUMBER;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.iata.bsplink.refund.loader.dto.Refund;
import org.iata.bsplink.refund.loader.restclient.RefundClient;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.batch.item.ItemWriter;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class RefundWriterTest {

    @Rule
    public OutputCapture capture;

    @Mock
    private RefundClient client;

    private ItemWriter<Refund> writer;
    private Refund refundFromFile;
    private Refund refundToUpdate;

    @Before
    public void setUp() {

        capture = new OutputCapture();

        writer = new RefundWriter(client);

        refundFromFile = createRefund();

        refundToUpdate = createRefund();
        refundToUpdate.setId(ID);

        when(client.findRefund(ISO_COUNTRY_CODE, AIRLINE_CODE, TICKET_DOCUMENT_NUMBER))
                .thenReturn(ResponseEntity.ok().body(refundToUpdate));
    }

    private Refund createRefund() {

        Refund refund = new Refund();
        refund = new Refund();
        refund.setIsoCountryCode(ISO_COUNTRY_CODE);
        refund.setAirlineCode(AIRLINE_CODE);
        refund.setTicketDocumentNumber(TICKET_DOCUMENT_NUMBER);

        return refund;
    }

    @Test
    public void testUpdatesRefund() throws Exception {

        writer.write(Arrays.asList(refundFromFile));

        verify(client).updateRefund(ID, refundToUpdate);
    }

    @Test
    public void testLogsWhenIsWriting() throws Exception {

        writer.write(Arrays.asList(refundFromFile));

        capture.expect(containsString("writing refund"));
        assertRefundLog();
    }

    private void assertRefundLog() {

        capture.expect(
                containsString(String.format("isoCountryCode=%s", ISO_COUNTRY_CODE)));
        capture.expect(
                containsString(String.format("airlineCode=%s", AIRLINE_CODE)));
        capture.expect(
                containsString(String.format("ticketDocumentNumber=%s", TICKET_DOCUMENT_NUMBER)));
    }

    @Test
    public void testLogsReadDataAndActualData() throws Exception {

        writer.write(Arrays.asList(refundFromFile));

        capture.expect(containsString("read refund"));
        capture.expect(containsString("refund to update"));
        assertRefundLog();
    }

    @Test
    public void testLogsUpdatedData() throws Exception {

        writer.write(Arrays.asList(refundFromFile));

        capture.expect(containsString("updated refund"));
        assertRefundLog();
    }

    @Test
    @Ignore("implementation pending")
    public void testCopiesValuesFromReadRefund() throws Exception {

        // TODO: this test must ensure that read values are correctly updated in the refund
        // that is updated.
    }

}
