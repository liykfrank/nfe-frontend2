package org.iata.bsplink.refund.loader.test;

import static org.iata.bsplink.refund.loader.job.RefundJobParametersConverter.OUTPUT_PATH;
import static org.iata.bsplink.refund.loader.job.RefundJobParametersConverter.REQUIRED_PARAMETER;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;

public class Tools {

    private Tools() {
        // empty
    }

    /**
     * Returns an ObjectMapper configured to convert Java 8 date types.
     *
     * <p>
     * The method creates an ObjectMapper and registers all its modules in order to load
     * the jackson-datatype-jsr310 module that gives support to Java 8 date types.
     * </p>
     * <p>
     * The reason to configure the mapper like this is to avoid the use of @Autowired needlessly.
     * </p>
     */
    public static ObjectMapper getObjectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        return objectMapper;
    }

    /**
     * Returns a JobParameters object with the output and input file given.
     */
    public static JobParameters getJobParameters(String outputPath, String inputFileName) {

        Map<String, JobParameter> parameters = new HashMap<>();

        parameters.put(OUTPUT_PATH, new JobParameter(outputPath, false));
        parameters.put(REQUIRED_PARAMETER, new JobParameter(inputFileName, false));

        return new JobParameters(parameters);
    }
}
