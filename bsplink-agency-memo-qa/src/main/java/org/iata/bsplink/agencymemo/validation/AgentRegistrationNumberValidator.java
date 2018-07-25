package org.iata.bsplink.agencymemo.validation;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.model.entity.Config;
import org.iata.bsplink.agencymemo.service.ConfigService;

public class AgentRegistrationNumberValidator extends AcdmValueIsEnabledValidator {

    public static final String REGISTRATION_NUMBER_NOT_ENABLED_MSG =
            "Agent registration number has value but it is not allowed";

    public AgentRegistrationNumberValidator(ConfigService configService) {

        super(configService);
    }

    @Override
    protected String getFieldName() {

        return "agentRegistrationNumber";
    }

    @Override
    protected String getContraintViolationMessage() {

        return REGISTRATION_NUMBER_NOT_ENABLED_MSG;
    }

    @Override
    protected String getValue(AcdmRequest acdm) {

        return acdm.getAgentRegistrationNumber();
    }

    @Override
    protected boolean isEnabled(Config countryConfig) {

        return countryConfig.getCompanyRegistrationNumberEnabled();
    }
}
