package org.iata.bsplink.refund.loader.writer;

import java.util.List;

import org.iata.bsplink.refund.loader.model.Refund;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

public class RefundConsoleWriter implements ItemWriter<Refund> {

    private static final Logger log = LoggerFactory.getLogger(RefundConsoleWriter.class);

    @Override
    public void write(List<? extends Refund> items) throws Exception {

        for (Refund item : items) {

            if (log.isInfoEnabled()) {
                log.info(item.toString());
            }
        }
    }

}
