package org.iata.bsplink.refund.loader.report;

import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.hamcrest.CoreMatchers.containsString;
import static org.iata.bsplink.refund.loader.test.fixtures.FixtureLoader.createTmpDirectory;
import static org.iata.bsplink.refund.loader.test.fixtures.RefundDocumentFixtures.getValidationErrorsTransactionPhase;
import static org.springframework.batch.test.AssertFile.assertFileEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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
public class ProcessReportPrinterTest {

    @Rule
    public OutputCapture capture = new OutputCapture();

    private static String reportFileName;

    private ProcessReportPrinter printer;
    private List<RefundLoaderError> errors;

    @BeforeClass
    public static void setUpBeforeClass() {

        reportFileName = createTmpDirectory().getPath() + "/XXe80011_20180101_001";
    }

    @Before
    public void setUp() {

        RefundLoaderErrorToFieldLayoutMapper mapper =
                new RefundLoaderErrorToFieldLayoutMapper(new HashMap<>());

        printer = new ProcessReportPrinter(mapper);
        errors = new ArrayList<>();

        deleteQuietly(new File(reportFileName));
    }

    @Test
    public void testGeneratesReportIfThereAreNoErrors() throws Exception {

        printer.print(errors, reportFileName);

        Resource expected = new ClassPathResource("output/report/SUCCESSFULLY");
        Resource actual = new FileSystemResource(reportFileName);

        assertFileEquals(expected, actual);
    }

    @Test
    public void testGeneratesReportIfThereAreErrors() throws Exception {

        errors = getValidationErrorsTransactionPhase();

        printer.print(errors, reportFileName);

        Resource expected = new ClassPathResource("output/report/WITH_VALIDATION_ERRORS");
        Resource actual = new FileSystemResource(reportFileName);

        assertFileEquals(expected, actual);
    }

    @Test
    public void testGeneratesReportForErrorsWithoutLayout() throws Exception {

        errors = getValidationErrorsTransactionPhase();

        errors.get(0).setField("unknowField1");
        errors.get(1).setRecordIdentifier(null);

        printer.print(errors, reportFileName);

        Resource expected = new ClassPathResource("output/report/WITH_ERRORS_WITHOUT_LAYOUT");
        Resource actual = new FileSystemResource(reportFileName);

        assertFileEquals(expected, actual);
    }

    @Test
    public void testGeneratesReportWhenThereAreNoRecords() throws Exception {

        RefundLoaderError error = new RefundLoaderError();
        error.setMessage("The file is empty");

        errors.add(error);

        printer.print(errors, reportFileName);

        Resource expected = new ClassPathResource("output/report/WITH_ERRORS_WITHOUT_RECORDS");
        Resource actual = new FileSystemResource(reportFileName);

        assertFileEquals(expected, actual);
    }

    @Test
    public void testLogsErrors() throws Exception {

        errors = getValidationErrorsTransactionPhase();

        printer.print(errors, reportFileName);

        capture.expect(containsString("ERROR"));
        capture.expect(containsString("Refund loader errors: 2"));
        capture.expect(containsString("message=The record element is mandatory"));
        capture.expect(containsString("message=Non-numeric characters in numeric fields"));
    }

    @Test
    public void testNormalizesErrorMessages() throws Exception {

        RefundLoaderError error = new RefundLoaderError();
        error.setMessage("any error message");

        errors.add(error);

        printer.print(errors, reportFileName);

        Resource expected = new ClassPathResource("output/report/NORMALIZED_MESSAGE");
        Resource actual = new FileSystemResource(reportFileName);

        assertFileEquals(expected, actual);
    }

}
