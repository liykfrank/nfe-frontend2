package org.iata.bsplink.refund.loader.model.record;

import java.util.Map;

public interface RecordLayout {

    String getPattern();

    Map<String, String> getFieldsLayout();

}
