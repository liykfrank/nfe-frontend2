package org.iata.bsplink.refund.loader.builder;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.iata.bsplink.refund.loader.dto.RefundAmounts;
import org.iata.bsplink.refund.loader.model.record.RecordIt05;
import org.junit.Test;

public class RefundAmountsBuilderTest {

    @Test
    public void testBuild() {
        int grossFareValue = 2000;
        RecordIt05 it05 = new RecordIt05();
        it05.setCommissionAmount1("0");
        it05.setCommissionAmount2("0");
        it05.setTicketDocumentAmount("" + grossFareValue);

        Integer numDecimals = 2;
        BigDecimal cancellationPenalty = BigDecimal.ONE.setScale(numDecimals);
        BigDecimal miscellaneousFee = BigDecimal.valueOf(200, numDecimals);
        BigDecimal tax = BigDecimal.TEN.setScale(numDecimals);

        RefundAmountsBuilder builder = new RefundAmountsBuilder();
        builder.setIt05(it05);
        builder.setCancellationPenalty(cancellationPenalty);
        builder.setMiscellaneousFee(miscellaneousFee);
        builder.setNumDecimals(numDecimals);
        builder.setTax(tax);

        RefundAmounts amounts = builder.build();
        assertNotNull(amounts);
        assertThat(amounts.getCancellationPenalty(), is(cancellationPenalty));
        assertThat(amounts.getMiscellaneousFee(), is(miscellaneousFee));
        assertThat(amounts.getTax(), is(tax));
        BigDecimal grossFare = BigDecimal.valueOf(grossFareValue, numDecimals);
        assertThat(amounts.getGrossFare(), is(grossFare));
        assertThat(amounts.getRefundToPassenger(),
                is(grossFare.add(tax).subtract(miscellaneousFee).subtract(cancellationPenalty)));
    }

    @Test
    public void testBuildIt05ZeroValues() {
        RecordIt05 it05 = new RecordIt05();
        it05.setCommissionAmount1("0");
        it05.setCommissionAmount2("0");
        it05.setTicketDocumentAmount("0");

        Integer numDecimals = 2;
        BigDecimal tax = BigDecimal.TEN.setScale(numDecimals);

        RefundAmountsBuilder builder = new RefundAmountsBuilder();
        builder.setIt05(it05);
        builder.setNumDecimals(numDecimals);
        builder.setTax(tax);

        RefundAmounts amounts = builder.build();
        assertNotNull(amounts);
        assertThat(amounts.getTax(), is(tax));
        assertThat(amounts.getRefundToPassenger(), is(tax));
    }

    @Test
    public void testBuildIt05NullValues() {
        RecordIt05 it05 = new RecordIt05();

        Integer numDecimals = 2;

        RefundAmountsBuilder builder = new RefundAmountsBuilder();
        builder.setIt05(it05);
        builder.setNumDecimals(numDecimals);

        RefundAmounts amounts = builder.build();
        assertNotNull(amounts);
        assertNull(amounts.getRefundToPassenger());
    }

    @Test
    public void testBuildIt05NumDecimalsNull() {
        RecordIt05 it05 = new RecordIt05();
        it05.setCommissionAmount1("0");
        it05.setCommissionAmount2("0");
        it05.setTicketDocumentAmount("0");

        RefundAmountsBuilder builder = new RefundAmountsBuilder();
        builder.setIt05(it05);

        RefundAmounts amounts = builder.build();
        assertNotNull(amounts);
        assertNull(amounts.getRefundToPassenger());
    }

    @Test
    public void testBuildCommissionAmounts() {
        int grossFareValue = 20000;
        int coam1Value = 4321;
        int coam2Value = 1234;

        RecordIt05 it05 = new RecordIt05();
        it05.setCommissionAmount1("" + coam1Value);
        it05.setCommissionRate1("1");
        it05.setCommissionAmount2("" + coam2Value);
        it05.setCommissionRate2("2");
        it05.setTicketDocumentAmount("" + grossFareValue);

        Integer numDecimals = 2;
        BigDecimal tax = BigDecimal.TEN.setScale(numDecimals);

        RefundAmountsBuilder builder = new RefundAmountsBuilder();
        builder.setIt05(it05);
        builder.setNumDecimals(numDecimals);
        builder.setTax(tax);

        RefundAmounts amounts = builder.build();
        assertNotNull(amounts);
        assertThat(amounts.getTax(), is(tax));
        BigDecimal grossFare = BigDecimal.valueOf(grossFareValue, numDecimals);
        assertThat(amounts.getGrossFare(), is(grossFare));
        assertThat(amounts.getRefundToPassenger(), is(grossFare.add(tax)));

        BigDecimal coam1 = BigDecimal.valueOf(coam1Value, numDecimals);
        BigDecimal coam2 = BigDecimal.valueOf(coam2Value, numDecimals);
        assertThat(amounts.getCommissionRate(), is(BigDecimal.ZERO));
        assertThat(amounts.getCommissionAmount(), is(coam1));
        assertThat(amounts.getSpam(), is(coam2));
    }


