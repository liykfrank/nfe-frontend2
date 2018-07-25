package org.iata.bsplink.agencymemo.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.iata.bsplink.agencymemo.fake.Country;
import org.iata.bsplink.agencymemo.validation.constraints.CountryConstraint;


public class CountryValidator implements ConstraintValidator<CountryConstraint, String> {

    public static final String NOT_FOUND_MSG = "The country was not found.";

    @Override
    public boolean isValid(String isoCountryCode, ConstraintValidatorContext context) {

        if (isoCountryCode == null) {
            return true;
        }

        boolean countryExists = Country.findAllCountries().stream()
            .anyMatch(country -> country.getIsoCountryCode().equals(isoCountryCode));

        if (!countryExists) {
            addMessage(context, NOT_FOUND_MSG);
        }

        return countryExists;
    }

    private void addMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
