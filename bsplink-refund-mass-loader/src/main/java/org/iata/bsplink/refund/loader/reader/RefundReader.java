package org.iata.bsplink.refund.loader.reader;

import org.iata.bsplink.refund.loader.listener.JobCompletionNotificationListener;
import org.iata.bsplink.refund.loader.model.Refund;
import org.iata.bsplink.refund.loader.model.record.Record;
import org.iata.bsplink.refund.loader.model.record.RecordIdentifier;
import org.iata.bsplink.refund.loader.model.record.RecordIt01;
import org.iata.bsplink.refund.loader.model.record.RecordIt02;
import org.iata.bsplink.refund.loader.model.record.RecordRawLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;

public class RefundReader implements ItemReader<Refund> {

    private static final Logger log =
            LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private ItemReader<Record> delegate;

    public RefundReader(ItemReader<Record> delegate) {

        this.delegate = delegate;
    }

    @Override
    public Refund read() throws Exception {

        Refund refund = null;

        for (Record line = null; (line = delegate.read()) != null;) {

            if (refund == null) {
                refund = new Refund();
            }

            String recordIdentifier = line.getRecordIdentifier();

            if (RecordIdentifier.IT01.getIdentifier().equals(recordIdentifier)) {

                refund.setRecordIt01((RecordIt01) line);

            } else if (RecordIdentifier.IT02.getIdentifier().equals(recordIdentifier)) {

                refund.setRecordIt02((RecordIt02) line);

            } else {

                String rawLine = recordIdentifier + ((RecordRawLine) line).getLine();
                log.info(rawLine);
            }
        }

        return refund;
    }

}
