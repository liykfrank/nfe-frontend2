package org.iata.bsplink.refund.loader.job;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.iata.bsplink.refund.loader.job.RefundJobParametersConverter.JOBID_PARAMETER_NAME;
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
import org.iata.bsplink.refund.loader.dto.RefundStatus;
import org.iata.bsplink.refund.loader.dto.RefundStatusRequest;
import org.iata.bsplink.refund.loader.restclient.RefundClient;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class RefundWriterTest {

    @Rule
    public OutputCapture capture;

    @Mock
    private RefundClient client;

    private RefundWriter writer;
    private Refund refundFromFile;
    private Refund refundToUpdate;
    private String fileName;

    @Before
    public void setUp() {

        fileName = "FRe9EARS_20180718_023";

        capture = new OutputCapture();


        writer = new RefundWriter(client);
        writer.beforeStep(stepExecution());

        refundFromFile = createRefund();
        refundFromFile.getAmounts().setGrossFare(BigDecimal.TEN);
        refundFromFile.getAmounts().setRefundToPassenger(BigDecimal.TEN);
        refundFromFile.setPassenger("PASS FROM FILE");
        refundFromFile.setStatus(RefundStatus.AUTHORIZED);

        refundToUpdate = createRefund();
        refundToUpdate.setId(ID);
        refundToUpdate.getAmounts().setGrossFare(BigDecimal.ONE);
        refundToUpdate.getAmounts().setRefundToPassenger(BigDecimal.ONE);
        refundToUpdate.setPassenger("PASS TO UPDATE");

        when(client.findRefund(ISO_COUNTRY_CODE, AIRLINE_CODE, TRANSACTION_NUMBER_1))
                .thenReturn(ResponseEntity.ok().body(refundToUpdate));
    }


    private StepExecution stepExecution() {

        JobParameters jobParameters = Mockito.mock(JobParameters.class);
        when(jobParameters.getString(JOBID_PARAMETER_NAME)).thenReturn(fileName);

        JobExecution jobExecution = Mockito.mock(JobExecution.class);
        when(jobExecution.getJobParameters()).thenReturn(jobParameters);

        StepExecution stepExecution = Mockito.mock(StepExecution.class);
        when(stepExecution.getJobExecution()).thenReturn(jobExecution);
        return stepExecution;
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

        verify(client).updateRefund(ID, fileName, refundToUpdate);

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
    public void testUpdatesRefundStatusUnderInvestigation() throws Exception {

        refundFromFile.setStatus(RefundStatus.UNDER_INVESTIGATION);
        writer.write(Arrays.asList(refundFromFile));

        RefundStatusRequest refundStatusRequest = new RefundStatusRequest();
        refundStatusRequest.setAirlineRemark(refundFromFile.getAirlineRemark());
        refundStatusRequest.setStatus(RefundStatus.UNDER_INVESTIGATION);

        verify(client).updateStatus(ID, fileName, refundStatusRequest);
    }

    @Test
    public void testUpdatesRefundStatusRejected() throws Exception {

        refundFromFile.setStatus(RefundStatus.REJECTED);
        writer.write(Arrays.asList(refundFromFile));

        RefundStatusRequest refundStatusRequest = new RefundStatusRequest();
        refundStatusRequest.setRejectionReason(refundFromFile.getRejectionReason());
        refundStatusRequest.setStatus(RefundStatus.REJECTED);

        verify(client).updateStatus(ID, fileName, refundStatusRequest);
    }


    @Test
    @Ignore("implementation pending")
    public void testCopiesValuesFromReadRefund() throws Exception {

        // TODO: this test must ensure that read values are correctly updated in the refund
        // that is updated.
    }

}