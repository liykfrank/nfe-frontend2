package org.iata.bsplink.refund.loader.model.record;

import java.util.Map;

public class RecordIt03Layout extends RecordBaseLayout {

    private static final String PATTERN = "3*";

    public RecordIt03Layout() {

        super(PATTERN);
    }

    @Override
    protected void setFieldsLayout(Map<String, String> fieldsLayout) {

        fieldsLayout.put("checkDigit1", "27-27");
        fieldsLayout.put("checkDigit2", "47-47");
        fieldsLayout.put("checkDigit3", "67-67");
        fieldsLayout.put("checkDigit4", "87-87");
        fieldsLayout.put("checkDigit5", "107-107");
        fieldsLayout.put("checkDigit6", "127-127");
        fieldsLayout.put("checkDigit7", "147-147");
        fieldsLayout.put("dateOfIssueRelatedDocument", "148-153");
        fieldsLayout.put("relatedTicketDocumentCouponNumberIdentifier1", "8-11");
        fieldsLayout.put("relatedTicketDocumentCouponNumberIdentifier2", "28-31");
        fieldsLayout.put("relatedTicketDocumentCouponNumberIdentifier3", "48-51");
        fieldsLayout.put("relatedTicketDocumentCouponNumberIdentifier4", "68-71");
        fieldsLayout.put("relatedTicketDocumentCouponNumberIdentifier5", "88-91");
        fieldsLayout.put("relatedTicketDocumentCouponNumberIdentifier6", "108-111");
        fieldsLayout.put("relatedTicketDocumentCouponNumberIdentifier7", "128-131");
        fieldsLayout.put("relatedTicketDocumentNumber1", "12-26");
        fieldsLayout.put("relatedTicketDocumentNumber2", "32-46");
        fieldsLayout.put("relatedTicketDocumentNumber3", "52-66");
        fieldsLayout.put("relatedTicketDocumentNumber4", "72-86");
        fieldsLayout.put("relatedTicketDocumentNumber5", "92-106");
        fieldsLayout.put("relatedTicketDocumentNumber6", "112-126");
        fieldsLayout.put("relatedTicketDocumentNumber7", "132-146");
        fieldsLayout.put("tourCode", "154-168");
        fieldsLayout.put("transactionNumber", "2-7");
        fieldsLayout.put("waiverCode", "169-182");
    }

}
