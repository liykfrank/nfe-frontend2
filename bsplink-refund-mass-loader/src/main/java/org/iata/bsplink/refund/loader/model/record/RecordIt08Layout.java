package org.iata.bsplink.refund.loader.model.record;

import java.util.Map;

public class RecordIt08Layout extends RecordBaseLayout {

    private static final String PATTERN = "8*";

    public RecordIt08Layout() {

        super(PATTERN);
    }

    @Override
    protected void setFieldsLayout(Map<String, String> fieldsLayout) {

        fieldsLayout.put("addressVerificationCode1", "92-93");
        fieldsLayout.put("addressVerificationCode2", "215-216");
        fieldsLayout.put("approvalCode1", "38-43");
        fieldsLayout.put("approvalCode2", "161-166");
        fieldsLayout.put("authorisedAmount1", "120-130");
        fieldsLayout.put("authorisedAmount2", "243-253");
        fieldsLayout.put("creditCardCorporateContract1", "91-91");
        fieldsLayout.put("creditCardCorporateContract2", "214-214");
        fieldsLayout.put("currencyType1", "44-47");
        fieldsLayout.put("currencyType2", "167-170");
        fieldsLayout.put("customerFileReference1", "64-90");
        fieldsLayout.put("customerFileReference2", "187-213");
        fieldsLayout.put("expiryDate1", "60-63");
        fieldsLayout.put("expiryDate2", "183-186");
        fieldsLayout.put("extendedPaymentCode1", "48-49");
        fieldsLayout.put("extendedPaymentCode2", "171-172");
        fieldsLayout.put("formOfPaymentAccountNumber1", "8-26");
        fieldsLayout.put("formOfPaymentAccountNumber2", "131-149");
        fieldsLayout.put("formOfPaymentAmount1", "27-37");
        fieldsLayout.put("formOfPaymentAmount2", "150-160");
        fieldsLayout.put("formOfPaymentType1", "50-59");
        fieldsLayout.put("formOfPaymentType2", "173-182");
        fieldsLayout.put("formOfPaymentTransactionIdentifier1", "95-119");
        fieldsLayout.put("formOfPaymentTransactionIdentifier2", "218-242");
        fieldsLayout.put("sourceOfApprovalCode1", "94-94");
        fieldsLayout.put("sourceOfApprovalCode2", "217-217");
        fieldsLayout.put("transactionNumber", "2-7");
    }

}