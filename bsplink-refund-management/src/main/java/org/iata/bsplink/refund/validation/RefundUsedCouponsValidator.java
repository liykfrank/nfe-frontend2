package org.iata.bsplink.refund.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iata.bsplink.refund.model.entity.Config;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.model.entity.RelatedDocument;
import org.iata.bsplink.refund.service.ConfigService;
import org.springframework.validation.Errors;

/**
 * Validates the minimun and maximun number of coupons used.
 */
public class RefundUsedCouponsValidator extends RefundBaseValidator {

    private static final String ERROR_CODE = "value.not_allowed";

    public RefundUsedCouponsValidator(ConfigService configService) {

        super(configService);
    }

    @Override
    public void validate(Refund refund, Errors errors, Config countryConfig) {

        int minCoupons = getMinimumCouponsNumber(countryConfig);
        int maxCoupons = getMaximumCouponsNumber(countryConfig);
        int totalUsed = countUsedCoupons(refund);

        if (!(minCoupons <= totalUsed && totalUsed <= maxCoupons)) {

            errors.rejectValue("conjunctions", ERROR_CODE,
                    getMessage(minCoupons, maxCoupons, totalUsed));
        }
    }

    protected int getMinimumCouponsNumber(Config config) {

        return config.getIssueRefundsWithoutCouponsAllowed() ? 0 : 1;
    }

    protected int getMaximumCouponsNumber(Config config) {

        return config.getMaxCouponsInRelatedDocuments();
    }

    protected int countUsedCoupons(Refund refund) {

        int total = 0;

        for (RelatedDocument relatedDocument : getRelatedDocuments(refund)) {

            total += countUsedCoupons(relatedDocument);
        }

        return total;
    }

    protected int countUsedCoupons(RelatedDocument relatedDocument) {

        return (int ) Arrays.asList(
                relatedDocument.getRelatedTicketCoupon1(),
                relatedDocument.getRelatedTicketCoupon2(),
                relatedDocument.getRelatedTicketCoupon3(),
                relatedDocument.getRelatedTicketCoupon4())
        .stream()
        .filter(isUsed -> isUsed)
        .count();
    }

    private String getMessage(int minCoupons, int maxCoupons, int totalUsed) {

        return String.format("number of used coupons must be between %d and %d: %d used",
                minCoupons, maxCoupons, totalUsed);
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
