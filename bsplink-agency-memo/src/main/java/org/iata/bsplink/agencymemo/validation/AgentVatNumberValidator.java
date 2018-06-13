package org.iata.bsplink.agencymemo.validation;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.model.entity.Config;
import org.iata.bsplink.agencymemo.service.ConfigService;

public class AgentVatNumberValidator extends AcdmValueIsEnabledValidator {

    public static final String VAT_NUMBER_NOT_ENABLED_MSG =
            "Agent VAT number has value but it is not allowed";

    public AgentVatNumberValidator(ConfigService configService) {

        super(configService);
    }

    @Override
    protected String getFieldName() {

        return "agentVatNumber";
    }

    @Override
    protected String getContraintViolationMessage() {

        return VAT_NUMBER_NOT_ENABLED_MSG;
    }

    @Override
    protected String getValue(AcdmRequest acdm) {

        return acdm.getAgentVatNumber();
    }

    @Override
    protected boolean isEnabled(Config countryConfig) {

        return countryConfig.getAgentVatNumberEnabled();
    }
}
