package org.iata.bsplink.refund.loader.model.record;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RecordIt01 implements Record {

    private String fileType;
    private String fileTypeSequenceNumber;
    private String handbookRevisionNumber;
    private String isoCountryCode;
    private String processingDate;
    private String processingTime;
    private String recordIdentifier;
    private String reportingSystemIdentifier;
    private String reservedSpace1;
    private String reservedSpace2;
    private String systemProviderReportingPeriodEndingDate;
    private String testProductionStatus;
}
