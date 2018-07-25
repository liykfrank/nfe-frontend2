package org.iata.bsplink.agencymemo.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
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
public class CpAndMfValidatorTest extends CustomConstraintValidatorTestCase {

    public ConfigService configService;

    private CpAndMfValidator validator;
    private AcdmRequest acdm;
    private Config config;

    @Override
    @Before
    public void setUp() {
        super.setUp();

        config = new Config();
        config.setIsoCountryCode("XX");
        config.setCpPermittedForConcerningIssue(true);
        config.setMfPermittedForConcerningIssue(true);
        config.setCpPermittedForConcerningRefund(true);
        config.setMfPermittedForConcerningRefund(true);

        configService = mock(ConfigService.class);
        when(configService.find(config.getIsoCountryCode())).thenReturn(config);

        acdm = new AcdmRequest();
        acdm.setIsoCountryCode(config.getIsoCountryCode());
        acdm.setTransactionCode(TransactionCode.ADMA);
        acdm.setConcernsIndicator(ConcernsIndicator.I);

        TaxMiscellaneousFeeRequest tax = new TaxMiscellaneousFeeRequest();
        tax.setType("AB");
        tax.setAirlineAmount(BigDecimal.valueOf(10));
        tax.setAgentAmount(BigDecimal.valueOf(5));

        List<TaxMiscellaneousFeeRequest> taxes = new ArrayList<>();
        taxes.add(tax);

        tax = new TaxMiscellaneousFeeRequest();
        tax.setType("CD");
        tax.setAirlineAmount(BigDecimal.valueOf(7));
        tax.setAgentAmount(BigDecimal.valueOf(6));
        taxes.add(tax);

        tax = new TaxMiscellaneousFeeRequest();
        tax.setType("EF");
        tax.setAirlineAmount(BigDecimal.valueOf(3));
        tax.setAgentAmount(BigDecimal.valueOf(2));
        taxes.add(tax);

        acdm.setTaxMiscellaneousFees(taxes);

        validator = new CpAndMfValidator(configService);
    }

    @Test
    public void testIsValidWithNullIsoCountryCode() {
        acdm.setIsoCountryCode(null);
        assertTrue(validator.isValid(acdm, context));
    }

    @Test
    public void testIsValidWithNullTaxes() {
        acdm.setTaxMiscellaneousFees(null);
        assertTrue(validator.isValid(acdm, context));
    }

    @Test
    public void testIsValidWithNullTaxType() {
        acdm.getTaxMiscellaneousFees().get(1).setType(null);
        assertTrue(validator.isValid(acdm, context));
    }

    @Test
    @Parameters
    public void testIsValid(ConcernsIndicator concernsIndicator, String taxType,
            boolean cpOnIssuePermitted, boolean mfOnIssuePermitted,
            boolean cpOnRfndPermitted, boolean mfOnRfndPermitted) {
        acdm.setConcernsIndicator(concernsIndicator);
        acdm.getTaxMiscellaneousFees().get(1).setType(taxType);
        config.setCpPermittedForConcerningIssue(cpOnIssuePermitted);
        config.setMfPermittedForConcerningIssue(mfOnIssuePermitted);
        config.setCpPermittedForConcerningRefund(cpOnRfndPermitted);
        config.setMfPermittedForConcerningRefund(mfOnRfndPermitted);
        assertTrue(validator.isValid(acdm, context));
    }

    @Test
    @Parameters
    public void testIsNotValid(ConcernsIndicator concernsIndicator, String taxType,
            boolean cpOnIssuePermitted, boolean mfOnIssuePermitted,
            boolean cpOnRfndPermitted, boolean mfOnRfndPermitted,
            String message) {
        acdm.setConcernsIndicator(concernsIndicator);
        acdm.getTaxMiscellaneousFees().get(1).setType(taxType);
        config.setCpPermittedForConcerningIssue(cpOnIssuePermitted);
        config.setMfPermittedForConcerningIssue(mfOnIssuePermitted);
        config.setCpPermittedForConcerningRefund(cpOnRfndPermitted);
        config.setMfPermittedForConcerningRefund(mfOnRfndPermitted);
        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("taxMiscellaneousFees[1].type", message);
    }

    /**
     * Test Parameters.
     */
    public Object[][] parametersForTestIsValid() {
        return new Object[][] {
            { ConcernsIndicator.I, "CP", true, true, true, true },
            { ConcernsIndicator.I, "CP", true, false, true, true },
            { ConcernsIndicator.I, "MF", false, true, true, true },
            { ConcernsIndicator.R, "CP", false, true, true, true },
            { ConcernsIndicator.R, "MF", false, false, true, true},
            { ConcernsIndicator.R, "MF", false, true, false, true }
        };
    }

    /**
     * Test Parameters.
     */
    public Object[][] parametersForTestIsNotValid() {
        return new Object[][] {
            { ConcernsIndicator.I, "CP", false, true, true, true,
                CpAndMfValidator.NO_CP_ON_ISSUE_MSG },
            { ConcernsIndicator.I, "CP", false, false, true, true,
                CpAndMfValidator.NO_CP_ON_ISSUE_MSG },
            { ConcernsIndicator.I, "MF", false, false, true, true,
                CpAndMfValidator.NO_MF_ON_ISSUE_MSG },
            { ConcernsIndicator.I, "MF", false, false, true, true,
                CpAndMfValidator.NO_MF_ON_ISSUE_MSG },
            { ConcernsIndicator.R, "CP", false, true, false, true,
                CpAndMfValidator.NO_CP_ON_RFND_MSG },
            { ConcernsIndicator.R, "CP", false, false, false, true,
                CpAndMfValidator.NO_CP_ON_RFND_MSG },
            { ConcernsIndicator.R, "MF", false, false, true, false,
                CpAndMfValidator.NO_MF_ON_RFND_MSG },
            { ConcernsIndicator.R, "MF", false, true, false, false,
                CpAndMfValidator.NO_MF_ON_RFND_MSG }
        };
    }

}
