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

    private String isoc;
    private Config config;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        isoc = "AA";

        config = new Config();
        config.setIsoCountryCode(isoc);
        config.setTaxOnCommissionEnabled(true);
        config.setTaxOnCommissionSign(1);
        config.setNridAndSpamEnabled(true);

        configService = mock(ConfigService.class);
        when(configService.find(isoc)).thenReturn(config);

        acdm = new AcdmRequest();
        acdm.setIsoCountryCode(isoc);
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
        config.setNridAndSpamEnabled(true);
        acdm.setNetReporting(true);
        setAirlineSpam();
        assertTrue(validator.isValid(acdm, context));
    }

    @Test
    public void testIsNotValidNetRemitDisabled() {
        config.setNridAndSpamEnabled(false);
        acdm.setNetReporting(true);
        setAirlineSpam();
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("netReporting", CalculationsValidator.NO_NR_PERMITTED_MSG);
    }

    @Test
    public void testIsNotValidNetRemitDisabledWithAirlineSpam() {
        config.setNridAndSpamEnabled(false);
        acdm.setNetReporting(false);
        setAirlineSpam();
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("airlineCalculations.spam",
                CalculationsValidator.NO_SPAM_PERMITTED_MSG);
    }

    @Test
    public void testIsNotValidNetRemitDisabledWithAgentSpam() {
        config.setNridAndSpamEnabled(false);
        setAgentSpam();
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("agentCalculations.spam",
                CalculationsValidator.NO_SPAM_PERMITTED_MSG);
    }

    @Test
    public void testIsNotValidNetRemitEnabledNoNetReportingAirline() {
        config.setNridAndSpamEnabled(true);
        acdm.setNetReporting(false);
        setAirlineSpam();
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("airlineCalculations.spam",
                CalculationsValidator.SPAM_ONLY_IN_NR_MSG);
    }

    @Test
    public void testIsNotValidNetRemitEnabledNoNetReportingAgent() {
        config.setNridAndSpamEnabled(true);
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
        config.setTaxOnCommissionEnabled(false);
        setAirlineToca();
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("airlineCalculations.taxOnCommission",
                CalculationsValidator.TOCA_NOT_PERMITTED_MSG);
    }

    @Test
    public void testIsNotValidTocaDisabledAgent() {
        config.setTaxOnCommissionEnabled(false);
        setAgentToca();
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("agentCalculations.taxOnCommission",
                CalculationsValidator.TOCA_NOT_PERMITTED_MSG);
    }

    @Test
    public void testIsNotValidTocaEnabledAirline() {
        config.setTaxOnCommissionEnabled(true);
        setAirlineToca();
        acdm.setTaxOnCommissionType(null);
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("taxOnCommissionType",
                CalculationsValidator.TCTP_MISSING_MSG);
    }

    @Test
    public void testIsNotValidTocaEnabledAgent() {
        config.setTaxOnCommissionEnabled(true);
        setAgentToca();
        acdm.setTaxOnCommissionType("");
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("taxOnCommissionType",
                CalculationsValidator.TCTP_MISSING_MSG);
    }

    @Test
    public void testIsNotValidTctpAirline() {
        config.setTaxOnCommissionEnabled(true);
        acdm.setTaxOnCommissionType("TCTP");
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("taxOnCommissionType",
                CalculationsValidator.TCTP_NOT_PERMITTED_MSG);
    }

    @Test
    public void testIsNotValidTctpAgent() {
        config.setTaxOnCommissionEnabled(true);
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

    private void setAcdmValues(int taxOnCommissionSign, ConcernsIndicator concernsIndicator,
            TransactionCode transactionCode,  int amountPaidByCustomer,
            int airlineFare, int agentFare, int airlineTax, int agentTax,
            int airlineCommission, int agentCommission, int airlineSpam, int agentSpam,
            int airlineTaxOnCommission, int agentTaxOnCommission, boolean regularized) {
        config.setTaxOnCommissionSign(taxOnCommissionSign);
        config.setTaxOnCommissionEnabled(true);
        config.setNridAndSpamEnabled(true);
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
        acdm.setRegularized(regularized);
        if (airlineTaxOnCommission + agentTaxOnCommission > 0) {
            acdm.setTaxOnCommissionType("TCPT");
        }
    }

    @Test
    @Parameters
    public void testIsValidTotal(int taxOnCommissionSign, ConcernsIndicator concernsIndicator,
            TransactionCode transactionCode,  int amountPaidByCustomer,
            int airlineFare, int agentFare, int airlineTax, int agentTax,
            int airlineCommission, int agentCommission, int airlineSpam, int agentSpam,
            int airlineTaxOnCommission, int agentTaxOnCommission) throws Exception {
        setAcdmValues(taxOnCommissionSign, concernsIndicator, transactionCode, amountPaidByCustomer,
                airlineFare, agentFare, airlineTax, agentTax, airlineCommission, agentCommission,
                airlineSpam, agentSpam, airlineTaxOnCommission, agentTaxOnCommission, false);
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
        setAcdmValues(taxOnCommissionSign, concernsIndicator, transactionCode, amountPaidByCustomer,
                airlineFare, agentFare, airlineTax, agentTax, airlineCommission, agentCommission,
                airlineSpam, agentSpam, airlineTaxOnCommission, agentTaxOnCommission, false);
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("amountPaidByCustomer", message);
    }

    /**
     * Test for valid regularized.
     */
    @Test
    @Parameters
    public void testIsValidRegularized(int taxOnCommissionSign,
            ConcernsIndicator concernsIndicator, TransactionCode transactionCode,
            int amountPaidByCustomer, int airlineFare, int agentFare, int airlineTax, int agentTax,
            int airlineCommission, int agentCommission, int airlineSpam, int agentSpam,
            int airlineTaxOnCommission, int agentTaxOnCommission, boolean regularized,
            String message)
                    throws Exception {
        setAcdmValues(taxOnCommissionSign, concernsIndicator, transactionCode, amountPaidByCustomer,
                airlineFare, agentFare, airlineTax, agentTax, airlineCommission, agentCommission,
                airlineSpam, agentSpam, airlineTaxOnCommission, agentTaxOnCommission, !regularized);
        assertTrue(validator.isValid(acdm, context));
    }

    /**
     * Test for not valid regularized.
     */
    @Test
    @Parameters
    public void testIsNotValidRegularized(int taxOnCommissionSign,
            ConcernsIndicator concernsIndicator, TransactionCode transactionCode,
            int amountPaidByCustomer, int airlineFare, int agentFare, int airlineTax, int agentTax,
            int airlineCommission, int agentCommission, int airlineSpam, int agentSpam,
            int airlineTaxOnCommission, int agentTaxOnCommission, boolean regularized,
            String message)
                    throws Exception {
        setAcdmValues(taxOnCommissionSign, concernsIndicator, transactionCode, amountPaidByCustomer,
                airlineFare, agentFare, airlineTax, agentTax, airlineCommission, agentCommission,
                airlineSpam, agentSpam, airlineTaxOnCommission, agentTaxOnCommission, regularized);
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("regularized", message);
    }

    /**
     * Test for not tax valid regularized.
     */
    @Test
    public void testIsNotValidNullTaxRegularized()
                    throws Exception {
        setAcdmValues(1, ConcernsIndicator.I, TransactionCode.ADMA,
            800, 1000, 200, 0, 0, 0, 0, 0, 0, 0, 0, true);
        acdm.setTaxMiscellaneousFees(null);
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("regularized", CalculationsValidator.NO_REGULARIZED_MSG);
    }

    /**
     * Test for tax sum.
     */
    @Test
    public void testIsNotValidAirlineSumTax()
                    throws Exception {
        setAcdmValues(1, ConcernsIndicator.I, TransactionCode.ADMA,
            900, 1000, 200, 100, 0, 0, 0, 0, 0, 0, 0, false);
        TaxMiscellaneousFeeRequest tax = new TaxMiscellaneousFeeRequest();
        tax.setType("XY");
        tax.setAirlineAmount(BigDecimal.valueOf(1));
        acdm.getTaxMiscellaneousFees().add(tax);
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("airlineCalculations.tax",
                CalculationsValidator.INCORRECT_TAX_SUM_MSG);
    }

    /**
     * Test for tax sum.
     */
    @Test
    public void testIsNotValidAgentSumTax()
                    throws Exception {
        setAcdmValues(1, ConcernsIndicator.I, TransactionCode.ADMA,
            900, 1000, 200, 110, 10, 0, 0, 0, 0, 0, 0, true);
        TaxMiscellaneousFeeRequest tax = new TaxMiscellaneousFeeRequest();
        tax.setType("XY");
        tax.setAgentAmount(BigDecimal.valueOf(1));
        acdm.getTaxMiscellaneousFees().add(tax);
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("agentCalculations.tax",
                CalculationsValidator.INCORRECT_TAX_SUM_MSG);
    }


    /**
     * Test for toca.
     */
    @Test
    public void testIsValidTocaIfTocaDisabled()
                    throws Exception {
        setAcdmValues(1, ConcernsIndicator.I, TransactionCode.ADMA,
            900, 1000, 200, 110, 10, 0, 0, 0, 0, 0, 0, false);
        config.setTaxOnCommissionEnabled(false);
        assertTrue(validator.isValid(acdm, context));
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

    /**
     * Test Parameters:
     * taxOnCommissionSign, concernsIndicator, transactionCode, amountPaidByCustomer
     * airlineFare, agentFare, airlineTax, agentTax, airlineCommission, agentCommission,
     * airlineSpam, agentSpam, airlineTaxOnCommission, agentTaxOnCommission, regularized,
     * message.
     */
    public Object[][] parametersForTestIsNotValidRegularized() {
        return new Object[][] {
            { "1", ConcernsIndicator.I, TransactionCode.ADMA,
                800, 100, 200, 1000, 100, 0, 0, 0, 0, 0, 0, false,
                CalculationsValidator.REGULARIZED_MSG },
            { "1", ConcernsIndicator.I, TransactionCode.ADMA,
                710, 1000, 200, 10, 100, 0, 0, 0, 0, 0, 0, false,
                CalculationsValidator.REGULARIZED_MSG },
            { "1", ConcernsIndicator.R, TransactionCode.ACMA,
                800, 100, 200, 1000, 100, 0, 0, 0, 0, 0, 0, false,
                CalculationsValidator.REGULARIZED_MSG },
            { "1", ConcernsIndicator.R, TransactionCode.ACMA,
                710, 1000, 200, 10, 100, 0, 0, 0, 0, 0, 0, false,
                CalculationsValidator.REGULARIZED_MSG },

            { "1", ConcernsIndicator.R, TransactionCode.ADMA,
                800, 200, 100, 100, 1000, 0, 0, 0, 0, 0, 0, false,
                CalculationsValidator.REGULARIZED_MSG },
            { "1", ConcernsIndicator.R, TransactionCode.ADMA,
                710, 200, 1000, 100, 10, 0, 0, 0, 0, 0, 0, false,
                CalculationsValidator.REGULARIZED_MSG },
            { "1", ConcernsIndicator.I, TransactionCode.ACMA,
                800, 200, 100, 100, 1000, 0, 0, 0, 0, 0, 0, false,
                CalculationsValidator.REGULARIZED_MSG },
            { "1", ConcernsIndicator.I, TransactionCode.ACMA,
                710, 200, 1000, 100, 10, 0, 0, 0, 0, 0, 0, false,
                CalculationsValidator.REGULARIZED_MSG },

            { "1", ConcernsIndicator.I, TransactionCode.ADMA,
                892, 1000, 200, 100, 10, 3, 5, 0, 0, 0, 0, true,
                CalculationsValidator.NO_REGULARIZED_MSG },

            { "1", ConcernsIndicator.I, TransactionCode.ADMA,
                888, 1000, 200, 100, 10, 5, 3, 0, 0, 0, 0, true,
                CalculationsValidator.NO_REGULARIZED_MSG },

            { "1", ConcernsIndicator.I, TransactionCode.ADMA,
                891, 1000, 200, 100, 10, 0, 0, 2, 3, 0, 0, false,
                CalculationsValidator.REGULARIZED_MSG },

            { "1", ConcernsIndicator.I, TransactionCode.ADMA,
                5, 0, 0, 0, 0, 5, 10, 0, 0, 0, 0, true,
                CalculationsValidator.NO_REGULARIZED_MSG },

            { "1", ConcernsIndicator.I, TransactionCode.ADMA,
                9, 0, 0, 0, 0, 0, 0, 0, 0, 10, 1, true,
                CalculationsValidator.NO_REGULARIZED_MSG },

            { "1", ConcernsIndicator.I, TransactionCode.ADMA,
                100, 0, 0, 0, 0, 9, 100, 0, 0, 10, 1, false,
                CalculationsValidator.REGULARIZED_MSG },

            { "1", ConcernsIndicator.I, TransactionCode.ADMA,
                100, 0, 0, 0, 0, 0, 0, 9, 100, 10, 1, false,
                CalculationsValidator.REGULARIZED_MSG },

            { "1", ConcernsIndicator.I, TransactionCode.ADMA,
                82, 0, 0, 0, 0, 0, 0, 9, 100, 1, 10, true,
                CalculationsValidator.NO_REGULARIZED_MSG },
            { "1", ConcernsIndicator.I, TransactionCode.ADMA,
                89, 0, 0, 0, 0, 10, 0, 1, 100, 0, 0, false,
                CalculationsValidator.REGULARIZED_MSG },

            { "1", ConcernsIndicator.I, TransactionCode.ADMA,
                9, 0, 0, 0, 0, 0, 10, 2, 1, 0, 0, false,
                CalculationsValidator.REGULARIZED_MSG },

            { "1", ConcernsIndicator.I, TransactionCode.ADMA,
                99, 0, 0, 0, 0, 0, 0, 0, 0, 100, 1, true,
                CalculationsValidator.NO_REGULARIZED_MSG },

            { "-1", ConcernsIndicator.I, TransactionCode.ADMA,
                11, 0, 0, 0, 0, 0, 0, 0, 0, 10, 21, true,
                CalculationsValidator.NO_REGULARIZED_MSG },

            { "-1", ConcernsIndicator.I, TransactionCode.ADMA,
                101, 100, 0, 0, 0, 0, 0, 0, 0, 1, 2, false,
                CalculationsValidator.REGULARIZED_MSG },

            { "1", ConcernsIndicator.I, TransactionCode.ADMA,
                99, 100, 0, 0, 0, 0, 0, 0, 0, 1, 2, false,
                CalculationsValidator.REGULARIZED_MSG },

            { "1", ConcernsIndicator.I, TransactionCode.ADMA,
                89, 100, 0, 0, 0, 10, 0, 0, 0, 1, 2, false,
                CalculationsValidator.REGULARIZED_MSG },

            { "-1", ConcernsIndicator.I, TransactionCode.ADMA,
                97, 100, 0, 0, 0, 2, 7, 0, 0, 10, 2, false,
                CalculationsValidator.REGULARIZED_MSG },

            { "1", ConcernsIndicator.I, TransactionCode.ADMA,
                109, 100, 0, 0, 0, 2, 10, 0, 0, 2, 1, true,
                CalculationsValidator.NO_REGULARIZED_MSG },

            { "1", ConcernsIndicator.I, TransactionCode.ADMA,
                107, 100, 0, 0, 0, 2, 10, 0, 0, 1, 2, false,
                CalculationsValidator.REGULARIZED_MSG }

        };
    }

    /**
     * Test Parameters:
     * taxOnCommissionSign, concernsIndicator, transactionCode, amountPaidByCustomer
     * airlineFare, agentFare, airlineTax, agentTax, airlineCommission, agentCommission,
     * airlineSpam, agentSpam, airlineTaxOnCommission, agentTaxOnCommission, not regularized,
     * message.
     */
    public Object[][] parametersForTestIsValidRegularized() {
        return parametersForTestIsNotValidRegularized();
    }
}
