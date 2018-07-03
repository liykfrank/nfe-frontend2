package org.iata.bsplink.refund.loader.model.record;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RecordIt02 implements Record {

    private String recordIdentifier;
    private String transactionNumber;
}
