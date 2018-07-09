package org.iata.bsplink.refund.validation;

import org.iata.bsplink.refund.dto.Agent;
import org.iata.bsplink.refund.exception.CustomValidationException;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.service.AgentService;
import org.iata.bsplink.refund.utils.AgentCodeUtility;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AgentValidator implements Validator {

    public static final String NOT_FOUND_MSG = "The agent does not exist.";
    public static final String INCORRECT_COUNTRY_MSG
        = "Agent's Country does not match refund's ISO Country Code.";

    private static final String FIELD = "agentCode";

    private AgentService agentService;

    public AgentValidator(AgentService agentService) {
        this.agentService = agentService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Refund.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Refund refund = (Refund) target;
        String agentCode = refund.getAgentCode();

        if (agentCode == null) {
            return;
        }

        try {
            AgentCodeUtility.validateAgentCode(agentCode);
        } catch (CustomValidationException e) {
            errors.rejectValue(FIELD, e.getErrorCode(), e.getMessage());
            return;
        }

        validAgent(refund, errors);
    }

    private void validAgent(Refund refund, Errors errors) {
        Agent agent = agentService.findAgent(refund.getAgentCode());

        if (agent == null) {
            errors.rejectValue(FIELD, "field.not_found", NOT_FOUND_MSG);

        } else if (refund.getIsoCountryCode() != null

                && !refund.getIsoCountryCode().equals(agent.getIsoCountryCode())) {
            errors.rejectValue(FIELD, "field.incorrect_country", INCORRECT_COUNTRY_MSG);
        }
    }

}
