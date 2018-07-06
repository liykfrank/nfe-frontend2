package org.iata.bsplink.refund.loader.job;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.TICKET_DOCUMENT_NUMBER_1;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.TICKET_DOCUMENT_NUMBER_2;
import static org.iata.bsplink.refund.loader.test.fixtures.RefundDocumentFixtures.getTransactions;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.iata.bsplink.refund.loader.model.RefundDocument;
import org.iata.bsplink.refund.loader.model.record.Record;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.batch.item.ItemReader;

@RunWith(MockitoJUnitRunner.class)
public class RefundReaderTest {

    @Mock
    private ItemReader<Record> delegate;

    private RefundReader reader;

    @Before
    public void setUp() {

        reader = new RefundReader(delegate);
    }

    @Test
    public void testCanReadMoreThanOneRefund() throws Exception {

        configureDelegateMock();

        RefundDocument refund1 = reader.read();
        RefundDocument refund2 = reader.read();

        assertThat(refund1, notNullValue());
        assertThat(refund1.getRecordIt02().getTransactionNumber(),
                equalTo(TICKET_DOCUMENT_NUMBER_1));

        assertThat(refund2, notNullValue());
        assertThat(refund2.getRecordIt02().getTransactionNumber(),
                equalTo(TICKET_DOCUMENT_NUMBER_2));
    }

    private void configureDelegateMock() throws Exception {

        OngoingStubbing<Record> stub = when(delegate.read());

        List<Record> records = getTransactions();
        records.add(null);

        for (Record record : records) {
            stub = stub.thenReturn(record);
        }
    }

    @Test
    public void testReturnsNullItThereIsNoMoreData() throws Exception {

        when(delegate.read()).thenReturn(null);

        assertThat(reader.read(), nullValue());
    }

}
