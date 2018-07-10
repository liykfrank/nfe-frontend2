package org.iata.bsplink.refund.loader.builder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.collection.IsEmptyCollection;
import org.iata.bsplink.refund.loader.dto.TaxMiscellaneousFee;
import org.iata.bsplink.refund.loader.model.record.RecordIt05;
import org.junit.Test;

public class TaxMiscellaneousFeesBuilderTest {

    @Test
    public void testBuild() {
        RecordIt05 it05 = it05();
        String tmft = "TX";
        it05.setTaxMiscellaneousFeeType1(tmft);
        int amount = 1000;
        it05.setTaxMiscellaneousFeeAmount1(String.valueOf(amount));
        TaxMiscellaneousFeesBuilder builder = new TaxMiscellaneousFeesBuilder();
        int numDecimals = 2;
        builder.setNumDecimals(numDecimals);
        builder.setIt05s(Arrays.asList(it05));
        List<TaxMiscellaneousFee> taxMiscellaneousFees = builder.build();
        assertNotNull(taxMiscellaneousFees);
        assertThat(taxMiscellaneousFees, hasSize(1));
        TaxMiscellaneousFee taxMiscellaneousFee = taxMiscellaneousFees.get(0);
        assertThat(taxMiscellaneousFee.getAmount(),
                equalTo(BigDecimal.valueOf(amount, numDecimals)));
        assertThat(taxMiscellaneousFee.getType(), equalTo(tmft));
    }

    @Test
    public void testBuildWithoutTaxes() {
        RecordIt05 it05 = it05();
        TaxMiscellaneousFeesBuilder builder = new TaxMiscellaneousFeesBuilder();
        int numDecimals = 2;
        builder.setNumDecimals(numDecimals);
        builder.setIt05s(Arrays.asList(it05));
        List<TaxMiscellaneousFee> taxMiscellaneousFees = builder.build();
        assertNotNull(taxMiscellaneousFees);
        assertThat(taxMiscellaneousFees, IsEmptyCollection.empty());
    }

    private RecordIt05 it05() {
        RecordIt05 it05 = new RecordIt05();
        it05.setTaxMiscellaneousFeeAmount1("0");
        it05.setTaxMiscellaneousFeeAmount2("0");
        it05.setTaxMiscellaneousFeeAmount3("0");
        it05.setTaxMiscellaneousFeeAmount4("0");
        it05.setTaxMiscellaneousFeeAmount5("0");
        it05.setTaxMiscellaneousFeeAmount6("0");

        it05.setTaxMiscellaneousFeeType1("");
        it05.setTaxMiscellaneousFeeType2("");
        it05.setTaxMiscellaneousFeeType3("");
        it05.setTaxMiscellaneousFeeType4("");
        it05.setTaxMiscellaneousFeeType5("");
        it05.setTaxMiscellaneousFeeType6("");
        return it05;
    }
}
