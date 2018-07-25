package org.iata.bsplink.agencymemo.validation;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.dto.RelatedTicketDocumentRequest;
import org.iata.bsplink.agencymemo.model.entity.Config;
import org.iata.bsplink.agencymemo.service.ConfigService;
import org.iata.bsplink.agencymemo.validation.constraints.AcdmConstraint;

public class RelatedDocumentsValidator
        implements ConstraintValidator<AcdmConstraint, AcdmRequest> {

    public static final String LIMIT_EXCEEDED_MSG =
            "Limit of number of allowed Related Documents exceeded.";

    public static final String DUPLICATE_MSG =
            "Related Document Numbers are duplicated.";

    private ConfigService configService;

    public RelatedDocumentsValidator(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public boolean isValid(AcdmRequest acdm, ConstraintValidatorContext context) {

        context.disableDefaultConstraintViolation();

        if (acdm.getIsoCountryCode() == null) {
            return true;
        }

        Config cfg = configService.find(acdm.getIsoCountryCode());

        if (cfg.getMaxNumberOfRelatedDocuments() < 0) {
            return true;
        }

        if (acdm.getRelatedTicketDocuments() == null) {
            return true;
        }

        if (acdm.getRelatedTicketDocuments().size() > cfg.getMaxNumberOfRelatedDocuments()) {
            context
                .buildConstraintViolationWithTemplate(LIMIT_EXCEEDED_MSG)
                .addPropertyNode("relatedTicketDocuments")
                .addConstraintViolation();
            return false;
        }


        boolean existsNullTicketNumber = acdm.getRelatedTicketDocuments().stream()
                .map(RelatedTicketDocumentRequest::getRelatedTicketDocumentNumber)
                .anyMatch(Objects::isNull);
        if (existsNullTicketNumber) {
            return true;
        }

        long distinctTicketNumbers = acdm.getRelatedTicketDocuments().stream()
                .map(RelatedTicketDocumentRequest::getRelatedTicketDocumentNumber)
                .map(rtdn ->  rtdn.length() > 3 ? rtdn.substring(3) : rtdn)
                .distinct().count();

        if (acdm.getRelatedTicketDocuments().size() != distinctTicketNumbers) {
            context
                .buildConstraintViolationWithTemplate(DUPLICATE_MSG)
                .addPropertyNode("relatedTicketDocuments")
                .addConstraintViolation();
            return false;
        }

        return true;
    }
}
