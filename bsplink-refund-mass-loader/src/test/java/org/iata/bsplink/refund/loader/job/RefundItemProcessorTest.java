package org.iata.bsplink.refund.loader.job;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.iata.bsplink.refund.loader.job.ReportPrinterStepListener.VALIDATION_ERRORS_KEY;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.collection.IsEmptyCollection;
import org.iata.bsplink.refund.loader.creator.RefundCreator;
import org.iata.bsplink.refund.loader.dto.Refund;
import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.model.RefundDocument;
import org.iata.bsplink.refund.loader.model.record.RecordIt02;
import org.iata.bsplink.refund.loader.validation.RefundDocumentValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;

@RunWith(MockitoJUnitRunner.class)
public class RefundItemProcessorTest {

    @Mock
    private RefundCreator refundCreator;

    @Mock
    private StepExecution stepExecution;

    private RefundItemProcessor processor;
    private List<RefundLoaderError> refundLoaderErrors;
    private Refund refundCreated;
    private ExecutionContext executionContext;

    @Before
    public void setUp() throws Exception {

        refundLoaderErrors = new ArrayList<>();
        refundCreated = new Refund();

        executionContext = new ExecutionContext();
        executionContext.put(VALIDATION_ERRORS_KEY, refundLoaderErrors);

        when(refundCreator.create()).thenReturn(refundCreated);
        when(stepExecution.getExecutionContext()).thenReturn(executionContext);

        processor = new RefundItemProcessor(refundCreator, new RefundDocumentValidator());
        processor.beforeStep(stepExecution);
    }

    @Test
    public void testProcess() throws Exception {

        RefundDocument refundDocument = new RefundDocument();

        Refund refund = processor.process(refundDocument);

        assertThat(refund, equalTo(refundCreated));
        verify(refundCreator).create();
    }

    @Test
    public void testProcessWithErrors() throws Exception {

        Refund refund = processor.process(getWrongRefundDocument());

        assertNull(refund);
        verifyZeroInteractions(refundCreator);
        assertThat(refundLoaderErrors, not(IsEmptyCollection.empty()));
        assertThat(refundLoaderErrors.get(0).getMessage(),
                equalTo(RefundDocumentValidator.INCORRECT_TRANSACTION_CODE));
    }

    private RefundDocument getWrongRefundDocument() {

        RefundDocument refundDocument = new RefundDocument();
        refundDocument.setRecordIt02(new RecordIt02());
        refundDocument.getRecordIt02().setTransactionCode("CANX");

        return refundDocument;
    }

    @Test
    public void testCreatesErrorListIfDoesNotExist() {

        ExecutionContext executionContext = new ExecutionContext();
        when(stepExecution.getExecutionContext()).thenReturn(executionContext);

        assertFalse(executionContext.containsKey(VALIDATION_ERRORS_KEY));

        processor.beforeStep(stepExecution);

        assertTrue(executionContext.containsKey(VALIDATION_ERRORS_KEY));
    }

    @Test
    public void testUsesExistingErrorList() throws Exception {

        assertThat(refundLoaderErrors, empty());

        processor.process(getWrongRefundDocument());

        assertThat(refundLoaderErrors, not(empty()));
    }
}
