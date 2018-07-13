package org.iata.bsplink.refund.loader.model.record;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RecordIt0y implements TransactionRecord {

    @Setter(AccessLevel.NONE)
    private RecordIdentifier recordIdentifier = RecordIdentifier.IT0Y;
    private int lineNumber;

    private String transactionNumber;
}
