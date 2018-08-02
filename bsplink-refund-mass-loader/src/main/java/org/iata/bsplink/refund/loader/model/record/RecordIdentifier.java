package org.iata.bsplink.refund.loader.model.record;

public enum RecordIdentifier {

    IT01("1", new RecordIt01Layout()),
    IT02("2", new RecordIt02Layout()),
    IT03("3", new RecordIt03Layout()),
    IT05("5", new RecordIt05Layout()),
    IT08("8", new RecordIt08Layout()),
    IT0Y("Y", new RecordIt0yLayout()),
    IT0H("H", new RecordIt0hLayout()),
    IT0Z("Z", new RecordIt0zLayout()),
    UNKNOWN("UNKNOWN", new RecordRawLineLayout());

    private String identifier;
    private RecordLayout layout;

    RecordIdentifier(String identifier, RecordLayout layout) {

        this.identifier = identifier;
        this.layout = layout;
    }

    public String getIdentifier() {

        return identifier;
    }

    public RecordLayout getLayout() {

        return layout;
    }

    public boolean matches(String value) {

        return identifier.equals(value);
    }
}
