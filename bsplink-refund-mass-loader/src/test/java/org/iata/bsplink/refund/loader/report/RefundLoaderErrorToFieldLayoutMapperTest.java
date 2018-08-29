package org.iata.bsplink.refund.loader.report;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.iata.bsplink.refund.loader.test.fixtures.RefundDocumentFixtures.getValidationErrorsTransactionPhase;
import static org.iata.bsplink.refund.loader.test.fixtures.RefundDocumentFixtures.getValidationErrorsUpdatePhase;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.model.record.RecordIdentifier;
import org.junit.Before;
import org.junit.Test;

public class RefundLoaderErrorToFieldLayoutMapperTest {

    private RefundLoaderErrorToFieldLayoutMapper mapper;
    private List<RefundLoaderError> errors;

    @Before
    public void setUp() {

        mapper = new RefundLoaderErrorToFieldLayoutMapper();

        errors = new ArrayList<>();
        errors.addAll(getValidationErrorsTransactionPhase());
        errors.addAll(getValidationErrorsUpdatePhase());
    }

    @Test
    public void testAddsFieldLayoutsWhenThereIsRecordIdentifier() {

        mapper.addFieldLayouts(errors);

        RefundLoaderError error0 = errors.get(0);

        assertThat(error0.getFieldLayout().getField(), equalTo("relatedTicketDocumentNumber1"));
        assertThat(error0.getRecordIdentifier(), equalTo(RecordIdentifier.IT03));

        RefundLoaderError error1 = errors.get(1);

        assertThat(error1.getFieldLayout().getField(), equalTo("taxMiscellaneousFeeAmount2"));
        assertThat(error1.getRecordIdentifier(), equalTo(RecordIdentifier.IT05));
    }

    @Test
    public void testAddsFieldLayoutsWhenThereAreNotRecordIdentifier() {

        mapper.addFieldLayouts(errors);

        RefundLoaderError error2 = errors.get(2);

        assertThat(error2.getFieldLayout().getField(), equalTo("agentNumericCode"));
        assertThat(error2.getRecordIdentifier(), equalTo(RecordIdentifier.IT02));

        RefundLoaderError error3 = errors.get(3);
        assertThat(error3.getFieldLayout().getField(), equalTo("currencyType"));
        assertThat(error3.getRecordIdentifier(), equalTo(RecordIdentifier.IT05));
    }

}
