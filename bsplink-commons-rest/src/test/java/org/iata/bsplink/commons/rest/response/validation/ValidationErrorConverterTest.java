package org.iata.bsplink.commons.rest.response.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.convert.converter.Converter;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@RunWith(JUnitParamsRunner.class)
public class ValidationErrorConverterTest {

    private ValidationErrorConverter converter;

    @Before
    public void setUp() throws Exception {

        this.converter = new ValidationErrorConverter();
    }

    @Test
    public void testIsaConverter() {

        assertThat(converter, instanceOf(Converter.class));
    }

    @Test
    @Parameters
    public void testConvertsErrors(Errors errors, List<ValidationError> expected) {

        assertThat(converter.convert(errors), equalTo(expected));
    }

    @SuppressWarnings("unused")
    private Object[][] parametersForTestConvertsErrors() {

        return new Object[][] {
                {
                    createErrorsMockForFieldError(),
                    Arrays.asList(new ValidationError("field1", "validation error"))
                },
                {
                    createErrorsMockForFieldErrorWithBindingFailure(),
                    Arrays.asList(new ValidationError("field1", "Field has an invalid value"))
                },
                {
                    createErrorsMockForObjectError(),
                    Arrays.asList(new ValidationError("object error default message"))
                },
                {
                    createErrorsMockForMoreThanOneFieldError(),
                        Arrays.asList(new ValidationError("field1", "validation error 1"),
                                new ValidationError("field2", "validation error 2"))
                }
        };
    }

    private Errors createErrorsMockForFieldError() {

        Errors errors = mock(Errors.class);

        FieldError fieldError = createFieldErrorMock("field1", "validation error");

        List<ObjectError> errorList = new ArrayList<>();
        when(errors.getAllErrors()).thenReturn(errorList);
        when(errors.getFieldError()).thenReturn(fieldError);

        errorList.add(fieldError);

        return errors;
    }

    private FieldError createFieldErrorMock(String fieldName, String message) {

        FieldError fieldError = mock(FieldError.class);

        when(fieldError.getField()).thenReturn(fieldName);
        when(fieldError.getDefaultMessage()).thenReturn(message);

        return fieldError;
    }

    private Errors createErrorsMockForFieldErrorWithBindingFailure() {

        Errors errors = createErrorsMockForFieldError();

        FieldError fieldError = errors.getFieldError();
        when(fieldError.isBindingFailure()).thenReturn(true);

        return errors;
    }

    private Errors createErrorsMockForObjectError() {

        Errors errors = mock(Errors.class);

        ObjectError objectError = mock(ObjectError.class);

        when(objectError.getDefaultMessage()).thenReturn("object error default message");

        List<ObjectError> errorList = new ArrayList<>();
        when(errors.getAllErrors()).thenReturn(errorList);

        errorList.add(objectError);

        return errors;
    }

    private Errors createErrorsMockForMoreThanOneFieldError() {

        Errors errors = mock(Errors.class);

        FieldError fieldError1 = createFieldErrorMock("field1", "validation error 1");
        FieldError fieldError2 = createFieldErrorMock("field2", "validation error 2");

        List<ObjectError> errorList = new ArrayList<>();
        when(errors.getAllErrors()).thenReturn(errorList);

        errorList.add(fieldError1);
        errorList.add(fieldError2);

        return errors;
    }

}
