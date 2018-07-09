package org.iata.bsplink.refund.validation;

import org.iata.bsplink.refund.model.entity.Config;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.service.ConfigService;

public class RefundAirlineVatNumberValidator extends RefundValueIsEnabledValidator {

    private static final String FIELD_NAME = "airlineVatNumber";

    public RefundAirlineVatNumberValidator(ConfigService configService) {

        super(configService);
    }

    @Override
    public String getFieldName() {

        return FIELD_NAME;
    }

    @Override
    public boolean isEnabled(Config config) {

        return config.getAirlineVatNumberEnabled();
    }

    @Override
    protected boolean valueMustBeEnabled(Refund refund) {

        return isNotEmpty(refund.getAirlineVatNumber());
    }

}
