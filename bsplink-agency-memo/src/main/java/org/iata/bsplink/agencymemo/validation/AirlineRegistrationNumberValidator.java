package org.iata.bsplink.agencymemo.validation;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.model.entity.Config;
import org.iata.bsplink.agencymemo.service.ConfigService;

public class AirlineRegistrationNumberValidator extends AcdmValueIsEnabledValidator {

    public static final String REGISTRATION_NUMBER_NOT_ENABLED_MSG =
            "Airline registration number has value but it is not allowed";

    public AirlineRegistrationNumberValidator(ConfigService configService) {

        super(configService);
    }

    @Override
    protected String getFieldName() {

        return "airlineRegistrationNumber";
    }

    @Override
    protected String getContraintViolationMessage() {

        return REGISTRATION_NUMBER_NOT_ENABLED_MSG;
    }

    @Override
    protected String getValue(AcdmRequest acdm) {

        return acdm.getAirlineRegistrationNumber();
    }

    @Override
    protected boolean isEnabled(Config countryConfig) {

        return countryConfig.getCompanyRegistrationNumberEnabled();
    }
}
