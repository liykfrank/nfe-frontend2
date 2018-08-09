package org.iata.bsplink.refund.validation;

import org.iata.bsplink.refund.model.entity.Config;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.service.ConfigService;
import org.iata.bsplink.refund.test.validation.RefundStringValueIsEnabledValidatorTestCase;

public class RefundAirlineRegistrationNumberValidatorTest
        extends RefundStringValueIsEnabledValidatorTestCase {

    @Override
    protected void setValue(Refund refund, String value) {

        refund.setAirlineRegistrationNumber(value);
    }

    @Override
    protected void enableValue(Config config, boolean enable) {

        config.setCompanyRegistrationNumberEnabled(enable);;
    }

    @Override
    protected RefundValueIsEnabledValidator getValidator(ConfigService configService) {

        return new RefundAirlineRegistrationNumberValidator(configService);
    }

}
