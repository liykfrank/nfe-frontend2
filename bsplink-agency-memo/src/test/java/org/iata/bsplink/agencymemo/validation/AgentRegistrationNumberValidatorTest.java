package org.iata.bsplink.agencymemo.validation;

import static org.iata.bsplink.agencymemo.validation.AgentRegistrationNumberValidator.REGISTRATION_NUMBER_NOT_ENABLED_MSG;

import junitparams.JUnitParamsRunner;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.model.entity.Config;
import org.iata.bsplink.agencymemo.service.ConfigService;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class AgentRegistrationNumberValidatorTest extends AcdmValueIsEnabledValidatorTestCase {

    @Override
    protected AcdmValueIsEnabledValidator getValidator(ConfigService configService) {

        return new AgentRegistrationNumberValidator(configService);
    }

    @Override
    protected void setValue(AcdmRequest acdm, String value) {

        acdm.setAgentRegistrationNumber(value);
    }

    @Override
    protected void enableValue(Config config, boolean enable) {

        config.setCompanyRegistrationNumberEnabled(enable);
    }

    @Override
    protected String getFieldName() {

        return "agentRegistrationNumber";
    }

    @Override
    protected String getContraintViolationMessage() {

        return REGISTRATION_NUMBER_NOT_ENABLED_MSG;
    }

}
