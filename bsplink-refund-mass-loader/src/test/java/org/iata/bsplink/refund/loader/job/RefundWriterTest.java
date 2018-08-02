package org.iata.bsplink.refund.loader.job;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.iata.bsplink.refund.loader.job.RefundJobParametersConverter.JOBID_PARAMETER_NAME;
import static org.iata.bsplink.refund.loader.job.ReportPrinterStepListener.VALIDATION_ERRORS_KEY;
import static org.iata.bsplink.refund.loader.test.Tools.getObjectMapper;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.AGENT_CODE;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.AIRLINE_CODE;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.ID;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.ISO_COUNTRY_CODE;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.TICKET_DOCUMENT_NUMBER_1;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.TRANSACTION_NUMBER_1;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Response;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.iata.bsplink.refund.loader.dto.Refund;
import org.iata.bsplink.refund.loader.dto.RefundAmounts;
import org.iata.bsplink.refund.loader.dto.RefundStatus;
import org.iata.bsplink.refund.loader.dto.RefundStatusRequest;
import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.error.ValidationPhase;
import org.iata.bsplink.refund.loader.exception.RefundLoaderException;
import org.iata.bsplink.refund.loader.response.ValidationError;
import org.iata.bsplink.refund.loader.response.ValidationErrorResponse;
import org.iata.bsplink.refund.loader.restclient.RefundClient;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;



@RunWith(JUnitParamsRunner.class)
public class RefundWriterTest {

    private static final String FILE_NAME = "FRe9EARS_20180718_023";
    private static final String ANY_ERROR_FIELD_NAME = "anyField";
    private static final String ANY_ERROR_MESSAGE = "any error message";

    @Rule
    public OutputCapture capture;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RefundClient client;
    private RefundWriter writer;
    private Refund refundFromFile;
    private Refund refundToUpdate;
    private ExecutionContext executionContext;

    @Before
    public void setUp() {

        capture = new OutputCapture();
        client = mock(RefundClient.class);
        executionContext = new ExecutionContext();

        writer = new RefundWriter(client, getObjectMapper());
        writer.beforeStep(stepExecution());

        setRefundFromFile();
        setRefundToUpdate();

        Response response = getResponse(HttpStatus.OK, null);

        when(client.findRefund(ISO_COUNTRY_CODE, AIRLINE_CODE, TICKET_DOCUMENT_NUMBER_1))
                .thenReturn(ResponseEntity.ok().body(refundToUpdate));
        when(client.updateRefund(ID, FILE_NAME, refundToUpdate))
                .thenReturn(response);
        when(client.updateStatus(eq(ID), eq(FILE_NAME), any(RefundStatusRequest.class)))
                .thenReturn(response);
    }

    private Response getResponse(HttpStatus status, String body) {

        return Response.builder()
                .status(status.value())
                .headers(new HashMap<>())
                .body(body, Charset.defaultCharset())
                .reason(status.getReasonPhrase())
                .build();
    }

    private void setRefundFromFile() {

        refundFromFile = createRefund();

        refundFromFile.setAgentCode(AGENT_CODE);
        refundFromFile.setAirlineCodeRelatedDocument("030");
        refundFromFile.setAirlineRemark("airlineRemark");
        refundFromFile.getAmounts().setGrossFare(BigDecimal.TEN);
        refundFromFile.getAmounts().setRefundToPassenger(BigDecimal.TEN);
        refundFromFile.setCustomerFileReference("customerFileReference");
        refundFromFile.setDateOfIssueRelatedDocument(LocalDate.now());
        refundFromFile.setSettlementAuthorisationCode("ESAC FROM FILE");
        refundFromFile.setStatus(RefundStatus.AUTHORIZED);
        refundFromFile.setNetReporting(true);
        refundFromFile.setWaiverCode("waiverCode");
        refundFromFile.setTransactionNumber(TRANSACTION_NUMBER_1);
    }

    private void setRefundToUpdate() {

        refundToUpdate = createRefund();
        refundToUpdate.setId(ID);
        refundToUpdate.getAmounts().setGrossFare(BigDecimal.ONE);
        refundToUpdate.getAmounts().setRefundToPassenger(BigDecimal.ONE);
        refundFromFile.setSettlementAuthorisationCode("ESAC TO UPDATE");
    }

