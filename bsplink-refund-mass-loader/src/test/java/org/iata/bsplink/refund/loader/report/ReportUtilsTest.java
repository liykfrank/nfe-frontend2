package org.iata.bsplink.refund.loader.report;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.iata.bsplink.refund.loader.report.ReportUtils.getReportFileName;
import static org.junit.Assert.assertThat;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class ReportUtilsTest {

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

}
