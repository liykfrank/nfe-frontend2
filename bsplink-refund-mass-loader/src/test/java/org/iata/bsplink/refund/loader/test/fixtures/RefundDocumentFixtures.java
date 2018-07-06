package org.iata.bsplink.refund.loader.test.fixtures;

import static org.iata.bsplink.refund.loader.test.fixtures.Constants.TICKET_DOCUMENT_NUMBER_1;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.TICKET_DOCUMENT_NUMBER_2;

import java.util.ArrayList;
import java.util.List;

import org.iata.bsplink.refund.loader.model.record.Record;
import org.iata.bsplink.refund.loader.model.record.RecordIt01;
import org.iata.bsplink.refund.loader.model.record.RecordIt02;
import org.iata.bsplink.refund.loader.model.record.RecordIt03;

public class RefundDocumentFixtures {

    /**
     * Returns a list of transactions composed by Record objects.
     */
    public static List<Record> getTransactions() {

        List<Record> records = new ArrayList<>();

        RecordIt01 recordIt01 = new RecordIt01();

        records.add(recordIt01);
        records.addAll(getTransaction(TICKET_DOCUMENT_NUMBER_1));
        records.addAll(getTransaction(TICKET_DOCUMENT_NUMBER_2));

        return records;
    }

    private static List<Record> getTransaction(String transactionNumber) {

        List<Record> records = new ArrayList<>();

        RecordIt02 recordIt02 = new RecordIt02();
        recordIt02.setTransactionNumber(transactionNumber);
        records.add(recordIt02);

        RecordIt03 recordIt03 = new RecordIt03();
        recordIt03.setTransactionNumber(transactionNumber);
        records.add(recordIt03);

        return records;
    }

}
