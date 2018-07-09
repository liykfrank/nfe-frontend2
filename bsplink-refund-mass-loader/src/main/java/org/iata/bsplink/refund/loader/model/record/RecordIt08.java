package org.iata.bsplink.refund.loader.model.record;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RecordIt08 implements Record {

    @Setter(AccessLevel.NONE)
    private RecordIdentifier recordIdentifier = RecordIdentifier.IT08;

    private String addressVerificationCode1;
    private String addressVerificationCode2;
    private String approvalCode1;
    private String approvalCode2;
    private String authorisedAmount1;
    private String authorisedAmount2;
    private String creditCardCorporateContract1;
    private String creditCardCorporateContract2;
    private String currencyType1;
    private String currencyType2;
    private String customerFileReference1;
    private String customerFileReference2;
    private String expiryDate1;
    private String expiryDate2;
    private String extendedPaymentCode1;
    private String extendedPaymentCode2;
    private String formOfPaymentAccountNumber1;
    private String formOfPaymentAccountNumber2;
    private String formOfPaymentAmount1;
    private String formOfPaymentAmount2;
    private String formOfPaymentType1;
    private String formOfPaymentType2;
    private String formOfPaymentTransactionIdentifier1;
    private String formOfPaymentTransactionIdentifier2;
    private String sourceOfApprovalCode1;
    private String sourceOfApprovalCode2;
    private String transactionNumber;
}