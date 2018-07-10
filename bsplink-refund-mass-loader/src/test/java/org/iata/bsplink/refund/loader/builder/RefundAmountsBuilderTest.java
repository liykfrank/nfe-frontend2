package org.iata.bsplink.refund.loader.builder;

import static org.hamcrest.CoreMatchers.equalTo;
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
        assertThat(amounts.getCancellationPenalty(), equalTo(cancellationPenalty));
        assertThat(amounts.getMiscellaneousFee(), equalTo(miscellaneousFee));
        assertThat(amounts.getTax(), equalTo(tax));
        BigDecimal grossFare = BigDecimal.valueOf(grossFareValue, numDecimals);
        assertThat(amounts.getGrossFare(), equalTo(grossFare));
        assertThat(amounts.getRefundToPassenger(),equalTo(
                grossFare.add(tax).subtract(miscellaneousFee).subtract(cancellationPenalty)));
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
        assertThat(amounts.getTax(), equalTo(tax));
        assertThat(amounts.getRefundToPassenger(), equalTo(tax));
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
        assertThat(amounts.getTax(), equalTo(tax));
        BigDecimal grossFare = BigDecimal.valueOf(grossFareValue, numDecimals);
        assertThat(amounts.getGrossFare(), equalTo(grossFare));
        assertThat(amounts.getRefundToPassenger(), equalTo(grossFare.add(tax)));

        BigDecimal coam1 = BigDecimal.valueOf(coam1Value, numDecimals);
        BigDecimal coam2 = BigDecimal.valueOf(coam2Value, numDecimals);
        assertThat(amounts.getCommissionRate(), equalTo(BigDecimal.ZERO));
        assertThat(amounts.getCommissionAmount(), equalTo(coam1));
        assertThat(amounts.getSpam(), equalTo(coam2));
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
        assertThat(amounts.getTax(), equalTo(tax));
        BigDecimal grossFare = BigDecimal.valueOf(grossFareValue, numDecimals);
        assertThat(amounts.getGrossFare(), equalTo(grossFare));
        assertThat(amounts.getRefundToPassenger(), equalTo(grossFare.add(tax)));

        BigDecimal cort1 = BigDecimal.valueOf(cort1Value, numDecimals);
        assertThat(amounts.getCommissionAmount(), equalTo(BigDecimal.ZERO.setScale(numDecimals)));
        assertThat(amounts.getCommissionRate(), equalTo(cort1));
        assertThat(amounts.getSpam(), equalTo(BigDecimal.ZERO.setScale(numDecimals)));
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
        assertThat(amounts.getGrossFare(), equalTo(grossFare));
        assertThat(amounts.getRefundToPassenger(), equalTo(grossFare));

        BigDecimal xlp = BigDecimal.valueOf(xlpValue, numDecimals);
        assertThat(amounts.getCommissionOnCpAndMfAmount(), equalTo(xlp));
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
        assertThat(amounts.getGrossFare(), equalTo(grossFare));
        assertThat(amounts.getRefundToPassenger(), equalTo(grossFare));

        BigDecimal xlp = BigDecimal.valueOf(xlpRateValue, numDecimals);
        assertThat(amounts.getCommissionOnCpAndMfAmount(), equalTo(BigDecimal.ZERO));
        assertThat(amounts.getCommissionOnCpAndMfRate(), equalTo(xlp));
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
        assertThat(amounts.getCommissionOnCpAndMfAmount(), equalTo(xlp));
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
        assertThat(amounts.getCommissionOnCpAndMfAmount(), equalTo(BigDecimal.ZERO));
        assertThat(amounts.getCommissionOnCpAndMfRate(), equalTo(xlp));
    }
}
