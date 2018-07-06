package org.iata.bsplink.refund.loader.test.fixtures;

import static org.iata.bsplink.refund.loader.test.fixtures.Constants.TRANSACTION_NUMBER_1;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.TRANSACTION_NUMBER_2;

import java.util.ArrayList;
import java.util.List;

import org.iata.bsplink.refund.loader.model.record.Record;
import org.iata.bsplink.refund.loader.model.record.RecordIt01;
import org.iata.bsplink.refund.loader.model.record.RecordIt02;
import org.iata.bsplink.refund.loader.model.record.RecordIt03;
import org.iata.bsplink.refund.loader.model.record.RecordIt05;
import org.iata.bsplink.refund.loader.model.record.RecordIt08;
import org.iata.bsplink.refund.loader.model.record.RecordIt0h;
import org.iata.bsplink.refund.loader.model.record.RecordIt0y;
import org.iata.bsplink.refund.loader.model.record.RecordIt0z;

public class RefundDocumentFixtures {

    /**
     * Returns a list of transactions composed by Record objects.
     */
    public static List<Record> getTransactions() {

        List<Record> records = new ArrayList<>();

        records.add(new RecordIt01());
        records.addAll(getTransaction(TRANSACTION_NUMBER_1));
        records.addAll(getTransaction(TRANSACTION_NUMBER_2));
        records.add(new RecordIt0z());

        return records;
    }

    private static List<Record> getTransaction(String transactionNumber) {

        List<Record> records = new ArrayList<>();

        // IT02
        RecordIt02 recordIt02 = new RecordIt02();
        recordIt02.setTransactionNumber(transactionNumber);
        records.add(recordIt02);

        // IT03
        RecordIt03 firstRecordIt03 = new RecordIt03();
        firstRecordIt03.setTransactionNumber(transactionNumber);
        records.add(firstRecordIt03);

        RecordIt03 secondRecordIt03 = new RecordIt03();
        secondRecordIt03.setTransactionNumber(transactionNumber);
        records.add(secondRecordIt03);

        // IT05
        RecordIt05 firstRecordIt05 = new RecordIt05();
        firstRecordIt05.setTransactionNumber(transactionNumber);
        records.add(firstRecordIt05);

        RecordIt05 secondRecordIt05 = new RecordIt05();
        secondRecordIt05.setTransactionNumber(transactionNumber);
        records.add(secondRecordIt05);

        // IT08
        RecordIt08 firstRecordIt08 = new RecordIt08();
        firstRecordIt08.setTransactionNumber(transactionNumber);
        records.add(firstRecordIt08);

        RecordIt08 secondRecordIt08 = new RecordIt08();
        secondRecordIt08.setTransactionNumber(transactionNumber);
        records.add(secondRecordIt08);

        // IT0Y
        RecordIt0y firstRecordIt0y = new RecordIt0y();
        records.add(firstRecordIt0y);

        RecordIt0y secondRecordIt0y = new RecordIt0y();
        records.add(secondRecordIt0y);

        // IT0H
        RecordIt0h firstRecordIt0h = new RecordIt0h();
        firstRecordIt0h.setTransactionNumber(transactionNumber);
        records.add(firstRecordIt0h);

        RecordIt0h secondRecordIt0h = new RecordIt0h();
        secondRecordIt0h.setTransactionNumber(transactionNumber);
        records.add(secondRecordIt0h);

        return records;
    }

}
