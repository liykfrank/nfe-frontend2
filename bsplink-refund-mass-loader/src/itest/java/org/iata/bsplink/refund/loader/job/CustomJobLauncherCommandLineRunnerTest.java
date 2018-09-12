package org.iata.bsplink.refund.loader.job;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.emptyArray;
import static org.iata.bsplink.refund.loader.test.fixtures.FixtureLoader.createTmpDirectory;
import static org.junit.Assert.assertThat;
import static org.springframework.batch.test.AssertFile.assertFileEquals;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.iata.bsplink.refund.loader.configuration.BatchConfiguration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.batch.JobLauncherCommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.batch.job.enabled = false")
// DirtiesContext doesn't removes the DataSource for some
// reason so we use an SQL script for cleanup
@Sql({"classpath:sql/cleanup.sql"})
public class CustomJobLauncherCommandLineRunnerTest {

    @Rule
    public OutputCapture capture = new OutputCapture();

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private JobRegistry jobRegistry;

    @Autowired
    private BatchProperties properties;

    @Autowired
    private Job job;

    @MockBean
    private RefundWriter refundWriter;

    private JobLauncherCommandLineRunner jobLauncherCommandLineRunner;
    private String[] jobArguments;

    private String tmpDirectory;

    @Before
    public void setUp() {

        tmpDirectory = createTmpDirectory().getPath();

        properties.getJob().setNames(BatchConfiguration.LOADER_JOB_NAME);

        jobLauncherCommandLineRunner = new BatchConfiguration()
                .jobLauncherCommandLineRunner(properties, jobLauncher, jobExplorer);
        jobLauncherCommandLineRunner.setJobs(Arrays.asList(job));

        jobArguments = new String[]{
            "file=./src/test/resources/fixtures/files/ALe9EARS_20170410_0744_016",
            "outputPath=" + tmpDirectory
        };
    }

    @Test
    public void test() {

        assertThat(jobLauncher, notNullValue());
        assertThat(properties, notNullValue());
        assertThat(jobExplorer, notNullValue());
        assertThat(jobRegistry, notNullValue());
    }

    @Test
    public void testLogsMessagesWhenFileIsProcessedMoreThanOnce() {

        runJobTimes(2);

        capture.expect(
                containsString("message=A file with the same name has already been processed."));
    }

    private void runJobTimes(int times) {

        for (int i = 0; i < times; i++) {

            try {

                jobLauncherCommandLineRunner.run(jobArguments);

            } catch (Exception exception) {

                throw new RuntimeException(exception);
            }
        }
    }

    @Test
    public void testGeneratesExpectedProcessReportWhenFileIsProcessedMoreThanOnce()
            throws Exception {

        int jobExecutions = 5;

        runJobTimes(jobExecutions);

        Resource expected =
                new ClassPathResource("output/report/ALe80744_20170410_016_ALREADY_PROCESSED");

        File dir = new File(tmpDirectory);
        FileFilter fileFilter = new WildcardFileFilter("ALe80744_20170410_016_?????????????");
        File[] files = dir.listFiles(fileFilter);

        assertThat(files, not(emptyArray()));
        assertThat(files.length, equalTo(jobExecutions - 1));

        for (int i = 0; i < jobExecutions; i++) {

            Resource actual = new FileSystemResource(files[0]);

            assertFileEquals(expected, actual);
        }
    }

}
