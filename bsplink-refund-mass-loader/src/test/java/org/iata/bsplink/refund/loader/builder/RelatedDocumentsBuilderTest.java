package org.iata.bsplink.refund.loader.builder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.collection.IsEmptyCollection;
import org.iata.bsplink.refund.loader.dto.RelatedDocument;
import org.iata.bsplink.refund.loader.model.record.RecordIt03;
import org.junit.Before;
import org.junit.Test;

public class RelatedDocumentsBuilderTest {

    List<String> documents;
    String rcpn;

    @Before
    public void setUp() throws Exception {
        rcpn = "1000";
        documents = Arrays.asList("1234567891", "1234567892", "1234567893", "1234567894",
                "1234567895", "1234567896", "1234567897");
    }


    @Test
    public void testBuild() {
        RelatedDocumentsBuilder builder = new RelatedDocumentsBuilder();
        builder.setIt03s(Arrays.asList(it03()));
        List<RelatedDocument> relatedDocuments =  builder.build();
        assertNotNull(relatedDocuments);
        assertThat(relatedDocuments, hasSize(7));
        assertThat(relatedDocuments.get(0).getRelatedTicketDocumentNumber(),
                equalTo(documents.get(0)));
        assertThat(relatedDocuments.get(1).getRelatedTicketDocumentNumber(),
                equalTo(documents.get(1)));
        assertThat(relatedDocuments.get(2).getRelatedTicketDocumentNumber(),
                equalTo(documents.get(2)));
        assertThat(relatedDocuments.get(3).getRelatedTicketDocumentNumber(),
                equalTo(documents.get(3)));
        assertThat(relatedDocuments.get(4).getRelatedTicketDocumentNumber(),
                equalTo(documents.get(4)));
        assertThat(relatedDocuments.get(5).getRelatedTicketDocumentNumber(),
                equalTo(documents.get(5)));
        assertThat(relatedDocuments.get(6).getRelatedTicketDocumentNumber(),
                equalTo(documents.get(6)));
    }

    @Test
    public void testBuildMoreThan7RelatedDocuments() {
        String document = "1234567898";
        RelatedDocumentsBuilder builder = new RelatedDocumentsBuilder();
        RecordIt03 it03 = emptyIt03();
        it03.setRelatedTicketDocumentNumber1("999" + document);
        builder.setIt03s(Arrays.asList(it03(), it03));
        List<RelatedDocument> relatedDocuments =  builder.build();
        assertNotNull(relatedDocuments);
        assertThat(relatedDocuments, hasSize(8));
        assertThat(relatedDocuments.get(7).getRelatedTicketDocumentNumber(), equalTo(document));
    }

    @Test
    public void testBuildWithoutRelatedDocuments() {
        RelatedDocumentsBuilder builder = new RelatedDocumentsBuilder();
        builder.setIt03s(Collections.emptyList());
        List<RelatedDocument> relatedDocuments =  builder.build();
        assertNotNull(relatedDocuments);
        assertThat(relatedDocuments, IsEmptyCollection.empty());
    }


    @Test
    public void testBuildBlankDocument() {
        RecordIt03 it03 = it03();
        it03.setRelatedTicketDocumentNumber7("");
        RelatedDocumentsBuilder builder = new RelatedDocumentsBuilder();
        builder.setIt03s(Arrays.asList(it03));
        List<RelatedDocument> relatedDocuments =  builder.build();
        assertNotNull(relatedDocuments);
        assertThat(relatedDocuments, hasSize(7));
        assertNull(relatedDocuments.get(6).getRelatedTicketDocumentNumber());
    }


