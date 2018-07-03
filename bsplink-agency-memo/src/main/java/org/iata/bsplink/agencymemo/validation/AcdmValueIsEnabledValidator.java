package org.iata.bsplink.agencymemo.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.model.entity.Config;
import org.iata.bsplink.agencymemo.service.ConfigService;
import org.iata.bsplink.agencymemo.validation.constraints.AcdmConstraint;

public abstract class AcdmValueIsEnabledValidator
        implements ConstraintValidator<AcdmConstraint, AcdmRequest> {

    protected ConfigService configService;

    public AcdmValueIsEnabledValidator(ConfigService configService) {
        this.configService = configService;
    }

    protected abstract String getFieldName();

    protected abstract String getContraintViolationMessage();

    protected abstract String getValue(AcdmRequest acdm);

    protected abstract boolean isEnabled(Config countryConfig);

    @Override
    public final boolean isValid(AcdmRequest acdm, ConstraintValidatorContext context) {

        if (StringUtils.isEmpty(acdm.getIsoCountryCode())) {
            return true;
        }

        if (valueIsDefinedButNotEnabled(acdm)) {

            addToContext(context, getFieldName(), getContraintViolationMessage());

            return false;
        }

        return true;
    }

    private boolean valueIsDefinedButNotEnabled(AcdmRequest acdm) {

        return ! StringUtils.isEmpty(getValue(acdm))
                && ! isEnabled(configService.find(acdm.getIsoCountryCode()));
    }

    private void addToContext(ConstraintValidatorContext context, String property, String message) {
        context
            .buildConstraintViolationWithTemplate(message)
            .addPropertyNode(property)
            .addConstraintViolation();
    }

}
