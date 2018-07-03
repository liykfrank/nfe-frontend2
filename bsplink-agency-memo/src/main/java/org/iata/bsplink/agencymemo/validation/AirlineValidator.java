package org.iata.bsplink.agencymemo.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.dto.Airline;
import org.iata.bsplink.agencymemo.service.AirlineService;
import org.iata.bsplink.agencymemo.validation.constraints.AirlineConstraint;


public class AirlineValidator implements ConstraintValidator<AirlineConstraint, AcdmRequest> {


    public static final String NOT_FOUND_MSG = "The airline does not exist.";

    private static String property = "airlineCode";

    private AirlineService airlineService;

    public AirlineValidator(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    @Override
    public boolean isValid(AcdmRequest acdm, ConstraintValidatorContext context) {

        String airlineCode = acdm.getAirlineCode();

        if (airlineCode == null) {
            return true;
        }

        return isValidAirline(acdm, context);
    }


    private boolean isValidAirline(AcdmRequest acdm, ConstraintValidatorContext context) {

        Airline airline =
                airlineService.findAirline(acdm.getIsoCountryCode(), acdm.getAirlineCode());

        if (airline == null) {
            addMessage(context, property, NOT_FOUND_MSG);
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
