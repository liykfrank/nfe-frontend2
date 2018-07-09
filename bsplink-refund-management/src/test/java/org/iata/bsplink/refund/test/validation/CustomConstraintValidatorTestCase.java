package org.iata.bsplink.refund.test.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;

import org.junit.Before;
import org.mockito.ArgumentCaptor;

public abstract class CustomConstraintValidatorTestCase {

    protected ConstraintValidatorContext context;

    private ConstraintViolationBuilder builder;
    private NodeBuilderCustomizableContext nodeBuilderContext;

    @Before
    public void setUp() {

        prepareContextValidatorMock();
    }

    private void prepareContextValidatorMock() {

        nodeBuilderContext = mock(NodeBuilderCustomizableContext.class);

        builder = mock(ConstraintViolationBuilder.class);

        when(builder.addPropertyNode(anyString())).thenReturn(nodeBuilderContext);

        context = mock(ConstraintValidatorContext.class);

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
    }

    protected void verifyConstraintViolation(String message) {
        verifyConstraintViolation(null, message);
    }

    protected void verifyConstraintViolation(String propertyName, String message) {

        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);

        verify(context).buildConstraintViolationWithTemplate(argument.capture());
        assertThat(argument.getValue(), equalTo(message));

        if (propertyName != null) {
            verify(builder).addPropertyNode(argument.capture());
            assertThat(argument.getValue(), equalTo(propertyName));
            verify(nodeBuilderContext).addConstraintViolation();
        } else {
            verify(builder).addConstraintViolation();
        }


    }

}
