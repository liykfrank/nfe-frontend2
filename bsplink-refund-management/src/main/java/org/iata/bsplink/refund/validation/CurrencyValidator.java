package org.iata.bsplink.refund.validation;

import java.util.List;
import java.util.Optional;

import org.iata.bsplink.refund.fake.Country;
import org.iata.bsplink.refund.fake.Currency;
import org.iata.bsplink.refund.fake.IsocCurrencies;
import org.iata.bsplink.refund.model.entity.Refund;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CurrencyValidator implements Validator {

    public static final String NOT_FOUND_MSG = "The currency was not found.";
    public static final String EXPIRED_MSG = "The currency has expired.";

    @Override
    public boolean supports(Class<?> clazz) {
        return Refund.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Refund refund = (Refund) target;

        String field = "currency";

        if (refund.getIsoCountryCode() == null) {
            return;
        }

        if (refund.getCurrency() == null) {
            return;
        }

        String code = refund.getCurrency().getCode();
        if (code == null) {
            return;
        }

        Integer decimals = refund.getCurrency().getDecimals();
        if (decimals == null) {
            return;
        }

        if (Country.findAllCountries().stream().map(Country::getIsoCountryCode)
            .noneMatch(isoc -> isoc.equals(refund.getIsoCountryCode()))) {
            return;
        }

        List<IsocCurrencies> isocCurrencies =
                new IsocCurrencies().getCurrenciesByIsoc1(refund.getIsoCountryCode());

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
            errors.rejectValue(field, "field.not_found", NOT_FOUND_MSG);
            return;
        }

        if (currencyFound.getExpirationDate() == null) {
            return;
        }

        if (refund.getDateOfIssue() == null) {
            return;
        }

        if (currencyFound.getExpirationDate().isBefore(refund.getDateOfIssue())) {
            errors.rejectValue(field, "field.not_found", EXPIRED_MSG);
        }
    }
}
