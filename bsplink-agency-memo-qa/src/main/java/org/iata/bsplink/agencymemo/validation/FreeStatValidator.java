package org.iata.bsplink.agencymemo.validation;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.model.entity.Config;
import org.iata.bsplink.agencymemo.service.ConfigService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class FreeStatValidator implements Validator {

    public static final String INCORRECT_VALUE_MSG = "The value is not admitted.";
    public static final String ERROR_CODE = "field.incorrect_value";

    private ConfigService configService;

    public FreeStatValidator(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return AcdmRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        AcdmRequest acdm = (AcdmRequest) target;

        if (acdm.getIsoCountryCode() == null) {
            return;
        }

        if (acdm.getStatisticalCode() == null
                || !acdm.getStatisticalCode().matches("^( +)|([ID][ A-Z0-9]{0,2})?$")) {
            return;
        }

        Config cfg = configService.find(acdm.getIsoCountryCode());

        if (!cfg.getFreeStat() && !acdm.getStatisticalCode().matches("^[ID]$")) {
            errors.rejectValue("statisticalCode", ERROR_CODE, INCORRECT_VALUE_MSG);
        }

    }
}
