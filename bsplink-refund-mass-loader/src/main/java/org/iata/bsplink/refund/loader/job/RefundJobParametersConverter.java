package org.iata.bsplink.refund.loader.job;

import java.nio.file.FileSystems;
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

    @Override
    public JobParameters getJobParameters(Properties properties) {

        if (properties == null || properties.isEmpty()) {
            return new JobParameters();
        }

        Map<String, JobParameter> parameters = new HashMap<>();

        for (Object key : Collections.list(properties.keys())) {

            if (!REQUIRED_PARAMETER.equals(key)) {
                continue;
            }

            String fileName = (String) properties.get(REQUIRED_PARAMETER);
            // TODO: refatorize, use filenameutils
            String baseFileName =
                    FileSystems.getDefault().getPath(fileName).getFileName().toString();

            parameters.put(REQUIRED_PARAMETER, new JobParameter(fileName, false));
            parameters.put(JOBID_PARAMETER_NAME, new JobParameter(baseFileName, true));
        }

        return new JobParameters(parameters);
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
