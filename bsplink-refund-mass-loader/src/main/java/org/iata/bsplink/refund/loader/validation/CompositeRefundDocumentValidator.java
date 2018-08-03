package org.iata.bsplink.refund.loader.validation;

import java.util.ArrayList;
import java.util.List;

import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.model.RefundDocument;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CompositeRefundDocumentValidator {

    private RecordFieldContentTypeValidator recordValidator;
    private RefundDocumentValidator documentValidator;

    /**
     * Creates the validator.
     */
    public CompositeRefundDocumentValidator(RecordFieldContentTypeValidator recordValidator,
            RefundDocumentValidator documentValidator) {

        this.recordValidator = recordValidator;
        this.documentValidator = documentValidator;
    }

    /**
     * Validates the document.
     */
    public boolean validate(RefundDocument refund, List<RefundLoaderError> refundLoaderErrors) {

        return validateRecords(refund, refundLoaderErrors)
                && documentValidator.validate(refund, refundLoaderErrors);
    }

    private boolean validateRecords(RefundDocument refund,
            List<RefundLoaderError> refundLoaderErrors) {

        List<RefundLoaderError> errors = new ArrayList<>();

        if (refund.getRecordIt02() != null) {
            recordValidator.validate(refund.getRecordIt02(), refundLoaderErrors);
        }

        refund.getRecordsIt03().forEach(record -> recordValidator.validate(record, errors));
        refund.getRecordsIt05().forEach(record -> recordValidator.validate(record, errors));
        refund.getRecordsIt08().forEach(record -> recordValidator.validate(record, errors));
        refund.getRecordsIt0h().forEach(record -> recordValidator.validate(record, errors));
        refund.getRecordsIt0y().forEach(record -> recordValidator.validate(record, errors));

        refundLoaderErrors.addAll(errors);

        return errors.isEmpty();
    }

}
