package org.iata.bsplink.refund.loader.model.record;

public interface Record {

    RecordIdentifier getRecordIdentifier();

    void setLineNumber(int lineNumber);

    int getLineNumber();
}