    @Test
    public void testBuildCommissionRates() {
        int grossFareValue = 20000;
        int cort1Value = 4321;

        RecordIt05 it05 = new RecordIt05();
        it05.setCommissionAmount1("0");
        it05.setCommissionRate1("" + cort1Value);
        it05.setCommissionAmount2("0");
        it05.setCommissionRate2("1234");
        it05.setTicketDocumentAmount("" + grossFareValue);

        Integer numDecimals = 2;
        BigDecimal tax = BigDecimal.TEN.setScale(numDecimals);

        RefundAmountsBuilder builder = new RefundAmountsBuilder();
        builder.setIt05(it05);
        builder.setNumDecimals(numDecimals);
        builder.setTax(tax);

        RefundAmounts amounts = builder.build();
        assertNotNull(amounts);
        assertThat(amounts.getTax(), is(tax));
        BigDecimal grossFare = BigDecimal.valueOf(grossFareValue, numDecimals);
        assertThat(amounts.getGrossFare(), is(grossFare));
        assertThat(amounts.getRefundToPassenger(), is(grossFare.add(tax)));

        BigDecimal cort1 = BigDecimal.valueOf(cort1Value, numDecimals);
        assertThat(amounts.getCommissionAmount(), is(BigDecimal.ZERO.setScale(numDecimals)));
        assertThat(amounts.getCommissionRate(), is(cort1));
        assertThat(amounts.getSpam(), is(BigDecimal.ZERO.setScale(numDecimals)));
    }


    @Test
    public void testBuildTotalIsNull() {
        RecordIt05 it05 = new RecordIt05();
        it05.setCommissionAmount1("0");
        it05.setCommissionAmount2("0");
        it05.setTicketDocumentAmount("10000");

        Integer numDecimals = 2;
        RefundAmountsBuilder builder = new RefundAmountsBuilder();
        builder.setIt05(it05);
        builder.setNumDecimals(numDecimals);
        builder.setTax(null);

        RefundAmounts amounts = builder.build();
        assertNotNull(amounts);
        assertNull(amounts.getRefundToPassenger());
    }

    @Test
    public void testBuildCommissionXlp() {
        int grossFareValue = 20000;
        int xlpValue = 1000;
        RecordIt05 it05 = new RecordIt05();
        it05.setCommissionAmount1("0");
        it05.setCommissionType2("XLP");
        it05.setCommissionAmount2("" + xlpValue);
        it05.setCommissionRate2("1");
        it05.setTicketDocumentAmount("" + grossFareValue);

        Integer numDecimals = 2;

        RefundAmountsBuilder builder = new RefundAmountsBuilder();
        builder.setIt05(it05);
        builder.setNumDecimals(numDecimals);

        RefundAmounts amounts = builder.build();
        assertNotNull(amounts);

        BigDecimal grossFare = BigDecimal.valueOf(grossFareValue, numDecimals);
        assertThat(amounts.getGrossFare(), is(grossFare));
        assertThat(amounts.getRefundToPassenger(), is(grossFare));

        BigDecimal xlp = BigDecimal.valueOf(xlpValue, numDecimals);
        assertThat(amounts.getCommissionOnCpAndMfAmount(), is(xlp));
    }


    @Test
    public void testBuildCommissionXlpRate() {
        int grossFareValue = 20000;
        int xlpRateValue = 200;
        RecordIt05 it05 = new RecordIt05();
        it05.setCommissionAmount1("0");
        it05.setCommissionType2("XLP");
        it05.setCommissionAmount2("0");
        it05.setCommissionRate2("0" + xlpRateValue);
        it05.setTicketDocumentAmount("" + grossFareValue);

        Integer numDecimals = 2;

        RefundAmountsBuilder builder = new RefundAmountsBuilder();
        builder.setIt05(it05);
        builder.setNumDecimals(numDecimals);

        RefundAmounts amounts = builder.build();
        assertNotNull(amounts);

        BigDecimal grossFare = BigDecimal.valueOf(grossFareValue, numDecimals);
        assertThat(amounts.getGrossFare(), is(grossFare));
        assertThat(amounts.getRefundToPassenger(), is(grossFare));

        BigDecimal xlp = BigDecimal.valueOf(xlpRateValue, numDecimals);
        assertThat(amounts.getCommissionOnCpAndMfAmount(), is(BigDecimal.ZERO));
        assertThat(amounts.getCommissionOnCpAndMfRate(), is(xlp));
    }


    @Test
    public void testBuildCommissionXlp3() {
        int xlpValue = 1000;
        RecordIt05 it05 = new RecordIt05();
        it05.setCommissionAmount1("0");
        it05.setCommissionType3("XLP");
        it05.setCommissionAmount3("" + xlpValue);
        it05.setCommissionRate3("1");
        it05.setTicketDocumentAmount("0");

        Integer numDecimals = 2;

        RefundAmountsBuilder builder = new RefundAmountsBuilder();
        builder.setIt05(it05);
        builder.setNumDecimals(numDecimals);

        RefundAmounts amounts = builder.build();
        assertNotNull(amounts);

        BigDecimal xlp = BigDecimal.valueOf(xlpValue, numDecimals);
        assertThat(amounts.getCommissionOnCpAndMfAmount(), is(xlp));
    }


    @Test
    public void testBuildCommissionXlpRate3() {
        int xlpRateValue = 200;
        RecordIt05 it05 = new RecordIt05();
        it05.setCommissionAmount1("0");
        it05.setCommissionType3("XLP");
        it05.setCommissionAmount3("0");
        it05.setCommissionRate3("0" + xlpRateValue);
        it05.setTicketDocumentAmount("0");

        Integer numDecimals = 2;

        RefundAmountsBuilder builder = new RefundAmountsBuilder();
        builder.setIt05(it05);
        builder.setNumDecimals(numDecimals);

        RefundAmounts amounts = builder.build();
        assertNotNull(amounts);

        BigDecimal xlp = BigDecimal.valueOf(xlpRateValue, numDecimals);
        assertThat(amounts.getCommissionOnCpAndMfAmount(), is(BigDecimal.ZERO));
        assertThat(amounts.getCommissionOnCpAndMfRate(), is(xlp));
    }
}
