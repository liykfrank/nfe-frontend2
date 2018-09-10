package org.iata.bsplink.user.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.iata.bsplink.user.validation.CountryValidationMessages.ISOC_DUPLICATED_MESSAGE;
import static org.iata.bsplink.user.validation.CountryValidationMessages.ISOC_EMPTY_MESSAGE;
import static org.iata.bsplink.user.validation.CountryValidationMessages.ISOC_FORMAT_MESSAGE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.iata.bsplink.user.model.entity.BsplinkTemplate;
import org.iata.bsplink.user.model.entity.User;
import org.iata.bsplink.user.model.entity.UserTemplate;
import org.iata.bsplink.user.model.entity.UserType;
import org.iata.bsplink.user.pojo.Agent;
import org.iata.bsplink.user.pojo.Airline;
import org.iata.bsplink.user.service.AgentService;
import org.iata.bsplink.user.service.AirlineService;
import org.iata.bsplink.user.service.BsplinkTemplateService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class UserTemplateValidatorTest {

    private UserTemplateValidator validator;
    private BsplinkTemplateService bsplinkTemplateService;
    private AgentService agentService;
    private AirlineService airlineService;
    private Errors errors;
    private User user;
    private UserTemplate userTemplate;
    private BsplinkTemplate bsplinkTemplate;
    private final String notFoundBsplinkTemplate = "notFound";


    @Before
    public void setUp() {

        user = new User();
        user.setTemplates(new ArrayList<>());
        userTemplate = new UserTemplate();
        bsplinkTemplate = new BsplinkTemplate();
        bsplinkTemplate.setId("idBsplinkTemplate");
        bsplinkTemplate.setUserTypes(Arrays.asList(UserType.AIRLINE, UserType.AGENT,
                UserType.BSP));

        airlineService = mock(AirlineService.class);
        agentService = mock(AgentService.class);

        bsplinkTemplateService = mock(BsplinkTemplateService.class);
        when(bsplinkTemplateService.findById(bsplinkTemplate.getId()))
                .thenReturn(Optional.of(bsplinkTemplate));
        when(bsplinkTemplateService.findById(notFoundBsplinkTemplate))
                .thenReturn(Optional.empty());

        errors = new BeanPropertyBindingResult(user, "user");
        validator = new UserTemplateValidator(bsplinkTemplateService, airlineService,
                agentService);
    }


    @Test
    public void testSupports() {

        assertTrue(validator.supports(User.class));
        assertFalse(validator.supports(Object.class));
    }


    @Test
    public void testTemplatesNull() {

        user.setTemplates(null);
        validator.validate(user, errors);

        assertFalse(errors.hasErrors());
    }


    @Test
    public void testTemplatesEmpty() {

        validator.validate(user, errors);

        assertFalse(errors.hasErrors());
    }


    @Test
    public void testTemplatesTemplateNull() {

        user.getTemplates().add(null);
        validator.validate(user, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors());
        assertThat(errors.getFieldError().getDefaultMessage(),
                equalTo(UserTemplateValidator.TEMPLATE_NULL_MESSAGE));
    }


    @Test
    public void testTemplatesUserTypeNull() {

        user.getTemplates().add(userTemplate);
        validator.validate(user, errors);

        assertFalse(errors.hasErrors());
    }


    @Test
    public void testTemplatesUserType() {

        user.setUserType(UserType.BSP);

        userTemplate.setTemplate(bsplinkTemplate.getId());

        user.getTemplates().add(userTemplate);
        validator.validate(user, errors);

        assertFalse(errors.hasErrors());
    }


    @Test
    public void testTemplatesNullTemplate() {

        user.setUserType(UserType.BSP);

        userTemplate.setTemplate(null);

        user.getTemplates().add(userTemplate);
        validator.validate(user, errors);

        assertFalse(errors.hasErrors());
    }


    @Test
    public void testTemplatesIncorrectUserType() {

        user.setUserType(UserType.DPC);

        userTemplate.setTemplate(bsplinkTemplate.getId());

        user.getTemplates().add(userTemplate);
        validator.validate(user, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors());
        assertThat(errors.getFieldError().getDefaultMessage(),
                equalTo(UserTemplateValidator
                        .TEMPLATE_NOT_AVAILABLE_FOR_USERTYPE_MESSAGE));
    }


    @Test
    public void testTemplatesTemplateNotFound() {

        user.setUserType(UserType.DPC);

        userTemplate.setTemplate(notFoundBsplinkTemplate);

        user.getTemplates().add(userTemplate);
        validator.validate(user, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors());
        assertThat(errors.getFieldError().getDefaultMessage(),
                equalTo(UserTemplateValidator
                        .TEMPLATE_NOT_FOUND_MESSAGE));
    }


    @Test
    public void testTemplatesTemplateDuplicated() {

        user.setUserType(UserType.BSP);

        userTemplate.setTemplate(bsplinkTemplate.getId());

        user.getTemplates().add(userTemplate);
        user.getTemplates().add(userTemplate);

        validator.validate(user, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors());
        assertThat(errors.getFieldError().getDefaultMessage(),
                equalTo(UserTemplateValidator
                        .TEMPLATE_DUPLICATED_MESSAGE));
    }


    @Test
    public void testTemplatesIncorrectIsoCountryCodeFormat() {

        user.setUserType(UserType.BSP);

        userTemplate.setTemplate(bsplinkTemplate.getId());
        userTemplate.setIsoCountryCodes(Arrays.asList("ABC"));

        user.getTemplates().add(userTemplate);

        validator.validate(user, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors());
        assertThat(errors.getFieldError().getDefaultMessage(),
                equalTo(ISOC_FORMAT_MESSAGE));
    }


    @Test
    public void testTemplatesNullIsoCountryCode() {

        user.setUserType(UserType.BSP);

        userTemplate.setTemplate(bsplinkTemplate.getId());
        userTemplate.setIsoCountryCodes(new ArrayList<>());
        userTemplate.getIsoCountryCodes().add(null);

        user.getTemplates().add(userTemplate);

        validator.validate(user, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors());
        assertThat(errors.getFieldError().getDefaultMessage(),
                equalTo(ISOC_EMPTY_MESSAGE));
    }


    @Test
    public void testTemplatesDuplicatedIsoCountryCode() {

        user.setUserType(UserType.BSP);

        userTemplate.setTemplate(bsplinkTemplate.getId());
        userTemplate.setIsoCountryCodes(Arrays.asList("BZ", "BZ"));

        user.getTemplates().add(userTemplate);

        validator.validate(user, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors());
        assertThat(errors.getFieldError().getDefaultMessage(),
                equalTo(ISOC_DUPLICATED_MESSAGE));
    }


    @Test
    public void testTemplatesAirlineUser() {
        String airlineCode = "123";
        String isoc = "BZ";

        when(airlineService.findAirline(isoc, airlineCode)).thenReturn(new Airline());

        user.setUserType(UserType.AIRLINE);
        user.setUserCode(airlineCode);
        userTemplate.setTemplate(bsplinkTemplate.getId());
        userTemplate.setIsoCountryCodes(Arrays.asList(isoc));

        user.getTemplates().add(userTemplate);
        validator.validate(user, errors);

        assertFalse(errors.hasErrors());
    }


    @Test
    public void testTemplatesNullAirlineUser() {

        user.setUserType(UserType.AIRLINE);
        String airlineCode = null;
        user.setUserCode(airlineCode);
        userTemplate.setTemplate(bsplinkTemplate.getId());
        String isoc = "BZ";
        userTemplate.setIsoCountryCodes(Arrays.asList(isoc));

        user.getTemplates().add(userTemplate);
        validator.validate(user, errors);

        assertFalse(errors.hasErrors());
    }


    @Test
    public void testTemplatesAirlineUserNotFound() {
        String airlineCode = "123";
        String isoc = "BZ";

        when(airlineService.findAirline(isoc, airlineCode)).thenReturn(null);

        user.setUserType(UserType.AIRLINE);
        user.setUserCode(airlineCode);
        userTemplate.setTemplate(bsplinkTemplate.getId());
        userTemplate.setIsoCountryCodes(Arrays.asList(isoc));

        user.getTemplates().add(userTemplate);
        validator.validate(user, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors());
        assertThat(errors.getFieldError().getDefaultMessage(),
                equalTo(UserTemplateValidator
                        .ISOC_AIRLINE_MESSAGE));
    }


    @Test
    public void testTemplatesAgentUser() {
        String agentCode = "78200102";
        String isoc = "BZ";

        Agent agent = new Agent();
        agent.setIsoCountryCode(isoc);

        when(agentService.findAgent(agentCode)).thenReturn(agent);

        user.setUserType(UserType.AGENT);
        user.setUserCode(agentCode);
        userTemplate.setTemplate(bsplinkTemplate.getId());
        userTemplate.setIsoCountryCodes(Arrays.asList(isoc));

        user.getTemplates().add(userTemplate);
        validator.validate(user, errors);

        assertFalse(errors.hasErrors());
    }


    @Test
    public void testTemplatesNullAgentUser() {

        user.setUserType(UserType.AGENT);
        String agentCode = null;
        user.setUserCode(agentCode);
        userTemplate.setTemplate(bsplinkTemplate.getId());
        userTemplate.setIsoCountryCodes(Arrays.asList("BZ"));

        user.getTemplates().add(userTemplate);
        validator.validate(user, errors);

        assertFalse(errors.hasErrors());
    }


    @Test
    public void testTemplatesAgentUserNotFound() {
        String agentCode = "78200102";

        when(agentService.findAgent(agentCode)).thenReturn(null);

        user.setUserType(UserType.AGENT);
        user.setUserCode(agentCode);
        userTemplate.setTemplate(bsplinkTemplate.getId());
        userTemplate.setIsoCountryCodes(Arrays.asList("BZ"));

        user.getTemplates().add(userTemplate);
        validator.validate(user, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors());
        assertThat(errors.getFieldError().getDefaultMessage(),
                equalTo(UserTemplateValidator
                        .AGENT_NOT_FOUND_MESSAGE));
    }


    @Test
    public void testTemplatesAgentUserIncorrectIsoc() {
        String agentCode = "78200102";

        Agent agent = new Agent();
        agent.setIsoCountryCode("ZB");

        when(agentService.findAgent(agentCode)).thenReturn(agent);

        user.setUserType(UserType.AGENT);
        user.setUserCode(agentCode);
        userTemplate.setTemplate(bsplinkTemplate.getId());
        userTemplate.setIsoCountryCodes(Arrays.asList("BZ"));

        user.getTemplates().add(userTemplate);
        validator.validate(user, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors());
        assertThat(errors.getFieldError().getDefaultMessage(),
                equalTo(UserTemplateValidator
                        .ISOC_AGENT_MESSAGE));
    }
}