    @Test
    public void testBuildCoupons() {
        RecordIt03 it03 = it03();
        it03.setRelatedTicketDocumentCouponNumberIdentifier1("1234");
        it03.setRelatedTicketDocumentCouponNumberIdentifier2("0000");
        it03.setRelatedTicketDocumentCouponNumberIdentifier3("1030");
        it03.setRelatedTicketDocumentCouponNumberIdentifier4("FVVV");
        RelatedDocumentsBuilder builder = new RelatedDocumentsBuilder();
        builder.setIt03s(Arrays.asList(it03));
        List<RelatedDocument> relatedDocuments =  builder.build();
        assertNotNull(relatedDocuments);
        assertThat(relatedDocuments, hasSize(7));
        assertTrue(relatedDocuments.get(0).getRelatedTicketCoupon1());
        assertTrue(relatedDocuments.get(0).getRelatedTicketCoupon2());
        assertTrue(relatedDocuments.get(0).getRelatedTicketCoupon3());
        assertTrue(relatedDocuments.get(0).getRelatedTicketCoupon4());
        assertFalse(relatedDocuments.get(1).getRelatedTicketCoupon1());
        assertFalse(relatedDocuments.get(1).getRelatedTicketCoupon2());
        assertFalse(relatedDocuments.get(1).getRelatedTicketCoupon3());
        assertFalse(relatedDocuments.get(1).getRelatedTicketCoupon4());
        assertTrue(relatedDocuments.get(2).getRelatedTicketCoupon1());
        assertFalse(relatedDocuments.get(2).getRelatedTicketCoupon2());
        assertTrue(relatedDocuments.get(2).getRelatedTicketCoupon3());
        assertFalse(relatedDocuments.get(2).getRelatedTicketCoupon4());
        assertNull(relatedDocuments.get(3).getRelatedTicketCoupon1());
        assertNull(relatedDocuments.get(3).getRelatedTicketCoupon2());
        assertNull(relatedDocuments.get(3).getRelatedTicketCoupon3());
        assertNull(relatedDocuments.get(3).getRelatedTicketCoupon4());
    }


    private RecordIt03 it03() {
        String airline = "999";
        RecordIt03 it03 = new RecordIt03();
        it03.setRelatedTicketDocumentNumber1(airline + documents.get(0));
        it03.setRelatedTicketDocumentNumber2(airline + documents.get(1));
        it03.setRelatedTicketDocumentNumber3(airline + documents.get(2));
        it03.setRelatedTicketDocumentNumber4(airline + documents.get(3));
        it03.setRelatedTicketDocumentNumber5(airline + documents.get(4));
        it03.setRelatedTicketDocumentNumber6(airline + documents.get(5));
        it03.setRelatedTicketDocumentNumber7(airline + documents.get(6));
        it03.setRelatedTicketDocumentCouponNumberIdentifier1(rcpn);
        it03.setRelatedTicketDocumentCouponNumberIdentifier2(rcpn);
        it03.setRelatedTicketDocumentCouponNumberIdentifier3(rcpn);
        it03.setRelatedTicketDocumentCouponNumberIdentifier4(rcpn);
        it03.setRelatedTicketDocumentCouponNumberIdentifier5(rcpn);
        it03.setRelatedTicketDocumentCouponNumberIdentifier6(rcpn);
        it03.setRelatedTicketDocumentCouponNumberIdentifier7(rcpn);
        return it03;
    }

    private RecordIt03 emptyIt03() {
        RecordIt03 it03 = new RecordIt03();
        it03.setRelatedTicketDocumentNumber1("");
        it03.setRelatedTicketDocumentNumber2("");
        it03.setRelatedTicketDocumentNumber3("");
        it03.setRelatedTicketDocumentNumber4("");
        it03.setRelatedTicketDocumentNumber5("");
        it03.setRelatedTicketDocumentNumber6("");
        it03.setRelatedTicketDocumentNumber7("");
        it03.setRelatedTicketDocumentCouponNumberIdentifier1("0000");
        it03.setRelatedTicketDocumentCouponNumberIdentifier2("0000");
        it03.setRelatedTicketDocumentCouponNumberIdentifier3("0000");
        it03.setRelatedTicketDocumentCouponNumberIdentifier4("0000");
        it03.setRelatedTicketDocumentCouponNumberIdentifier5("0000");
        it03.setRelatedTicketDocumentCouponNumberIdentifier6("0000");
        it03.setRelatedTicketDocumentCouponNumberIdentifier7("0000");
        return it03;
    }
}
