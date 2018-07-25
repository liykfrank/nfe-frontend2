package org.iata.bsplink.agencymemo.validation;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.model.entity.Config;
import org.iata.bsplink.agencymemo.service.ConfigService;

public class AirlineVatNumberValidator extends AcdmValueIsEnabledValidator {

    public static final String VAT_NUMBER_NOT_ENABLED_MSG =
            "Airline VAT number has value but it is not allowed";

    public AirlineVatNumberValidator(ConfigService configService) {

        super(configService);
    }

    @Override
    protected String getFieldName() {

        return "airlineVatNumber";
    }

    @Override
    protected String getContraintViolationMessage() {

        return VAT_NUMBER_NOT_ENABLED_MSG;
    }

    @Override
    protected String getValue(AcdmRequest acdm) {

        return acdm.getAirlineVatNumber();
    }

    @Override
    protected boolean isEnabled(Config countryConfig) {

        return countryConfig.getAirlineVatNumberEnabled();
    }
}
