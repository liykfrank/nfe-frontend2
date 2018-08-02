package org.iata.bsplink.refund.loader.report;

import static org.apache.commons.io.FilenameUtils.getName;

public class ReportUtils {

    private static final String REPORT_FILE_DESCRIPTOR = "e8";
    private static final String FILE_NAME_FIELD_SEPARATOR = "_";

    private ReportUtils() {
        // this class only has static methods
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
}
