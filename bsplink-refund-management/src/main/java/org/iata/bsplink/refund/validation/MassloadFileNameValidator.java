package org.iata.bsplink.refund.validation;

import org.iata.bsplink.refund.model.entity.Refund;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class MassloadFileNameValidator {
    public static final String INCORRECT_FILENAME =
            "The fileName parameter value has incorrect format.";
    public static final String INCORRECT_COUNTRY =
            "The fileName and refund's ISO country code do not match.";
    public static final String INCORRECT_AIRLINE =
            "The fileName and refund's airline code do not match.";

    /**
     * Validates massload file name against refund.
     */
    public void validate(Refund refund, String fileName, Errors errors) {
        if (fileName == null
                || !fileName.matches("^[A-Z]{2}e9EARS_\\d{8}_[A-Z0-9]{3}[0-69]_\\d{3}")) {
            errors.reject("param.incorrect_format", INCORRECT_FILENAME);
        }

        if (fileName != null && refund.getIsoCountryCode() != null
                && !fileName.startsWith(refund.getIsoCountryCode())) {
            errors.reject("param.incorrect_country", INCORRECT_COUNTRY);
        }

        if (fileName != null && refund.getAirlineCode() != null
                && fileName.indexOf(refund.getAirlineCode(), 18) != 18) {
            errors.reject("param.incorrect_airline", INCORRECT_AIRLINE);
        }
    }

}
