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

        if (!numbersAreCorrelatives(documentNumbers)) {

            errors.rejectValue(null, ERROR_CODE, "conjunctions numbers must be correlatives");
        }
    }

    private boolean numbersAreCorrelatives(List<String> documentNumbers) {

        Integer expectedNumber = null;

        for (String documentNumber : documentNumbers) {

            if (!StringUtils.isNumeric(documentNumber)) {

                return false;
            }

            int currentNumber = Integer.parseInt(documentNumber);

            expectedNumber = expectedNumber == null ? currentNumber : expectedNumber + 1;

            if (currentNumber != expectedNumber) {

                return false;
            }
        }

        return true;
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
