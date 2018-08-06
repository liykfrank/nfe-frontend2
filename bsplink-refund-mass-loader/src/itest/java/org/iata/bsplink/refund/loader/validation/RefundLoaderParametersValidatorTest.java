package org.iata.bsplink.refund.loader.validation;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;

public class RefundLoaderParametersValidatorTest {

    private static final String ANY_FILE_NAME = "ALe9EARS_20170410_0744_016";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RefundLoaderParametersValidator validator;

    @Before
    public void setUp() {

        validator = new RefundLoaderParametersValidator();
    }

    @Test
    public void testThrowsExceptionIfParametersIsNull() throws Exception {

        thrown.expect(JobParametersInvalidException.class);
        thrown.expectMessage("The JobParameters can not be null");

        validator.validate(null);
    }

    @Test
    public void testThrowsExceptionIfThereIsNoFileParameter() throws Exception {

        thrown.expect(JobParametersInvalidException.class);
        thrown.expectMessage("A refund file must be passed with the option file=REFUND_FILE");

        validator.validate(new JobParameters());
    }

    @Test
    public void testThrowsExceptionIfFileDoesNotExist() throws Exception {

        thrown.expect(JobParametersInvalidException.class);
        thrown.expectMessage("File " + ANY_FILE_NAME + " does not exist");

        Map<String,JobParameter> parameters = new HashMap<>();
        parameters.put("file", new JobParameter(ANY_FILE_NAME));

        validator.validate(new JobParameters(parameters));
    }

    @Test
    public void testValidationSuccessIfFileExists() throws Exception {

        Map<String,JobParameter> parameters = new HashMap<>();

        parameters.put("file",
                new JobParameter("./src/test/resources/fixtures/files/ALe9EARS_20170410_0744_016"));

        validator.validate(new JobParameters(parameters));
    }

    @Test
    public void testThrowsExceptionIfFileNameIsNotValid() throws Exception {

        thrown.expect(JobParametersInvalidException.class);
        thrown.expectMessage("Invalid refund file name: FOO");

        Map<String,JobParameter> parameters = new HashMap<>();
        parameters.put("file", new JobParameter("FOO"));

        validator.validate(new JobParameters(parameters));
    }

}
