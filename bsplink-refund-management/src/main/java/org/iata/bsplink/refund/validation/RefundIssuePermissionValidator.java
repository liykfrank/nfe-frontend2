package org.iata.bsplink.refund.validation;

import org.iata.bsplink.refund.dto.Agent;
import org.iata.bsplink.refund.dto.Airline;
import org.iata.bsplink.refund.exception.CustomValidationException;
import org.iata.bsplink.refund.model.entity.RefundIssuePermission;
import org.iata.bsplink.refund.service.AgentService;
import org.iata.bsplink.refund.service.AirlineService;
import org.iata.bsplink.refund.utils.AgentCodeUtility;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RefundIssuePermissionValidator implements Validator {

    public static final String AGENT_NOT_FOUND_MSG = "The agent does not exist.";
    public static final String AIRLINE_NOT_FOUND_MSG = "The airline does not exist in the country.";
    public static final String AGENT_INCORRECT_COUNTRY_MSG
        = "Agent's Country does not match ISO Country Code.";

    private AgentService agentService;
    private AirlineService airlineService;

    /**
     * Creates instance for RefundIssuePermissionValidator.
     */
    public RefundIssuePermissionValidator(AgentService agentService,
            AirlineService airlineService) {
        this.agentService = agentService;
        this.airlineService = airlineService;
    }

    @Override
    public void validate(Object target, Errors errors) {
        RefundIssuePermission permission = (RefundIssuePermission) target;

        validateAgent(permission, errors);

        validateAirline(permission, errors);
    }

    private void validateAgent(RefundIssuePermission permission, Errors errors) {

        String agentCode = permission.getAgentCode();

        if (agentCode == null) {
            return;
        }

        String property = "agentCode";

        try {
            AgentCodeUtility.validateAgentCode(agentCode);
        } catch (CustomValidationException e) {
            errors.rejectValue(property, e.getErrorCode(), e.getMessage());
            return;
        }

        Agent agent = agentService.findAgent(agentCode);

        if (agent == null) {
            errors.rejectValue(property, "field.not_found", AGENT_NOT_FOUND_MSG);
            return;
        }

        if (permission.getIsoCountryCode() != null
                && !permission.getIsoCountryCode().equals(agent.getIsoCountryCode())) {
            errors.rejectValue(property, "field.incorrect_country", AGENT_INCORRECT_COUNTRY_MSG);
        }
    }

    private void validateAirline(RefundIssuePermission permission, Errors errors) {
        String airlineCode = permission.getAirlineCode();

        if (airlineCode == null) {
            return;
        }
        if (permission.getIsoCountryCode() == null) {
            return;
        }

        Airline airline = airlineService.findAirline(permission.getIsoCountryCode(), airlineCode);

        if (airline == null) {
            errors.rejectValue("airlineCode", "field.not_found", AIRLINE_NOT_FOUND_MSG);
        }
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return RefundIssuePermission.class.isAssignableFrom(clazz);
    }
}
