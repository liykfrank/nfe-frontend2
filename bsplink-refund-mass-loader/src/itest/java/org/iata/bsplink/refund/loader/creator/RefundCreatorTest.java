package org.iata.bsplink.refund.loader.creator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.collection.IsEmptyCollection;
import org.iata.bsplink.refund.loader.builder.FormOfPaymentAmountsBuilder;
import org.iata.bsplink.refund.loader.builder.RefundAmountsBuilder;
import org.iata.bsplink.refund.loader.builder.RefundCurrencyBuilder;
import org.iata.bsplink.refund.loader.builder.RelatedDocumentsBuilder;
import org.iata.bsplink.refund.loader.builder.RemarkBuilder;
import org.iata.bsplink.refund.loader.builder.TaxMiscellaneousFeesBuilder;
import org.iata.bsplink.refund.loader.dto.FormOfPaymentAmount;
import org.iata.bsplink.refund.loader.dto.Refund;
import org.iata.bsplink.refund.loader.dto.RefundAmounts;
import org.iata.bsplink.refund.loader.dto.RefundCurrency;
import org.iata.bsplink.refund.loader.dto.RelatedDocument;
import org.iata.bsplink.refund.loader.dto.TaxMiscellaneousFee;
import org.iata.bsplink.refund.loader.model.RefundDocument;
import org.iata.bsplink.refund.loader.model.record.RecordIt05;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.batch.job.enabled = false")
public class RefundCreatorTest {

    @Autowired
    RefundCreator refundCreator = new RefundCreator();
    @MockBean
    RefundCurrencyBuilder refundCurrencyBuilder;
    @MockBean
    FormOfPaymentAmountsBuilder fopBuilder;
    @MockBean
    RemarkBuilder remarkBuilder;
    @MockBean
    TaxMiscellaneousFeesBuilder taxBuilder;
    @MockBean
    RefundAmountsBuilder amountsBuilder;
    @MockBean
    RelatedDocumentsBuilder relatedDocumentBuilder;

    List<RecordIt05> it05s;
    RecordIt05 it05;
    RefundDocument refundDocument;


    @Before
    public void setUp() throws Exception {
        it05s = Arrays.asList(new RecordIt05(), new RecordIt05());
        it05 = it05s.get(0);
        refundDocument = new RefundDocument();
        refundDocument.setRecordsIt05(it05s);
        refundCreator.setRefundDocument(refundDocument);
    }


    @Test
    public void testCreate() throws Exception {
        Refund refund = refundCreator.create();
        assertNotNull(refund);
    }


    @Test
    public void testCurrency() throws Exception {
        int decimals = 9;
        RefundCurrency refundCurrency = new RefundCurrency();
        refundCurrency.setDecimals(decimals);
        when(refundCurrencyBuilder.build()).thenReturn(refundCurrency);
        Refund refund = refundCreator.create();
        verify(refundCurrencyBuilder).build();
        verify(refundCurrencyBuilder).setIt05(it05);
        assertNotNull(refund.getCurrency());
        assertThat(refund.getCurrency(), sameInstance(refundCurrency));
        verify(amountsBuilder).setNumDecimals(decimals);
        verify(taxBuilder).setNumDecimals(decimals);
        verify(fopBuilder).setNumDecimals(decimals);
    }


    @Test
    public void testRelatedDocument() throws Exception {
        RelatedDocument relatedDocument1 = new RelatedDocument();
        RelatedDocument relatedDocument2 = new RelatedDocument();
        when(relatedDocumentBuilder.build()).thenReturn(
                Arrays.asList(relatedDocument1, relatedDocument2));
        Refund refund = refundCreator.create();
        verify(relatedDocumentBuilder).build();
        verify(relatedDocumentBuilder).setIt03s(refundDocument.getRecordsIt03());
        assertThat(refund.getRelatedDocument(), sameInstance(relatedDocument1));
        assertNotNull(refund.getConjunctions());
        assertThat(refund.getConjunctions(), hasSize(1));
        assertThat(refund.getConjunctions().get(0), sameInstance(relatedDocument2));
    }


    @Test
    public void testFormOfPaymentAmounts() throws Exception {
        List<FormOfPaymentAmount> fops = new ArrayList<>();
        when(fopBuilder.build()).thenReturn(fops);
        Refund refund = refundCreator.create();
        verify(fopBuilder).build();
        verify(fopBuilder).setIt08s(refundDocument.getRecordsIt08());
        assertNotNull(refund.getFormOfPaymentAmounts());
        assertThat(refund.getFormOfPaymentAmounts(), sameInstance(fops));
    }


