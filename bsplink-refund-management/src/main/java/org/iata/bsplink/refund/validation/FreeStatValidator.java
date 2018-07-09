package org.iata.bsplink.refund.validation;

import org.iata.bsplink.refund.model.entity.Config;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.service.ConfigService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class FreeStatValidator implements Validator {

    public static final String INCORRECT_VALUE_MSG = "The value is not admitted.";
    public static final String ERROR_CODE = "field.incorrect_value";

    private ConfigService configService;

    public FreeStatValidator(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Refund.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Refund refund = (Refund) target;

        if (refund.getIsoCountryCode() == null) {
            return;
        }

        if (refund.getStatisticalCode() == null
                || !refund.getStatisticalCode().matches("^( +)|([ID][ A-Z0-9]{0,2})?$")) {
            return;
        }

        Config cfg = configService.find(refund.getIsoCountryCode());

        if (!cfg.getFreeStat() && !refund.getStatisticalCode().matches("^[ID]$")) {
            errors.rejectValue("statisticalCode", ERROR_CODE, INCORRECT_VALUE_MSG);
        }

    }
}
