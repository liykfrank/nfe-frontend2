package org.iata.bsplink.agencymemo.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.dto.TaxMiscellaneousFeeRequest;
import org.iata.bsplink.agencymemo.model.TransactionCode;
import org.iata.bsplink.agencymemo.model.entity.Calculations;
import org.iata.bsplink.agencymemo.model.entity.Config;
import org.junit.Test;

public class CalculationsUtilityTest {

    @Test
    public void testCalculationsDifference() {

        Calculations expected = new Calculations();
        expected.setCommission(BigDecimal.valueOf(201));
        expected.setFare(BigDecimal.valueOf(202));
        expected.setSpam(BigDecimal.valueOf(203));
        expected.setTax(BigDecimal.valueOf(204));
        expected.setTaxOnCommission(BigDecimal.valueOf(204));

        Calculations diff = CalculationsUtility.calculationsDifference(acdm());
        assertEquals(expected, diff);
    }


    @Test
    public void testIsZero() {

        assertTrue(CalculationsUtility.isZero(BigDecimal.ZERO));
    }


    @Test
    public void testIsZeroBecauseItIsZeroDotZeros() {

        assertTrue(CalculationsUtility.isZero(BigDecimal.valueOf(0, 10)));
    }

    @Test
    public void testIsNotZeroBecauseItIsTen() {

        assertFalse(CalculationsUtility.isZero(BigDecimal.TEN));
    }


    @Test
    public void testIsToRegularizeFare() {

        AcdmRequest acdm = acdm();
        acdm.getAirlineCalculations().setFare(BigDecimal.ONE);
        acdm.getAgentCalculations().setFare(BigDecimal.TEN);
        assertTrue(CalculationsUtility.isToRegularize(new Config(), acdm));
    }


    @Test
    public void testIsNotToRegularize() {

        assertFalse(CalculationsUtility.isToRegularize(new Config(), acdm()));
    }


    @Test
    public void testIsToRegularizeTax() {

        AcdmRequest acdm = acdm();
        acdm.getAirlineCalculations().setTax(BigDecimal.ONE);
        acdm.getAgentCalculations().setTax(BigDecimal.TEN);
        assertTrue(CalculationsUtility.isToRegularize(new Config(), acdm));
    }


    @Test
    public void testIsToRegularizeCommission() {

        AcdmRequest acdm = new AcdmRequest();
        acdm.setTransactionCode(TransactionCode.ADMA);
        acdm.getAirlineCalculations().setCommission(BigDecimal.TEN);
        acdm.getAgentCalculations().setCommission(BigDecimal.ONE);

        assertTrue(CalculationsUtility.isToRegularize(new Config(), acdm));
    }


    @Test
    public void testIsNotToRegularizeCommission() {

        AcdmRequest acdm = new AcdmRequest();
        acdm.setTransactionCode(TransactionCode.ADMA);
        acdm.getAirlineCalculations().setCommission(BigDecimal.ONE);
        acdm.getAgentCalculations().setCommission(BigDecimal.TEN);

        assertFalse(CalculationsUtility.isToRegularize(new Config(), acdm));
    }


    @Test
    public void testIsNotToRegularizeCommissionBecauseOfFare() {

        AcdmRequest acdm = new AcdmRequest();
        acdm.setTransactionCode(TransactionCode.ADMA);
        acdm.getAirlineCalculations().setFare(BigDecimal.valueOf(100));
        acdm.getAirlineCalculations().setCommission(BigDecimal.TEN);
        acdm.getAgentCalculations().setCommission(BigDecimal.ONE);

        assertFalse(CalculationsUtility.isToRegularize(new Config(), acdm));
    }


    @Test
    public void testIsToRegularizeTaxes() {

        AcdmRequest acdm = acdm();
        addTaxes(acdm);
        acdm.getTaxMiscellaneousFees().get(1).setAirlineAmount(BigDecimal.ONE);
        acdm.getTaxMiscellaneousFees().get(1).setAgentAmount(BigDecimal.TEN);
        assertTrue(CalculationsUtility.isToRegularize(new Config(), acdm));
    }


    @Test
    public void testIsNotToRegularizeTaxesBecauseOfNullTaxes() {

        AcdmRequest acdm = acdm();
        acdm.setTaxMiscellaneousFees(null);
        assertFalse(CalculationsUtility.isToRegularize(new Config(), acdm));
    }

    @Test
    public void testIsNotToRegularizeTaxes() {

        AcdmRequest acdm = acdm();
        addTaxes(acdm);
        assertFalse(CalculationsUtility.isToRegularize(new Config(), acdm));
    }


    @Test
    public void testIsToRegularizeTaxOnCommission() {

        AcdmRequest acdm = acdm();
        acdm.getAgentCalculations().setTaxOnCommission(BigDecimal.valueOf(105));
        acdm.getAirlineCalculations().setTaxOnCommission(BigDecimal.valueOf(104));

        assertTrue(CalculationsUtility.isToRegularize(new Config(), acdm));
    }


    @Test
    public void testIsNotToRegularizeTaxOnCommission() {

        AcdmRequest acdm = new AcdmRequest();
        acdm.setTransactionCode(TransactionCode.ADMA);
        acdm.getAgentCalculations().setTaxOnCommission(BigDecimal.valueOf(105));
        acdm.getAirlineCalculations().setTaxOnCommission(BigDecimal.valueOf(104));

        assertFalse(CalculationsUtility.isToRegularize(new Config(), acdm));
    }


    private void addTaxes(AcdmRequest acdm) {
        TaxMiscellaneousFeeRequest tax;
        tax = new TaxMiscellaneousFeeRequest();
        tax.setAgentAmount(BigDecimal.TEN);
        tax.setAirlineAmount(BigDecimal.valueOf(15));
        acdm.getTaxMiscellaneousFees().add(tax);
        tax = new TaxMiscellaneousFeeRequest();
        tax.setAgentAmount(BigDecimal.valueOf(12));
        tax.setAirlineAmount(BigDecimal.valueOf(14));
        acdm.getTaxMiscellaneousFees().add(tax);
    }


    private AcdmRequest acdm() {

        AcdmRequest acdm = new AcdmRequest();
        acdm.setTransactionCode(TransactionCode.ADMA);
        acdm.getAgentCalculations().setCommission(BigDecimal.valueOf(100));
        acdm.getAgentCalculations().setFare(BigDecimal.valueOf(101));
        acdm.getAgentCalculations().setSpam(BigDecimal.valueOf(102));
        acdm.getAgentCalculations().setTax(BigDecimal.valueOf(103));
        acdm.getAgentCalculations().setTaxOnCommission(BigDecimal.valueOf(104));
        acdm.getAirlineCalculations().setCommission(BigDecimal.valueOf(301));
        acdm.getAirlineCalculations().setFare(BigDecimal.valueOf(303));
        acdm.getAirlineCalculations().setSpam(BigDecimal.valueOf(305));
        acdm.getAirlineCalculations().setTax(BigDecimal.valueOf(307));
        acdm.getAirlineCalculations().setTaxOnCommission(BigDecimal.valueOf(308));
        return acdm;
    }
}
