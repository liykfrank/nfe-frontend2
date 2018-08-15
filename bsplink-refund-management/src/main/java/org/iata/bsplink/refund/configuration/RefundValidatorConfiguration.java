package org.iata.bsplink.refund.configuration;

import java.util.Arrays;
import java.util.List;

import org.iata.bsplink.refund.service.AgentService;
import org.iata.bsplink.refund.service.ConfigService;
import org.iata.bsplink.refund.validation.FreeStatValidator;
import org.iata.bsplink.refund.validation.MixedTaxesValidator;
import org.iata.bsplink.refund.validation.RefundAgentRegistrationNumberValidator;
import org.iata.bsplink.refund.validation.RefundAgentVatNumberValidator;
import org.iata.bsplink.refund.validation.RefundAirlineRegistrationNumberValidator;
import org.iata.bsplink.refund.validation.RefundAirlineVatNumberValidator;
import org.iata.bsplink.refund.validation.RefundCompositeValidator;
import org.iata.bsplink.refund.validation.RefundConjunctionsValidator;
import org.iata.bsplink.refund.validation.RefundCreditCardEnabledValidator;
import org.iata.bsplink.refund.validation.RefundEasyPayAuthorizedValidator;
import org.iata.bsplink.refund.validation.RefundEasyPayEnabledValidator;
import org.iata.bsplink.refund.validation.RefundMaxConjunctionsValidator;
import org.iata.bsplink.refund.validation.RefundUsedCouponsValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;

@Configuration
public class RefundValidatorConfiguration {

    private List<Validator> refundValidators(ConfigService configService,
            AgentService agentService) {

        return Arrays.asList(
                new FreeStatValidator(configService),
                new MixedTaxesValidator(configService),
                new RefundAgentRegistrationNumberValidator(configService),
                new RefundAgentVatNumberValidator(configService),
                new RefundAirlineRegistrationNumberValidator(configService),
                new RefundAirlineVatNumberValidator(configService),
                new RefundConjunctionsValidator(configService),
                new RefundCreditCardEnabledValidator(configService),
                new RefundEasyPayEnabledValidator(configService),
                new RefundMaxConjunctionsValidator(configService),
                new RefundUsedCouponsValidator(configService),
                new RefundEasyPayAuthorizedValidator(agentService));
    }

    @Bean
    public RefundCompositeValidator refundValidator(ConfigService configService,
            AgentService agentService) {

        return new RefundCompositeValidator(refundValidators(configService, agentService));
    }
}
