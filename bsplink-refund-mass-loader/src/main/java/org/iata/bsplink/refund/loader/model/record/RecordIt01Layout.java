package org.iata.bsplink.refund.loader.model.record;

import java.util.Map;

public class RecordIt01Layout extends RecordBaseLayout {

    private static final String PATTERN = "1*";

    public RecordIt01Layout() {

        super(PATTERN);
    }

    @Override
    protected void setFieldsLayout(Map<String, String> fieldsLayout) {

        fieldsLayout.put("fileType", "37-37");
        fieldsLayout.put("fileTypeSequenceNumber", "38-39");
        fieldsLayout.put("handbookRevisionNumber", "12-14");
        fieldsLayout.put("isoCountryCode", "29-30");
        fieldsLayout.put("processingDate", "19-24");
        fieldsLayout.put("processingTime", "25-28");
        fieldsLayout.put("recordIdentifier", "1-1");
        fieldsLayout.put("reportingSystemIdentifier", "8-11");
        fieldsLayout.put("reservedSpace1", "31-36");
        fieldsLayout.put("reservedSpace2", "40-255");
        fieldsLayout.put("systemProviderReportingPeriodEndingDate", "2-7");
        fieldsLayout.put("testProductionStatus", "15-18");
    }

}
