package org.iata.bsplink.refund.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
public class RefundMaxConjunctionsValidatorTest {

    private static final String ANY_ISO_COUNTRY_CODE = "ES";
    private static final int MAX_CONJUNCTIONS = 2;

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
        config.setMaxConjunctions(MAX_CONJUNCTIONS);

        configService = mock(ConfigService.class);
        when(configService.find(ANY_ISO_COUNTRY_CODE)).thenReturn(config);

        errors = new BeanPropertyBindingResult(refund, "refund");

        validator = new RefundMaxConjunctionsValidator(configService);
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

        int totalConjunctions = MAX_CONJUNCTIONS + 1;

        refund.setConjunctions(getConjunctionsList(totalConjunctions));

        validator.validate(refund, errors);

        assertTrue(errors.hasErrors());

        String expectedMessage =
                String.format("number of conjunctions must be less or equal that %d: %d found",
                        MAX_CONJUNCTIONS, totalConjunctions);

        assertEquals(expectedMessage, errors.getGlobalError().getDefaultMessage());
    }

    private List<RelatedDocument> getConjunctionsList(int total) {

        List<RelatedDocument> conjunctions = new ArrayList<>();

        for (int i = 0; i < total; i++) {

            conjunctions.add(new RelatedDocument());
        }

        return conjunctions;
    }

    @Test
    @Parameters
    public void testValidatesMaxConjunctions(List<RelatedDocument> conjunctions,
            boolean hasErrors) {

        refund.setConjunctions(conjunctions);

        validator.validate(refund, errors);

        assertEquals(hasErrors, errors.hasErrors());
    }

    @SuppressWarnings("unused")
    private Object[][] parametersForTestValidatesMaxConjunctions() {

        return new Object[][] {

            { getConjunctionsList(MAX_CONJUNCTIONS), false },
            { getConjunctionsList(MAX_CONJUNCTIONS - 1), false },
            { getConjunctionsList(MAX_CONJUNCTIONS + 1), true },
        };
    }

    @Test
    public void testCanManageNullValueInConjuntions() {

        refund.setConjunctions(null);

        try {

            validator.validate(refund, errors);

        } catch (NullPointerException exception) {

            fail("The validator should be able to manage null value in conjunctions");
        }

        assertFalse(errors.hasErrors());
    }
}
