package org.iata.bsplink.refund.loader.builder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.iata.bsplink.refund.loader.dto.RefundCurrency;
import org.iata.bsplink.refund.loader.model.record.RecordIt05;
import org.junit.Test;

public class RefundCurrencyBuilderTest {

    @Test
    public void testBuild() {
        String currencyCode = "EUR";
        int decimals = 2;
        RecordIt05 it05 = new RecordIt05();
        it05.setCurrencyType(currencyCode +  decimals);

        RefundCurrencyBuilder builder = new RefundCurrencyBuilder();
        builder.setIt05(it05);
        RefundCurrency refundCurrency = builder.build();
        assertThat(refundCurrency.getCode(), equalTo(currencyCode));
        assertThat(refundCurrency.getDecimals(), equalTo(decimals));
    }

    @Test
    public void testBuildWithoutDecimals() {
        String currencyCode = "EUR";
        RecordIt05 it05 = new RecordIt05();
        it05.setCurrencyType(currencyCode);

        RefundCurrencyBuilder builder = new RefundCurrencyBuilder();
        builder.setIt05(it05);
        RefundCurrency refundCurrency = builder.build();
        assertThat(refundCurrency.getCode(), equalTo(currencyCode));
        assertNull(refundCurrency.getDecimals());
    }

    @Test
    public void testBuildWithoutCurrency() {
        RecordIt05 it05 = new RecordIt05();
        it05.setCurrencyType("");

        RefundCurrencyBuilder builder = new RefundCurrencyBuilder();
        builder.setIt05(it05);
        RefundCurrency refundCurrency = builder.build();
        assertNull(refundCurrency.getCode());
        assertNull(refundCurrency.getDecimals());
    }
}
