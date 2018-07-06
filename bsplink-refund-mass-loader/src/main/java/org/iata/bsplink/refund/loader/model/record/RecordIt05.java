package org.iata.bsplink.refund.loader.model.record;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RecordIt05 implements Record {

    @Setter(AccessLevel.NONE)
    private RecordIdentifier recordIdentifier = RecordIdentifier.IT05;

    private String amountEnteredByAgent;
    private String amountPaidByCustomer;
    private String commissionAmount1;
    private String commissionAmount2;
    private String commissionAmount3;
    private String commissionRate1;
    private String commissionRate2;
    private String commissionRate3;
    private String commissionType1;
    private String commissionType2;
    private String commissionType3;
    private String couponSegmentCurrencyType;
    private String netReportingIndicator;
    private String reservedSpace1;
    private String reservedSpace2;
    private String taxOnCommissionAmount;
    private String taxOnCommissionType;
    private String taxMiscellaneousFeeAmount1;
    private String taxMiscellaneousFeeAmount2;
    private String taxMiscellaneousFeeAmount3;
    private String taxMiscellaneousFeeAmount4;
    private String taxMiscellaneousFeeAmount5;
    private String taxMiscellaneousFeeAmount6;
    private String taxMiscellaneousFeeType1;
    private String taxMiscellaneousFeeType2;
    private String taxMiscellaneousFeeType3;
    private String taxMiscellaneousFeeType4;
    private String taxMiscellaneousFeeType5;
    private String taxMiscellaneousFeeType6;
    private String ticketDocumentAmount;
    private String transactionNumber;
}