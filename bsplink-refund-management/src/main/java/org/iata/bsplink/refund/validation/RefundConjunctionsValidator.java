package org.iata.bsplink.refund.validation;

import org.apache.commons.lang3.StringUtils;
import org.iata.bsplink.refund.model.entity.Config;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.model.entity.RelatedDocument;
import org.iata.bsplink.refund.service.ConfigService;
import org.springframework.validation.Errors;

/**
 * Validates the conjunction document numbers.
 */
public class RefundConjunctionsValidator extends RefundBaseValidator {

    public static final String CONJUNCTION_MSG = "The refunded documents should be conjunctions.";
    public static final String DUPLICATED_MSG =
            "You have entered a number of a refunded document twice.";

    private static final String ERROR_CODE = "number.correlative";

    private static final String FIELD_NAME = "conjunctions[%d].relatedTicketDocumentNumber";


    public RefundConjunctionsValidator(ConfigService configService) {

        super(configService);
    }

    @Override
    public void validate(Refund refund, Errors errors, Config countryConfig) {

        int cnjPosition = -1;

        String rtdn = refund.getRelatedDocument().getRelatedTicketDocumentNumber();

        if (rtdn == null || !StringUtils.isNumeric(rtdn) || refund.getConjunctions() == null) {

            return;
        }

        long rtdnNumber = Long.parseLong(rtdn);

        int maxCnjNum = countryConfig.getMaxConjunctions();

        for (RelatedDocument cnj : refund.getConjunctions()) {

            String cnjRtdn;
            cnjPosition++;

            cnjRtdn = cnj.getRelatedTicketDocumentNumber();

            if (cnjRtdn == null || !StringUtils.isNumeric(cnjRtdn)) {

                continue;
            }

            long cnjRtdnNumber = Long.parseLong(cnjRtdn);

            if (cnjRtdnNumber > rtdnNumber + maxCnjNum) {

                errors.rejectValue(String.format(FIELD_NAME, cnjPosition), ERROR_CODE,
                        CONJUNCTION_MSG);
            }

            if (cnjRtdnNumber == rtdnNumber) {

                errors.rejectValue(String.format(FIELD_NAME, cnjPosition), ERROR_CODE,
                        DUPLICATED_MSG);

            } else {

                validateNoDuplicates(refund, errors, cnjRtdnNumber, cnjPosition);
            }
        }
    }


    private void validateNoDuplicates(Refund refund, Errors errors,
        long cnjRtdnNumber, int cnjPosition) {

        int pos = -1;

        for (RelatedDocument cnjBefore : refund.getConjunctions()) {

            pos++;

            if (cnjBefore != null) {

                if (pos == cnjPosition) {
                    break;
                }

                String cnjBeforeRtdn = cnjBefore.getRelatedTicketDocumentNumber();

                if (StringUtils.isNumeric(cnjBeforeRtdn)
                        && cnjRtdnNumber == Long.parseLong(cnjBeforeRtdn)) {

                    errors.rejectValue(String.format(FIELD_NAME, cnjPosition), ERROR_CODE,
                            DUPLICATED_MSG);
                }
            }
        }
    }
}
