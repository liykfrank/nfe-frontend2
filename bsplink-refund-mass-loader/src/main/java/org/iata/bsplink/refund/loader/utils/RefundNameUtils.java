package org.iata.bsplink.refund.loader.utils;

import static org.apache.commons.io.FilenameUtils.getName;

public class RefundNameUtils {

    private static final String REPORT_FILE_DESCRIPTOR = "e8";
    private static final String FILE_NAME_FIELD_SEPARATOR = "_";

    private RefundNameUtils() {
        // this class has only static methods
    }

    /**
     * Gets the report file name from the refund file name.
     */
    public static String getReportFileName(String refundFileName) {

        String baseName = getName(refundFileName);
        String[] values = baseName.split(FILE_NAME_FIELD_SEPARATOR);

        String isoCountryCode = values[0].substring(0, 2);
        String date = values[1];
        String airline = values[2];
        String sequence = values[3];

        return isoCountryCode
                + REPORT_FILE_DESCRIPTOR
                + airline
                + FILE_NAME_FIELD_SEPARATOR
                + date
                + FILE_NAME_FIELD_SEPARATOR
                + sequence;
    }

    public static boolean isValidRefundFileName(String refundFileName) {

        return refundFileName.matches("^[A-Z]{2}e9EARS_\\d{8}_[A-Z0-9]{3}[0-69]_\\d{3}");
    }
}
