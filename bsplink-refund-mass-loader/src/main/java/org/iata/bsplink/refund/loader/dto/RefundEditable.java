package org.iata.bsplink.refund.loader.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class RefundEditable {
    private String agentCode;
    private String airlineCodeRelatedDocument;
    private String airlineRemark;
    private RefundAmounts amounts;
    private List<RelatedDocument> conjunctions;
    private RefundCurrency currency;
    private String customerFileReference;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfIssueRelatedDocument;
    private List<FormOfPaymentAmount> formOfPaymentAmounts;
    private Boolean netReporting;
    private Boolean partialRefund = false;
    private RelatedDocument relatedDocument;
    private String settlementAuthorisationCode;
    private String statisticalCode;
    private RefundStatus status;
    private List<TaxMiscellaneousFee> taxMiscellaneousFees;
    private String waiverCode;
}
