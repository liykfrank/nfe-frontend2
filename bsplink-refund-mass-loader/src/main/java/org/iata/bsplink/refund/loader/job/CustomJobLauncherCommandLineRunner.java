package org.iata.bsplink.refund.loader.job;

import static org.apache.commons.io.FilenameUtils.getName;
import static org.iata.bsplink.refund.loader.job.RefundJobParametersConverter.REQUIRED_PARAMETER;
import static org.iata.bsplink.refund.loader.utils.RefundNameUtils.getUniqueProcessReportPathName;

import java.util.Arrays;
import java.util.List;

import lombok.extern.apachecommons.CommonsLog;

import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.report.ErrorsReportPrinter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.autoconfigure.batch.JobLauncherCommandLineRunner;

/**
 * Custom JobLauncherCommandLineRunner that custom logic to manage job exceptions.
 */
@CommonsLog
public class CustomJobLauncherCommandLineRunner extends JobLauncherCommandLineRunner {

    private static final String FILE_ALREADY_PROCESSED_MESSAGE =
            "A file with the same name has already been processed.";

    private ErrorsReportPrinter printer;

    /**
     * Creates the launcher.
     */
    public CustomJobLauncherCommandLineRunner(JobLauncher jobLauncher, JobExplorer jobExplorer,
            ErrorsReportPrinter printer) {

        super(jobLauncher, jobExplorer);
        this.printer = printer;
    }

    @Override
    protected void execute(Job job, JobParameters parameters)
            throws JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException, JobParametersInvalidException,
            JobParametersNotFoundException {

        try {

            super.execute(job, parameters);

        } catch (JobInstanceAlreadyCompleteException exception) {

            log.error(String.format("File %s already processed.", getInputFileName(parameters)));

            printer.print(createFileAlreadyProcessedError(),
                    getUniqueProcessReportPathName(parameters));
        }
    }

    private String getInputFileName(JobParameters parameters) {

        return getName(parameters.getString(REQUIRED_PARAMETER));
    }

    private List<RefundLoaderError> createFileAlreadyProcessedError() {

        RefundLoaderError error = new RefundLoaderError();
        error.setMessage(FILE_ALREADY_PROCESSED_MESSAGE);

        return Arrays.asList(error);
    }

}
