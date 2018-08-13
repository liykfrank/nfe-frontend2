package org.iata.bsplink.refund.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.iata.bsplink.refund.model.entity.Config;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.model.entity.RelatedDocument;
import org.iata.bsplink.refund.service.ConfigService;
import org.springframework.validation.Errors;

/**
 * Validates the maximum number of conjunctions.
 */
public class RefundCorrelativeConjunctionsValidator extends RefundBaseValidator {

    private static final String ERROR_CODE = "number.correlative";

    public RefundCorrelativeConjunctionsValidator(ConfigService configService) {

        super(configService);
    }

    @Override
    public void validate(Refund refund, Errors errors, Config countryConfig) {

        List<String> documentNumbers = getRelatedDocuments(refund).stream()
                .map(RelatedDocument::getRelatedTicketDocumentNumber)
                .collect(Collectors.toList());

        int cnjNonCorrelativyPosition = nonCorrelativyConjunctionPosition(documentNumbers);

        if (cnjNonCorrelativyPosition >= 0) {

            errors.rejectValue(
                    "conjunctions[" + cnjNonCorrelativyPosition + "].relatedTicketDocumentNumber",
                    ERROR_CODE, "conjunctions numbers must be correlatives");
        }
    }

    private int nonCorrelativyConjunctionPosition(List<String> documentNumbers) {

        Long expectedNumber = null;
        int cnjPosition = -2;

        for (String documentNumber : documentNumbers) {

            cnjPosition++;

            if (!StringUtils.isNumeric(documentNumber)) {

                return -1;
            }

            long currentNumber = Long.parseLong(documentNumber);

            expectedNumber = expectedNumber == null ? currentNumber : expectedNumber + 1;

            if (currentNumber != expectedNumber) {

                return cnjPosition;
            }
        }

        return -1;
    }

    private List<RelatedDocument> getRelatedDocuments(Refund refund) {

        List<RelatedDocument> relatedDocuments = new ArrayList<>();

        if (refund.getRelatedDocument() != null) {

            relatedDocuments.add(refund.getRelatedDocument());
        }

        if (refund.getConjunctions() != null) {

            relatedDocuments.addAll(refund.getConjunctions());
        }

        return relatedDocuments;
    }
}
