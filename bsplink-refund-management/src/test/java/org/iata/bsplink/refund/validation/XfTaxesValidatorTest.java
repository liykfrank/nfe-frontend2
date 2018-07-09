package org.iata.bsplink.refund.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.model.entity.TaxMiscellaneousFee;
import org.iata.bsplink.refund.test.validation.CustomConstraintValidatorTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)

public class XfTaxesValidatorTest extends CustomConstraintValidatorTestCase {

    private XfTaxesValidator validator;
    private Refund refund;

    @Override
    @Before
    public void setUp() {
        super.setUp();

        refund = new Refund();
        refund.setTaxMiscellaneousFees(
            Stream.of("AB", "CD", "EF", "GH", "IJ", "KL", "MN")
                .map(taxType -> {
                    TaxMiscellaneousFee tax = new TaxMiscellaneousFee();
                    tax.setType(taxType);
                    tax.setAmount(BigDecimal.valueOf(10));
                    return tax;
                }).collect(Collectors.toList()));

        validator = new XfTaxesValidator();
    }

    @Test
    public void testIsValidWithNullTaxes() {
        refund.setTaxMiscellaneousFees(null);
        assertTrue(validator.isValid(refund, context));
    }

    @Test
    public void testIsValidWithNullTax() {
        refund.getTaxMiscellaneousFees().add(null);
        assertTrue(validator.isValid(refund, context));
    }

    @Test
    public void testIsValidWithNullTaxType() {
        refund.getTaxMiscellaneousFees().get(1).setType(null);
        assertTrue(validator.isValid(refund, context));
    }

    @Test
    @Parameters
    public void testIsValid(String taxType1, int taxAmount1, String taxType2, int taxAmount2,
            String taxType3, int taxAmount3, String taxType4, int taxAmount4) {
        List<TaxMiscellaneousFee> taxes = refund.getTaxMiscellaneousFees();
        taxes.get(1).setType(taxType1);
        taxes.get(1).setAmount(BigDecimal.valueOf(taxAmount1));

        taxes.get(2).setType(taxType2);
        taxes.get(2).setAmount(BigDecimal.valueOf(taxAmount2));

        taxes.get(3).setType(taxType3);
        taxes.get(3).setAmount(BigDecimal.valueOf(taxAmount3));

        taxes.get(4).setType(taxType4);
        taxes.get(4).setAmount(BigDecimal.valueOf(taxAmount4));

        assertTrue(validator.isValid(refund, context));
    }

    @Test
    @Parameters
    public void testIsNotValid(String taxType1, int taxAmount1, String taxType2, int taxAmount2,
            String taxType3, int taxAmount3, String taxType4, int taxAmount4,
            String message, int propertyIndex) {
        List<TaxMiscellaneousFee> taxes = refund.getTaxMiscellaneousFees();
        taxes.get(1).setType(taxType1);
        taxes.get(1).setAmount(BigDecimal.valueOf(taxAmount1));

        taxes.get(2).setType(taxType2);
        taxes.get(2).setAmount(BigDecimal.valueOf(taxAmount2));

        taxes.get(3).setType(taxType3);
        taxes.get(3).setAmount(BigDecimal.valueOf(taxAmount3));

        taxes.get(4).setType(taxType4);
        taxes.get(4).setAmount(BigDecimal.valueOf(taxAmount4));

        assertFalse(validator.isValid(refund, context));
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
