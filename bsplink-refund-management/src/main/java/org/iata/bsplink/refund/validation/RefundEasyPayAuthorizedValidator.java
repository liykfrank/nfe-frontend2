package org.iata.bsplink.refund.validation;

import java.util.Optional;

import org.iata.bsplink.refund.dto.Agent;
import org.iata.bsplink.refund.dto.FormOfPaymentStatus;
import org.iata.bsplink.refund.model.entity.FormOfPaymentType;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.service.AgentService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class RefundEasyPayAuthorizedValidator implements Validator {

    private static final String ERROR_CODE = "value.not_allowed";

    private AgentService agentService;

    public RefundEasyPayAuthorizedValidator(AgentService agentService) {

        this.agentService = agentService;
    }

    @Override
    public boolean supports(Class<?> clazz) {

        return Refund.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Refund refund = (Refund) target;

        if (refund.getAgentCode() == null) {

            return;
        }

        Optional<Agent> optionalAgent =
                Optional.ofNullable(agentService.findAgent(refund.getAgentCode()));

        boolean isValid = optionalAgent.isPresent()
                && ! (hasEasyPay(refund) && easyPayIsNotActiveForAgent(optionalAgent.get()));

        if (!isValid) {

            errors.rejectValue("formOfPaymentAmounts", ERROR_CODE,
                    "agent is not authorized to issue refunds with form of payment 'Easy Pay'");
        }

    }

    private boolean hasEasyPay(Refund refund) {

        return refund.getFormOfPaymentAmounts().stream()
            .anyMatch(x -> FormOfPaymentType.EP.equals(x.getType()));
    }

    private boolean easyPayIsNotActiveForAgent(Agent agent) {

        return agent.getFormOfPayment().stream()
                .noneMatch(x ->
                    FormOfPaymentType.EP.equals(x.getType())
                    && FormOfPaymentStatus.ACTIVE.equals(x.getStatus()));
    }

}
