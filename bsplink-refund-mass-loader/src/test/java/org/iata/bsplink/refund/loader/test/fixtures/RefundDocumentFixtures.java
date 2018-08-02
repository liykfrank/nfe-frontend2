package org.iata.bsplink.refund.loader.test.fixtures;

import static org.iata.bsplink.refund.loader.test.fixtures.Constants.TRANSACTION_NUMBER_1;
import static org.iata.bsplink.refund.loader.test.fixtures.Constants.TRANSACTION_NUMBER_2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.error.ValidationPhase;
import org.iata.bsplink.refund.loader.model.record.FieldLayout;
import org.iata.bsplink.refund.loader.model.record.Record;
import org.iata.bsplink.refund.loader.model.record.RecordIdentifier;
import org.iata.bsplink.refund.loader.model.record.RecordIt01;
import org.iata.bsplink.refund.loader.model.record.RecordIt02;
import org.iata.bsplink.refund.loader.model.record.RecordIt02Layout;
import org.iata.bsplink.refund.loader.model.record.RecordIt03;
import org.iata.bsplink.refund.loader.model.record.RecordIt03Layout;
import org.iata.bsplink.refund.loader.model.record.RecordIt05;
import org.iata.bsplink.refund.loader.model.record.RecordIt05Layout;
import org.iata.bsplink.refund.loader.model.record.RecordIt08;
import org.iata.bsplink.refund.loader.model.record.RecordIt0h;
import org.iata.bsplink.refund.loader.model.record.RecordIt0y;
import org.iata.bsplink.refund.loader.model.record.RecordIt0z;
import org.iata.bsplink.refund.loader.model.record.RecordLayout;

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

    /**
     * Returns a list of validation errors occurred in the transaction phase.
     */
    public static List<RefundLoaderError> getValidationErrorsTransactionPhase() {

        RefundLoaderError error1 = new RefundLoaderError();

        error1.setField("relatedTicketDocumentNumber1");
        error1.setLineNumber(10);
        error1.setMessage("The record element is mandatory");
        error1.setTransactionNumber("000002");
        error1.setRecordIdentifier(RecordIdentifier.IT03);
        error1.setValidationPhase(ValidationPhase.TRANSACTION);

        RefundLoaderError error2 = new RefundLoaderError();

        error2.setField("taxMiscellaneousFeeAmount2");
        error2.setLineNumber(20);
        error2.setMessage("Non-numeric characters in numeric fields");
        error2.setTransactionNumber("000004");
        error2.setRecordIdentifier(RecordIdentifier.IT05);
        error2.setValidationPhase(ValidationPhase.TRANSACTION);

        return Arrays.asList(error1, error2);
    }

    /**
     * Returns a list of validation errors occurred in the update phase.
     */
    public static List<RefundLoaderError> getValidationErrorsUpdatePhase() {

        RefundLoaderError error1 = new RefundLoaderError();

        error1.setField("agentCode");
        error1.setLineNumber(null);
        error1.setMessage("The agent does not exist.");
        error1.setTransactionNumber("000006");
        error1.setValidationPhase(ValidationPhase.UPDATE);

        RefundLoaderError error2 = new RefundLoaderError();

        error2.setField("currency");
        error2.setLineNumber(null);
        error2.setMessage("The currency was not found.");
        error2.setTransactionNumber("000008");
        error2.setValidationPhase(ValidationPhase.UPDATE);

        return Arrays.asList(error1, error2);
    }

    /**
     * Returns a map of record layouts.
     */
    public static Map<RecordIdentifier, RecordLayout> getRecordLayouts() {

        Map<RecordIdentifier, RecordLayout> recordLayouts = new HashMap<>();

        recordLayouts.put(RecordIdentifier.IT03, new RecordIt03Layout());
        recordLayouts.put(RecordIdentifier.IT05, new RecordIt05Layout());

        return recordLayouts;
    }

    /**
     * Returns a map of field names and theirs layouts.
     */
    public static Map<String, FieldLayout> getFieldNameToFieldLayoutMap() {

        RecordIt02Layout recordIt02Layout = new RecordIt02Layout();
        RecordIt05Layout recordIt05Layout = new RecordIt05Layout();

        Map<String, FieldLayout> fieldLayouts = new HashMap<>();

        fieldLayouts.put("agentCode", recordIt02Layout.getFieldLayout("agentNumericCode"));
        fieldLayouts.put("currency", recordIt05Layout.getFieldLayout("currencyType"));

        return fieldLayouts;
    }

}