    @Test
    public void testRemark() throws Exception {
        String remark = "REMARK";
        when(remarkBuilder.build()).thenReturn(remark);
        Refund refund = refundCreator.create();
        verify(remarkBuilder).build();
        verify(remarkBuilder).setIt0hs(refundDocument.getRecordsIt0h());
        assertNotNull(refund.getAirlineRemark());
        assertThat(refund.getAirlineRemark(), sameInstance(remark));
    }


    @Test
    public void testRefundAmounts() throws Exception {
        RefundAmounts amounts = new RefundAmounts();
        when(amountsBuilder.build()).thenReturn(amounts);
        Refund refund = refundCreator.create();
        verify(amountsBuilder).build();
        verify(amountsBuilder).setIt05(it05);
        assertNotNull(refund.getAmounts());
        assertThat(refund.getAmounts(), sameInstance(amounts));
    }


    @Test
    public void testTaxes() throws Exception {
        TaxMiscellaneousFee tax = new TaxMiscellaneousFee();
        tax.setType("TX");
        tax.setAmount(BigDecimal.ONE);
        List<TaxMiscellaneousFee> taxes = Arrays.asList(tax);
        when(taxBuilder.build()).thenReturn(taxes);
        Refund refund = refundCreator.create();
        verify(taxBuilder).build();
        verify(taxBuilder).setIt05s(it05s);
        assertNotNull(refund.getTaxMiscellaneousFees());
        assertThat(refund.getTaxMiscellaneousFees(), equalTo(taxes));
        verify(amountsBuilder).setTax(BigDecimal.ONE);
        verify(amountsBuilder).setCancellationPenalty(BigDecimal.ZERO);
        verify(amountsBuilder).setMiscellaneousFee(BigDecimal.ZERO);
    }

    @Test
    public void testCancellationPenalty() throws Exception {
        TaxMiscellaneousFee tax1 = new TaxMiscellaneousFee();
        tax1.setType("TX");
        tax1.setAmount(BigDecimal.TEN);
        TaxMiscellaneousFee tax2 = new TaxMiscellaneousFee();
        tax2.setType("CP");
        tax2.setAmount(BigDecimal.ONE);
        List<TaxMiscellaneousFee> taxes = Arrays.asList(tax1, tax2);
        when(taxBuilder.build()).thenReturn(taxes);
        Refund refund = refundCreator.create();
        assertNotNull(refund.getTaxMiscellaneousFees());
        assertThat(refund.getTaxMiscellaneousFees(), hasSize(1));
        assertThat(refund.getTaxMiscellaneousFees(), contains(tax1));
        verify(amountsBuilder).setTax(BigDecimal.TEN);
        verify(amountsBuilder).setCancellationPenalty(BigDecimal.ONE);
        verify(amountsBuilder).setMiscellaneousFee(BigDecimal.ZERO);
    }

    @Test
    public void testTaxMiscellaneousFee() throws Exception {
        TaxMiscellaneousFee tax1 = new TaxMiscellaneousFee();
        tax1.setType("MF");
        tax1.setAmount(BigDecimal.TEN);
        TaxMiscellaneousFee tax2 = new TaxMiscellaneousFee();
        tax2.setType("CP");
        tax2.setAmount(BigDecimal.ONE);
        List<TaxMiscellaneousFee> taxes = Arrays.asList(tax1, tax2);
        when(taxBuilder.build()).thenReturn(taxes);
        Refund refund = refundCreator.create();
        assertNotNull(refund.getTaxMiscellaneousFees());
        assertThat(refund.getTaxMiscellaneousFees(), IsEmptyCollection.empty());
        verify(amountsBuilder).setTax(BigDecimal.ZERO);
        verify(amountsBuilder).setCancellationPenalty(BigDecimal.ONE);
        verify(amountsBuilder).setMiscellaneousFee(BigDecimal.TEN);
    }


    @Test
    public void testCreateWithoutIt05s() {
        refundDocument.setRecordsIt05(Collections.emptyList());
        Refund refund = refundCreator.create();
        assertNull(refund.getCurrency());
        assertNull(refund.getAmounts());
        assertThat(refund.getTaxMiscellaneousFees(), IsEmptyCollection.empty());
    }


    @Test
    public void testCreateWithInvalidTaxAmount() throws Exception {
        TaxMiscellaneousFee tax = new TaxMiscellaneousFee();
        tax.setType("TX");
        tax.setAmount(null);
        List<TaxMiscellaneousFee> taxes = Arrays.asList(tax);
        when(taxBuilder.build()).thenReturn(taxes);
        refundCreator.create();
        verify(amountsBuilder).setTax(null);
    }
}
