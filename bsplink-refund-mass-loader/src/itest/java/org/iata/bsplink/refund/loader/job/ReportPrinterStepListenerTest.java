package org.iata.bsplink.refund.loader.job;

import static org.hamcrest.CoreMatchers.containsString;
import static org.iata.bsplink.refund.loader.configuration.BatchConfiguration.LOADER_STEP_NAME;
import static org.iata.bsplink.refund.loader.configuration.BatchConfiguration.VALIDATION_STEP_NAME;
import static org.iata.bsplink.refund.loader.job.ReportPrinterStepListener.VALIDATION_ERRORS_KEY;
import static org.iata.bsplink.refund.loader.test.Tools.getJobParameters;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.report.ProcessReportPrinter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ReportPrinterStepListenerTest {

    private static final String ANY_REFUND_FILE_NAME = "ALe9EARS_20170410_0744_016";
    private static final String ANY_REPORT_OUTPUT_PATH = "output" + File.separator + "path";
    private static final String ANY_REPORT_FILE_NAME =
            ANY_REPORT_OUTPUT_PATH + File.separator + "ALe80744_20170410_016";


    private static final ExitStatus EXIT_COMPLETED =
            new ExitStatus(ExitStatus.COMPLETED.getExitCode(), "any description");
    private static final ExitStatus EXIT_FAILED =
            new ExitStatus(ExitStatus.FAILED.getExitCode(), "any description");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public OutputCapture capture = new OutputCapture();

    private ProcessReportPrinter printer;
    private ReportPrinterStepListener listener;
    private ExecutionContext executionContext;
    private StepExecution stepExecution;
    private List<RefundLoaderError> errors = Collections.emptyList();

    @Before
    public void setUp() {

        executionContext = new ExecutionContext();

        stepExecution = mock(StepExecution.class);
        when(stepExecution.getExecutionContext()).thenReturn(executionContext);
        when(stepExecution.getJobParameters())
                .thenReturn(getJobParameters(ANY_REPORT_OUTPUT_PATH, ANY_REFUND_FILE_NAME));

        printer = mock(ProcessReportPrinter.class);

        listener = new ReportPrinterStepListener(printer);
        listener.beforeStep(stepExecution);

        capture = new OutputCapture();
    }

    @Test
    public void testThrowsExceptionIfValidationErrorsAreNotSet() {

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(
                String.format("No key %s found in execution context", VALIDATION_ERRORS_KEY));

        configureStepExecution(VALIDATION_STEP_NAME, EXIT_FAILED);

        listener.afterStep(stepExecution);
    }

    private void configureStepExecution(String stepName, ExitStatus exitStatus) {

        when(stepExecution.getStepName()).thenReturn(stepName);
        when(stepExecution.getExitStatus()).thenReturn(exitStatus);
    }

    @Test
    public void testPrintsErrorReportWhenValidationStepFails() {

        addValidationErrors();
        configureStepExecution(VALIDATION_STEP_NAME, EXIT_FAILED);

        listener.afterStep(stepExecution);

        verify(printer).print(errors, ANY_REPORT_FILE_NAME);
    }

    private void addValidationErrors() {

        executionContext.put(VALIDATION_ERRORS_KEY, errors);
    }

    @Test
    public void testDoesNotPrintErrorReportIfValidationStepSuccess() {

        configureStepExecution(VALIDATION_STEP_NAME, EXIT_COMPLETED);

        listener.afterStep(stepExecution);

        verify(printer, never()).print(any(), any());
    }

    @Test
    public void testPrintsErrorReportWhenLoaderStepSuccess() {

        addValidationErrors();
        configureStepExecution(LOADER_STEP_NAME, EXIT_COMPLETED);

        listener.afterStep(stepExecution);

        verify(printer).print(errors, ANY_REPORT_FILE_NAME);
    }

    @Test
    public void testDoesNotPrintErrorReportIfLoaderStepFails() {

        configureStepExecution(LOADER_STEP_NAME, EXIT_FAILED);

        listener.afterStep(stepExecution);

        verify(printer, never()).print(any(), any());
    }

    @Test
    public void testNormalizesOutputPathEndSeparator() {

        addValidationErrors();
        configureStepExecution(VALIDATION_STEP_NAME, EXIT_FAILED);

        when(stepExecution.getJobParameters()).thenReturn(getJobParameters(
                ANY_REPORT_OUTPUT_PATH + File.separator + File.separator, ANY_REFUND_FILE_NAME));

        listener.afterStep(stepExecution);

        verify(printer).print(errors, ANY_REPORT_FILE_NAME);
    }

    @Test
    public void testLogsReadAndWrittenRecords() {

        addValidationErrors();
        configureStepExecution(LOADER_STEP_NAME, EXIT_COMPLETED);

        when(stepExecution.getReadCount()).thenReturn(10);
        when(stepExecution.getWriteCount()).thenReturn(8);

        listener.afterStep(stepExecution);

        capture.expect(containsString("Total read transactions: 10"));
        capture.expect(containsString("Total written transactions: 8"));
    }

}
