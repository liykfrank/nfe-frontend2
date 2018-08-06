package org.iata.bsplink.refund.loader.job;

import static org.apache.commons.io.FilenameUtils.getFullPathNoEndSeparator;
import static org.apache.commons.io.FilenameUtils.getName;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.stereotype.Component;

/**
 * Prepares the JobParameters for execution.
 *
 * <p>
 * We want the base file name to be the job identifier, so the file parameter is set as not
 * identifier and a new parameter jobid is added which will be the identifier of the job.
 * </p>
 */
@Component
public class RefundJobParametersConverter implements JobParametersConverter {

    public static final String REQUIRED_PARAMETER = "file";
    public static final String JOBID_PARAMETER_NAME = "jobid";
    public static final String OUTPUT_PATH = "outputPath";

    @Override
    public JobParameters getJobParameters(Properties properties) {

        Map<String, JobParameter> parameters = new HashMap<>();

        getJobParameters(properties, parameters);

        throwExceptionIfRequiredParameterDoesNotExist(parameters);

        addDefaultOutputPathIfItIsNotDefined(parameters);

        return new JobParameters(parameters);
    }

    private void getJobParameters(Properties properties, Map<String, JobParameter> parameters) {

        for (Object key : Collections.list(properties.keys())) {

            if (REQUIRED_PARAMETER.equals(key)) {

                String fileName = (String) properties.get(REQUIRED_PARAMETER);

                parameters.put(REQUIRED_PARAMETER, new JobParameter(fileName, false));
                parameters.put(JOBID_PARAMETER_NAME, new JobParameter(getName(fileName), true));
            }

            if (OUTPUT_PATH.equals(key)) {

                parameters.put(OUTPUT_PATH,
                        new JobParameter((String) properties.get(OUTPUT_PATH), false));
            }
        }
    }

    private void throwExceptionIfRequiredParameterDoesNotExist(
            Map<String, JobParameter> parameters) {

        if (parameters.containsKey(REQUIRED_PARAMETER)) {

            return;
        }

        throw new IllegalArgumentException(
                String.format("Parameter \"%s\" is required", REQUIRED_PARAMETER));
    }

    private void addDefaultOutputPathIfItIsNotDefined(Map<String, JobParameter> parameters) {

        if (parameters.containsKey(OUTPUT_PATH)) {

            return;
        }

        String fileName = (String) parameters.get(REQUIRED_PARAMETER).getValue();

        parameters.put(OUTPUT_PATH,
                new JobParameter(getFullPathNoEndSeparator(fileName), false));
    }

    @Override
    public Properties getProperties(JobParameters params) {

        Properties properties = new Properties();

        if (!params.isEmpty()) {
            for (Entry<String, JobParameter> parameter : params.getParameters().entrySet()) {

                properties.put(parameter.getKey(), parameter.getValue().getValue());
            }
        }

        return properties;

    }

}
