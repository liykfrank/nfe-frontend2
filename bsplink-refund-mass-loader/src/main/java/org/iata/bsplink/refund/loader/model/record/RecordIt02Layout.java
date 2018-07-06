package org.iata.bsplink.refund.loader.model.record;

import java.util.Map;

public class RecordIt02Layout extends RecordBaseLayout {

    private static final String PATTERN = "2*";

    public RecordIt02Layout() {

        super(PATTERN);
    }

    @Override
    protected void setFieldsLayout(Map<String, String> fieldsLayout) {

        fieldsLayout.put("agentNumericCode", "8-15");
        fieldsLayout.put("approvedLocationNumericCode1", "145-152");
        fieldsLayout.put("approvedLocationNumericCode2", "174-181");
        fieldsLayout.put("approvedLocationNumericCode3", "203-210");
        fieldsLayout.put("approvedLocationType1", "124-124");
        fieldsLayout.put("approvedLocationType2", "153-153");
        fieldsLayout.put("approvedLocationType3", "182-182");
        fieldsLayout.put("approvedLocationType4", "211-211");
        fieldsLayout.put("checkDigit1", "47-47");
        fieldsLayout.put("checkDigit2", "73-73");
        fieldsLayout.put("conjunctionTicketIndicator", "16-18");
        fieldsLayout.put("couponUseIndicator", "19-22");
        fieldsLayout.put("dataInputStatusIndicator", "246-246");
        fieldsLayout.put("dateOfIssue", "23-28");
        fieldsLayout.put("formatIdentifier", "74-74");
        fieldsLayout.put("stockSetStockControlNumberFrom1", "154-169");
        fieldsLayout.put("stockSetStockControlNumberFrom2", "183-198");
        fieldsLayout.put("stockSetStockControlNumberFrom3", "212-227");
        fieldsLayout.put("stockSetStockControlNumberTo1", "141-144");
        fieldsLayout.put("stockSetStockControlNumberTo2", "170-173");
        fieldsLayout.put("stockSetStockControlNumberTo3", "199-202");
        fieldsLayout.put("stockSetStockControlNumberTo4", "228-231");
        fieldsLayout.put("isoCountryCode", "247-248");
        fieldsLayout.put("passengerName", "75-123");
        fieldsLayout.put("refundApplicationStatus", "255-255");
        fieldsLayout.put("settlementAuthorisationCode", "232-245");
        fieldsLayout.put("statisticalCode", "29-31");
        fieldsLayout.put("stockControlNumber", "125-140");
        fieldsLayout.put("ticketDocumentNumber", "32-46");
        fieldsLayout.put("ticketingAirlineCodeNumber", "68-72");
        fieldsLayout.put("transactionCode", "48-51");
        fieldsLayout.put("transactionNumber", "2-7");
        fieldsLayout.put("transmissionControlNumber", "52-66");
        fieldsLayout.put("transmissionControlNumberCheckDigit", "67-67");
        fieldsLayout.put("vendorIdentification", "251-254");
        fieldsLayout.put("vendorIsoCountryCode", "249-250");
    }

}
