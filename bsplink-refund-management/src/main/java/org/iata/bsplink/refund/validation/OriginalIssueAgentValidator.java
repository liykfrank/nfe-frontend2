package org.iata.bsplink.refund.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.iata.bsplink.refund.exception.CustomValidationException;
import org.iata.bsplink.refund.utils.AgentCodeUtility;
import org.iata.bsplink.refund.validation.constraints.OriginalIssueAgentConstraint;
import org.springframework.stereotype.Component;

@Component
public class OriginalIssueAgentValidator
        implements ConstraintValidator<OriginalIssueAgentConstraint, String> {

    @Override
    public boolean isValid(String agentCode, ConstraintValidatorContext context) {
        if (agentCode == null) {
            return true;
        }
        try {
            AgentCodeUtility.validateAgentCode(agentCode);
        } catch (CustomValidationException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                    .addPropertyNode("agentCode")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
