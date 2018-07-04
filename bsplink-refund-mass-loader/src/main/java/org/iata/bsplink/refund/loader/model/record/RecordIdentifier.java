package org.iata.bsplink.refund.loader.model.record;

public enum RecordIdentifier {

    IT01("1"),
    IT02("2");

    private String identifier;

    RecordIdentifier(String identifier) {

        this.identifier = identifier;
    }

    public String getIdentifier() {

        return identifier;
    }

    public boolean matches(String value) {

        return identifier.equals(value);
    }
}
