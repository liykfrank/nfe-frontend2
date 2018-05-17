package org.iata.bsplink.commons.rest.configuration;

import org.iata.bsplink.commons.rest.exception.handler.ApplicationExceptionHandler;
import org.iata.bsplink.commons.rest.response.ApplicationErrorResponseBuilder;
import org.iata.bsplink.commons.rest.response.validation.ValidationErrorConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration loaded on auto-configuration.
 */
@Configuration
public class ExceptionHandlerConfiguration {

    /**
     * Creates the handler which will convert all exceptions.
     */
    @Bean
    public ApplicationExceptionHandler applicationExceptionHandler() {

        return new ApplicationExceptionHandler(
                new ApplicationErrorResponseBuilder(new ValidationErrorConverter()));
    }

}
