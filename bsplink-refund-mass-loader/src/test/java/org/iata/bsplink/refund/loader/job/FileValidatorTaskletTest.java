package org.iata.bsplink.refund.loader.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import org.iata.bsplink.refund.loader.exception.RefundLoaderException;
import org.iata.bsplink.refund.loader.validation.RefundFileValidator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;

public class FileValidatorTaskletTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ChunkContext chunkContext;
    private StepContribution contribution;
    private RefundFileValidator refundFileValidator;
    private FileValidatorTasklet validatorTasklet;

    @Before
    public void setUp() {

        chunkContext = mock(ChunkContext.class);
        contribution = mock(StepContribution.class);
        refundFileValidator = mock(RefundFileValidator.class);

        validatorTasklet = new FileValidatorTasklet(mock(Resource.class), refundFileValidator);
    }

    @Test
    public void testRetrowsExceptionOnValidationError() throws Exception {

        doThrow(new RefundLoaderException("")).when(refundFileValidator).validate(any());

        thrown.expect(RefundLoaderException.class);

        executeTaskletValidator();
    }

    private RepeatStatus executeTaskletValidator() throws Exception {

        return validatorTasklet.execute(contribution, chunkContext);
    }

    @Test
    public void testFinishCorrectlyIfThereAreNoValidationErrors() throws Exception {

        assertEquals(RepeatStatus.FINISHED, executeTaskletValidator());
    }

}
