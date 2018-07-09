package org.iata.bsplink.refund.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.model.entity.RelatedDocument;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class PartialRefundValidatorTest {
    private PartialRefundValidator validator;
    private Errors errors;
    private Refund refund;

    @Before
    public void setUp() {
        refund = new Refund();
        refund.setPartialRefund(false);
        refund.setRelatedDocument(new RelatedDocument());
        setAllCoupons(refund.getRelatedDocument());
        refund.getConjunctions().add(new RelatedDocument());
        setAllCoupons(refund.getConjunctions().get(0));
        errors = new BeanPropertyBindingResult(refund, "refund");
        validator = new PartialRefundValidator();
    }

    @Test
    public void testIsValidNoPartial() throws Exception {
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsValidPartialConjunctionCouponFalse() throws Exception {
        refund.setPartialRefund(true);
        refund.getConjunctions().get(0).setRelatedTicketCoupon3(false);
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsValidPartialRelatedDocumentNull() throws Exception {
        refund.setPartialRefund(true);
        refund.setRelatedDocument(null);
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsValidPartialCouponFalse() throws Exception {
        refund.setPartialRefund(true);
        refund.getRelatedDocument().setRelatedTicketCoupon3(null);
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsValidPartialConjunctionCouponNull() throws Exception {
        refund.setPartialRefund(true);
        refund.getConjunctions().get(0).setRelatedTicketCoupon3(null);
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsValidPartialCouponNull() throws Exception {
        refund.setPartialRefund(true);
        refund.getRelatedDocument().setRelatedTicketCoupon3(false);
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsNotValidPartial() throws Exception {
        refund.setPartialRefund(true);
        validator.validate(refund, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getErrorCount());
        assertEquals(PartialRefundValidator.COUPON_MSG,
                errors.getAllErrors().get(0).getDefaultMessage());
    }

    @Test
    public void testIsValidLessGrossFareUsed() throws Exception {
        refund.setPartialRefund(true);
        refund.getRelatedDocument().setRelatedTicketCoupon3(false);
        refund.getAmounts().setLessGrossFareUsed(BigDecimal.ONE);
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsNotValidLessGrossFareUsed() throws Exception {
        refund.setPartialRefund(false);
        refund.getAmounts().setLessGrossFareUsed(BigDecimal.ONE);
        validator.validate(refund, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getErrorCount());
        assertEquals(PartialRefundValidator.LESS_GROSS_FARE_ONLY_FOR_PARTIAL_REFUND_MSG,
                errors.getAllErrors().get(0).getDefaultMessage());
    }


    private void setAllCoupons(RelatedDocument relatedDocument) {
        relatedDocument.setRelatedTicketCoupon1(true);
        relatedDocument.setRelatedTicketCoupon2(true);
        relatedDocument.setRelatedTicketCoupon3(true);
        relatedDocument.setRelatedTicketCoupon4(true);
    }

    @Test
    public void testSupports() {
        assertTrue(validator.supports(refund.getClass()));
        assertFalse(validator.supports(Object.class));
    }
}
