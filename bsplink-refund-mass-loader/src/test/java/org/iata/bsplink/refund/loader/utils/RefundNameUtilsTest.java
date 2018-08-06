package org.iata.bsplink.refund.loader.utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.iata.bsplink.refund.loader.utils.RefundNameUtils.getReportFileName;
import static org.iata.bsplink.refund.loader.utils.RefundNameUtils.isValidRefundFileName;
import static org.junit.Assert.assertThat;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class RefundNameUtilsTest {

    @Test
    @Parameters
    public void testGetReportFileName(String refundFileName, String expected) {

        assertThat(getReportFileName(refundFileName), equalTo(expected));
    }

    @SuppressWarnings("unused")
    private Object[][] parametersForTestGetReportFileName() {

        return new Object[][] {
            { "dir1/dir2/dir3/ALe9EARS_20170410_0744_016", "ALe80744_20170410_016" },
            { "ALe9EARS_20170410_0744_016", "ALe80744_20170410_016" },
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

}
