package org.iata.bsplink.refund.loader;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.iata.bsplink.refund.loader.job.RefundJobParametersConverter.OUTPUT_PATH;
import static org.iata.bsplink.refund.loader.job.RefundJobParametersConverter.REQUIRED_PARAMETER;
import static org.iata.bsplink.refund.loader.test.fixtures.FixtureLoader.createTmpDirectory;
import static org.iata.bsplink.refund.loader.test.fixtures.FixtureLoader.getFileFixture;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.batch.test.AssertFile.assertFileEquals;

import feign.Response;

import java.util.HashMap;
import java.util.Properties;

import org.iata.bsplink.refund.loader.dto.Refund;
import org.iata.bsplink.refund.loader.dto.RefundStatusRequest;
import org.iata.bsplink.refund.loader.job.RefundJobParametersConverter;
import org.iata.bsplink.refund.loader.restclient.RefundClient;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.batch.job.enabled = false")
public class JobTest {

    private static long REFUND_ID = 1;

    @Autowired
    private Job job;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private RefundJobParametersConverter jobParameterConverter;

    @MockBean
    private RefundClient client;

    private static String tmpDirectory;

    @BeforeClass
    public static void setUpBeforeClass() {

        tmpDirectory = createTmpDirectory().getPath();
    }

    @Test
    public void testUpdatesRefundFileSuccessfully() throws Exception {

        String isoCountryCode = "AL";
        String airlineCode = "074";
        String ticketDocumentNumber = "0745200000047";
        String inputFile = "ALe9EARS_20170410_0744_016";

        Refund refundToUpdate =
                createRefundToUpdate(isoCountryCode, airlineCode, ticketDocumentNumber);

        configureRestClient(isoCountryCode, airlineCode, inputFile, refundToUpdate);

        runJobAndAssertStatus(inputFile, ExitStatus.COMPLETED);

        verify(client).updateRefund(REFUND_ID, inputFile, refundToUpdate);

        Resource expected = new ClassPathResource("output/report/SUCCESSFULLY");
        Resource actual = new FileSystemResource(tmpDirectory + "/ALe80744_20170410_016");

        assertFileEquals(expected, actual);
    }

    private void configureRestClient(String isoCountryCode, String airlineCode,
            String inputFile, Refund refundToUpdate) {

        when(client.findRefund(eq(isoCountryCode), eq(airlineCode), any()))
                .thenReturn(ResponseEntity.ok().body(refundToUpdate));

        when(client.updateRefund(REFUND_ID, inputFile, refundToUpdate))
                .thenReturn(getOkResponse());

        when(client.updateStatus(eq(REFUND_ID), eq(inputFile), any(RefundStatusRequest.class)))
                .thenReturn(getOkResponse());
    }

    private Response getOkResponse() {

        return Response.builder()
                .status(200)
                .headers(new HashMap<>())
                .build();
    }

    private Refund createRefundToUpdate(String isoCountryCode, String airlineCode,
            String ticketDocumentNumber) {

        Refund refundToUpdate = new Refund();

        refundToUpdate.setId(REFUND_ID);
        refundToUpdate.setIsoCountryCode(isoCountryCode);
        refundToUpdate.setAirlineCode(airlineCode);
        refundToUpdate.setTicketDocumentNumber(ticketDocumentNumber);

        return refundToUpdate;
    }

    private void runJobAndAssertStatus(String inputFile, ExitStatus expectedExitStatus)
            throws Exception {

        JobExecution jobExecution = jobLauncher.run(job, getJobParameters(inputFile));

        assertThat(jobExecution.getExitStatus().getExitCode(),
                equalTo(expectedExitStatus.getExitCode()));
    }

    private JobParameters getJobParameters(String fileFixtureName) throws Exception {

        String fileFixturePath = getFileFixture(fileFixtureName).getFile().getPath();

        Properties properties = new Properties();
        properties.setProperty(REQUIRED_PARAMETER, fileFixturePath);
        properties.setProperty(OUTPUT_PATH, tmpDirectory);

        return jobParameterConverter.getJobParameters(properties);
    }

    @Test
    public void testProcessFilesWithErrors() throws Exception {

        runJobAndAssertStatus("wrong/XXe9EARS_20180101_0011_001", ExitStatus.FAILED);

        Resource expected = new ClassPathResource("output/report/XXe80011_20180101_001");
        Resource actual = new FileSystemResource(tmpDirectory + "/XXe80011_20180101_001");

        assertFileEquals(expected, actual);
    }

    @Test
    public void testProcessTransactionsWithErrors() throws Exception {

        String isoCountryCode = "FR";
        String airlineCode = "030";
        String inputFile = "FRe9EARS_20150602_0302_003";

        Refund refundToUpdate =
                createRefundToUpdate(isoCountryCode, airlineCode, null);

        configureRestClient(isoCountryCode, airlineCode, inputFile, refundToUpdate);

        runJobAndAssertStatus("wrong/" + inputFile, ExitStatus.COMPLETED);

        Resource expected = new ClassPathResource("output/report/FRe80302_20150602_003");
        Resource actual = new FileSystemResource(tmpDirectory + "/FRe80302_20150602_003");

        assertFileEquals(expected, actual);
    }
}
