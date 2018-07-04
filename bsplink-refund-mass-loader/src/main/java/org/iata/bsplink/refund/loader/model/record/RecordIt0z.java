package org.iata.bsplink.refund.loader.model.record;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RecordIt0z implements Record {

    private String recordIdentifier;
    private String reportRecordCounter;
}
