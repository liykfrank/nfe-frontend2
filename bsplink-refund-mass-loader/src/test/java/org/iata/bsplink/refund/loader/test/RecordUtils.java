package org.iata.bsplink.refund.loader.test;

import static org.apache.commons.beanutils.PropertyUtils.setProperty;

import org.iata.bsplink.refund.loader.model.record.Record;

public class RecordUtils {

    private RecordUtils() {
        // only will contain static methods
    }

    /**
     * Initializes a Record with empty strings.
     */
    public static void initializeRecordFieldsWithEmptyStrings(Record record) {

        try {

            for (String layoutFieldName : record.getRecordIdentifier().getLayout()
                    .getFieldsNames()) {

                setProperty(record, layoutFieldName, "");
            }
        } catch (Exception exception) {

            throw new RuntimeException(exception);
        }
    }

}
