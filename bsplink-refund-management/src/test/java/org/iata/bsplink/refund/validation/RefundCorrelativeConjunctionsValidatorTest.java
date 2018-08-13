package org.iata.bsplink.refund.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
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
public class RefundCorrelativeConjunctionsValidatorTest {

    private static final String ANY_ISO_COUNTRY_CODE = "ES";

    private Refund refund;
    private Validator validator;
    private Config config;
    private ConfigService configService;
    private Errors errors;

    @Before
    public void setUp() {

        refund = new Refund();
        refund.setIsoCountryCode(ANY_ISO_COUNTRY_CODE);

        configService = mock(ConfigService.class);
        when(configService.find(ANY_ISO_COUNTRY_CODE)).thenReturn(config);

        errors = new BeanPropertyBindingResult(refund, "refund");

        validator = new RefundCorrelativeConjunctionsValidator(configService);
    }

    @Test
    public void testSupports() {

        assertTrue(validator.supports(refund.getClass()));
        assertFalse(validator.supports(Object.class));
    }

    @Test
    public void testResultIsValidIfDoesntHaveIsoCountryCode() {

        refund.setRelatedDocument(new RelatedDocument());

        refund.setIsoCountryCode(null);
        validator.validate(refund, errors);

        assertFalse(errors.hasErrors());

        refund.setIsoCountryCode("");
        validator.validate(refund, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    public void testAddExpectedMessageWhenNotValid() {

        refund.setRelatedDocument(createRelatedDocument("123450"));
        refund.setConjunctions(
                Arrays.asList(createRelatedDocument("123451"), createRelatedDocument("123459")));

        validator.validate(refund, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors());

        String expectedMessage = "conjunctions numbers must be correlatives";

        assertEquals(expectedMessage, errors.getFieldError().getDefaultMessage());
    }

    private RelatedDocument createRelatedDocument(String number) {

        RelatedDocument relatedDocument = new RelatedDocument();
        relatedDocument.setRelatedTicketDocumentNumber(number);

        return relatedDocument;
    }

    @Test
    @Parameters
    public void testValidatesMaxConjunctions(RelatedDocument relatedDocument,
            List<RelatedDocument> conjunctions, boolean hasErrors, int cnjIndex) {

        refund.setRelatedDocument(relatedDocument);
        refund.setConjunctions(conjunctions);

        validator.validate(refund, errors);

        assertEquals(hasErrors, errors.hasFieldErrors());

        if (hasErrors) {

            assertEquals("conjunctions[" + cnjIndex + "].relatedTicketDocumentNumber",
                    errors.getFieldError().getField());
        }
    }


    @SuppressWarnings("unused")
    private Object[][] parametersForTestValidatesMaxConjunctions() {

        RelatedDocument relatedDocument = createRelatedDocument("123450");
        RelatedDocument relatedDocumentNan = createRelatedDocument("AAAA");

        List<RelatedDocument> correlativeConjunctions =
                Arrays.asList(createRelatedDocument("123451"), createRelatedDocument("123452"));

        List<RelatedDocument> notCorrelativeConjunctionsCase1 =
                Arrays.asList(createRelatedDocument("123451"), createRelatedDocument("123453"));

        List<RelatedDocument> notCorrelativeConjunctionsCase2 =
                Arrays.asList(createRelatedDocument("123455"), createRelatedDocument("123456"));

        List<RelatedDocument> notCorrelativeConjunctionsCase3 =
                Arrays.asList(createRelatedDocument("123451"), relatedDocumentNan);

        return new Object[][] {

            { relatedDocument, correlativeConjunctions, false, -1 },
            { relatedDocument, notCorrelativeConjunctionsCase1, true, 1 },
            { relatedDocument, notCorrelativeConjunctionsCase2, true, 0 },
            { relatedDocument, notCorrelativeConjunctionsCase3, false, -1 },
            { relatedDocumentNan, correlativeConjunctions, false, -1 },
            { new RelatedDocument(), correlativeConjunctions, false, -1 }
        };
    }

    @Test
    public void testResultIsValidIfConjuntionsAreCorrect() {

        refund.setRelatedDocument(createRelatedDocument("123450"));
        refund.setConjunctions(
                Arrays.asList(createRelatedDocument("123451"), createRelatedDocument("123452")));

        validator.validate(refund, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    public void testCanManageBigDocumentNumbers() {

        refund.setRelatedDocument(createRelatedDocument("100000000123450"));
        refund.setConjunctions(Arrays.asList(
                createRelatedDocument("100000000123451"),
                createRelatedDocument("100000000123452")));

        try {

            validator.validate(refund, errors);

        } catch (NumberFormatException exception) {

            fail("Validation should be able to manage big document numbers");
        }
    }
}
