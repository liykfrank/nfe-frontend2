package org.iata.bsplink.refund.loader.job;

import static org.apache.commons.io.FilenameUtils.concat;
import static org.iata.bsplink.refund.loader.configuration.BatchConfiguration.LOADER_STEP_NAME;
import static org.iata.bsplink.refund.loader.configuration.BatchConfiguration.VALIDATION_STEP_NAME;
import static org.iata.bsplink.refund.loader.job.RefundJobParametersConverter.OUTPUT_PATH;
import static org.iata.bsplink.refund.loader.job.RefundJobParametersConverter.REQUIRED_PARAMETER;

import java.util.List;

import lombok.extern.apachecommons.CommonsLog;

import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.report.ProcessReportPrinter;
import org.iata.bsplink.refund.loader.utils.RefundNameUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

@Component
@CommonsLog
public class ReportPrinterStepListener extends StepExecutionListenerSupport {

    public static final String VALIDATION_ERRORS_KEY = "validation.errors";

    private ProcessReportPrinter printer;

    public ReportPrinterStepListener(ProcessReportPrinter printer) {

        this.printer = printer;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        if (reportShouldBePrinted(stepExecution)) {

            printer.print(getValidationErrors(stepExecution), getReportFileName(stepExecution));
        }

        log.info("Total read transactions: " + stepExecution.getReadCount());
        log.info("Total written transactions: " + stepExecution.getWriteCount());

        return stepExecution.getExitStatus();
    }

    private boolean reportShouldBePrinted(StepExecution stepExecution) {

        String stepName = stepExecution.getStepName();
        String exitCode = stepExecution.getExitStatus().getExitCode();

        switch (stepName) {

            case VALIDATION_STEP_NAME:
                return ExitStatus.FAILED.getExitCode().equals(exitCode);

            case LOADER_STEP_NAME:
                return ExitStatus.COMPLETED.getExitCode().equals(exitCode);

            default:
                return false;
        }

    }

    @SuppressWarnings("unchecked")
    private List<RefundLoaderError> getValidationErrors(StepExecution stepExecution) {

        ExecutionContext executionContext = stepExecution.getExecutionContext();

        if (!executionContext.containsKey(VALIDATION_ERRORS_KEY)) {

            throw new IllegalStateException(
                    String.format("No key %s found in execution context", VALIDATION_ERRORS_KEY));
        }

        return (List<RefundLoaderError>) executionContext.get(VALIDATION_ERRORS_KEY);
    }

    private String getReportFileName(StepExecution stepExecution) {

        JobParameters parameters = stepExecution.getJobParameters();

        String fileName =
                RefundNameUtils.getReportFileName(parameters.getString(REQUIRED_PARAMETER));
        String outputPath = parameters.getString(OUTPUT_PATH);

        return concat(outputPath, fileName);
    }

}