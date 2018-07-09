package org.iata.bsplink.refund.validation;

import org.apache.commons.lang3.StringUtils;
import org.iata.bsplink.refund.model.entity.Config;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.service.ConfigService;
import org.springframework.validation.Errors;

public abstract class RefundValueIsEnabledValidator extends RefundBaseValidator {

    private static final String ERROR_CODE = "value.not_allowed";
    public static final String DEFAULT_MESSAGE = "value is not allowed";

    /**
     * Returns the field name under validation.
     */
    public abstract String getFieldName();

    /**
     * Returns true if the value should be enabled.
     */
    protected abstract boolean valueMustBeEnabled(Refund refund);

    /**
     * Whether the field is allowed to have value or not.
     */
    protected abstract boolean isEnabled(Config config);

    public RefundValueIsEnabledValidator(ConfigService configService) {

        super(configService);
    }

    @Override
    public void validate(Refund refund, Errors errors, Config countryConfig) {

        if (valueIsDefinedButNotEnabled(refund, countryConfig)) {

            errors.rejectValue(getFieldName(), ERROR_CODE, getDefaultMessage());
        }
    }

    public String getDefaultMessage() {

        return DEFAULT_MESSAGE;
    }

    private boolean valueIsDefinedButNotEnabled(Refund refund, Config countryConfig) {

        return valueMustBeEnabled(refund)
                && ! isEnabled(countryConfig);
    }

    protected boolean isNotEmpty(String value) {

        return ! StringUtils.isEmpty(value);
    }

}
