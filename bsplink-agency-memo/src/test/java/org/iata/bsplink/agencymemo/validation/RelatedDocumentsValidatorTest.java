package org.iata.bsplink.agencymemo.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.dto.RelatedTicketDocumentRequest;
import org.iata.bsplink.agencymemo.model.entity.Config;
import org.iata.bsplink.agencymemo.service.ConfigService;
import org.iata.bsplink.agencymemo.test.validation.CustomConstraintValidatorTestCase;
import org.junit.Before;
import org.junit.Test;

public class RelatedDocumentsValidatorTest extends CustomConstraintValidatorTestCase {

    public ConfigService configService;

    private RelatedDocumentsValidator validator;
    private AcdmRequest acdm;
    private Config config;

    @Override
    @Before
    public void setUp() {
        super.setUp();

        config = new Config();
        config.setIsoCountryCode("XX");
        config.setMaxNumberOfRelatedDocuments(10);

        configService = mock(ConfigService.class);
        when(configService.find(config.getIsoCountryCode())).thenReturn(config);

        acdm = new AcdmRequest();
        acdm.setIsoCountryCode(config.getIsoCountryCode());

        List<RelatedTicketDocumentRequest> relatedTicketDocuments =
                new ArrayList<>();

        RelatedTicketDocumentRequest rtdn = new RelatedTicketDocumentRequest();
        rtdn.setRelatedTicketDocumentNumber("1234567890123");
        relatedTicketDocuments.add(rtdn);

        rtdn = new RelatedTicketDocumentRequest();
        rtdn.setRelatedTicketDocumentNumber("1234567890124");
        relatedTicketDocuments.add(rtdn);

        acdm.setRelatedTicketDocuments(relatedTicketDocuments);

        validator = new RelatedDocumentsValidator(configService);
    }

    @Test
    public void testIsValid() {
        config.setMaxNumberOfRelatedDocuments(2);
        assertTrue(validator.isValid(acdm, context));
    }

    @Test
    public void testIsValidWithNullIsoCountryCode() {
        acdm.setIsoCountryCode(null);
        assertTrue(validator.isValid(acdm, context));
    }

    @Test
    public void testIsValidWithNegativeLimit() {
        config.setMaxNumberOfRelatedDocuments(-2);
        assertTrue(validator.isValid(acdm, context));
    }

    @Test
    public void testIsValidWithNullRelatedDocuments() {
        acdm.setRelatedTicketDocuments(null);
        assertTrue(validator.isValid(acdm, context));
    }

    @Test
    public void testIsNotValidWithLimitExceeded() {
        config.setMaxNumberOfRelatedDocuments(1);
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("relatedTicketDocuments",
                RelatedDocumentsValidator.LIMIT_EXCEEDED_MSG);
    }

    @Test
    public void testIsNotValidWithDuplicatedTicketNumbers() {
        acdm.getRelatedTicketDocuments().get(1).setRelatedTicketDocumentNumber(
                acdm.getRelatedTicketDocuments().get(0)
                .getRelatedTicketDocumentNumber());
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("relatedTicketDocuments",
                RelatedDocumentsValidator.DUPLICATE_MSG);
    }

    @Test
    public void testIsValidWithNullTicketNumber() {
        acdm.getRelatedTicketDocuments().get(1).setRelatedTicketDocumentNumber(null);
        assertTrue(validator.isValid(acdm, context));
    }

    @Test
    public void testIsValidWithIncorrectThreeCharTicketNumber() {
        acdm.getRelatedTicketDocuments().get(1).setRelatedTicketDocumentNumber("123");
        assertTrue(validator.isValid(acdm, context));
    }
}
