package org.iata.bsplink.refund.loader.model.record;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RecordIt02 implements TransactionRecord {

    @Setter(AccessLevel.NONE)
    private RecordIdentifier recordIdentifier = RecordIdentifier.IT02;

    private String agentNumericCode;
    private String approvedLocationNumericCode1;
    private String approvedLocationNumericCode2;
    private String approvedLocationNumericCode3;
    private String approvedLocationType1;
    private String approvedLocationType2;
    private String approvedLocationType3;
    private String approvedLocationType4;
    private String checkDigit1;
    private String checkDigit2;
    private String conjunctionTicketIndicator;
    private String couponUseIndicator;
    private String dataInputStatusIndicator;
    private String dateOfIssue;
    private String formatIdentifier;
    private String stockSetStockControlNumberFrom1;
    private String stockSetStockControlNumberFrom2;
    private String stockSetStockControlNumberFrom3;
    private String stockSetStockControlNumberTo1;
    private String stockSetStockControlNumberTo2;
    private String stockSetStockControlNumberTo3;
    private String stockSetStockControlNumberTo4;
    private String isoCountryCode;
    private String passengerName;
    private String refundApplicationStatus;
    private String settlementAuthorisationCode;
    private String statisticalCode;
    private String stockControlNumber;
    private String ticketDocumentNumber;
    private String ticketingAirlineCodeNumber;
    private String transactionCode;
    private String transactionNumber;
    private String transmissionControlNumber;
    private String transmissionControlNumberCheckDigit;
    private String vendorIdentification;
    private String vendorIsoCountryCode;
}