    private StepExecution stepExecution() {

        JobParameters jobParameters = Mockito.mock(JobParameters.class);
        when(jobParameters.getString(JOBID_PARAMETER_NAME)).thenReturn(FILE_NAME);

        JobExecution jobExecution = Mockito.mock(JobExecution.class);
        when(jobExecution.getJobParameters()).thenReturn(jobParameters);

        StepExecution stepExecution = Mockito.mock(StepExecution.class);
        when(stepExecution.getJobExecution()).thenReturn(jobExecution);
        when(stepExecution.getExecutionContext()).thenReturn(executionContext);

        return stepExecution;
    }

    private Refund createRefund() {

        Refund refund = new Refund();
        refund = new Refund();
        refund.setIsoCountryCode(ISO_COUNTRY_CODE);
        refund.setAirlineCode(AIRLINE_CODE);
        refund.setTicketDocumentNumber(TICKET_DOCUMENT_NUMBER_1);
        refund.setAmounts(new RefundAmounts());

        return refund;
    }

    @Test
    public void testUpdatesRefundWhenAuthorized() throws Exception {

        writer.write(Arrays.asList(refundFromFile));

        verify(client).updateRefund(ID, FILE_NAME, refundToUpdate);

        // we set some properties that should not be set in the refundToUpdate
        // object in order to the next assert to work
        refundFromFile.setId(ID);
        refundFromFile.setTransactionNumber(null);

        assertThat(refundToUpdate, samePropertyValuesAs(refundFromFile));
    }

    @Test
    public void testLogsWhenIsWriting() throws Exception {

        writer.write(Arrays.asList(refundFromFile));

        capture.expect(containsString("writing refund"));
        assertRefundLog();
    }

