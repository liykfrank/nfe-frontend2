package org.iata.bsplink.refund.loader.job;

import lombok.extern.java.Log;

import org.iata.bsplink.refund.loader.model.RefundDocument;
import org.iata.bsplink.refund.loader.model.record.Record;
import org.iata.bsplink.refund.loader.model.record.RecordIdentifier;
import org.iata.bsplink.refund.loader.model.record.RecordIt01;
import org.iata.bsplink.refund.loader.model.record.RecordIt02;
import org.iata.bsplink.refund.loader.model.record.RecordRawLine;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Component
@Log
public class RefundReader implements ItemReader<RefundDocument> {

    private ItemReader<Record> delegate;

    public RefundReader(ItemReader<Record> delegate) {

        this.delegate = delegate;
    }

    @Override
    public RefundDocument read() throws Exception {

        RefundDocument refund = null;

        for (Record line = null; (line = delegate.read()) != null;) {

            if (refund == null) {
                refund = new RefundDocument();
            }

            String recordIdentifier = line.getRecordIdentifier();

            if (RecordIdentifier.IT01.matches(recordIdentifier)) {

                refund.setRecordIt01((RecordIt01) line);

            } else if (RecordIdentifier.IT02.matches(recordIdentifier)) {

                refund.setRecordIt02((RecordIt02) line);

            } else {

                log.info(recordIdentifier + ((RecordRawLine) line).getLine());
            }
        }

        return refund;
    }

}
