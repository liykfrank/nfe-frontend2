package org.iata.bsplink.agencymemo.validation;

import static org.iata.bsplink.agencymemo.test.fixtures.AgentFixtures.getAgents;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.dto.Agent;
import org.iata.bsplink.agencymemo.service.AgentService;
import org.iata.bsplink.agencymemo.test.validation.CustomConstraintValidatorTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class AgentValidatorTest extends CustomConstraintValidatorTestCase {
    private final String agentCodeNotExists = "00000070";
    private final String agentCodeIncorrectCountry = "00000081";
    private final String agentCodeExpired = "00000092";

    public AgentService agentService;

    private AgentValidator agentValidator;
    private AcdmRequest acdm;

    @Override
    @Before
    public void setUp() {
        super.setUp();

        Agent agent = getAgents().get(0);
        agentService = mock(AgentService.class);
        when(agentService.findAgent(agent.getIataCode())).thenReturn(agent);

        when(agentService.findAgent(agentCodeNotExists)).thenReturn(null);

        Agent agent2 = getAgents().get(1);
        if (agent.getIsoCountryCode().equals("ZZ")) {
            agent2.setIsoCountryCode("AA");
        } else {
            agent2.setIsoCountryCode("ZZ");
        }
        agent2.setIataCode(agentCodeIncorrectCountry);
        when(agentService.findAgent(agentCodeIncorrectCountry)).thenReturn(agent2);

        Agent agent3 = getAgents().get(0);
        agent3.setDefaultDate(LocalDate.of(2018, 5, 11));
        agent3.setIataCode(agentCodeExpired);
        when(agentService.findAgent(agentCodeExpired)).thenReturn(agent3);

        acdm = new AcdmRequest();
        acdm.setIsoCountryCode(agent.getIsoCountryCode());
        acdm.setAgentCode(agent.getIataCode());
        acdm.setDateOfIssue(LocalDate.of(2018, 5, 12));
        agent.setDefaultDate(null);

        agentValidator = new AgentValidator(agentService);
    }


    @Test
    public void testIsValid() {
        assertTrue(agentValidator.isValid(acdm, context));
    }

    @Test
    public void testIsNullAgent() {
        acdm.setAgentCode(null);
        assertTrue(agentValidator.isValid(acdm, context));
    }

    @Test
    @Parameters
    public void testAgentIsNotValid(String agentCode, String message) {
        acdm.setAgentCode(agentCode);
        assertFalse(agentValidator.isValid(acdm, context));
        verifyConstraintViolation("agentCode", message);
    }

    /**
     * Test Parameters.
     */
    public Object[][] parametersForTestAgentIsNotValid() {
        return new Object[][] {
            { "782001XX", ValidationMessages.INCORRECT_FORMAT },
            { "78200108", AgentValidator.INVALID_CHECK_DIGIT_MSG },
            { "78200103", AgentValidator.INCORRECT_CHECK_DIGIT_MSG },
            { agentCodeNotExists, AgentValidator.NOT_FOUND_MSG },
            { agentCodeIncorrectCountry, AgentValidator.INCORRECT_COUNTRY_MSG },
            { agentCodeExpired, AgentValidator.EXPIRED_MSG }
        };
    }
}
