package org.iata.bsplink.agencymemo.validation;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.fake.Country;
import org.iata.bsplink.agencymemo.fake.Currency;
import org.iata.bsplink.agencymemo.fake.IsocCurrencies;
import org.iata.bsplink.agencymemo.validation.constraints.CountryConstraint;


public class CurrencyValidator implements ConstraintValidator<CountryConstraint, AcdmRequest> {

    public static final String NOT_FOUND_MSG = "The currency was not found.";
    public static final String EXPIRED_MSG = "The currency has expired.";

    @Override
    public boolean isValid(AcdmRequest acdm, ConstraintValidatorContext context) {

        if (acdm.getIsoCountryCode() == null) {
            return true;
        }

        if (acdm.getCurrency() == null) {
            return true;
        }

        String code = acdm.getCurrency().getCode();
        if (code == null) {
            return true;
        }

        Integer decimals = acdm.getCurrency().getDecimals();
        if (decimals == null) {
            return true;
        }

        if (Country.findAllCountries().stream().map(Country::getIsoCountryCode)
            .noneMatch(isoc -> isoc.equals(acdm.getIsoCountryCode()))) {
            return true;
        }

        List<IsocCurrencies> isocCurrencies =
                new IsocCurrencies().getCurrenciesByIsoc1(acdm.getIsoCountryCode());

        Currency currencyFound = null;
        for (IsocCurrencies ic: isocCurrencies) {
            Optional<Currency> optionalCurrency = ic.getCurrencies().stream()
                    .filter(c -> c.getName().equals(code) && c.getNumDecimals().equals(decimals))
                    .findFirst();
            if (optionalCurrency.isPresent()) {
                currencyFound = optionalCurrency.get();
                break;
            }
        }

        if (currencyFound == null) {
            addMessage(context, "currency", NOT_FOUND_MSG);
            return false;
        }

        if (currencyFound.getExpirationDate() == null) {
            return true;
        }

        if (acdm.getDateOfIssue() == null) {
            return true;
        }

        if (currencyFound.getExpirationDate().isBefore(acdm.getDateOfIssue())) {
            addMessage(context, "currency", EXPIRED_MSG);
            return false;
        }

        return true;
    }

    private void addMessage(ConstraintValidatorContext context, String property, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addPropertyNode(property)
                .addConstraintViolation();
    }
}
