package org.iata.bsplink.refund.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.iata.bsplink.refund.dto.Agent;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.service.AgentService;
import org.iata.bsplink.refund.utils.AgentCodeUtility;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class AgentValidatorTest {
    private AgentValidator validator;
    private AgentService agentService;
    private Errors errors;
    private Refund refund;
    private Agent agent;
    private String agentNotFound = "00000070";

    @Before
    public void setUp() {
        agent = new Agent();
        agent.setIataCode("78200102");
        agent.setIsoCountryCode("ES");
        refund = new Refund();
        refund.setIsoCountryCode(agent.getIsoCountryCode());
        refund.setAgentCode(agent.getIataCode());
        agentService = mock(AgentService.class);
        when(agentService.findAgent(refund.getAgentCode())).thenReturn(agent);
        when(agentService.findAgent(agentNotFound)).thenReturn(null);
        errors = new BeanPropertyBindingResult(refund, "refund");
        validator = new AgentValidator(agentService);
    }

    @Test
    public void testIsValid() {
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsNotValid() {
        refund.setIsoCountryCode("ABC");
        validator.validate(refund, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getErrorCount());
        assertEquals(AgentValidator.INCORRECT_COUNTRY_MSG,
                errors.getAllErrors().get(0).getDefaultMessage());
    }

    @Test
    public void testIsNotValidCheckDigit() {
        refund.setAgentCode("12345671");;
        validator.validate(refund, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getErrorCount());
        assertEquals(AgentCodeUtility.INCORRECT_CHECK_DIGIT_MSG,
                errors.getAllErrors().get(0).getDefaultMessage());
    }

    @Test
    public void testIsValidAgentNotFound() {
        refund.setIsoCountryCode("ABC");
        refund.setAgentCode(agentNotFound);
        validator.validate(refund, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getErrorCount());
        assertEquals(AgentValidator.NOT_FOUND_MSG,
                errors.getAllErrors().get(0).getDefaultMessage());
    }

    @Test
    public void testIsValidNullAgent() {
        refund.setIsoCountryCode("ABC");
        refund.setAgentCode(null);
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsValidNullCountry() {
        refund.setIsoCountryCode(null);
        validator.validate(refund, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testSupports() {
        assertTrue(validator.supports(refund.getClass()));
        assertFalse(validator.supports(Object.class));
    }
}
