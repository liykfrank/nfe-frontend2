package org.iata.bsplink.refund.loader.job;

import org.iata.bsplink.refund.loader.creator.RefundCreator;
import org.iata.bsplink.refund.loader.dto.Refund;
import org.iata.bsplink.refund.loader.model.RefundDocument;
import org.iata.bsplink.refund.loader.validation.CompositeRefundDocumentValidator;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Converts a read refund in the DTO that will be used to send the update to the refund service.
 */
@Component
public class RefundItemProcessor extends RefundLoaderErrorsAware
        implements ItemProcessor<RefundDocument, Refund> {

    private RefundCreator refundCreator;
    private CompositeRefundDocumentValidator validator;

    /**
     * Creates the refund processor.
     */
    public RefundItemProcessor(RefundCreator refundCreator,
            CompositeRefundDocumentValidator validator) {

        this.refundCreator = refundCreator;
        this.validator = validator;
    }

    @Override
    public Refund process(RefundDocument item) throws Exception {

        if (validator.validate(item, refundLoaderErrors)) {

            refundCreator.setRefundDocument(item);
            return refundCreator.create();
        }

        return null;
    }
}
