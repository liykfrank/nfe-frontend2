package org.iata.bsplink.refund.loader.model.record;

import java.util.Map;

public class RecordIt05Layout extends RecordBaseLayout {

    private static final String PATTERN = "5*";

    public RecordIt05Layout() {

        super(PATTERN);
    }

    @Override
    protected void setFieldsLayout(Map<String, String> fieldsLayout) {

        fieldsLayout.put("amountEnteredByAgent", "8-18");
        fieldsLayout.put("amountPaidByCustomer", "239-249");
        fieldsLayout.put("commissionAmount1", "182-192");
        fieldsLayout.put("commissionAmount2", "204-214");
        fieldsLayout.put("commissionAmount3", "226-236");
        fieldsLayout.put("commissionRate1", "177-181");
        fieldsLayout.put("commissionRate2", "199-203");
        fieldsLayout.put("commissionRate3", "221-225");
        fieldsLayout.put("commissionType1", "171-176");
        fieldsLayout.put("commissionType2", "193-198");
        fieldsLayout.put("commissionType3", "215-220");
        fieldsLayout.put("currencyType", "98-101");
        fieldsLayout.put("netReportingIndicator", "237-238");
        fieldsLayout.put("taxOnCommissionAmount", "102-112");
        fieldsLayout.put("taxOnCommissionType", "250-255");
        fieldsLayout.put("taxMiscellaneousFeeAmount1", "38-48");
        fieldsLayout.put("taxMiscellaneousFeeAmount2", "57-67");
        fieldsLayout.put("taxMiscellaneousFeeAmount3", "76-86");
        fieldsLayout.put("taxMiscellaneousFeeAmount4", "122-132");
        fieldsLayout.put("taxMiscellaneousFeeAmount5", "141-151");
        fieldsLayout.put("taxMiscellaneousFeeAmount6", "160-170");
        fieldsLayout.put("taxMiscellaneousFeeType1", "30-37");
        fieldsLayout.put("taxMiscellaneousFeeType2", "49-56");
        fieldsLayout.put("taxMiscellaneousFeeType3", "68-75");
        fieldsLayout.put("taxMiscellaneousFeeType4", "114-121");
        fieldsLayout.put("taxMiscellaneousFeeType5", "133-140");
        fieldsLayout.put("taxMiscellaneousFeeType6", "152-159");
        fieldsLayout.put("ticketDocumentAmount", "87-97");
        fieldsLayout.put("transactionNumber", "2-7");
    }

}
