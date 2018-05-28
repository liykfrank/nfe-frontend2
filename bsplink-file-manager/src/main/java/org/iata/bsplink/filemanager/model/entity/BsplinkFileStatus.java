package org.iata.bsplink.filemanager.model.entity;

public enum BsplinkFileStatus {
    DELETED("DELETED"), DOWNLOADED("DOWNLOADED"), NOT_DOWNLOADED("NOT DOWNLOADED"), TRASHED(
            "TRASHED");

    private final String value;

    BsplinkFileStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
