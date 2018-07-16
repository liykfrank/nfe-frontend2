package org.iata.bsplink.refund.loader.job;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.AIRLINE_CODE;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.ID;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.ISO_COUNTRY_CODE;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.TRANSACTION_NUMBER_1;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;

import org.iata.bsplink.refund.loader.dto.Refund;
import org.iata.bsplink.refund.loader.dto.RefundAmounts;
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
        refundFromFile.getAmounts().setGrossFare(BigDecimal.TEN);
        refundFromFile.getAmounts().setRefundToPassenger(BigDecimal.TEN);
        refundFromFile.setPassenger("PASS FROM FILE");

        refundToUpdate = createRefund();
        refundToUpdate.setId(ID);
        refundToUpdate.getAmounts().setGrossFare(BigDecimal.ONE);
        refundToUpdate.getAmounts().setRefundToPassenger(BigDecimal.ONE);
        refundToUpdate.setPassenger("PASS TO UPDATE");

        when(client.findRefund(ISO_COUNTRY_CODE, AIRLINE_CODE, TRANSACTION_NUMBER_1))
                .thenReturn(ResponseEntity.ok().body(refundToUpdate));
    }

    private Refund createRefund() {

        Refund refund = new Refund();
        refund = new Refund();
        refund.setIsoCountryCode(ISO_COUNTRY_CODE);
        refund.setAirlineCode(AIRLINE_CODE);
        refund.setTicketDocumentNumber(TRANSACTION_NUMBER_1);
        refund.setAmounts(new RefundAmounts());

        return refund;
    }

    @Test
    public void testUpdatesRefund() throws Exception {

        writer.write(Arrays.asList(refundFromFile));

        verify(client).updateRefund(ID, refundToUpdate);

        assertThat(refundToUpdate.getId(), equalTo(ID));
        assertThat(refundToUpdate.getPassenger(), equalTo(refundFromFile.getPassenger()));
        assertThat(refundToUpdate.getAmounts().getGrossFare(),
                equalTo(refundFromFile.getAmounts().getGrossFare()));
        assertThat(refundToUpdate.getAmounts().getRefundToPassenger(),
                equalTo(refundFromFile.getAmounts().getRefundToPassenger()));
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
                containsString(String.format("ticketDocumentNumber=%s", TRANSACTION_NUMBER_1)));
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
