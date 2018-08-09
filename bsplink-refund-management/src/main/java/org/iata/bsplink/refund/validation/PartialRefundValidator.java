package org.iata.bsplink.refund.validation;

import java.math.BigDecimal;

import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.model.entity.RelatedDocument;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PartialRefundValidator implements Validator {

    public static final String COUPON_MSG =
            "Partial Refund has to be false if all coupons have been selected.";
    public static final String LESS_GROSS_FARE_ONLY_FOR_PARTIAL_REFUND_MSG =
            "Less Gross Fare Used amount only to be reported for partial refunds.";

    @Override
    public boolean supports(Class<?> clazz) {
        return Refund.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Refund refund = (Refund) target;

        if ((refund.getPartialRefund() == null || !refund.getPartialRefund())
                && refund.getAmounts() != null && refund.getAmounts().getLessGrossFareUsed() != null
                && refund.getAmounts().getLessGrossFareUsed().compareTo(BigDecimal.ZERO) > 0) {
            errors.rejectValue("amounts.lessGrossFareUsed", "field.invalid",
                    LESS_GROSS_FARE_ONLY_FOR_PARTIAL_REFUND_MSG);
        }

        if (refund.getPartialRefund() == null || !refund.getPartialRefund()
                || refund.getRelatedDocument() == null) {
            return;
        }

        if (!areAllCouponsSelected(refund.getRelatedDocument())) {
            return;
        }

        if (refund.getConjunctions() == null
                 || refund.getConjunctions().stream().allMatch(this::areAllCouponsSelected)) {
            errors.rejectValue("partialRefund", "field.invalid", COUPON_MSG);
        }
    }

    private boolean areAllCouponsSelected(RelatedDocument relatedDocument) {
        return relatedDocument != null
                && isCouponSelected(relatedDocument.getRelatedTicketCoupon1())
                && isCouponSelected(relatedDocument.getRelatedTicketCoupon2())
                && isCouponSelected(relatedDocument.getRelatedTicketCoupon3())
                && isCouponSelected(relatedDocument.getRelatedTicketCoupon4());
    }

    private boolean isCouponSelected(Boolean coupon) {
        return coupon != null && coupon;
    }
}
