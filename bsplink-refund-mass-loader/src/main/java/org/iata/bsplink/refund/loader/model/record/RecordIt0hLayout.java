package org.iata.bsplink.refund.loader.model.record;

import java.util.Map;

public class RecordIt0hLayout extends RecordBaseLayout {

    private static final String PATTERN = "H*";

    public RecordIt0hLayout() {

        super(PATTERN);
    }

    @Override
    protected void setFieldsLayout(Map<String, String> fieldsLayout) {

        fieldsLayout.put("reasonForMemoInformation1", "11-55");
        fieldsLayout.put("reasonForMemoInformation2", "59-103");
        fieldsLayout.put("reasonForMemoInformation3", "107-151");
        fieldsLayout.put("reasonForMemoInformation4", "155-199");
        fieldsLayout.put("reasonForMemoInformation5", "203-247");
        fieldsLayout.put("reasonForMemoIssuanceCode", "248-252");
        fieldsLayout.put("reasonForMemoLineIdentifier1", "8-10");
        fieldsLayout.put("reasonForMemoLineIdentifier2", "56-58");
        fieldsLayout.put("reasonForMemoLineIdentifier3", "104-106");
        fieldsLayout.put("reasonForMemoLineIdentifier4", "152-154");
        fieldsLayout.put("reasonForMemoLineIdentifier5", "200-202");
        fieldsLayout.put("transactionNumber", "2-7");
    }

}
