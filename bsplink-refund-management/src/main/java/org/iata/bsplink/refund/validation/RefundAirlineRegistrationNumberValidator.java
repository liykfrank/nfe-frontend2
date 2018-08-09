package org.iata.bsplink.refund.validation;

import org.iata.bsplink.refund.model.entity.Config;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.service.ConfigService;

public class RefundAirlineRegistrationNumberValidator extends RefundValueIsEnabledValidator {

    private static final String FIELD_NAME = "airlineRegistrationNumber";

    public RefundAirlineRegistrationNumberValidator(ConfigService configService) {

        super(configService);
    }

    @Override
    public String getFieldName() {

        return FIELD_NAME;
    }

    @Override
    protected boolean isEnabled(Config config) {

        return config.getCompanyRegistrationNumberEnabled();
    }

    @Override
    protected boolean valueMustBeEnabled(Refund refund) {

        return isNotEmpty(refund.getAirlineRegistrationNumber());
    }

}
