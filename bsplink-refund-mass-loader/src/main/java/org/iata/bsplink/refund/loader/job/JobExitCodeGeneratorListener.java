package org.iata.bsplink.refund.loader.job;

import static org.iata.bsplink.refund.loader.configuration.BatchConfiguration.VALIDATION_STEP_NAME;

import java.util.Collection;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;

/**
 * Sets the application exit code.
 *
 * <p>
 * The listener will change the application exit in this way:
 *
 * - if the job ends with COMPLETED status the return code is successful (0).
 * - if the job ends with a status different than COMPLETED but the failure was in the file
 *   validation the exit code will be successful (0).
 * - any other case will end with a failure exit code (1).
 * </p>
 *
 * <p>
 * Note: Spring Batch has a better mechanism to change the exit code (see
 * https://docs.spring.io/spring-batch/trunk/reference/html/configureJob.html#exitCodes) but it
 * doesn't seem to work here for some reason.
 * </p>
 */
@Component
public class JobExitCodeGeneratorListener extends JobExecutionListenerSupport
        implements ExitCodeGenerator {

    public static final int SUCCESSFUL_EXIT_CODE = 0;
    public static final int FAILURE_EXIT_CODE = 1;

    private int exitCode = SUCCESSFUL_EXIT_CODE;

    @Override
    public void afterJob(JobExecution jobExecution) {

        // if job status is COMPLETED the exit code is always successful
        if (isCompleted(jobExecution.getExitStatus())) {

            return;
        }

        Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();

        // if job status is FAILED but the fail is due to a validation in the input file
        // the exit code is also successful (0)
        stepExecutions.forEach(stepExecution -> {

            if (isCompleted(stepExecution.getExitStatus())) {

                return;
            }

            if (VALIDATION_STEP_NAME.equals(stepExecution.getStepName())
                    && !stepExecution.getFailureExceptions().isEmpty()) {

                Throwable failureException = stepExecution.getFailureExceptions().get(0);

                if (failureException instanceof ValidationException) {

                    return;
                }

            }

            exitCode = FAILURE_EXIT_CODE;
        });

    }

    private boolean isCompleted(ExitStatus exitStatus) {

        return ExitStatus.COMPLETED.getExitCode().equals(exitStatus.getExitCode());
    }

    @Override
    public int getExitCode() {

        return exitCode;
    }
}
