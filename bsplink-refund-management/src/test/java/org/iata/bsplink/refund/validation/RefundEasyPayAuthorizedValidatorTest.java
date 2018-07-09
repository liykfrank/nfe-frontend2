package org.iata.bsplink.refund.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.iata.bsplink.refund.dto.Agent;
import org.iata.bsplink.refund.dto.FormOfPayment;
import org.iata.bsplink.refund.dto.FormOfPaymentStatus;
import org.iata.bsplink.refund.model.entity.FormOfPaymentAmount;
import org.iata.bsplink.refund.model.entity.FormOfPaymentType;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.service.AgentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

@RunWith(JUnitParamsRunner.class)
public class RefundEasyPayAuthorizedValidatorTest {

    private static final String ANY_AGENT_CODE = "12345678";

    private Agent agent;
    private AgentService agentService;
    private Refund refund;
    private Validator validator;
    private Errors errors;

    @Before
    public void setUp() {

        refund = new Refund();

        errors = new BeanPropertyBindingResult(refund, "refund");

        agent = new Agent();
        agentService = mock(AgentService.class);

        when(agentService.findAgent(ANY_AGENT_CODE)).thenReturn(agent);

        validator = new RefundEasyPayAuthorizedValidator(agentService);
    }

    @Test
    public void testSupports() {

        assertTrue(validator.supports(refund.getClass()));
        assertFalse(validator.supports(Object.class));
    }

    @Test
    public void testAddsExpectedMessageWhenNotValid() {

        configureErrorTestCase();

        validator.validate(refund, errors);

        assertTrue(errors.hasErrors());

        FieldError fieldError = errors.getFieldError();

        assertEquals("agent is not authorized to issue refunds with form of payment 'Easy Pay'",
                fieldError.getDefaultMessage());
        assertEquals("formOfPaymentAmounts", fieldError.getField());
    }

    private void configureErrorTestCase() {

        configureTest(
                Arrays.asList(
                        createFormOfPayment(FormOfPaymentType.EP, FormOfPaymentStatus.NON_ACTIVE)),
                Arrays.asList(
                        createFormOfPaymentAmount(FormOfPaymentType.EP)),
                ANY_AGENT_CODE);
    }

    private void configureTest(List<FormOfPayment> agentFormsOfPayment,
            List<FormOfPaymentAmount> refundFormsOfPayment, String agentCode) {

        agent.setFormOfPayment(agentFormsOfPayment);

        refund.setFormOfPaymentAmounts(refundFormsOfPayment);
        refund.setAgentCode(agentCode);
    }

    private FormOfPayment createFormOfPayment(FormOfPaymentType type, FormOfPaymentStatus status) {

        FormOfPayment formOfPayment = new FormOfPayment();
        formOfPayment.setType(type);
        formOfPayment.setStatus(status);

        return formOfPayment;
    }

    private FormOfPaymentAmount createFormOfPaymentAmount(FormOfPaymentType type) {

        FormOfPaymentAmount formOfPaymentAmount = new FormOfPaymentAmount();
        formOfPaymentAmount.setType(type);

        return formOfPaymentAmount;
    }

    @Test
    @Parameters
    public void testValidatesFormOfPayment(List<FormOfPayment> agentFormsOfPayment,
            List<FormOfPaymentAmount> refundFormsOfPayment, boolean hasErrors) {

        configureTest(agentFormsOfPayment, refundFormsOfPayment, ANY_AGENT_CODE);

        validator.validate(refund, errors);

        assertEquals(hasErrors, errors.hasErrors());
    }

    @SuppressWarnings("unused")
    private Object[][] parametersForTestValidatesFormOfPayment() {

        List<FormOfPayment> agentEasyPayActive = Arrays.asList(
                createFormOfPayment(FormOfPaymentType.CC, FormOfPaymentStatus.NOT_AUTHORIZED),
                createFormOfPayment(FormOfPaymentType.EP, FormOfPaymentStatus.ACTIVE));

        List<FormOfPayment> agentEasyPayNotActive = Arrays.asList(
                createFormOfPayment(FormOfPaymentType.CC, FormOfPaymentStatus.ACTIVE),
                createFormOfPayment(FormOfPaymentType.EP, FormOfPaymentStatus.NON_ACTIVE));

        List<FormOfPayment> agentEasyPayNotAuthorized = Arrays.asList(
                createFormOfPayment(FormOfPaymentType.CC, FormOfPaymentStatus.ACTIVE),
                createFormOfPayment(FormOfPaymentType.EP, FormOfPaymentStatus.NOT_AUTHORIZED));

        List<FormOfPayment> agentWithoutEasyPay = Arrays.asList(
                createFormOfPayment(FormOfPaymentType.CC, FormOfPaymentStatus.ACTIVE),
                createFormOfPayment(FormOfPaymentType.MSCC, FormOfPaymentStatus.NOT_AUTHORIZED));

        List<FormOfPaymentAmount> withEasyPay = Arrays.asList(
                createFormOfPaymentAmount(FormOfPaymentType.CC),
                createFormOfPaymentAmount(FormOfPaymentType.EP));

        List<FormOfPaymentAmount> withoutEasyPay = Arrays.asList(
                createFormOfPaymentAmount(FormOfPaymentType.CC),
                createFormOfPaymentAmount(FormOfPaymentType.MSCC));

        return new Object[][] {

            { agentEasyPayActive, withEasyPay, false },
            { agentEasyPayActive, withoutEasyPay, false },

            { agentEasyPayNotActive, withEasyPay, true },
            { agentEasyPayNotActive, withoutEasyPay, false },

            { agentEasyPayNotAuthorized, withEasyPay, true },
            { agentEasyPayNotAuthorized, withoutEasyPay, false },

            { agentWithoutEasyPay, withEasyPay, true },
            { agentWithoutEasyPay, withoutEasyPay, false },
        };
    }

    @Test
    public void testResultIsValidIfDoesntHaveAgentCode() {

        configureErrorTestCase();

        refund.setAgentCode(null);

        validator.validate(refund, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    public void testValidationFailsIfAgentDoesNotExist() {

        configureErrorTestCase();

        refund.setAgentCode("00000000");

        validator.validate(refund, errors);

        assertTrue(errors.hasErrors());
    }
}
