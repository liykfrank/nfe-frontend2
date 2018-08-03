package org.iata.bsplink.refund.loader.model.record;

import java.util.List;

public class RecordIt0hLayout extends RecordBaseLayout {

    private static final String PATTERN = "H*";

    public RecordIt0hLayout() {

        super(PATTERN);
    }

    @Override
    protected void setFieldsLayouts(List<FieldLayout> fieldsLayouts) {

        fieldsLayouts.add(new FieldLayout(
                "reasonForMemoInformation1", 4, "RMIN", FieldType.AX, 11, 45));
        fieldsLayouts.add(new FieldLayout(
                "reasonForMemoInformation2", 6, "RMIN", FieldType.AX, 59, 45));
        fieldsLayouts.add(new FieldLayout(
                "reasonForMemoInformation3", 8, "RMIN", FieldType.AX, 107, 45));
        fieldsLayouts.add(new FieldLayout(
                "reasonForMemoInformation4", 10, "RMIN", FieldType.AX, 155, 45));
        fieldsLayouts.add(new FieldLayout(
                "reasonForMemoInformation5", 12, "RMIN", FieldType.AX, 203, 45));
        fieldsLayouts.add(new FieldLayout(
                "reasonForMemoIssuanceCode", 13, "", FieldType.AN, 248, 5));
        fieldsLayouts.add(new FieldLayout(
                "reasonForMemoLineIdentifier1", 3, "RMLI", FieldType.AN, 8, 3));
        fieldsLayouts.add(new FieldLayout(
                "reasonForMemoLineIdentifier2", 5, "RMLI", FieldType.N, 56, 3));
        fieldsLayouts.add(new FieldLayout(
                "reasonForMemoLineIdentifier3", 7, "RMLI", FieldType.N, 104, 3));
        fieldsLayouts.add(new FieldLayout(
                "reasonForMemoLineIdentifier4", 9, "RMLI", FieldType.N, 152, 3));
        fieldsLayouts.add(new FieldLayout(
                "reasonForMemoLineIdentifier5", 11, "RMLI", FieldType.N, 200, 3));
        fieldsLayouts.add(new FieldLayout(
                "transactionNumber", 2, "TRNN", FieldType.N, 2, 6));
    }

}
