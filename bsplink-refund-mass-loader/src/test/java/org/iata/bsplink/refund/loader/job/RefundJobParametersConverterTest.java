package org.iata.bsplink.refund.loader.job;

import static org.iata.bsplink.refund.loader.job.RefundJobParametersConverter.JOBID_PARAMETER_NAME;
import static org.iata.bsplink.refund.loader.job.RefundJobParametersConverter.REQUIRED_PARAMETER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;

public class RefundJobParametersConverterTest {

    private static final String ANY_FILE = "dir1/dir2/dir3/file.txt";
    private static final String ANY_FILE_BASENAME = "file.txt";

    private RefundJobParametersConverter converter;
    private Properties properties;

    @Before
    public void setUp() {

        converter = new RefundJobParametersConverter();
        properties = new Properties();
    }

    @Test
    public void testReturnsEmptyJobParametersIfThereAreNoProperties() {

        assertTrue(converter.getJobParameters(null).isEmpty());
        assertTrue(converter.getJobParameters(properties).isEmpty());
    }

    @Test
    public void testFiltersOutNotRequiredParameters() {

        properties.put(REQUIRED_PARAMETER, ANY_FILE_BASENAME);
        properties.put("foo", "bar");

        JobParameters parameters = converter.getJobParameters(properties);

        assertTrue(parameters.getParameters().containsKey(REQUIRED_PARAMETER));
        assertFalse(parameters.getParameters().containsKey("foo"));
    }

    @Test
    public void testFileIsNotIdentifying() {

        properties.put(REQUIRED_PARAMETER, ANY_FILE);

        JobParameters parameters = converter.getJobParameters(properties);

        assertTrue(parameters.getParameters().containsKey(REQUIRED_PARAMETER));
        assertEquals(ANY_FILE, parameters.getParameters().get(REQUIRED_PARAMETER).getValue());
        assertFalse(parameters.getParameters().get(REQUIRED_PARAMETER).isIdentifying());
    }

    @Test
    public void testFileValueDoesNotChange() {

        properties.put(REQUIRED_PARAMETER, ANY_FILE);

        JobParameters parameters = converter.getJobParameters(properties);

        assertTrue(parameters.getParameters().containsKey(REQUIRED_PARAMETER));
        assertEquals(ANY_FILE, parameters.getParameters().get(REQUIRED_PARAMETER).getValue());
    }

    @Test
    public void testAddsBaseFileNameAsJobIdentifier() {

        properties.put(REQUIRED_PARAMETER, ANY_FILE);

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

}
