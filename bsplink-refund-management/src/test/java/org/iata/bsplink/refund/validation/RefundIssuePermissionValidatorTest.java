package org.iata.bsplink.refund.validation;

import static org.iata.bsplink.refund.test.fixtures.AgentFixtures.getAgents;
import static org.iata.bsplink.refund.test.fixtures.AirlineFixtures.getAirlines;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.iata.bsplink.refund.dto.Agent;
import org.iata.bsplink.refund.dto.Airline;
import org.iata.bsplink.refund.model.entity.RefundIssuePermission;
import org.iata.bsplink.refund.service.AgentService;
import org.iata.bsplink.refund.service.AirlineService;
import org.iata.bsplink.refund.utils.AgentCodeUtility;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;

public class RefundIssuePermissionValidatorTest  {

    private RefundIssuePermissionValidator validator;

    private AgentService agentService;
    private AirlineService airlineService;

    private Agent agent1;
    private Agent agent2;
    private Airline airline1;
    private Airline airline2;
    private String agentNotFound = "88888881";
    private RefundIssuePermission refundIssuePermission;
    private BeanPropertyBindingResult errors;

    @Before
    public void setUp() {
        refundIssuePermission = new RefundIssuePermission();
        errors = new BeanPropertyBindingResult(refundIssuePermission, "refundIssuePermission");

        agent1 = getAgents().get(0);
        airline1 = getAirlines().get(0);
        airline1.setIsoCountryCode(agent1.getIsoCountryCode());

        agent2 = getAgents().get(1);
        agent2.setIsoCountryCode("XX");
        airline2 = getAirlines().get(1);
        airline2.setIsoCountryCode("XX");

        agentService = mock(AgentService.class);
        when(agentService.findAgent(agent1.getIataCode())).thenReturn(agent1);
        when(agentService.findAgent(agent2.getIataCode())).thenReturn(agent2);
        when(agentService.findAgent(agentNotFound)).thenReturn(null);


        airlineService = mock(AirlineService.class);
        when(airlineService.findAirline(airline1.getIsoCountryCode(), airline1.getAirlineCode()))
                .thenReturn(airline1);
        when(airlineService.findAirline(airline2.getIsoCountryCode(), airline2.getAirlineCode()))
                .thenReturn(null);

        validator = new RefundIssuePermissionValidator(agentService, airlineService);
    }


    @Test
    public void testSupports() {
        assertTrue(validator.supports(RefundIssuePermission.class));
        assertFalse(validator.supports(Object.class));
    }

    @Test
    public void testIsValid() {
        RefundIssuePermission permission = new RefundIssuePermission();
        permission.setIsoCountryCode(agent1.getIsoCountryCode());
        permission.setAgentCode(agent1.getIataCode());
        permission.setAirlineCode(airline1.getAirlineCode());
        validator.validate(permission, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsValidAgentNull() {
        RefundIssuePermission permission = new RefundIssuePermission();
        permission.setIsoCountryCode(agent1.getIsoCountryCode());
        permission.setAgentCode(null);
        permission.setAirlineCode(airline1.getAirlineCode());
        validator.validate(permission, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsValidAirlineNull() {
        RefundIssuePermission permission = new RefundIssuePermission();
        permission.setIsoCountryCode(agent1.getIsoCountryCode());
        permission.setAgentCode(agent1.getIataCode());
        permission.setAirlineCode(null);
        validator.validate(permission, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsValidIsocNull() {
        RefundIssuePermission permission = new RefundIssuePermission();
        permission.setIsoCountryCode(null);
        permission.setAgentCode(agent1.getIataCode());
        validator.validate(permission, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testIsNotValidAirlineNotFound() {
        RefundIssuePermission permission = new RefundIssuePermission();
        permission.setIsoCountryCode(airline2.getIsoCountryCode());
        permission.setAgentCode(agent2.getIataCode());
        permission.setAirlineCode(airline2.getAirlineCode());
        validator.validate(permission, errors);
        assertTrue(errors.hasErrors());
        assertEquals(RefundIssuePermissionValidator.AIRLINE_NOT_FOUND_MSG,
                errors.getAllErrors().get(0).getDefaultMessage());
    }

    @Test
    public void testIsNotValidAgentDigitIncorrect() {
        RefundIssuePermission permission = new RefundIssuePermission();
        permission.setIsoCountryCode(airline1.getIsoCountryCode());
        permission.setAgentCode("78200103");
        permission.setAirlineCode(airline1.getAirlineCode());
        validator.validate(permission, errors);
        assertTrue(errors.hasErrors());
        assertEquals(AgentCodeUtility.INCORRECT_CHECK_DIGIT_MSG,
                errors.getAllErrors().get(0).getDefaultMessage());
    }

    @Test
    public void testIsNotValidAgentNotFound() {
        RefundIssuePermission permission = new RefundIssuePermission();
        permission.setIsoCountryCode(airline1.getIsoCountryCode());
        permission.setAgentCode(agentNotFound);
        permission.setAirlineCode(airline1.getAirlineCode());
        validator.validate(permission, errors);
        assertTrue(errors.hasErrors());
        assertEquals(RefundIssuePermissionValidator.AGENT_NOT_FOUND_MSG,
                errors.getAllErrors().get(0).getDefaultMessage());
    }

    @Test
    public void testIsNotValidAgentIncorrectCountry() {
        RefundIssuePermission permission = new RefundIssuePermission();
        permission.setIsoCountryCode(airline1.getIsoCountryCode());
        permission.setAgentCode(agent2.getIataCode());
        permission.setAirlineCode(airline1.getAirlineCode());
        validator.validate(permission, errors);
        assertTrue(errors.hasErrors());
        assertEquals(RefundIssuePermissionValidator.AGENT_INCORRECT_COUNTRY_MSG,
                errors.getAllErrors().get(0).getDefaultMessage());
    }
}
