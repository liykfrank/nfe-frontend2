package org.iata.bsplink.agencymemo.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.service.ConfigService;
import org.iata.bsplink.agencymemo.validation.AgentRegistrationNumberValidator;
import org.iata.bsplink.agencymemo.validation.AgentVatNumberValidator;
import org.iata.bsplink.agencymemo.validation.AirlineRegistrationNumberValidator;
import org.iata.bsplink.agencymemo.validation.AirlineVatNumberValidator;
import org.iata.bsplink.agencymemo.validation.CalculationsValidator;
import org.iata.bsplink.agencymemo.validation.CpAndMfValidator;
import org.iata.bsplink.agencymemo.validation.RelatedDocumentsValidator;
import org.iata.bsplink.agencymemo.validation.constraints.AcdmConstraint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AcdmValidatorConfig {

    /**
     * List of Constraint Validators for config-dependent ACDM Validation.
     */
    @Bean
    public List<ConstraintValidator<AcdmConstraint, AcdmRequest>>
        acdmValidators(ConfigService configService) {

        List<ConstraintValidator<AcdmConstraint, AcdmRequest>> validators = new ArrayList<>();

        validators.add(new AgentRegistrationNumberValidator(configService));
        validators.add(new AgentVatNumberValidator(configService));
        validators.add(new AirlineRegistrationNumberValidator(configService));
        validators.add(new AirlineVatNumberValidator(configService));
        validators.add(new CpAndMfValidator(configService));
        validators.add(new CalculationsValidator(configService));
        validators.add(new RelatedDocumentsValidator(configService));

        return validators;
    }
}
