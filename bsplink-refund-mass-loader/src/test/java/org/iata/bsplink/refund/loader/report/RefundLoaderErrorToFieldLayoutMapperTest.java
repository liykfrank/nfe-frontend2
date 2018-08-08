package org.iata.bsplink.refund.loader.report;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.iata.bsplink.refund.loader.test.fixtures.RefundDocumentFixtures.getFieldNameToFieldLayoutMap;
import static org.iata.bsplink.refund.loader.test.fixtures.RefundDocumentFixtures.getValidationErrorsTransactionPhase;
import static org.iata.bsplink.refund.loader.test.fixtures.RefundDocumentFixtures.getValidationErrorsUpdatePhase;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.junit.Before;
import org.junit.Test;

public class RefundLoaderErrorToFieldLayoutMapperTest {

    private RefundLoaderErrorToFieldLayoutMapper mapper;
    private List<RefundLoaderError> errors;

    @Before
    public void setUp() {

        mapper = new RefundLoaderErrorToFieldLayoutMapper(getFieldNameToFieldLayoutMap());

        errors = new ArrayList<>();
        errors.addAll(getValidationErrorsTransactionPhase());
        errors.addAll(getValidationErrorsUpdatePhase());
    }

    @Test
    public void testAddsFieldLayoutsWhenThereIsRecordIdentifier() {

        mapper.addFieldLayouts(errors);

        assertThat(errors.get(0).getFieldLayout().getField(),
                equalTo("relatedTicketDocumentNumber1"));
        assertThat(errors.get(1).getFieldLayout().getField(),
                equalTo("taxMiscellaneousFeeAmount2"));
    }

    @Test
    public void testAddsFieldLayoutsWhenThereAreNotRecordIdentifier() {

        mapper.addFieldLayouts(errors);

        assertThat(errors.get(2).getFieldLayout().getField(), equalTo("agentNumericCode"));
        assertThat(errors.get(3).getFieldLayout().getField(), equalTo("currencyType"));
    }

}
