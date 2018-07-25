package org.iata.bsplink.agencymemo.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.dto.Agent;
import org.iata.bsplink.agencymemo.service.AgentService;
import org.iata.bsplink.agencymemo.validation.constraints.AgentConstraint;


public class AgentValidator implements ConstraintValidator<AgentConstraint, AcdmRequest> {

    public static final String INCORRECT_CHECK_DIGIT_MSG = "Incorrect Check-Digit";
    public static final String NOT_FOUND_MSG = "The agent does not exist.";
    public static final String EXPIRED_MSG = "Agent is in default.";
    public static final String INVALID_CHECK_DIGIT_MSG
        = "Incorrect Format. Expected: 7 digit agent code + Check-Digit";
    public static final String INCORRECT_COUNTRY_MSG
        = "Agent's Country does not match Agency Memo ISO Country Code.";

    private static String property = "agentCode";

    private AgentService agentService;

    public AgentValidator(AgentService agentService) {
        this.agentService = agentService;
    }

    @Override
    public boolean isValid(AcdmRequest acdm, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        String agentCode = acdm.getAgentCode();

        if (agentCode == null) {
            return true;
        }

        if (!isValidCheckDigit(agentCode, context)) {
            return false;
        }

        return isValidAgent(acdm, context);
    }


    private boolean isValidCheckDigit(String agentCode, ConstraintValidatorContext context) {

        if (!agentCode.matches("^[0-9]{8}$")) {
            addMessage(context, property, ValidationMessages.INCORRECT_FORMAT);
            return false;
        }

        if (!agentCode.matches(".*[0-6]$")) {
            addMessage(context, property, INVALID_CHECK_DIGIT_MSG);
            return false;
        }

        int code = Integer.parseInt(agentCode.substring(0, 7));
        int checkDigit = Integer.parseInt(agentCode.substring(7));

        if (code % 7 != checkDigit) {
            addMessage(context, property, INCORRECT_CHECK_DIGIT_MSG);
            return false;
        }

        return true;
    }


    private boolean isValidAgent(AcdmRequest acdm, ConstraintValidatorContext context) {

        Agent agent = agentService.findAgent(acdm.getAgentCode());

        if (agent == null) {
            addMessage(context, property, NOT_FOUND_MSG);
            return false;
        }

        if (acdm.getIsoCountryCode() != null
                && !acdm.getIsoCountryCode().equals(agent.getIsoCountryCode())) {
            addMessage(context, property, INCORRECT_COUNTRY_MSG);
            return false;
        }

        if (agent.getDefaultDate() != null && acdm.getDateOfIssue() != null
                && agent.getDefaultDate().isBefore(acdm.getDateOfIssue())) {
            addMessage(context, property, EXPIRED_MSG);
            return false;
        }
        return true;
    }


    private void addMessage(ConstraintValidatorContext context, String property,
            String message) {
        context
            .buildConstraintViolationWithTemplate(message)
            .addPropertyNode(property)
            .addConstraintViolation();
    }
}
