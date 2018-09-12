package org.iata.bsplink.refund.loader.utils;

import static org.apache.commons.io.FilenameUtils.concat;
import static org.apache.commons.io.FilenameUtils.getName;
import static org.iata.bsplink.refund.loader.job.RefundJobParametersConverter.OUTPUT_PATH;
import static org.iata.bsplink.refund.loader.job.RefundJobParametersConverter.REQUIRED_PARAMETER;

import org.springframework.batch.core.JobParameters;

public class RefundNameUtils {

    private static final String PROCESS_REPORT_FILE_DESCRIPTOR = "e8";
    private static final String FILE_NAME_FIELD_SEPARATOR = "_";

    private RefundNameUtils() {
        // this class has only static methods
    }

    /**
     * Gets the process report file name from the refund file name.
     */
    public static String getProcessReportFileName(String refundFileName) {

        return getReportFileName(refundFileName, PROCESS_REPORT_FILE_DESCRIPTOR);
    }

    private static String getReportFileName(String refundFileName, String fileDescriptor) {

        String baseName = getName(refundFileName);
        String[] values = baseName.split(FILE_NAME_FIELD_SEPARATOR);

        String isoCountryCode = values[0].substring(0, 2);
        String date = values[1];
        String airline = values[2];
        String sequence = values[3];

        return isoCountryCode
                + fileDescriptor
                + airline
                + FILE_NAME_FIELD_SEPARATOR
                + date
                + FILE_NAME_FIELD_SEPARATOR
                + sequence;
    }

    public static boolean isValidRefundFileName(String refundFileName) {

        return refundFileName.matches("^[A-Z]{2}e9EARS_\\d{8}_[A-Z0-9]{3}[0-69]_\\d{3}");
    }

    /**
     * Gets the process report path name from the job parameters.
     */
    public static String getProcessReportPathName(JobParameters parameters) {

        return concat(parameters.getString(OUTPUT_PATH),
                getProcessReportFileName(parameters.getString(REQUIRED_PARAMETER)));
    }

    /**
     * Gets a unique process report path name from the job parameters.
     */
    public static String getUniqueProcessReportPathName(JobParameters parameters) {

        return concat(parameters.getString(OUTPUT_PATH),
                getProcessReportFileName(parameters.getString(REQUIRED_PARAMETER)))
                + FILE_NAME_FIELD_SEPARATOR + System.currentTimeMillis();
    }
}
