package org.iata.bsplink.refund.validation;

import org.iata.bsplink.refund.model.entity.Config;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.service.ConfigService;
import org.springframework.validation.Errors;

/**
 * Validates the maximum number of conjunctions.
 */
public class RefundMaxConjunctionsValidator extends RefundBaseValidator {

    private static final String ERROR_CODE = "value.max";

    public RefundMaxConjunctionsValidator(ConfigService configService) {

        super(configService);
    }

    @Override
    public void validate(Refund refund, Errors errors, Config countryConfig) {

        int maxConjunctions = countryConfig.getMaxConjunctions();
        int totalConjunctions =
                refund.getConjunctions() == null ? 0 : refund.getConjunctions().size();

        if (totalConjunctions > maxConjunctions) {

            errors.rejectValue(null, ERROR_CODE, getMessage(maxConjunctions, totalConjunctions));
        }
    }

    private String getMessage(int maxConjunctions, int totalConjunctions) {

        return String.format("number of conjunctions must be less or equal than %d: %d found",
                maxConjunctions, totalConjunctions);
    }
}
