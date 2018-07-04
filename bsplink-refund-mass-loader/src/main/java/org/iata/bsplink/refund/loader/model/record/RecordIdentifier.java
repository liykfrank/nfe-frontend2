package org.iata.bsplink.refund.loader.model.record;

public enum RecordIdentifier {

    IT01("1"),
    IT02("2"),
    IT03("3"),
    IT05("5"),
    IT08("8"),
    IT0Y("Y"),
    IT0H("H"),
    IT0Z("Z");

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