    private void assertRefundLog() {

        capture.expect(containsString(String.format("isoCountryCode=%s", ISO_COUNTRY_CODE)));
        capture.expect(containsString(String.format("airlineCode=%s", AIRLINE_CODE)));
        capture.expect(
                containsString(String.format("ticketDocumentNumber=%s", TICKET_DOCUMENT_NUMBER_1)));
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
    @Parameters
    public void testUpdatesStatus(RefundStatus status, String airlineRemark, String rejectionReason)
            throws Exception {

        refundFromFile.setStatus(status);
        refundFromFile.setAirlineRemark(airlineRemark);
        refundFromFile.setRejectionReason(rejectionReason);

        writer.write(Arrays.asList(refundFromFile));

        ArgumentCaptor<RefundStatusRequest> captor =
                ArgumentCaptor.forClass(RefundStatusRequest.class);

        verify(client).updateStatus(eq(ID), eq(FILE_NAME), captor.capture());

        assertThat(captor.getValue().getStatus(), equalTo(status));
        assertThat(captor.getValue().getAirlineRemark(), equalTo(airlineRemark));
        assertThat(captor.getValue().getRejectionReason(), equalTo(rejectionReason));

    }

    @SuppressWarnings("unused")
    private Object[][] parametersForTestUpdatesStatus() {

        String anyAirlineRemark = "any airline remark";
        String anyRejectionReason = "any rejection reason";

        return new Object[][] {

                { RefundStatus.REJECTED, null, anyRejectionReason },
                { RefundStatus.PENDING, anyAirlineRemark, null },
                { RefundStatus.DRAFT, anyAirlineRemark, null },
                { RefundStatus.PENDING_SUPERVISION, anyAirlineRemark, null },
                { RefundStatus.RESUBMITTED, anyAirlineRemark, null },
                { RefundStatus.UNDER_INVESTIGATION, anyAirlineRemark, null } };
    }

    @Test
    public void testAddsRefundDoesNotExistError() throws Exception {

        configureFindRefundToRespondWithStatus(HttpStatus.NOT_FOUND);

        writer.write(Arrays.asList(refundFromFile));

        assertUpdateIsNotCalled();
        assertThat(executionContext.containsKey(VALIDATION_ERRORS_KEY), is(true));

        assertErrorContent("ticketDocumentNumber", "The Refund does not exist.");
    }

    private void assertErrorContent(String fieldName, String message) {

        RefundLoaderError error = getStepFirstError();

        assertThat(error.getField(), equalTo(fieldName));
        assertThat(error.getMessage(), equalTo(message));
        assertThat(error.getTransactionNumber(), equalTo(TRANSACTION_NUMBER_1));
        assertThat(error.getValidationPhase(), equalTo(ValidationPhase.UPDATE));
    }

    @SuppressWarnings("unchecked")
    private RefundLoaderError getStepFirstError() {

        return ((List<RefundLoaderError>) executionContext.get(VALIDATION_ERRORS_KEY)).get(0);
    }

    private void assertUpdateIsNotCalled() {

        verify(client, never()).updateRefund(any(), any(), any());
        verify(client, never()).updateStatus(any(), any(), any());
    }

    private void configureFindRefundToRespondWithStatus(HttpStatus status) {

        when(client.findRefund(ISO_COUNTRY_CODE, AIRLINE_CODE, TICKET_DOCUMENT_NUMBER_1))
                .thenReturn(ResponseEntity.status(status).build());
    }

    @Test
    public void testLogsRefundDoesNotExistError() throws Exception {

        configureFindRefundToRespondWithStatus(HttpStatus.NOT_FOUND);

        writer.write(Arrays.asList(refundFromFile));

        capture.expect(containsString("ERROR"));
        capture.expect(containsString("Refund does not exist"));

        assertRefundLog();
    }

    @Test
    public void testThrowsExceptionIfResponseIsNotOkWhenTriesToFindRefund() throws Exception {

        configureExpectsExceptionForInternalServerError();
        configureFindRefundToRespondWithStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        try {

            writer.write(Arrays.asList(refundFromFile));

        } catch (Exception exception) {

            assertUpdateIsNotCalled();
            throw exception;
        }
    }

    private void configureExpectsExceptionForInternalServerError() {

        thrown.expect(RefundLoaderException.class);
        thrown.expectMessage(containsString(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }

    @Test
    public void testThrowsExceptionIfResponseIsNotOkWhenTriesToUpdateRefund() throws Exception {

        configureExpectsExceptionForInternalServerError();
        configureUpdateRefundToRespondWithStatus(HttpStatus.INTERNAL_SERVER_ERROR, null);

        writer.write(Arrays.asList(refundFromFile));
    }

    private void configureUpdateRefundToRespondWithStatus(HttpStatus status, String body) {

        when(client.updateRefund(any(), any(), any())).thenReturn(getResponse(status, body));
    }

    @Test
    public void testThrowsExceptionIfResponseIsNotOkWhenTriesToUpdateRefundStatus()
            throws Exception {

        configureExpectsExceptionForInternalServerError();
        configureUpdateStatusToRespondWithStatus(HttpStatus.INTERNAL_SERVER_ERROR, null);

        refundFromFile.setStatus(RefundStatus.REJECTED);

        writer.write(Arrays.asList(refundFromFile));
    }

    private void configureUpdateStatusToRespondWithStatus(HttpStatus status, String body) {

        when(client.updateStatus(any(), any(), any())).thenReturn(getResponse(status, body));
    }

    @Test
    public void testAddsResponseErrorsWhenTriesToUpdateRefund() throws Exception {

        configureUpdateRefundToRespondWithStatus(HttpStatus.BAD_REQUEST, getValidationErrorJson());

        writer.write(Arrays.asList(refundFromFile));

        assertErrorContent(ANY_ERROR_FIELD_NAME, ANY_ERROR_MESSAGE);
    }

    private String getValidationErrorJson() throws Exception {

        ValidationError validationError =
                new ValidationError(ANY_ERROR_FIELD_NAME, ANY_ERROR_MESSAGE);

        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse();
        validationErrorResponse.setValidationErrors(Arrays.asList(validationError));

        return new ObjectMapper().writeValueAsString(validationErrorResponse);
    }

    @Test
    public void testAddsResponseErrorsWhenTriesToUpdateRefundStatus() throws Exception {

        configureUpdateStatusToRespondWithStatus(HttpStatus.BAD_REQUEST, getValidationErrorJson());

        refundFromFile.setStatus(RefundStatus.REJECTED);

        writer.write(Arrays.asList(refundFromFile));

        assertErrorContent(ANY_ERROR_FIELD_NAME, ANY_ERROR_MESSAGE);
    }

}
