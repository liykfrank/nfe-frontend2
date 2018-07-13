package org.iata.bsplink.refund.loader.error;

import lombok.Getter;
import lombok.Setter;

import org.iata.bsplink.refund.loader.model.record.RecordIdentifier;

@Getter
@Setter
public class RefundLoaderError {

    private String message;
    private String transactionNumber;
    private String field;
    private Integer lineNumber;
    private RecordIdentifier recordIdentifier;
}
