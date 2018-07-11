package org.iata.bsplink.refund.loader.error;

import lombok.Getter;
import lombok.Setter;

import org.iata.bsplink.refund.loader.model.record.RecordIdentifier;

@Getter
@Setter
public class RefundLoaderError {
    String message;
    String transactionNumber;
    String field;
    RecordIdentifier recordIdentifier;
}
