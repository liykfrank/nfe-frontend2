package org.iata.bsplink.refund.loader.model.record;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RecordIt03 implements TransactionRecord {

    @Setter(AccessLevel.NONE)
    private RecordIdentifier recordIdentifier = RecordIdentifier.IT03;

    private String checkDigit1;
    private String checkDigit2;
    private String checkDigit3;
    private String checkDigit4;
    private String checkDigit5;
    private String checkDigit6;
    private String checkDigit7;
    private String dateOfIssueRelatedDocument;
    private String relatedTicketDocumentCouponNumberIdentifier1;
    private String relatedTicketDocumentCouponNumberIdentifier2;
    private String relatedTicketDocumentCouponNumberIdentifier3;
    private String relatedTicketDocumentCouponNumberIdentifier4;
    private String relatedTicketDocumentCouponNumberIdentifier5;
    private String relatedTicketDocumentCouponNumberIdentifier6;
    private String relatedTicketDocumentCouponNumberIdentifier7;
    private String relatedTicketDocumentNumber1;
    private String relatedTicketDocumentNumber2;
    private String relatedTicketDocumentNumber3;
    private String relatedTicketDocumentNumber4;
    private String relatedTicketDocumentNumber5;
    private String relatedTicketDocumentNumber6;
    private String relatedTicketDocumentNumber7;
    private String tourCode;
    private String transactionNumber;
    private String waiverCode;
}
