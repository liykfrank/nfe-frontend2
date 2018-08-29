package org.iata.bsplink.refund.loader.test;

import static org.iata.bsplink.refund.loader.utils.BeanPropertyUtils.setProperty;

import org.iata.bsplink.refund.loader.model.record.Record;
import org.iata.bsplink.refund.loader.model.record.RecordLayouts;

public class RecordUtils {

    private RecordUtils() {
        // only will contain static methods
    }

    /**
     * Initializes a Record with empty strings.
     */
    public static void initializeRecordFieldsWithEmptyStrings(Record record) {

        try {

            for (String layoutFieldName : RecordLayouts.get(record.getRecordIdentifier())
                    .getFieldsNames()) {

                setProperty(record, layoutFieldName, "");
            }
        } catch (Exception exception) {

            throw new RuntimeException(exception);
        }
    }

}
