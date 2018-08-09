package org.iata.bsplink.refund.loader.job;

import static org.iata.bsplink.refund.loader.job.RefundJobParametersConverter.JOBID_PARAMETER_NAME;
import static org.iata.bsplink.refund.loader.job.RefundJobParametersConverter.OUTPUT_PATH;
import static org.iata.bsplink.refund.loader.job.RefundJobParametersConverter.REQUIRED_PARAMETER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;

public class RefundJobParametersConverterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static final String ANY_OUTPUT_PATH = "any/output/path";
    private static final String ANY_FILE_PATH = "dir1/dir2/dir3";
    private static final String ANY_FILE = ANY_FILE_PATH + "/file.txt";
    private static final String ANY_FILE_BASENAME = "file.txt";

    private RefundJobParametersConverter converter;
    private Properties properties;

    @Before
    public void setUp() {

        converter = new RefundJobParametersConverter();
        properties = new Properties();
        properties.put(REQUIRED_PARAMETER, ANY_FILE);
    }

    @Test
    public void testFiltersOutNotRequiredParameters() {

        properties.put("foo", "bar");

        JobParameters parameters = converter.getJobParameters(properties);

        assertTrue(parameters.getParameters().containsKey(REQUIRED_PARAMETER));
        assertFalse(parameters.getParameters().containsKey("foo"));
    }

    @Test
    public void testFileIsNotIdentifying() {

        JobParameters parameters = converter.getJobParameters(properties);

        assertTrue(parameters.getParameters().containsKey(REQUIRED_PARAMETER));
        assertEquals(ANY_FILE, parameters.getParameters().get(REQUIRED_PARAMETER).getValue());
        assertFalse(parameters.getParameters().get(REQUIRED_PARAMETER).isIdentifying());
    }

    @Test
    public void testFileValueDoesNotChange() {

        JobParameters parameters = converter.getJobParameters(properties);

        assertTrue(parameters.getParameters().containsKey(REQUIRED_PARAMETER));
        assertEquals(ANY_FILE, parameters.getParameters().get(REQUIRED_PARAMETER).getValue());
    }

    @Test
    public void testAddsBaseFileNameAsJobIdentifier() {

        JobParameters parameters = converter.getJobParameters(properties);

        assertTrue(parameters.getParameters().containsKey(JOBID_PARAMETER_NAME));
        assertEquals(ANY_FILE_BASENAME,
                parameters.getParameters().get(JOBID_PARAMETER_NAME).getValue());
        assertTrue(parameters.getParameters().get(JOBID_PARAMETER_NAME).isIdentifying());
    }

    @Test
    public void testReturnsEmptyPropertiesIfThereAreNoJobParameters() {

        Properties properties = converter.getProperties(new JobParameters());

        assertTrue(properties.isEmpty());
    }

    @Test
    public void testGetsPropertiesFromJobParameters() {

        Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put(REQUIRED_PARAMETER, new JobParameter(ANY_FILE, false));
        parameters.put(JOBID_PARAMETER_NAME, new JobParameter(ANY_FILE_BASENAME, true));

        Properties properties = converter.getProperties(new JobParameters(parameters));

        assertTrue(properties.containsKey(REQUIRED_PARAMETER));
        assertEquals(ANY_FILE, properties.get(REQUIRED_PARAMETER));
    }

    @Test
    public void testAddsOutputPathWhenItExists() {

        properties.put(OUTPUT_PATH, ANY_OUTPUT_PATH);

        JobParameters parameters = converter.getJobParameters(properties);

        assertTrue(parameters.getParameters().containsKey(OUTPUT_PATH));
        assertEquals(ANY_OUTPUT_PATH, parameters.getParameters().get(OUTPUT_PATH).getValue());
        assertFalse(parameters.getParameters().get(OUTPUT_PATH).isIdentifying());
    }

    @Test
    public void testAddsDefaultOutputPathWhenItDoesNotExist() {

        JobParameters parameters = converter.getJobParameters(properties);

        assertTrue(parameters.getParameters().containsKey(OUTPUT_PATH));
        assertEquals(ANY_FILE_PATH, parameters.getParameters().get(OUTPUT_PATH).getValue());
        assertFalse(parameters.getParameters().get(OUTPUT_PATH).isIdentifying());
    }

    @Test
    public void testThrowsExceptionIfRequiredParameterDoesNotExist() {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format("Parameter \"%s\" is required", REQUIRED_PARAMETER));

        properties.remove(REQUIRED_PARAMETER);

        converter.getJobParameters(properties);
    }

    @Test
    public void testThrowsExceptionIfOutputPathIsDefinedButEmpty() {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format("Parameter \"%s\" must have a value", OUTPUT_PATH));

        properties.put(OUTPUT_PATH, "");

        converter.getJobParameters(properties);
    }

    @Test
    public void testThrowsExceptionIfNoPropertyIsDefined() {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format("Parameter \"%s\" is required", REQUIRED_PARAMETER));

        properties = null;

        converter.getJobParameters(properties);
    }

}
