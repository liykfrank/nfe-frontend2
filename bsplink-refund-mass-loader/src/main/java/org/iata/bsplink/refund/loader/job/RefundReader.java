package org.iata.bsplink.refund.loader.job;

import static org.iata.bsplink.refund.loader.model.record.RecordIdentifier.IT01;
import static org.iata.bsplink.refund.loader.model.record.RecordIdentifier.IT0Z;

import java.util.Optional;

import lombok.extern.java.Log;

import org.iata.bsplink.refund.loader.model.RefundDocument;
import org.iata.bsplink.refund.loader.model.record.Record;
import org.iata.bsplink.refund.loader.model.record.RecordIdentifier;
import org.iata.bsplink.refund.loader.model.record.RecordIt02;
import org.iata.bsplink.refund.loader.model.record.RecordIt03;
import org.iata.bsplink.refund.loader.model.record.RecordIt05;
import org.iata.bsplink.refund.loader.model.record.RecordIt08;
import org.iata.bsplink.refund.loader.model.record.RecordIt0h;
import org.iata.bsplink.refund.loader.model.record.RecordIt0y;
import org.iata.bsplink.refund.loader.model.record.RecordRawLine;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Component
@Log
public class RefundReader implements ItemReader<RefundDocument> {

    private ItemReader<Record> delegate;
    private Optional<RecordIt02> newTransaction = Optional.empty();
    private boolean readingTransaction = false;

    public RefundReader(ItemReader<Record> delegate) {

        this.delegate = delegate;
    }

    @Override
    public RefundDocument read() throws Exception {

        RefundDocument refund = null;
        readingTransaction = false;

        for (Record line = null; (line = delegate.read()) != null;) {

            if (isTransactionRecord(line)) {

                if (refund == null) {

                    refund = new RefundDocument();

                    if (newTransaction.isPresent()) {
                        refund.setRecordIt02(newTransaction.get());
                        readingTransaction = true;
                        newTransaction = Optional.empty();
                    }
                }

                readRecord(refund, line);

                if (!readingTransaction) {
                    break;
                }
            }
        }

        return refund;
    }

    private boolean isTransactionRecord(Record record) {

        RecordIdentifier recordIdentifier = record.getRecordIdentifier();

        return ! (IT01.equals(recordIdentifier)
                || IT0Z.equals(recordIdentifier));
    }

    private void readRecord(RefundDocument refund, Record line) {

        switch (line.getRecordIdentifier()) {

            case IT02:

                if (readingTransaction) {

                    newTransaction = Optional.ofNullable((RecordIt02) line);
                    readingTransaction = false;

                    return;
                }

                readingTransaction = true;
                refund.setRecordIt02((RecordIt02) line);

                break;

            case IT03:
                refund.addrecordIt03(((RecordIt03) line));
                break;

            case IT05:
                refund.addrecordIt05(((RecordIt05) line));
                break;

            case IT08:
                refund.addrecordIt08(((RecordIt08) line));
                break;

            case IT0Y:
                refund.addrecordIt0y(((RecordIt0y) line));
                break;

            case IT0H:
                refund.addrecordIt0h(((RecordIt0h) line));
                break;

            default:
                log.info("UNKNOW RECORD: " + ((RecordRawLine) line).getLine());
                break;
        }

    }

}
