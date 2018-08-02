package org.iata.bsplink.refund.loader.job;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.iata.bsplink.refund.loader.job.ReportPrinterStepListener.VALIDATION_ERRORS_KEY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.validation.RefundFileValidator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;

public class FileValidatorTaskletTest {

    private static final String ANY_VALIDATION_FAILING_FIELD = "foo";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ChunkContext chunkContext;
    private StepContribution contribution;
    private ExecutionContext executionContext;
    private RefundFileValidator refundFileValidator;
    private FileValidatorTasklet validatorTasklet;

    @Before
    public void setUp() {

        executionContext = new ExecutionContext();
        prepareChunkContextMock();

        contribution = mock(StepContribution.class);
        refundFileValidator = mock(RefundFileValidator.class);

        validatorTasklet = new FileValidatorTasklet(mock(Resource.class), refundFileValidator);
    }

    private void prepareChunkContextMock() {

        StepExecution stepExecution = mock(StepExecution.class);
        when(stepExecution.getExecutionContext()).thenReturn(executionContext);

        StepContext stepContext = mock(StepContext.class);
        when(stepContext.getStepExecution()).thenReturn(stepExecution);

        chunkContext = mock(ChunkContext.class);
        when(chunkContext.getStepContext()).thenReturn(stepContext);
    }

    @Test
    public void testRetrowsExceptionOnValidationError() throws Exception {

        doThrow(new ValidationException("")).when(refundFileValidator).validate(any(), any());

        thrown.expect(ValidationException.class);

        executeTaskletValidator();
    }

    private RepeatStatus executeTaskletValidator() throws Exception {

        return validatorTasklet.execute(contribution, chunkContext);
    }

    @Test
    public void testFinishCorrectlyIfThereAreNoValidationErrors() throws Exception {

        assertEquals(RepeatStatus.FINISHED, executeTaskletValidator());
    }

    @Test
    public void testAddsValidationErrorsToContext() {

        configureValidatorToFail();

        try {
            executeTaskletValidator();
        } catch (Exception e) {
            // do nothing
        }

        assertTrue(executionContext.containsKey(VALIDATION_ERRORS_KEY));

        @SuppressWarnings("unchecked")
        List<RefundLoaderError> errors =
                (List<RefundLoaderError>) executionContext.get(VALIDATION_ERRORS_KEY);

        assertThat(errors.get(0).getField(), equalTo(ANY_VALIDATION_FAILING_FIELD));

    }

    private void configureValidatorToFail() {

        doAnswer(new Answer<Void>() {

            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {

                @SuppressWarnings("unchecked")
                List<RefundLoaderError> errors =
                        (List<RefundLoaderError>) invocation.getArgument(1);

                RefundLoaderError refundLoaderError = new RefundLoaderError();
                refundLoaderError.setField(ANY_VALIDATION_FAILING_FIELD);

                errors.add(refundLoaderError);

                return null;
            }

        }).when(refundFileValidator).validate(any(), any());
    }

}
