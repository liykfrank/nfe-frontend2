package org.iata.bsplink.agencymemo.validation;

import static org.iata.bsplink.agencymemo.validation.AgentVatNumberValidator.VAT_NUMBER_NOT_ENABLED_MSG;

import junitparams.JUnitParamsRunner;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.model.entity.Config;
import org.iata.bsplink.agencymemo.service.ConfigService;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class AgentVatNumberValidatorTest extends AcdmValueIsEnabledValidatorTestCase {

    @Override
    protected void setValue(AcdmRequest acdm, String value) {

        acdm.setAgentVatNumber(value);
    }

    @Override
    protected void enableValue(Config config, boolean enable) {

        config.setAgentVatNumberEnabled(enable);
    }

    @Override
    protected String getFieldName() {

        return "agentVatNumber";
    }

    @Override
    protected AcdmValueIsEnabledValidator getValidator(ConfigService configService) {

        return new AgentVatNumberValidator(configService);
    }

    @Override
    protected String getContraintViolationMessage() {

        return VAT_NUMBER_NOT_ENABLED_MSG;
    }

}
