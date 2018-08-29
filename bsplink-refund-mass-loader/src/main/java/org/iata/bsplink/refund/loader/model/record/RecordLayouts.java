package org.iata.bsplink.refund.loader.model.record;

import java.util.EnumMap;
import java.util.Map;

/**
 * Relation between record types and its layouts.
 */
public class RecordLayouts {

    protected static final Map<RecordIdentifier, RecordLayout> RECORD_LAYOUTS =
            new EnumMap<>(RecordIdentifier.class);

    static {

        RECORD_LAYOUTS.put(RecordIdentifier.IT01, new RecordIt01Layout());
        RECORD_LAYOUTS.put(RecordIdentifier.IT02, new RecordIt02Layout());
        RECORD_LAYOUTS.put(RecordIdentifier.IT03, new RecordIt03Layout());
        RECORD_LAYOUTS.put(RecordIdentifier.IT05, new RecordIt05Layout());
        RECORD_LAYOUTS.put(RecordIdentifier.IT08, new RecordIt08Layout());
        RECORD_LAYOUTS.put(RecordIdentifier.IT0Y, new RecordIt0yLayout());
        RECORD_LAYOUTS.put(RecordIdentifier.IT0H, new RecordIt0hLayout());
        RECORD_LAYOUTS.put(RecordIdentifier.IT0Z, new RecordIt0zLayout());
        RECORD_LAYOUTS.put(RecordIdentifier.UNKNOWN, new RecordRawLineLayout());
    }

    public static RecordLayout get(RecordIdentifier recordIdentifier) {

        return RECORD_LAYOUTS.get(recordIdentifier);
    }

    private RecordLayouts() {
        // class only contains static values
    }

}
