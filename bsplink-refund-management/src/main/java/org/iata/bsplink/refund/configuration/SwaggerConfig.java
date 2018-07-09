package org.iata.bsplink.refund.configuration;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Profile("!test")
public class SwaggerConfig {

    /**
     * Configures Swagger in order to force it to generate documentation only for the specified
     * controllers. If this configuration bean is removed, the plugin will scan and document all
     * exposed APIs
     *
     * @return the generated Docket with the provided configuration
     * @see Docket
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("org.iata.bsplink.refund.controller"))
                .paths(PathSelectors.any()).build().apiInfo(apiInfo());
    }

    @SuppressWarnings("rawtypes")
    private ApiInfo apiInfo() {

        return new ApiInfo("BSPlink Refund Management", "BSPlink Refund Management", "1", null,
                new Contact("", "", ""), null, null, new ArrayList<VendorExtension>());
    }
}
