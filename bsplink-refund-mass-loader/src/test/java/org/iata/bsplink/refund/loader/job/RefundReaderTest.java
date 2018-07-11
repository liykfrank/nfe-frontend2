package org.iata.bsplink.refund.loader.job;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.TRANSACTION_NUMBER_1;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.TRANSACTION_NUMBER_2;
import static org.iata.bsplink.refund.loader.test.fixtures.RefundDocumentFixtures.getTransactions;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.iata.bsplink.refund.loader.model.RefundDocument;
import org.iata.bsplink.refund.loader.model.record.Record;
import org.iata.bsplink.refund.loader.model.record.RecordIt01;
import org.iata.bsplink.refund.loader.model.record.RecordIt0z;
import org.iata.bsplink.refund.loader.model.record.RecordRawLine;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.boot.test.rule.OutputCapture;

@RunWith(MockitoJUnitRunner.class)
public class RefundReaderTest {

    private static final String TRANSACTIONS_READ_COUNT_KEY = "transactions_read_count";

    @Mock
    private ItemReader<Record> delegate;

    @Rule
    public OutputCapture capture;

    private RefundDocument refund;
    private RefundReader reader;
    private ExecutionContext executionContext;

    @Before
    public void setUp() {

        capture = new OutputCapture();

        reader = new RefundReader(delegate);

        StepExecution stepExecution = mock(StepExecution.class);
        executionContext = new ExecutionContext();

        when(stepExecution.getExecutionContext()).thenReturn(executionContext);

        reader.beforeStep(stepExecution);
    }

    @Test
    public void testCanReadMoreThanOneRefund() throws Exception {

        configureDelegateMock();

        RefundDocument refund1 = reader.read();
        RefundDocument refund2 = reader.read();

        assertThat(refund1, notNullValue());
        assertThat(refund1.getRecordIt02().getTransactionNumber(),
                equalTo(TRANSACTION_NUMBER_1));

        assertThat(refund2, notNullValue());
        assertThat(refund2.getRecordIt02().getTransactionNumber(),
                equalTo(TRANSACTION_NUMBER_2));
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

    @Test
    public void testRecordIt01IsExcluded() throws Exception {

        when(delegate.read()).thenReturn(new RecordIt01(), (Record) null);

        assertThat(reader.read(), nullValue());
    }

    @Test
    public void testRecordIt0zIsExcluded() throws Exception {

        when(delegate.read()).thenReturn(new RecordIt0z(), (Record) null);

        assertThat(reader.read(), nullValue());
    }

    @Test
    public void testReadsRecordIt02() throws Exception {

        configureDelegateMock();

        refund = reader.read();

        assertThat(refund.getRecordIt02().getTransactionNumber(), equalTo(TRANSACTION_NUMBER_1));
    }

    @Test
    public void testReadsRecordsIt03() throws Exception {

        configureDelegateMock();

        refund = reader.read();

        assertThat(refund.getRecordsIt03(), hasSize(2));
        assertThat(refund.getRecordsIt03().get(0).getTransactionNumber(),
                equalTo(TRANSACTION_NUMBER_1));
        assertThat(refund.getRecordsIt03().get(1).getTransactionNumber(),
                equalTo(TRANSACTION_NUMBER_1));
    }

    @Test
    public void testReadsRecordsIt05() throws Exception {

        configureDelegateMock();

        refund = reader.read();

        assertThat(refund.getRecordsIt05(), hasSize(2));
        assertThat(refund.getRecordsIt05().get(0).getTransactionNumber(),
                equalTo(TRANSACTION_NUMBER_1));
        assertThat(refund.getRecordsIt05().get(1).getTransactionNumber(),
                equalTo(TRANSACTION_NUMBER_1));
    }

    @Test
    public void testReadsRecordsIt08() throws Exception {

        configureDelegateMock();

        refund = reader.read();

        assertThat(refund.getRecordsIt08(), hasSize(2));
        assertThat(refund.getRecordsIt08().get(0).getTransactionNumber(),
                equalTo(TRANSACTION_NUMBER_1));
        assertThat(refund.getRecordsIt08().get(1).getTransactionNumber(),
                equalTo(TRANSACTION_NUMBER_1));
    }

    @Test
    public void testReadsRecordsIt0y() throws Exception {

        configureDelegateMock();

        refund = reader.read();

        assertThat(refund.getRecordsIt0y(), hasSize(2));
    }

    @Test
    public void testReadsRecordsIt0h() throws Exception {

        configureDelegateMock();

        refund = reader.read();

        assertThat(refund.getRecordsIt0h(), hasSize(2));
        assertThat(refund.getRecordsIt0h().get(0).getTransactionNumber(),
                equalTo(TRANSACTION_NUMBER_1));
        assertThat(refund.getRecordsIt0h().get(1).getTransactionNumber(),
                equalTo(TRANSACTION_NUMBER_1));
    }

    @Test
    public void testLogsUnknownRecords() throws Exception {

        when(delegate.read()).thenReturn(new RecordRawLine(), (Record) null);

        refund = reader.read();

        capture.expect(containsString("UNKNOW RECORD"));
    }

    @Test
    public void testContinuesReadingFromPreviousExecutions() throws Exception {

        configureDelegateMock();

        executionContext.put(TRANSACTIONS_READ_COUNT_KEY, 1L);

        RefundDocument refund1 = reader.read();
        RefundDocument refund2 = reader.read();

        assertThat(refund1, notNullValue());
        assertThat(refund1.getRecordIt02().getTransactionNumber(),
                equalTo(TRANSACTION_NUMBER_2));

        assertThat(refund2, nullValue());
    }

    @Test
    public void testUpdatesTransactionsReadCounter() throws Exception {

        configureDelegateMock();

        reader.read();

        long firstReadCounter = executionContext.getLong(TRANSACTIONS_READ_COUNT_KEY);

        reader.read();

        long secondReadCounter = executionContext.getLong(TRANSACTIONS_READ_COUNT_KEY);

        assertThat(firstReadCounter, equalTo(1L));
        assertThat(secondReadCounter, equalTo(2L));
    }

}
