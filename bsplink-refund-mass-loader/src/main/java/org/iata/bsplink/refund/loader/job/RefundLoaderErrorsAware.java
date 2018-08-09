package org.iata.bsplink.refund.loader.job;

import static org.iata.bsplink.refund.loader.job.ReportPrinterStepListener.VALIDATION_ERRORS_KEY;

import java.util.ArrayList;
import java.util.List;

import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.item.ExecutionContext;

/**
 * Reads safely the validation errors list from the step ExecutionContext.
 */
public abstract class RefundLoaderErrorsAware extends StepExecutionListenerSupport {

    protected List<RefundLoaderError> refundLoaderErrors = new ArrayList<>();

    /**
     * Creates the list of errors in the step context.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void beforeStep(StepExecution stepExecution) {

        ExecutionContext executionContext = stepExecution.getExecutionContext();

        if (!executionContext.containsKey(VALIDATION_ERRORS_KEY)) {

            executionContext.put(VALIDATION_ERRORS_KEY, new ArrayList<>());
        }

        refundLoaderErrors = (List<RefundLoaderError>) executionContext.get(VALIDATION_ERRORS_KEY);
    }
}
