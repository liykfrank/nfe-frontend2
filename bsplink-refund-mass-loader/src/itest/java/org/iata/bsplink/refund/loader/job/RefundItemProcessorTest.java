package org.iata.bsplink.refund.loader.job;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

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
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.batch.job.enabled = false")
public class RefundItemProcessorTest {

    @Autowired
    ItemProcessor<RefundDocument, Refund> processor;

    @Autowired
    List<RefundLoaderError> refundLoaderErrors;

    @MockBean
    private RefundCreator refundCreator;

    private Refund refundCreated;

    @Before
    public void setUp() throws Exception {
        refundCreated = new Refund();
        when(refundCreator.create()).thenReturn(refundCreated);
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

        RefundDocument refundDocument = new RefundDocument();
        refundDocument.setRecordIt02(new RecordIt02());
        refundDocument.getRecordIt02().setTransactionCode("CANX");

        Refund refund = processor.process(refundDocument);

        assertNull(refund);
        verifyZeroInteractions(refundCreator);
        assertThat(refundLoaderErrors, not(IsEmptyCollection.empty()));
        assertThat(refundLoaderErrors.get(0).getMessage(),
                equalTo(RefundDocumentValidator.INCORRECT_TRANSACTION_CODE));
    }
}
