package org.iata.bsplink.refund.loader.model.record;

/**
 * Field types of the refund massive load file.
 */
public enum FieldType {

    A("Alphabetic"),
    AN("Alphanumeric"),
    AX("Extended alphanumeric"),
    N("Numeric");

    private String description;

    FieldType(String description) {

        this.description = description;
    }

    public String description() {

        return description;
    }
}
