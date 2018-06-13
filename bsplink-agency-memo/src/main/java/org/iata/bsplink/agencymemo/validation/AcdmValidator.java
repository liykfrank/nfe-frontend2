package org.iata.bsplink.agencymemo.validation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.validation.constraints.AcdmConstraint;
import org.springframework.beans.factory.annotation.Autowired;


public class AcdmValidator
        implements ConstraintValidator<AcdmConstraint, AcdmRequest> {

    @Autowired
    List<ConstraintValidator<AcdmConstraint, AcdmRequest>> acdmValidators;

    @Override
    public boolean isValid(AcdmRequest acdm,
            ConstraintValidatorContext context) {

        if (acdm.getIsoCountryCode() == null) {
            return true;
        }

        boolean result = true;

        for (ConstraintValidator<AcdmConstraint, AcdmRequest> validator : acdmValidators) {
            result &= validator.isValid(acdm, context);
        }

        return result;
    }
}
