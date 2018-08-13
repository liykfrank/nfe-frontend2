package org.iata.bsplink.user.validation;

import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
import org.iata.bsplink.user.model.entity.User;
import org.iata.bsplink.user.model.entity.UserType;
import org.iata.bsplink.user.pojo.Agent;
import org.iata.bsplink.user.service.AgentService;
import org.iata.bsplink.user.utils.AgentCodeUtility;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AgentValidator implements Validator {

    private static final String USER_CODE = "userCode";
    private static final String NOT_FOUND_MSG = "The agent does not exist.";

    private AgentService agentService;

    public AgentValidator(AgentService agentService) {
        this.agentService = agentService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if (user.getUserType().equals(UserType.AGENT)) {

            String userCode = user.getUserCode();

            AgentCodeUtility.validateAgentCode(userCode, errors);

            if (errors.hasErrors()) {
                throw new ApplicationValidationException(errors);
            }

            validAgent(user, errors);

        }
    }

    private void validAgent(User user, Errors errors) {
        Agent agent = agentService.findAgent(user.getUserCode());

        if (agent == null) {
            errors.rejectValue(USER_CODE, "field.not_found", NOT_FOUND_MSG);
        }
    }
}
