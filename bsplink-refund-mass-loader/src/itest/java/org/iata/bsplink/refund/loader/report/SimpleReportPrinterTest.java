package org.iata.bsplink.refund.loader.report;

import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.hamcrest.CoreMatchers.containsString;
import static org.iata.bsplink.refund.loader.test.fixtures.FixtureLoader.createTmpDirectory;
import static org.springframework.batch.test.AssertFile.assertFileEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class SimpleReportPrinterTest {

    @Rule
    public OutputCapture capture = new OutputCapture();

    private static String reportFileName;
    private SimpleReportPrinter printer;
    private List<RefundLoaderError> errors;

    @BeforeClass
    public static void setUpBeforeClass() {

        reportFileName = createTmpDirectory().getPath() + "/SIMPLE_REPORT";
    }

    @Before
    public void setUp() {

        printer = new SimpleReportPrinter();

        RefundLoaderError error1 = new RefundLoaderError();
        error1.setMessage("Message 1");

        RefundLoaderError error2 = new RefundLoaderError();
        error2.setMessage("Message 2");

        errors = new ArrayList<>();
        errors.add(error1);
        errors.add(error2);

        deleteQuietly(new File(reportFileName));
    }

    @Test
    public void testGeneratesReport() throws Exception {

        printer.print(errors, reportFileName);

        Resource expected = new ClassPathResource("output/report/SIMPLE_REPORT");
        Resource actual = new FileSystemResource(reportFileName);

        assertFileEquals(expected, actual);
    }

    @Test
    public void testLogsGeneratedFileName() {

        printer.print(errors, reportFileName);

        capture.expect(containsString("File generated: " + reportFileName));
    }

    @Test
    public void testLogsErrors() {

        printer.print(errors, reportFileName);

        capture.expect(containsString("ERROR"));
        capture.expect(containsString("Refund loader errors: 2"));
        capture.expect(containsString("message=Message 1"));
        capture.expect(containsString("message=Message 2"));
    }

}
