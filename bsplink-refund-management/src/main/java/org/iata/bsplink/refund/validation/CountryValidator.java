package org.iata.bsplink.refund.validation;

import org.iata.bsplink.refund.fake.Country;
import org.iata.bsplink.refund.model.entity.Refund;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CountryValidator implements Validator {

    public static final String NOT_FOUND_MSG = "The country was not found.";

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

        boolean countryExists = Country.findAllCountries().stream()
            .anyMatch(country -> country.getIsoCountryCode().equals(refund.getIsoCountryCode()));

        if (!countryExists) {
            errors.rejectValue("isoCountryCode", "field.not_found", NOT_FOUND_MSG);
        }
    }
}
