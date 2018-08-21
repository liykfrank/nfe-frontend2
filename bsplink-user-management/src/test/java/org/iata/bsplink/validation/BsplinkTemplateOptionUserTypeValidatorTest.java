package org.iata.bsplink.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.iata.bsplink.user.model.entity.BsplinkOption;
import org.iata.bsplink.user.model.entity.BsplinkTemplate;
import org.iata.bsplink.user.model.entity.UserType;
import org.iata.bsplink.user.service.BsplinkOptionService;
import org.iata.bsplink.user.validation.BsplinkTemplateOptionUserTypeValidator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class BsplinkTemplateOptionUserTypeValidatorTest {

    private BsplinkTemplateOptionUserTypeValidator validator;
    private BsplinkOptionService optionService;
    private BsplinkOption optionForAgentAirlineBsp;
    private BsplinkOption optionForAgentAirline;
    private BsplinkTemplate template;
    private String optionIdNotFound;

    @Before
    public void setUp() {

        template = new BsplinkTemplate();
        optionForAgentAirlineBsp = new BsplinkOption();
        optionForAgentAirlineBsp.setId("optionForAgentAirlineBsp");
        optionForAgentAirlineBsp.setUserTypes(
                Arrays.asList(UserType.AGENT, UserType.AIRLINE, UserType.BSP));
        optionForAgentAirline = new BsplinkOption();
        optionForAgentAirline.setId("optionForAgentAirline");
        optionForAgentAirline.setUserTypes(Arrays.asList(UserType.AGENT, UserType.AIRLINE));
        optionIdNotFound = "OPTION_NOT_FOUND";

        optionService = mock(BsplinkOptionService.class);
        when(optionService.findById(optionForAgentAirlineBsp.getId()))
                .thenReturn(Optional.of(optionForAgentAirlineBsp));
        when(optionService.findById(optionForAgentAirline.getId()))
                .thenReturn(Optional.of(optionForAgentAirline));
        when(optionService.findById(optionIdNotFound))
                .thenReturn(Optional.empty());
        validator = new BsplinkTemplateOptionUserTypeValidator(optionService);
    }

    @Test
    public void testValidateTemplateWithNullOptions() {

        template.setOptions(null);

        Errors errors = new BeanPropertyBindingResult(template, "template");

        validator.validate(template, errors);

        assertFalse(errors.hasErrors());
    }


    @Test
    public void testValidateTemplateWithoutOptions() {

        template.setOptions(null);

        Errors errors = new BeanPropertyBindingResult(template, "template");

        validator.validate(template, errors);

        assertFalse(errors.hasErrors());
    }


    @Test
    public void testValidateTemplateWithCorrectOptions() {

        template.setUserTypes(Arrays.asList(UserType.AGENT, UserType.AIRLINE));
        template.setOptions(Arrays.asList(optionForAgentAirlineBsp, optionForAgentAirline));

        Errors errors = new BeanPropertyBindingResult(template, "template");

        validator.validate(template, errors);

        assertFalse(errors.hasErrors());
    }


    @Test
    public void testValidateTemplateWithOptionnotFound() {

        BsplinkOption optionNoFound = new BsplinkOption();
        optionNoFound.setId(optionIdNotFound);
        optionNoFound.setUserTypes(Arrays.asList(UserType.AGENT, UserType.AIRLINE));

        template.setUserTypes(Arrays.asList(UserType.AGENT, UserType.AIRLINE));
        template.setOptions(Arrays.asList(optionForAgentAirlineBsp, optionNoFound,
                optionForAgentAirline));

        Errors errors = new BeanPropertyBindingResult(template, "template");

        validator.validate(template, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors());
        assertThat(errors.getErrorCount(), equalTo(1));
        assertThat(errors.getFieldError().getField(), equalTo("options[1]"));
        assertThat(errors.getFieldError().getDefaultMessage(),
                equalTo(BsplinkTemplateOptionUserTypeValidator.OPTION_NOT_FOUND_MSG));
    }


    @Test
    public void testValidateTemplateWithOptionNotValid() {

        template.setUserTypes(Arrays.asList(UserType.AGENT, UserType.AIRLINE, UserType.BSP));
        template.setOptions(Arrays.asList(optionForAgentAirlineBsp, optionForAgentAirline));

        Errors errors = new BeanPropertyBindingResult(template, "template");

        validator.validate(template, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors());
        assertThat(errors.getErrorCount(), equalTo(1));
        assertThat(errors.getFieldError().getField(), equalTo("options[1]"));
        assertThat(errors.getFieldError().getDefaultMessage(),
                equalTo(BsplinkTemplateOptionUserTypeValidator.INVALID_OPTION_FOR_USER_TYPE_MSG));
    }


    @Test
    public void testValidateTemplateAndOption() {

        template.setUserTypes(Arrays.asList(UserType.AGENT, UserType.AIRLINE));
        template.setOptions(Arrays.asList(optionForAgentAirline));

        Errors errors = new BeanPropertyBindingResult(optionForAgentAirlineBsp,
                "optionForAgentAirlineBsp");

        validator.validate(template, optionForAgentAirlineBsp, errors);

        assertFalse(errors.hasErrors());
    }


    @Test
    public void testValidateTemplateAndInvalidOption() {

        template.setUserTypes(Arrays.asList(UserType.AGENT, UserType.AIRLINE, UserType.BSP));
        template.setOptions(Arrays.asList(optionForAgentAirlineBsp));
        Errors errors = new BeanPropertyBindingResult(optionForAgentAirline,
                "optionForAgentAirline");
        validator.validate(template, optionForAgentAirline, errors);
        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors());
        assertThat(errors.getErrorCount(), equalTo(1));
        assertThat(errors.getFieldError().getField(), equalTo("userTypes"));
        assertThat(errors.getFieldError().getDefaultMessage(),
                equalTo(BsplinkTemplateOptionUserTypeValidator.INVALID_OPTION_FOR_USER_TYPE_MSG));
    }


    @Test
    public void testValidateTemplateWithoutUsertTypes() {

        template.setOptions(Arrays.asList(optionForAgentAirlineBsp));

        Errors errors = new BeanPropertyBindingResult(optionForAgentAirline,
                "optionForAgentAirline");

        validator.validate(template, optionForAgentAirline, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors());
        assertThat(errors.getErrorCount(), equalTo(1));
        assertThat(errors.getFieldError().getField(), equalTo("userTypes"));
        assertThat(errors.getFieldError().getDefaultMessage(),
                equalTo(BsplinkTemplateOptionUserTypeValidator.INVALID_OPTION_FOR_USER_TYPE_MSG));
    }


    @Test
    public void testValidateTemplateAndUserType() {

        template.setUserTypes(Arrays.asList(UserType.AGENT));
        template.setOptions(Arrays.asList(optionForAgentAirlineBsp));
        UserType userType = UserType.BSP;
        Errors errors = new BeanPropertyBindingResult(userType, "userType");
        validator.validate(template, userType, errors);
        assertFalse(errors.hasErrors());
    }


    @Test
    public void testValidateTemplateWithNullOptionsAndUserType() {

        template.setUserTypes(Arrays.asList(UserType.AGENT));
        template.setOptions(null);
        UserType userType = UserType.BSP;
        Errors errors = new BeanPropertyBindingResult(userType, "userType");
        validator.validate(template, userType, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testValidateTemplateWithoutOptionsAndUserType() {

        template.setUserTypes(Arrays.asList(UserType.AGENT));
        UserType userType = UserType.BSP;
        Errors errors = new BeanPropertyBindingResult(userType, "userType");
        validator.validate(template, userType, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testValidateTemplateAndInvalidUserType() {

        template.setUserTypes(Arrays.asList(UserType.AGENT, UserType.AIRLINE, UserType.BSP));
        template.setOptions(Arrays.asList(optionForAgentAirlineBsp));
        UserType userType = UserType.GDS;
        Errors errors = new BeanPropertyBindingResult(userType, "userType");
        validator.validate(template, userType, errors);
        assertTrue(errors.hasErrors());
        assertTrue(errors.hasGlobalErrors());
        assertThat(errors.getErrorCount(), equalTo(1));
        assertThat(errors.getGlobalError().getDefaultMessage(),
                equalTo(BsplinkTemplateOptionUserTypeValidator.INVALID_USER_TYPE_MSG));
    }
}
