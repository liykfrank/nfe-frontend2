package org.iata.bsplink.refund.validation;

import org.apache.commons.lang3.StringUtils;
import org.iata.bsplink.refund.model.entity.Config;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.service.ConfigService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public abstract class RefundBaseValidator implements Validator {

    private ConfigService configService;

    public RefundBaseValidator(ConfigService configService) {

        this.configService = configService;
    }

    @Override
    public boolean supports(Class<?> clazz) {

        return Refund.class.isAssignableFrom(clazz);
    }

    protected abstract void validate(Refund refund, Errors errors, Config countryConfig);

    @Override
    public void validate(Object target, Errors errors) {

        Refund refund = (Refund) target;

        if (countryIsNotDefined(refund)) {

            return;
        }

        Config countryConfig = configService.find(refund.getIsoCountryCode());

        validate(refund, errors, countryConfig);
    }

    private boolean countryIsNotDefined(Refund refund) {

        return StringUtils.isEmpty(refund.getIsoCountryCode());
    }

}
