package org.iata.bsplink.agencymemo.validation;

import static org.iata.bsplink.agencymemo.validation.AirlineVatNumberValidator.VAT_NUMBER_NOT_ENABLED_MSG;

import junitparams.JUnitParamsRunner;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.model.entity.Config;
import org.iata.bsplink.agencymemo.service.ConfigService;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class AirlineVatNumberValidatorTest extends AcdmValueIsEnabledValidatorTestCase {

    @Override
    protected void setValue(AcdmRequest acdm, String value) {

        acdm.setAirlineVatNumber(value);
    }

    @Override
    protected void enableValue(Config config, boolean enable) {

        config.setAirlineVatNumberEnabled(enable);
    }

    @Override
    protected String getFieldName() {

        return "airlineVatNumber";
    }

    @Override
    protected AcdmValueIsEnabledValidator getValidator(ConfigService configService) {

        return new AirlineVatNumberValidator(configService);
    }

    @Override
    protected String getContraintViolationMessage() {

        return VAT_NUMBER_NOT_ENABLED_MSG;
    }

}
