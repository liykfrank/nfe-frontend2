package org.iata.bsplink.agencymemo.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.apache.commons.beanutils.BeanUtils;
import org.iata.bsplink.agencymemo.dto.AcdmCurrencyRequest;
import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.dto.CalculationsRequest;
import org.iata.bsplink.agencymemo.dto.TaxMiscellaneousFeeRequest;
import org.iata.bsplink.agencymemo.test.validation.CustomConstraintValidatorTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class DecimalsValidatorTest extends CustomConstraintValidatorTestCase {

    private DecimalsValidator validator;

    private AcdmRequest acdmRequest;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        acdmRequest = new AcdmRequest();
        AcdmCurrencyRequest acdmCurrency = new AcdmCurrencyRequest();
        acdmRequest.setCurrency(acdmCurrency);
        validator = new DecimalsValidator();
    }

    @Test
    public void testIsValidForNullCurrency() throws Exception {
        acdmRequest.setCurrency(null);
        assertTrue(validator.isValid(acdmRequest, context));
    }

    @Test
    public void testIsValidForNullCurrencyDecimals() throws Exception {
        acdmRequest.getCurrency().setDecimals(null);
        assertTrue(validator.isValid(acdmRequest, context));
    }


    @Test
    public void testIsValidAgentTaxAmount() throws Exception {
        acdmRequest.getCurrency().setDecimals(2);
        TaxMiscellaneousFeeRequest tax = new TaxMiscellaneousFeeRequest();
        tax.setAgentAmount(BigDecimal.valueOf(2.23));
        acdmRequest.getTaxMiscellaneousFees().add(tax);
        assertTrue(validator.isValid(acdmRequest, context));
    }

    @Test
    public void testIsNotValidAgentTaxAmount() throws Exception {
        acdmRequest.getCurrency().setDecimals(1);
        TaxMiscellaneousFeeRequest tax = new TaxMiscellaneousFeeRequest();
        tax.setAgentAmount(BigDecimal.valueOf(2.23));
        acdmRequest.getTaxMiscellaneousFees().add(tax);
        assertFalse(validator.isValid(acdmRequest, context));
        verifyConstraintViolation("taxMiscellaneousFees[0].agentAmount",
                DecimalsValidator.DECIMALS_MSG);
    }

    @Test
    public void testIsValidAirlineTaxAmount() throws Exception {
        acdmRequest.getCurrency().setDecimals(2);
        TaxMiscellaneousFeeRequest tax = new TaxMiscellaneousFeeRequest();
        tax.setAirlineAmount(BigDecimal.valueOf(2.23));
        acdmRequest.getTaxMiscellaneousFees().add(tax);
        assertTrue(validator.isValid(acdmRequest, context));
    }

    @Test
    public void testIsNotValidAirlineTaxAmount() throws Exception {
        acdmRequest.getCurrency().setDecimals(1);
        TaxMiscellaneousFeeRequest tax = new TaxMiscellaneousFeeRequest();
        tax.setAirlineAmount(BigDecimal.valueOf(2.23));
        acdmRequest.getTaxMiscellaneousFees().add(tax);
        assertFalse(validator.isValid(acdmRequest, context));
        verifyConstraintViolation("taxMiscellaneousFees[0].airlineAmount",
                DecimalsValidator.DECIMALS_MSG);
    }

    @Test
    public void testIsValidTotalAmount() throws Exception {
        acdmRequest.getCurrency().setDecimals(3);
        acdmRequest.setAmountPaidByCustomer(BigDecimal.valueOf(1.234));
        assertTrue(validator.isValid(acdmRequest, context));
    }

    @Test
    public void testIsNotValidTotalTaxAmount() throws Exception {
        acdmRequest.getCurrency().setDecimals(2);
        acdmRequest.setAmountPaidByCustomer(BigDecimal.valueOf(1.234));
        assertFalse(validator.isValid(acdmRequest, context));
        verifyConstraintViolation("amountPaidByCustomer",
                DecimalsValidator.DECIMALS_MSG);
    }


    @Test
    @Parameters
    public void testIsValidAgentCalculations(String field, double amountValue, int decimals,
            Boolean expected, String message) throws Exception {
        BigDecimal amount = BigDecimal.valueOf(amountValue);
        acdmRequest.getCurrency().setDecimals(decimals);

        acdmRequest.setAgentCalculations(new CalculationsRequest());
        BeanUtils.setProperty(acdmRequest.getAgentCalculations(), field, amount);
        assertThat(validator.isValid(acdmRequest, context), equalTo(expected));
        if (!expected) {
            verifyConstraintViolation("agentCalculations." + field, message);
        }
    }

    @Test
    @Parameters
    public void testIsValidAirlineCalculations(String field, double amountValue, int decimals,
            Boolean expected, String message) throws Exception {
        BigDecimal amount = BigDecimal.valueOf(amountValue);
        acdmRequest.getCurrency().setDecimals(decimals);

        acdmRequest.setAirlineCalculations(new CalculationsRequest());
        BeanUtils.setProperty(acdmRequest.getAirlineCalculations(), field, amount);
        assertThat(validator.isValid(acdmRequest, context), equalTo(expected));
        if (!expected) {
            verifyConstraintViolation("airlineCalculations." + field, message);
        }
    }

    /**
     * Test Parameters.
     */
    public Object[][] parametersForTestIsValidAgentCalculations() {
        return new Object[][] {
            {"fare",  0.23D, 2, Boolean.TRUE, null },
            {"tax",  0.23D, 3, Boolean.TRUE, null },
            {"commission",  0.23D, 0, Boolean.FALSE, DecimalsValidator.DECIMALS_MSG },
            {"spam",  1.234D, 2, Boolean.FALSE, DecimalsValidator.DECIMALS_MSG },
            {"taxOnCommission", 1.234D, 3, Boolean.TRUE, null },
            {"fare", 1.234D, 0, Boolean.FALSE, DecimalsValidator.DECIMALS_MSG },
            {"tax", 1234D, 7, Boolean.TRUE, null },
            {"spam", 1234D, 0, Boolean.TRUE, null }
        };
    }

    /**
     * Test Parameters.
     */
    public Object[][] parametersForTestIsValidAirlineCalculations() {
        return parametersForTestIsValidAgentCalculations();
    }
}
