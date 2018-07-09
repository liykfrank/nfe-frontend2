package org.iata.bsplink.refund.loader.builder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.collection.IsEmptyCollection;
import org.iata.bsplink.refund.loader.dto.FormOfPaymentAmount;
import org.iata.bsplink.refund.loader.dto.FormOfPaymentType;
import org.iata.bsplink.refund.loader.model.record.RecordIt08;
import org.junit.Test;

public class FormOfPaymentAmountsBuilderTest {

    @Test
    public void testBuild() {
        RecordIt08 it08 = new RecordIt08();
        it08.setFormOfPaymentAccountNumber1("12345");
        it08.setFormOfPaymentAmount1("1000");
        it08.setFormOfPaymentType1("MSCA");
        it08.setFormOfPaymentAccountNumber2("123488888");
        it08.setFormOfPaymentAmount2("1010");
        it08.setFormOfPaymentType2("MSCC1234");

        List<RecordIt08> it08s = new ArrayList<>();
        it08s.add(it08);

        it08 = new RecordIt08();
        it08.setFormOfPaymentAccountNumber1("1818");
        it08.setFormOfPaymentAmount1("12");
        it08.setFormOfPaymentType1("CA");
        it08.setFormOfPaymentAccountNumber2("");
        it08.setFormOfPaymentAmount2("000");
        it08.setFormOfPaymentType2("");
        it08s.add(it08);

        FormOfPaymentAmountsBuilder builder = new FormOfPaymentAmountsBuilder(it08s);

        builder.setNumDecimals(2);

        List<FormOfPaymentAmount> fops = builder.build();

        assertNotNull(fops);
        assertThat(fops, hasSize(3));

        List<FormOfPaymentAmount> expected = new ArrayList<>();
        expected.add(new FormOfPaymentAmount() {
            {
                setAmount(BigDecimal.valueOf(1000, 2));
                setType(FormOfPaymentType.MSCA);
            }
        });
        expected.add(new FormOfPaymentAmount() {
            {
                setAmount(BigDecimal.valueOf(1010, 2));
                setType(FormOfPaymentType.MSCC);
                setNumber("123488888");
                setVendorCode("12");
            }
        });
        expected.add(new FormOfPaymentAmount() {
            {
                setAmount(BigDecimal.valueOf(12, 2));
                setType(FormOfPaymentType.CA);
            }
        });
        assertThat(fops, is(expected));
    }


    @Test
    public void testBuildWithEmptyIt08() {
        RecordIt08 it08 = new RecordIt08();
        it08.setFormOfPaymentAccountNumber1("");
        it08.setFormOfPaymentAmount1("");
        it08.setFormOfPaymentType1("");
        it08.setFormOfPaymentAccountNumber2("");
        it08.setFormOfPaymentAmount2("0");
        it08.setFormOfPaymentType2("");

        List<RecordIt08> it08s = new ArrayList<>();
        it08s.add(it08);

        FormOfPaymentAmountsBuilder builder = new FormOfPaymentAmountsBuilder(Arrays.asList(it08));
        builder.setNumDecimals(2);

        List<FormOfPaymentAmount> fops = builder.build();

        FormOfPaymentAmount expected = new FormOfPaymentAmount();
        expected.setAmount(null);

        assertNotNull(fops);
        assertThat(fops, is(Arrays.asList(expected)));
    }

    @Test
    public void testBuildWithIt08WithZeroAmounts() {
        RecordIt08 it08 = new RecordIt08();
        it08.setFormOfPaymentAccountNumber1("");
        it08.setFormOfPaymentAmount1("0");
        it08.setFormOfPaymentType1("");
        it08.setFormOfPaymentAccountNumber2("");
        it08.setFormOfPaymentAmount2("0");
        it08.setFormOfPaymentType2("");

        List<RecordIt08> it08s = new ArrayList<>();
        it08s.add(it08);

        FormOfPaymentAmountsBuilder builder = new FormOfPaymentAmountsBuilder(Arrays.asList(it08));
        builder.setNumDecimals(2);

        List<FormOfPaymentAmount> fops = builder.build();
        assertNotNull(fops);
        assertThat(fops, IsEmptyCollection.empty());
    }

    @Test
    public void testBuildWithoutIt08() {
        FormOfPaymentAmountsBuilder builder =
                new FormOfPaymentAmountsBuilder(Collections.emptyList());
        builder.setNumDecimals(2);

        List<FormOfPaymentAmount> fops = builder.build();
        assertNotNull(fops);
        assertThat(fops, IsEmptyCollection.empty());
    }

    @Test
    public void testBuildWithNullDecimals() {
        RecordIt08 it08 = new RecordIt08();
        it08.setFormOfPaymentAccountNumber1("");
        it08.setFormOfPaymentAmount1("1234");
        it08.setFormOfPaymentType1("CA");
        it08.setFormOfPaymentAccountNumber2("");
        it08.setFormOfPaymentAmount2("99999");
        it08.setFormOfPaymentType2("MSCA1234");


        List<FormOfPaymentAmount> expected = new ArrayList<>();
        expected.add(new FormOfPaymentAmount() {
            {
                setAmount(null);
                setType(FormOfPaymentType.CA);
            }
        });
        expected.add(new FormOfPaymentAmount() {
            {
                setAmount(null);
                setType(FormOfPaymentType.MSCA);
            }
        });

        FormOfPaymentAmountsBuilder builder = new FormOfPaymentAmountsBuilder(Arrays.asList(it08));

        List<FormOfPaymentAmount> fops = builder.build();
        assertNotNull(fops);
        assertThat(fops, is(expected));
    }
}