package org.iata.bsplink.refund.loader.job;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.iata.bsplink.refund.loader.configuration.BatchConfiguration.LOADER_STEP_NAME;
import static org.iata.bsplink.refund.loader.configuration.BatchConfiguration.VALIDATION_STEP_NAME;
import static org.iata.bsplink.refund.loader.job.JobExitCodeGeneratorListener.FAILURE_EXIT_CODE;
import static org.iata.bsplink.refund.loader.job.JobExitCodeGeneratorListener.SUCCESSFUL_EXIT_CODE;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.validator.ValidationException;

public class JobExitCodeGeneratorListenerTest {

    private JobExitCodeGeneratorListener listener;
    private JobExecution jobExecution;
    private StepExecution stepExecutionValidation;
    private StepExecution stepExecutionLoader;

    @Before
    public void setUp() {

        listener = new JobExitCodeGeneratorListener();
        jobExecution = mock(JobExecution.class);

        stepExecutionValidation = mock(StepExecution.class);
        when(stepExecutionValidation.getStepName()).thenReturn(VALIDATION_STEP_NAME);

        stepExecutionLoader = mock(StepExecution.class);
        when(stepExecutionLoader.getStepName()).thenReturn(LOADER_STEP_NAME);

        Collection<StepExecution> stepExecutions =
                Arrays.asList(stepExecutionValidation, stepExecutionLoader);

        when(jobExecution.getStepExecutions()).thenReturn(stepExecutions);
    }

    @Test
    public void testDefaultExitCodeIsSuccessful() {

        assertExitCodeEqualTo(SUCCESSFUL_EXIT_CODE);
    }

    private void assertExitCodeEqualTo(int exitCode) {

        assertThat(listener.getExitCode(), equalTo(exitCode));
    }

    @Test
    public void testIfJobStatusIsCompletedExitStatusIsSuccessful() {

        when(jobExecution.getExitStatus()).thenReturn(ExitStatus.COMPLETED);

        invokeAfterJob();

        assertExitCodeEqualTo(SUCCESSFUL_EXIT_CODE);
    }

    private void invokeAfterJob() {

        listener.afterJob(jobExecution);
    }

    @Test
    public void testIfFileValidationStepHasFailedExitStatusIsSuccessful() {

        when(jobExecution.getExitStatus()).thenReturn(ExitStatus.FAILED);
        when(jobExecution.getStepExecutions()).thenReturn(Arrays.asList(stepExecutionValidation));
        when(stepExecutionValidation.getExitStatus()).thenReturn(ExitStatus.FAILED);
        when(stepExecutionValidation.getFailureExceptions())
                .thenReturn(Arrays.asList(new ValidationException(null)));

        invokeAfterJob();

        assertExitCodeEqualTo(SUCCESSFUL_EXIT_CODE);
    }

    @Test
    public void testIfFileValidationStepHasFailedButItsNotValidationErrorExitStatusIsFailed() {

        when(jobExecution.getExitStatus()).thenReturn(ExitStatus.FAILED);
        when(jobExecution.getStepExecutions()).thenReturn(Arrays.asList(stepExecutionValidation));
        when(stepExecutionValidation.getExitStatus()).thenReturn(ExitStatus.FAILED);
        when(stepExecutionValidation.getFailureExceptions())
                .thenReturn(Arrays.asList(new RuntimeException()));

        invokeAfterJob();

        assertExitCodeEqualTo(FAILURE_EXIT_CODE);
    }

    @Test
    public void testIfTheFailedStepIsOtherThanFileValidationStepExitStatusIsFail() {

        when(jobExecution.getExitStatus()).thenReturn(ExitStatus.FAILED);
        when(stepExecutionValidation.getExitStatus()).thenReturn(ExitStatus.COMPLETED);
        when(stepExecutionLoader.getExitStatus()).thenReturn(ExitStatus.FAILED);

        invokeAfterJob();

        assertExitCodeEqualTo(FAILURE_EXIT_CODE);
    }

}
