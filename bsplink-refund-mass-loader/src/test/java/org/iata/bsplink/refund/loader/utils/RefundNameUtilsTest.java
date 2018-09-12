package org.iata.bsplink.refund.loader.utils;

import static org.apache.commons.io.FilenameUtils.separatorsToSystem;
import static org.apache.commons.lang.StringEscapeUtils.escapeJava;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.iata.bsplink.refund.loader.test.Tools.getJobParameters;
import static org.iata.bsplink.refund.loader.utils.RefundNameUtils.getProcessReportFileName;
import static org.iata.bsplink.refund.loader.utils.RefundNameUtils.getProcessReportPathName;
import static org.iata.bsplink.refund.loader.utils.RefundNameUtils.getUniqueProcessReportPathName;
import static org.iata.bsplink.refund.loader.utils.RefundNameUtils.isValidRefundFileName;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobParameters;

@RunWith(JUnitParamsRunner.class)
public class RefundNameUtilsTest {

    private static final String ANY_INPUT_FILE_NAME = "ALe9EARS_20170410_0744_016";
    private static final String ANY_INPUT_FILE_PATH = "dir1/dir2/dir3/" + ANY_INPUT_FILE_NAME;

    private static final String ERROR_REPORT_FILE_NAME = "ALe80744_20170410_016";

    @Test
    @Parameters
    public void testGetProcessReportFileName(String refundFileName, String expected) {

        assertThat(getProcessReportFileName(refundFileName), equalTo(expected));
    }

    @SuppressWarnings("unused")
    private Object[][] parametersForTestGetProcessReportFileName() {

        return new Object[][] {
            { ANY_INPUT_FILE_PATH, ERROR_REPORT_FILE_NAME },
            { ANY_INPUT_FILE_NAME, ERROR_REPORT_FILE_NAME },
        };
    }

    @Test
    @Parameters
    public void testIsValidRefundFileName(String fileName, boolean expected) {

        assertThat(isValidRefundFileName(fileName), equalTo(expected));
    }

    @SuppressWarnings("unused")
    private Object[][] parametersForTestIsValidRefundFileName() {

        return new Object[][] {

            { "FRe9EARS_20150602_0302_001", true },
            { "FRe9EARS_20150602_0302_001_FOO", false },
            { "fre9EARS_20150602_0302_001", false },
            { "FREARS_20150602_0302_001", false },
            { "FRxxEARS_20150602_0302_001", false },
            { "FRe9ears_20150602_0302_001", false },
            { "FRe9EARS_2015060_0302_001", false },
            { "FRe9EARS_aaaaaaaa_0302_001", false }
        };
    }

    @Test
    public void testGetProcessReportPathNameFromJobParameters() {

        JobParameters parameters = getJobParameters("/tmp", ANY_INPUT_FILE_PATH);

        String expected = separatorsToSystem("/tmp/" + ERROR_REPORT_FILE_NAME);

        assertThat(getProcessReportPathName(parameters), equalTo(expected));
    }

    @Test
    public void testGetUniqueProcessReportPathName() {

        JobParameters parameters = getJobParameters("/tmp", ANY_INPUT_FILE_PATH);

        String reportPathName = getUniqueProcessReportPathName(parameters);
        String expectedPattern =
                escapeJava(separatorsToSystem("/tmp/" + ERROR_REPORT_FILE_NAME + "_[0-9]{13}"));

        assertTrue(reportPathName.matches(expectedPattern));
    }

    @Test
    public void testGetUniqueProcessReportPathNameReturnsDifferentNames() throws Exception {

        JobParameters parameters = getJobParameters("/tmp", ANY_INPUT_FILE_PATH);

        String firstReportPathName = getUniqueProcessReportPathName(parameters);

        TimeUnit.MICROSECONDS.sleep(1);
        String secondReportPathName = getUniqueProcessReportPathName(parameters);

        TimeUnit.MICROSECONDS.sleep(1);
        String thirdReportPathName = getUniqueProcessReportPathName(parameters);

        assertThat(firstReportPathName, not(equalTo(secondReportPathName)));
        assertThat(firstReportPathName, not(equalTo(thirdReportPathName)));
        assertThat(secondReportPathName, not(equalTo(thirdReportPathName)));
    }

}
