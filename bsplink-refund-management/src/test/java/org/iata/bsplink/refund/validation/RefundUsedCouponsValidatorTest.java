package org.iata.bsplink.refund.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.iata.bsplink.refund.model.entity.Config;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.model.entity.RelatedDocument;
import org.iata.bsplink.refund.service.ConfigService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RunWith(JUnitParamsRunner.class)
public class RefundUsedCouponsValidatorTest {

    private static final String ANY_ISO_COUNTRY_CODE = "ES";
    private static final int MAX_NUMBER_USED_COUPONS = 4;

    private Refund refund;
    private Validator validator;
    private Config config;
    private ConfigService configService;
    private Errors errors;

    @Before
    public void setUp() {

        refund = new Refund();
        refund.setIsoCountryCode(ANY_ISO_COUNTRY_CODE);

        config = new Config();
        configService = mock(ConfigService.class);
        when(configService.find(ANY_ISO_COUNTRY_CODE)).thenReturn(config);

        errors = new BeanPropertyBindingResult(refund, "refund");

        validator = new RefundUsedCouponsValidator(configService);
    }

    @Test
    public void testSupports() {

        assertTrue(validator.supports(refund.getClass()));
        assertFalse(validator.supports(Object.class));
    }

    @Test
    public void testResultIsValidIfDoesntHaveIsoCountryCode() {

        refund.setRelatedDocument(new RelatedDocument());
        config.setIssueRefundsWithoutCouponsAllowed(false);

        refund.setIsoCountryCode(null);
        validator.validate(refund, errors);

        assertFalse(errors.hasErrors());

        refund.setIsoCountryCode("");
        validator.validate(refund, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    public void testAddExpectedMessageWhenNotValid() {

        refund.setRelatedDocument(new RelatedDocument());
        config.setIssueRefundsWithoutCouponsAllowed(false);
        config.setMaxCouponsInRelatedDocuments(MAX_NUMBER_USED_COUPONS);

        validator.validate(refund, errors);

        assertTrue(errors.hasErrors());

        String expectedMessage =
                String.format("number of used coupons must be between %d and %d: %d used", 1,
                        MAX_NUMBER_USED_COUPONS, 0);

        assertEquals(expectedMessage, errors.getGlobalError().getDefaultMessage());
    }

    @Test
    @Parameters
    public void testValidatesNumberOfCoupons(RelatedDocument relatedDocument,
            List<RelatedDocument> conjunctions, boolean notUsedCouponsAllowed, boolean hasErrors) {

        refund.setRelatedDocument(relatedDocument);
        refund.setConjunctions(conjunctions);

        config.setIssueRefundsWithoutCouponsAllowed(notUsedCouponsAllowed);
        config.setMaxCouponsInRelatedDocuments(MAX_NUMBER_USED_COUPONS);

        validator.validate(refund, errors);

        assertEquals(hasErrors, errors.hasErrors());
    }

    @SuppressWarnings("unused")
    private Object[][] parametersForTestValidatesNumberOfCoupons() {

        RelatedDocument withoutCoupons = new RelatedDocument();

        RelatedDocument withCoupon1 = getDocumentWithCoupons(1);
        RelatedDocument withCoupon2 = getDocumentWithCoupons(2);
        RelatedDocument withCoupon3 = getDocumentWithCoupons(3);
        RelatedDocument withCoupon4 = getDocumentWithCoupons(4);
        RelatedDocument with4Coupons = getDocumentWithCoupons(1, 2, 3, 4);

        List<RelatedDocument> conjunctionsWithoutCoupons =
                Arrays.asList(withoutCoupons, withoutCoupons);

        List<RelatedDocument> conjunctionsWith4Coupons =
                Arrays.asList(withCoupon1, withCoupon2, withCoupon3, withCoupon4);

        return new Object[][] {

                { null, null, true, false },

                // Minimum number of coupons cases
                { withoutCoupons, conjunctionsWithoutCoupons, true, false },
                { withoutCoupons, conjunctionsWithoutCoupons, false, true },

                { withCoupon1, conjunctionsWithoutCoupons, true, false },
                { withCoupon1, conjunctionsWithoutCoupons, false, false },

                { withCoupon2, conjunctionsWithoutCoupons, true, false },
                { withCoupon2, conjunctionsWithoutCoupons, false, false },

                { withCoupon3, conjunctionsWithoutCoupons, true, false },
                { withCoupon3, conjunctionsWithoutCoupons, false, false },

                { withCoupon4, conjunctionsWithoutCoupons, true, false },
                { withCoupon4, conjunctionsWithoutCoupons, false, false },


                // Maximum number of coupons cases
                { withoutCoupons, conjunctionsWith4Coupons, true, false },
                { withCoupon1, conjunctionsWith4Coupons, true, true },

                { with4Coupons, conjunctionsWithoutCoupons, true, false },
                { with4Coupons, conjunctionsWith4Coupons, true, true },
        };
    }

    private RelatedDocument getDocumentWithCoupons(Integer... couponsNumbers) {

        RelatedDocument relatedDocument = new RelatedDocument();

        for (Integer couponNumber : Arrays.asList(couponsNumbers)) {

            switch (couponNumber) {

                case 1:
                    relatedDocument.setRelatedTicketCoupon1(true);
                    break;

                case 2:
                    relatedDocument.setRelatedTicketCoupon2(true);
                    break;

                case 3:
                    relatedDocument.setRelatedTicketCoupon3(true);
                    break;

                case 4:
                    relatedDocument.setRelatedTicketCoupon4(true);
                    break;

                default:
                    break;
            }
        }

        return relatedDocument;
    }
}
