package org.iata.bsplink.filemanager.validation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class ErrorsConverterTest {

    @Before
    public void setUp() throws Exception {}

    @Test
    public void testToStringValidationError() {
        ErrorsConverter converter = new ErrorsConverter(createErrorsMockForFieldError());
        assertEquals("validation error", converter.toString());
    }

    @Test
    public void testToStringBindingFailure() {
        ErrorsConverter converter =
                new ErrorsConverter(createErrorsMockForFieldErrorWithBindingFailure());
        assertEquals("field1 has an invalid value", converter.toString());
    }

    @Test
    public void testToStringObjectError() {
        ErrorsConverter converter = new ErrorsConverter(createErrorsMockForObjectError());
        assertEquals("object error default message", converter.toString());
    }

    @Test
    public void testToStringMultipleFieldErrors() {
        ErrorsConverter converter = new ErrorsConverter(createErrorsMockForMoreThanOneFieldError());
        assertEquals("validation error 1, validation error 2", converter.toString());
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
