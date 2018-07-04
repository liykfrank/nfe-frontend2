package org.iata.bsplink.refund.loader.model.record;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RecordIt0h implements Record {

    private String reasonForMemoInformation1;
    private String reasonForMemoInformation2;
    private String reasonForMemoInformation3;
    private String reasonForMemoInformation4;
    private String reasonForMemoInformation5;
    private String reasonForMemoIssuanceCode;
    private String reasonForMemoLineIdentifier1;
    private String reasonForMemoLineIdentifier2;
    private String reasonForMemoLineIdentifier3;
    private String reasonForMemoLineIdentifier4;
    private String reasonForMemoLineIdentifier5;
    private String recordIdentifier;
    private String transactionNumber;
}
