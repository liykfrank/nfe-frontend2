package org.iata.bsplink.refund.loader.model.record;

public class RecordIt0hLayout extends RecordBaseLayout {

    private static final String PATTERN = "H*";

    public RecordIt0hLayout() {

        super(PATTERN);
    }

    @Override
    public RecordIdentifier getRecordIdentifier() {

        return RecordIdentifier.IT0H;
    }

    @Override
    protected void setFieldsLayouts() {

        addFieldLayout("reasonForMemoInformation1", 4, "RMIN", FieldType.AX, 11, 45);
        addFieldLayout("reasonForMemoInformation2", 6, "RMIN", FieldType.AX, 59, 45);
        addFieldLayout("reasonForMemoInformation3", 8, "RMIN", FieldType.AX, 107, 45);
        addFieldLayout("reasonForMemoInformation4", 10, "RMIN", FieldType.AX, 155, 45);
        addFieldLayout("reasonForMemoInformation5", 12, "RMIN", FieldType.AX, 203, 45);
        addFieldLayout("reasonForMemoIssuanceCode", 13, "", FieldType.AN, 248, 5);
        addFieldLayout("reasonForMemoLineIdentifier1", 3, "RMLI", FieldType.AN, 8, 3);
        addFieldLayout("reasonForMemoLineIdentifier2", 5, "RMLI", FieldType.N, 56, 3);
        addFieldLayout("reasonForMemoLineIdentifier3", 7, "RMLI", FieldType.N, 104, 3);
        addFieldLayout("reasonForMemoLineIdentifier4", 9, "RMLI", FieldType.N, 152, 3);
        addFieldLayout("reasonForMemoLineIdentifier5", 11, "RMLI", FieldType.N, 200, 3);
        addFieldLayout("transactionNumber", 2, "TRNN", FieldType.N, 2, 6);
    }

}
