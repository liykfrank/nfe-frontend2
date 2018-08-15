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
public class RefundConjunctionsValidatorTest {

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



        refund.setIsoCountryCode(ANY_ISO_COUNTRY_CODE);

        config = new Config();
        config.setMaxConjunctions(5);


        configService = mock(ConfigService.class);
        when(configService.find(ANY_ISO_COUNTRY_CODE)).thenReturn(config);

        errors = new BeanPropertyBindingResult(refund, "refund");

        validator = new RefundConjunctionsValidator(configService);
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

        String expectedMessage = RefundConjunctionsValidator.CONJUNCTION_MSG;

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


    @Test
    public void testValidatesDuplicatedRtdnAndConjunction() {

        refund.setRelatedDocument(createRelatedDocument("123450"));
        refund.setConjunctions(
                Arrays.asList(createRelatedDocument("123451"), createRelatedDocument("123450")));

        validator.validate(refund, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors());

        String expectedMessage = RefundConjunctionsValidator.DUPLICATED_MSG;

        assertEquals(expectedMessage, errors.getFieldError().getDefaultMessage());
    }


    @Test
    public void testValidatesDuplicatedConjunctions() {

        refund.setRelatedDocument(createRelatedDocument("123450"));
        refund.setConjunctions(
                Arrays.asList(createRelatedDocument("123451"), createRelatedDocument("123451")));

        validator.validate(refund, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors());

        String expectedMessage = RefundConjunctionsValidator.DUPLICATED_MSG;

        assertEquals(expectedMessage, errors.getFieldError().getDefaultMessage());
    }


    @SuppressWarnings("unused")
    private Object[][] parametersForTestValidatesMaxConjunctions() {

        RelatedDocument relatedDocument = createRelatedDocument("123450");
        RelatedDocument relatedDocumentNan = createRelatedDocument("AAAA");

        List<RelatedDocument> correlativeConjunctions1 =
                Arrays.asList(createRelatedDocument("123451"), createRelatedDocument("123454"));

        List<RelatedDocument> correlativeConjunctions2 = Arrays.asList(
                createRelatedDocument("123451"),
                createRelatedDocument("123453"),
                createRelatedDocument("123452"),
                createRelatedDocument("123455"));

        List<RelatedDocument> notCorrelativeConjunctionsCase1 =
                Arrays.asList(createRelatedDocument("123456"), createRelatedDocument("123453"));

        List<RelatedDocument> notCorrelativeConjunctionsCase2 =
                Arrays.asList(createRelatedDocument("123455"), createRelatedDocument("123456"));

        List<RelatedDocument> notCorrelativeConjunctionsCase3 =
                Arrays.asList(createRelatedDocument("123451"), relatedDocumentNan);

        List<RelatedDocument> notCorrelativeConjunctionsCase4 = Arrays.asList(
                createRelatedDocument("123451"),
                createRelatedDocument("123453"),
                createRelatedDocument("123454"),
                createRelatedDocument("123456"));

        return new Object[][] {

            { relatedDocument, correlativeConjunctions1, false, -1 },
            { relatedDocument, correlativeConjunctions2, false, -1 },
            { relatedDocument, notCorrelativeConjunctionsCase1, true, 0 },
            { relatedDocument, notCorrelativeConjunctionsCase2, true, 1 },
            { relatedDocument, notCorrelativeConjunctionsCase3, false, -1 },
            { relatedDocument, notCorrelativeConjunctionsCase3, false, -1 },
            { relatedDocument, notCorrelativeConjunctionsCase4, true, 3 },
            { relatedDocumentNan, correlativeConjunctions1, false, -1 },
            { new RelatedDocument(), correlativeConjunctions1, false, -1 }
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
