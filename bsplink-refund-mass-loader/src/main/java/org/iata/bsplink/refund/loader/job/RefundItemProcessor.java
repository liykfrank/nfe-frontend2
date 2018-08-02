package org.iata.bsplink.refund.loader.job;

import static org.iata.bsplink.refund.loader.job.ReportPrinterStepListener.VALIDATION_ERRORS_KEY;

import java.util.ArrayList;
import java.util.List;

import org.iata.bsplink.refund.loader.creator.RefundCreator;
import org.iata.bsplink.refund.loader.dto.Refund;
import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.model.RefundDocument;
import org.iata.bsplink.refund.loader.validation.RefundDocumentValidator;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Converts a read refund in the DTO that will be used to send the update to the refund service.
 */
@Component
public class RefundItemProcessor implements ItemProcessor<RefundDocument, Refund> {

    private RefundCreator refundCreator;
    private RefundDocumentValidator validator;
    private List<RefundLoaderError> refundLoaderErrors = new ArrayList<>();

    /**
     * Creates the refund processor.
     */
    public RefundItemProcessor(RefundCreator refundCreator, RefundDocumentValidator validator) {

        this.refundCreator = refundCreator;
        this.validator = validator;
    }

    /**
     * Creates the list of errors in the step context.
     */
    @SuppressWarnings("unchecked")
    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {

        ExecutionContext executionContext = stepExecution.getExecutionContext();

        if (!executionContext.containsKey(VALIDATION_ERRORS_KEY)) {

            executionContext.put(VALIDATION_ERRORS_KEY, new ArrayList<>());
        }

        refundLoaderErrors = (List<RefundLoaderError>) executionContext.get(VALIDATION_ERRORS_KEY);
    }

    @Override
    public Refund process(RefundDocument item) throws Exception {

        // TODO: log validation error
        if (validator.validate(item, refundLoaderErrors)) {

            refundCreator.setRefundDocument(item);
            return refundCreator.create();
        }

        return null;
    }
}
