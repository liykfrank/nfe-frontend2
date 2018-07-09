package org.iata.bsplink.refund.validation;

import org.iata.bsplink.refund.dto.Airline;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.service.AirlineService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AirlineValidator implements Validator {

    public static final String NOT_FOUND_MSG = "The airline does not exist.";

    private AirlineService airlineService;

    public AirlineValidator(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Refund.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Refund refund = (Refund) target;

        if (refund.getAirlineCode() == null || refund.getIsoCountryCode() == null) {
            return;
        }

        Airline airline = airlineService.findAirline(
                refund.getIsoCountryCode(), refund.getAirlineCode());

        if (airline == null) {
            errors.rejectValue("airlineCode", "field.not_found", NOT_FOUND_MSG);
        }
    }
}
