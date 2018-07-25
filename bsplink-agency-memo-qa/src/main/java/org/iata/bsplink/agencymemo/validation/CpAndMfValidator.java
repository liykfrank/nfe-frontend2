package org.iata.bsplink.agencymemo.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.dto.TaxMiscellaneousFeeRequest;
import org.iata.bsplink.agencymemo.model.ConcernsIndicator;
import org.iata.bsplink.agencymemo.model.entity.Config;
import org.iata.bsplink.agencymemo.service.ConfigService;
import org.iata.bsplink.agencymemo.validation.constraints.AcdmConstraint;

public class CpAndMfValidator
        implements ConstraintValidator<AcdmConstraint, AcdmRequest> {

    public static final String NO_CP_ON_ISSUE_MSG
        = "CP-Tax on ACDMs concerning issues are not permitted.";
    public static final String NO_MF_ON_ISSUE_MSG
        = "MF-Tax on ACDMs concerning issues are not permitted.";
    public static final String NO_CP_ON_RFND_MSG
        = "CP-Tax on ACDMs concerning refunds are not permitted.";
    public static final String NO_MF_ON_RFND_MSG
        = "MF-Tax on ACDMs concerning refunds are not permitted.";

    private ConfigService configService;

    public CpAndMfValidator(ConfigService configService) {
        this.configService = configService;
    }


    @Override
    public boolean isValid(AcdmRequest acdm,
            ConstraintValidatorContext context) {

        context.disableDefaultConstraintViolation();

        if (acdm.getTaxMiscellaneousFees() == null) {
            return true;
        }
        if (acdm.getIsoCountryCode() == null) {
            return true;
        }

        Config cfg = configService.find(acdm.getIsoCountryCode());
        boolean result = true;

        if (!cfg.getCpPermittedForConcerningIssue()) {
            result &= taxCheck(context, acdm, "CP", true, NO_CP_ON_ISSUE_MSG);
        }
        if (!cfg.getMfPermittedForConcerningIssue()) {
            result &= taxCheck(context, acdm, "MF", true, NO_MF_ON_ISSUE_MSG);
        }
        if (!cfg.getCpPermittedForConcerningRefund()) {
            result &= taxCheck(context, acdm, "CP", false, NO_CP_ON_RFND_MSG);
        }
        if (!cfg.getMfPermittedForConcerningRefund()) {
            result &= taxCheck(context, acdm, "MF", false, NO_MF_ON_RFND_MSG);
        }
        return result;
    }


    private boolean taxCheck(ConstraintValidatorContext context, AcdmRequest acdm, String taxType,
            boolean isIssuance, String message) {

        if (isIssuance == ConcernsIndicator.R.equals(acdm.getConcernsIndicator())) {
            return true;
        }

        int i = 0;
        for (TaxMiscellaneousFeeRequest tmf : acdm.getTaxMiscellaneousFees()) {
            if (taxType.equals(tmf.getType())) {
                addToContext(context, "taxMiscellaneousFees[" + i + "].type", message);
                return false;
            }
            i++;
        }

        return true;
    }


    private void addToContext(ConstraintValidatorContext context, String property, String message) {
        context
            .buildConstraintViolationWithTemplate(message)
            .addPropertyNode(property)
            .addConstraintViolation();
    }
}
