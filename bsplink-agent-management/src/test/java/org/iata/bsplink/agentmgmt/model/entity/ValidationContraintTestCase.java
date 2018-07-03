package org.iata.bsplink.agentmgmt.model.entity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@RunWith(JUnitParamsRunner.class)
public abstract class ValidationContraintTestCase<T> {

    protected static final String MUST_NOT_BE_NULL_MESSAGE = "must not be null";
    protected static final String VERY_LONG_STRING = String.format("very long %255s ", "string");

    private Locale actualLocale;
    private LocalValidatorFactoryBean validator;
    private T objectToValidate;

    @Before
    public void setUp() {

        // we want the validation messages in English
        actualLocale = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);

        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        objectToValidate = getObjectToValidate();
    }

    protected abstract T getObjectToValidate();

    @After
    public void tearDown() {

        Locale.setDefault(actualLocale);
        validator.close();
    }

    @Test
    @Parameters
    public void testValidationConstraints(String fieldName, String fieldValue, String message)
            throws Exception {

        BeanUtils.setProperty(objectToValidate, fieldName, fieldValue);

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(objectToValidate,
                objectToValidate.getClass().getSimpleName());

        validator.validate(objectToValidate, errors);

        assertThat(errors.getErrorCount(), equalTo(1));

        FieldError fieldError = errors.getFieldErrors().get(0);

        assertThat(fieldError.getField(), equalTo(fieldName));
        assertThat(fieldError.getDefaultMessage(), equalTo(message));
    }

    protected String getSizeValidationErrorMessage(int min, int max) {

        return String.format("size must be between %d and %d", min, max);
    }

    protected abstract Object[][] parametersForTestValidationConstraints();
}
