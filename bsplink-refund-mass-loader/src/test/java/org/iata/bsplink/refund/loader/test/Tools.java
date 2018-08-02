package org.iata.bsplink.refund.loader.test;

import com.fasterxml.jackson.databind.ObjectMapper;

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
}
