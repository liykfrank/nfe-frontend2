package org.iata.bsplink.agencymemo.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.dto.TaxMiscellaneousFeeRequest;
import org.iata.bsplink.agencymemo.service.ConfigService;
import org.iata.bsplink.agencymemo.test.validation.CustomConstraintValidatorTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class XfTaxesValidatorTest extends CustomConstraintValidatorTestCase {

    public ConfigService configService;

    private XfTaxesValidator validator;
    private AcdmRequest acdm;

    @Override
    @Before
    public void setUp() {
        super.setUp();

        acdm = new AcdmRequest();
        acdm.setTaxMiscellaneousFees(
            Stream.of("AB", "CD", "EF", "GH", "IJ", "KL", "MN")
                .map(taxType -> {
                    TaxMiscellaneousFeeRequest tax = new TaxMiscellaneousFeeRequest();
                    tax.setType(taxType);
                    tax.setAirlineAmount(BigDecimal.valueOf(10));
                    tax.setAgentAmount(BigDecimal.valueOf(0));
                    return tax;
                }).collect(Collectors.toList()));

        validator = new XfTaxesValidator();
    }

    @Test
    public void testIsValidWithNullTaxes() {
        acdm.setTaxMiscellaneousFees(null);
        assertTrue(validator.isValid(acdm, context));
    }

    @Test
    public void testIsValidWithNullTax() {
        acdm.getTaxMiscellaneousFees().add(null);
        assertTrue(validator.isValid(acdm, context));
    }

    @Test
    public void testIsValidWithNullTaxType() {
        acdm.getTaxMiscellaneousFees().get(1).setType(null);
        assertTrue(validator.isValid(acdm, context));
    }

    @Test
    @Parameters
    public void testIsValid(String taxType1, int taxAmount1, String taxType2, int taxAmount2,
            String taxType3, int taxAmount3, String taxType4, int taxAmount4) {
        List<TaxMiscellaneousFeeRequest> taxes = acdm.getTaxMiscellaneousFees();
        taxes.get(1).setType(taxType1);
        taxes.get(1).setAirlineAmount(BigDecimal.valueOf(taxAmount1));

        taxes.get(2).setType(taxType2);
        taxes.get(2).setAirlineAmount(BigDecimal.valueOf(taxAmount2));

        taxes.get(3).setType(taxType3);
        taxes.get(3).setAirlineAmount(BigDecimal.valueOf(taxAmount3));

        taxes.get(4).setType(taxType4);
        taxes.get(4).setAirlineAmount(BigDecimal.valueOf(taxAmount4));

        assertTrue(validator.isValid(acdm, context));
    }

    @Test
    @Parameters
    public void testIsNotValid(String taxType1, int taxAmount1, String taxType2, int taxAmount2,
            String taxType3, int taxAmount3, String taxType4, int taxAmount4,
            String message, int propertyIndex) {
        List<TaxMiscellaneousFeeRequest> taxes = acdm.getTaxMiscellaneousFees();
        taxes.get(1).setType(taxType1);
        taxes.get(1).setAirlineAmount(BigDecimal.valueOf(taxAmount1));

        taxes.get(2).setType(taxType2);
        taxes.get(2).setAirlineAmount(BigDecimal.valueOf(taxAmount2));

        taxes.get(3).setType(taxType3);
        taxes.get(3).setAirlineAmount(BigDecimal.valueOf(taxAmount3));

        taxes.get(4).setType(taxType4);
        taxes.get(4).setAirlineAmount(BigDecimal.valueOf(taxAmount4));

        assertFalse(validator.isValid(acdm, context));
        verifyConstraintViolation("taxMiscellaneousFees[" + propertyIndex + "].type", message);
    }

    /**
     * Test Parameters.
     */
    public Object[][] parametersForTestIsValid() {
        return new Object[][] {
            { "BC", 1, "DE", 2, "FG", 3, "HI", 4 },
            { "BC", 1, "XFXXX1", 2, "XFXXX2", 3, "HI", 4 },
            { "XF", 1, "XFXXX1", 0, "XFXXX2", 0, "HI", 4 }
        };
    }

    /**
     * Test Parameters.
     */
    public Object[][] parametersForTestIsNotValid() {
        return new Object[][] {
            { "BC", 1, "DE", 2, "XF", 3, "HI", 4, XfTaxesValidator.XF_MISSING_MSG, 3 },
            { "BC", 1, "XFXXX1", 0, "XFXXX2", 0, "XF", 4, XfTaxesValidator.XF_FIRST_MSG, 4 },
            { "XF", 1, "XFXXX1", 2, "XFXXX2", 3, "HI", 4, XfTaxesValidator.ZERO_AMOUNT_MSG, 2 },
            { "XF", 1, "DE", 2, "XFXXX2", 0, "HI", 4, XfTaxesValidator.CONSECUTIVE_MSG, 3 },
            { "XF", 1, "XF", 2, "XFXXX2", 0, "HI", 4, XfTaxesValidator.ONLY_ONE_XF_MSG, 2 }
        };
    }

}
