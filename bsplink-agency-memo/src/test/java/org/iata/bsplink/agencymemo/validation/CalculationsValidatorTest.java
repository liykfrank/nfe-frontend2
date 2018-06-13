package org.iata.bsplink.agencymemo.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.dto.CalculationsRequest;
import org.iata.bsplink.agencymemo.dto.TaxMiscellaneousFeeRequest;
import org.iata.bsplink.agencymemo.model.ConcernsIndicator;
import org.iata.bsplink.agencymemo.model.TransactionCode;
import org.iata.bsplink.agencymemo.model.entity.Config;
import org.iata.bsplink.agencymemo.service.ConfigService;
import org.iata.bsplink.agencymemo.test.validation.CustomConstraintValidatorTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class CalculationsValidatorTest extends CustomConstraintValidatorTestCase {

    public ConfigService configService;

    private CalculationsValidator validator;
    private AcdmRequest acdm;

    private String isoc1;
    private String isoc2;
    private Config config1;
    private Config config2;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        isoc1 = "AA";
        isoc2 = "BB";

        config1 = new Config();
        config1.setIsoCountryCode(isoc1);
        config1.setTaxOnCommissionEnabled(true);
        config1.setTaxOnCommissionSign(1);
        config1.setNridAndSpamEnabled(true);

        config2 = new Config();
        config2.setIsoCountryCode(isoc2);
        config2.setTaxOnCommissionEnabled(true);
        config2.setTaxOnCommissionSign(2);
        config2.setNridAndSpamEnabled(true);


        configService = mock(ConfigService.class);
        when(configService.find(isoc1)).thenReturn(config1);
        when(configService.find(isoc2)).thenReturn(config2);

        acdm = new AcdmRequest();
        acdm.setIsoCountryCode(isoc1);
        acdm.setTransactionCode(TransactionCode.ADMA);
        acdm.setConcernsIndicator(ConcernsIndicator.I);
        validator = new CalculationsValidator(configService);
    }

    @Test
    public void testIsValidWithNullIsoCountryCode() {
        acdm.setIsoCountryCode(null);
        assertTrue(validator.isValid(acdm, context));
    }

    @Test
    public void testIsValidWithNullTrnc() {
        acdm.setTransactionCode(null);
        assertTrue(validator.isValid(acdm, context));
    }

    @Test
    public void testIsValidWithNullAgentCalculations() {
        acdm.setAgentCalculations(null);
        assertTrue(validator.isValid(acdm, context));
    }

    @Test
    public void testIsValidWithNullAirlineCalculations() {
        acdm.setAirlineCalculations(null);
        assertTrue(validator.isValid(acdm, context));
    }

    @Test
    @Parameters
    public void testIsValidWithNullAirlineAmount(String field) throws Exception {
        PropertyUtils.setProperty(acdm.getAirlineCalculations(), field, null);
        assertTrue(validator.isValid(acdm, context));
    }

    private void setAirlineSpam() {
        acdm.getAirlineCalculations().setFare(BigDecimal.valueOf(10));
        acdm.getAirlineCalculations().setSpam(BigDecimal.valueOf(4));
        acdm.setAmountPaidByCustomer(BigDecimal.valueOf(6));
    }

    private void setAgentSpam() {
        acdm.setNetReporting(false);
        acdm.setRegularized(true);
        acdm.getAirlineCalculations().setFare(BigDecimal.valueOf(10));
        acdm.getAgentCalculations().setSpam(BigDecimal.valueOf(7));
        acdm.setAmountPaidByCustomer(BigDecimal.valueOf(17));
    }

    @Test
    public void testIsValidNetRemitEnabled() {
        config1.setNridAndSpamEnabled(true);
        acdm.setNetReporting(true);
        setAirlineSpam();
        assertTrue(validator.isValid(acdm, context));
    }

    @Test
    public void testIsNotValidNetRemitDisabled() {
        config1.setNridAndSpamEnabled(false);
        acdm.setNetReporting(true);
        setAirlineSpam();
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("netReporting", CalculationsValidator.NO_NR_PERMITTED_MSG);
    }

    @Test
    public void testIsNotValidNetRemitDisabledWithAirlineSpam() {
        config1.setNridAndSpamEnabled(false);
        acdm.setNetReporting(false);
        setAirlineSpam();
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("airlineCalculations.spam",
                CalculationsValidator.NO_SPAM_PERMITTED_MSG);
    }

    @Test
    public void testIsNotValidNetRemitDisabledWithAgentSpam() {
        config1.setNridAndSpamEnabled(false);
        setAgentSpam();
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("agentCalculations.spam",
                CalculationsValidator.NO_SPAM_PERMITTED_MSG);
    }

    @Test
    public void testIsNotValidNetRemitEnabledNoNetReportingAirline() {
        config1.setNridAndSpamEnabled(true);
        acdm.setNetReporting(false);
        setAirlineSpam();
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("airlineCalculations.spam",
                CalculationsValidator.SPAM_ONLY_IN_NR_MSG);
    }

    @Test
    public void testIsNotValidNetRemitEnabledNoNetReportingAgent() {
        config1.setNridAndSpamEnabled(true);
        setAgentSpam();
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("agentCalculations.spam",
                CalculationsValidator.SPAM_ONLY_IN_NR_MSG);
    }

    private void setAirlineToca() {
        acdm.getAirlineCalculations().setFare(BigDecimal.valueOf(10));
        acdm.getAirlineCalculations().setTaxOnCommission(BigDecimal.valueOf(1));
        acdm.setAmountPaidByCustomer(BigDecimal.valueOf(11));
        acdm.setTaxOnCommissionType("TCTP");
    }

    private void setAgentToca() {
        acdm.setRegularized(true);
        acdm.getAirlineCalculations().setFare(BigDecimal.valueOf(10));
        acdm.getAgentCalculations().setTaxOnCommission(BigDecimal.valueOf(1));
        acdm.setAmountPaidByCustomer(BigDecimal.valueOf(9));
        acdm.setTaxOnCommissionType("TCTP");
    }

    @Test
    public void testIsNotValidTocaDisabledAirline() {
        config1.setTaxOnCommissionEnabled(false);
        setAirlineToca();
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("airlineCalculations.taxOnCommission",
                CalculationsValidator.TOCA_NOT_PERMITTED_MSG);
    }

    @Test
    public void testIsNotValidTocaDisabledAgent() {
        config1.setTaxOnCommissionEnabled(false);
        setAgentToca();
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("agentCalculations.taxOnCommission",
                CalculationsValidator.TOCA_NOT_PERMITTED_MSG);
    }

    @Test
    public void testIsNotValidTocaEnabledAirline() {
        config1.setTaxOnCommissionEnabled(true);
        setAirlineToca();
        acdm.setTaxOnCommissionType(null);
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("taxOnCommissionType",
                CalculationsValidator.TCTP_MISSING_MSG);
    }

    @Test
    public void testIsNotValidTocaEnabledAgent() {
        config1.setTaxOnCommissionEnabled(true);
        setAgentToca();
        acdm.setTaxOnCommissionType("");
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("taxOnCommissionType",
                CalculationsValidator.TCTP_MISSING_MSG);
    }

    @Test
    public void testIsNotValidTctpAirline() {
        config1.setTaxOnCommissionEnabled(true);
        acdm.setTaxOnCommissionType("TCTP");
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("taxOnCommissionType",
                CalculationsValidator.TCTP_NOT_PERMITTED_MSG);
    }

    @Test
    public void testIsNotValidTctpAgent() {
        config1.setTaxOnCommissionEnabled(true);
        acdm.setTaxOnCommissionType("TCTP");
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("taxOnCommissionType",
                CalculationsValidator.TCTP_NOT_PERMITTED_MSG);
    }

    @Test
    @Parameters
    public void testIsValidAcdmd(TransactionCode transactionCode,
            double airlineFare, double agentFare) {
        acdm.setTransactionCode(transactionCode);
        acdm.getAgentCalculations().setFare(BigDecimal.valueOf(agentFare));
        acdm.getAirlineCalculations().setFare(BigDecimal.valueOf(airlineFare));
        acdm.setAmountPaidByCustomer(BigDecimal.valueOf(agentFare + airlineFare));
        assertTrue(validator.isValid(acdm, context));
    }

    @Test
    @Parameters
    public void testIsNotValidAcdmd(TransactionCode transactionCode,
            double airlineFare, double agentFare,
            String property, String message) {
        acdm.setTransactionCode(transactionCode);
        //BeanUtils.setProperty(acdm.getAgentCalculations(), field1, amount);
        acdm.getAgentCalculations().setFare(BigDecimal.valueOf(agentFare));
        acdm.getAirlineCalculations().setFare(BigDecimal.valueOf(airlineFare));
        acdm.setAmountPaidByCustomer(BigDecimal.valueOf(agentFare + airlineFare));
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation(property, message);
    }

    @Test
    @Parameters
    public void testIsNotValidAmountAcdmd(String property, TransactionCode trnc, String field,
            int airlineFare, int agentFare, int airlineAmount, int agentAmount) throws Exception {
        acdm.setTransactionCode(trnc);
        BeanUtils.setProperty(acdm.getAirlineCalculations(), field,
                BigDecimal.valueOf(airlineAmount).abs());
        BeanUtils.setProperty(acdm.getAgentCalculations(), field,
                BigDecimal.valueOf(agentAmount).abs());
        acdm.getAirlineCalculations().setFare(BigDecimal.valueOf(airlineFare));
        acdm.getAgentCalculations().setFare(BigDecimal.valueOf(agentFare));
        acdm.setAmountPaidByCustomer(
                BigDecimal.valueOf(airlineFare - agentFare + airlineAmount - agentAmount));
        if (field.equals("taxOnCommission")) {
            acdm.setTaxOnCommissionType("TCTP");
        }
        if (field.equals("spam")) {
            acdm.setNetReporting(true);
        }

        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation(property + "." + field,
                CalculationsValidator.ACDMD_NO_AMOUNT_PERMITTED_MSG);
    }

    @Test
    @Parameters
    public void testIsValidTotal(int taxOnCommissionSign, ConcernsIndicator concernsIndicator,
            TransactionCode transactionCode,  int amountPaidByCustomer,
            int airlineFare, int agentFare, int airlineTax, int agentTax,
            int airlineCommission, int agentCommission, int airlineSpam, int agentSpam,
            int airlineTaxOnCommission, int agentTaxOnCommission) throws Exception {
        config1.setTaxOnCommissionSign(taxOnCommissionSign);
        config1.setTaxOnCommissionEnabled(true);
        config1.setNridAndSpamEnabled(true);
        acdm.setConcernsIndicator(concernsIndicator);
        acdm.setTransactionCode(transactionCode);
        acdm.setAmountPaidByCustomer(BigDecimal.valueOf(amountPaidByCustomer));
        CalculationsRequest air = acdm.getAirlineCalculations();
        air.setCommission(BigDecimal.valueOf(airlineCommission));
        air.setFare(BigDecimal.valueOf(airlineFare));
        air.setSpam(BigDecimal.valueOf(airlineSpam));
        air.setTax(BigDecimal.valueOf(airlineTax));
        air.setTaxOnCommission(BigDecimal.valueOf(airlineTaxOnCommission));
        CalculationsRequest agn = acdm.getAgentCalculations();
        agn.setCommission(BigDecimal.valueOf(agentCommission));
        agn.setFare(BigDecimal.valueOf(agentFare));
        agn.setSpam(BigDecimal.valueOf(agentSpam));
        agn.setTax(BigDecimal.valueOf(agentTax));
        agn.setTaxOnCommission(BigDecimal.valueOf(agentTaxOnCommission));
        TaxMiscellaneousFeeRequest tax = new TaxMiscellaneousFeeRequest();
        tax.setType("TX");
        tax.setAirlineAmount(BigDecimal.valueOf(airlineTax));
        tax.setAgentAmount(BigDecimal.valueOf(agentTax));
        acdm.getTaxMiscellaneousFees().add(tax);
        acdm.setNetReporting(airlineSpam + agentSpam > 0);
        if (airlineTaxOnCommission + agentTaxOnCommission > 0) {
            acdm.setTaxOnCommissionType("TCPT");
        }
        assertTrue(validator.isValid(acdm, context));
    }

    /**
     * Test for not valid Total.
     */
    @Test
    @Parameters
    public void testIsNotValidTotal(int taxOnCommissionSign, ConcernsIndicator concernsIndicator,
            TransactionCode transactionCode,  int amountPaidByCustomer,
            int airlineFare, int agentFare, int airlineTax, int agentTax,
            int airlineCommission, int agentCommission, int airlineSpam, int agentSpam,
            int airlineTaxOnCommission, int agentTaxOnCommission, String message)
                    throws Exception {
        config1.setTaxOnCommissionSign(taxOnCommissionSign);
        config1.setTaxOnCommissionEnabled(true);
        config1.setNridAndSpamEnabled(true);
        acdm.setConcernsIndicator(concernsIndicator);
        acdm.setTransactionCode(transactionCode);
        acdm.setAmountPaidByCustomer(BigDecimal.valueOf(amountPaidByCustomer));
        CalculationsRequest air = acdm.getAirlineCalculations();
        air.setCommission(BigDecimal.valueOf(airlineCommission));
        air.setFare(BigDecimal.valueOf(airlineFare));
        air.setSpam(BigDecimal.valueOf(airlineSpam));
        air.setTax(BigDecimal.valueOf(airlineTax));
        air.setTaxOnCommission(BigDecimal.valueOf(airlineTaxOnCommission));
        CalculationsRequest agn = acdm.getAgentCalculations();
        agn.setCommission(BigDecimal.valueOf(agentCommission));
        agn.setFare(BigDecimal.valueOf(agentFare));
        agn.setSpam(BigDecimal.valueOf(agentSpam));
        agn.setTax(BigDecimal.valueOf(agentTax));
        agn.setTaxOnCommission(BigDecimal.valueOf(agentTaxOnCommission));
        TaxMiscellaneousFeeRequest tax = new TaxMiscellaneousFeeRequest();
        tax.setType("TX");
        tax.setAirlineAmount(BigDecimal.valueOf(airlineTax));
        tax.setAgentAmount(BigDecimal.valueOf(agentTax));
        acdm.getTaxMiscellaneousFees().add(tax);
        acdm.setNetReporting(airlineSpam + agentSpam > 0);
        if (airlineTaxOnCommission + agentTaxOnCommission > 0) {
            acdm.setTaxOnCommissionType("TCPT");
        }
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("amountPaidByCustomer", message);
    }

    /**
     * Test Parameters.
     */
    public Object[][] parametersForTestIsValidAcdmd() {
        return new Object[][] {
            { "ADMD", 45D, 0D },
            { "ACMD", 0D, 55D }
        };
    }

    /**
     * Test Parameters.
     */
    public Object[][] parametersForTestIsNotValidAmountAcdmd() {
        return new Object[][] {
            { "airlineCalculations", TransactionCode.ADMD, "tax", 100, 0, 1, 0 },
            { "airlineCalculations", TransactionCode.ADMD, "commission", 100, 0, -1, 0 },
            { "airlineCalculations", TransactionCode.ADMD, "taxOnCommission", 100, 0, 1, 0 },
            { "airlineCalculations", TransactionCode.ADMD, "spam", 100, 0, -1, 0 },
            { "agentCalculations", TransactionCode.ACMD, "tax", 0, 100, 0, 1 },
            { "agentCalculations", TransactionCode.ACMD, "commission", 0, 100, 0, -1 },
            { "agentCalculations", TransactionCode.ACMD, "taxOnCommission", 0, 100, 0, 1 },
            { "agentCalculations", TransactionCode.ACMD, "spam", 0, 100, 0, -1 }
        };
    }

    /**
     * Test Parameters.
     */
    public Object[][] parametersForTestIsNotValidAcdmd() {
        return new Object[][] {
            { "ACMD", 45D, 5D, "airlineCalculations.fare",
                CalculationsValidator.ACDMD_NO_AMOUNT_PERMITTED_MSG },
            { "ADMD", 5D, 55D, "agentCalculations.fare",
                CalculationsValidator.ACDMD_NO_AMOUNT_PERMITTED_MSG },
            { "ADMD", 0D, 0D, "airlineCalculations.fare",
                CalculationsValidator.ACDMD_FARE_MISSING_MSG },
            { "ACMD", 0D, 0D, "agentCalculations.fare",
                CalculationsValidator.ACDMD_FARE_MISSING_MSG }
        };
    }

    /**
     * Test Parameters.
     */
    public Object[][] parametersForTestIsValidWithNullAirlineAmount() {
        return new Object[][] {
            { "commission" },
            { "fare" },
            { "spam" },
            { "tax" },
            { "taxOnCommission" }
        };
    }

    /**
     * Test Parameters:
     * taxOnCommissionSign, concernsIndicator, transactionCode, amountPaidByCustomer
     * airlineFare, agentFare, airlineTax, agentTax, airlineCommission, agentCommission,
     * airlineSpam, agentSpam, airlineTaxOnCommission, agentTaxOnCommission.
     */
    public Object[][] parametersForTestIsValidTotal() {
        return new Object[][] {
            { "1", ConcernsIndicator.I, TransactionCode.ADMA,
                200, 220, 20, 12, 2, 18, 8, 2, 1, 1, 0 },
            { "1", ConcernsIndicator.R, TransactionCode.ACMA,
                200, 220, 20, 12, 2, 18, 8, 2, 1, 1, 0 },
            { "-1", ConcernsIndicator.I, TransactionCode.ADMA,
                200, 222, 20, 12, 2, 18, 8, 2, 1, 1, 0 },
            { "-1", ConcernsIndicator.R, TransactionCode.ACMA,
                200, 222, 20, 12, 2, 18, 8, 2, 1, 1, 0 }
        };
    }

    /**
     * Test Parameters:
     * taxOnCommissionSign, concernsIndicator, transactionCode, amountPaidByCustomer
     * airlineFare, agentFare, airlineTax, agentTax, airlineCommission, agentCommission,
     * airlineSpam, agentSpam, airlineTaxOnCommission, agentTaxOnCommission, message.
     */
    public Object[][] parametersForTestIsNotValidTotal() {
        return new Object[][] {
            { "1", ConcernsIndicator.I, TransactionCode.ADMA,
                300, 220, 20, 12, 2, 18, 8, 2, 1, 1, 0,
                CalculationsValidator.INCORRECT_TOTAL_MSG },
            { "1", ConcernsIndicator.R, TransactionCode.ACMA,
                300, 220, 20, 12, 2, 18, 8, 2, 1, 1, 0,
                CalculationsValidator.INCORRECT_TOTAL_MSG },
            { "-1", ConcernsIndicator.I, TransactionCode.ADMA,
                300, 218, 20, 12, 2, 18, 8, 2, 1, 1, 0,
                CalculationsValidator.INCORRECT_TOTAL_MSG },
            { "-1", ConcernsIndicator.R, TransactionCode.ACMA,
                300, 218, 20, 12, 2, 18, 8, 2, 1, 1, 0,
                CalculationsValidator.INCORRECT_TOTAL_MSG },

            { "1", ConcernsIndicator.R, TransactionCode.ADMA,
                200, 220, 20, 12, 2, 18, 8, 2, 1, 1, 0,
                CalculationsValidator.INCORRECT_TOTAL_ADM_RFND_MSG },
            { "1", ConcernsIndicator.I, TransactionCode.ACMA,
                200, 220, 20, 12, 2, 18, 8, 2, 1, 1, 0,
                CalculationsValidator.INCORRECT_TOTAL_ACM_ISSUE_MSG },
            { "-1", ConcernsIndicator.R, TransactionCode.ADMA,
                200, 218, 20, 12, 2, 18, 8, 2, 1, 1, 0,
                CalculationsValidator.INCORRECT_TOTAL_ADM_RFND_MSG },
            { "-1", ConcernsIndicator.I, TransactionCode.ACMA,
                200, 218, 20, 12, 2, 18, 8, 2, 1, 1, 0,
                CalculationsValidator.INCORRECT_TOTAL_ACM_ISSUE_MSG },

            { "1", ConcernsIndicator.I, TransactionCode.ADMA,
                200, 20, 220, 2, 12, 8, 18, 1, 2, 0, 1,
                CalculationsValidator.INCORRECT_TOTAL_ADM_ISSUE_MSG },
            { "1", ConcernsIndicator.R, TransactionCode.ACMA,
                200, 20, 220, 2, 12, 8, 18, 1, 2, 0, 1,
                CalculationsValidator.INCORRECT_TOTAL_ACM_RFND_MSG },
            { "-1", ConcernsIndicator.I, TransactionCode.ADMA,
                200, 20, 218, 2, 12, 8, 18, 1, 2, 0, 1,
                CalculationsValidator.INCORRECT_TOTAL_ADM_ISSUE_MSG },
            { "-1", ConcernsIndicator.R, TransactionCode.ACMA,
                200, 20, 218, 2, 12, 8, 18, 1, 2, 0, 1,
                CalculationsValidator.INCORRECT_TOTAL_ACM_RFND_MSG }
        };
    }
}
