package org.iata.bsplink.refund.loader.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Refund {

    private Long id;
    private String agentCode;
    private Contact agentContact;
    private String agentRegistrationNumber;
    private String agentVatNumber;
    private String airlineCode;
    private String airlineCodeRelatedDocument;
    private Contact airlineContact;
    private String airlineRegistrationNumber;
    private String airlineRemark;
    private String airlineVatNumber;
    private RefundAmounts amounts;
    private Integer billingPeriod;
    private List<RelatedDocument> conjunctions;
    private RefundCurrency currency;
    private String customerFileReference;
    private LocalDate dateOfAirlineAction;
    private LocalDate dateOfIssue;
    private LocalDate dateOfIssueRelatedDocument;
    private Boolean exchange;
    private List<FormOfPaymentAmount> formOfPaymentAmounts;
    private String isoCountryCode;
    private String issueReason;
    private Boolean netReporting;
    private OriginalIssue originalIssue;
    private Boolean partialRefund;
    private String passenger;
    private String rejectionReason;
    private RelatedDocument relatedDocument;
    private String settlementAuthorisationCode;
    private String statisticalCode;
    private RefundStatus status;
    private List<TaxMiscellaneousFee> taxMiscellaneousFees;
    private String ticketDocumentNumber;
    private String tourCode;
    private String waiverCode;
}
