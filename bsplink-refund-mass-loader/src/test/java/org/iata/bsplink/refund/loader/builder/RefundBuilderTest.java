package org.iata.bsplink.refund.loader.builder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.iata.bsplink.refund.loader.dto.FormOfPaymentAmount;
import org.iata.bsplink.refund.loader.dto.Refund;
import org.iata.bsplink.refund.loader.dto.RefundAmounts;
import org.iata.bsplink.refund.loader.dto.RefundCurrency;
import org.iata.bsplink.refund.loader.dto.RefundStatus;
import org.iata.bsplink.refund.loader.dto.RelatedDocument;
import org.iata.bsplink.refund.loader.dto.TaxMiscellaneousFee;
import org.iata.bsplink.refund.loader.model.RefundDocument;
import org.iata.bsplink.refund.loader.model.record.RecordIt02;
import org.iata.bsplink.refund.loader.model.record.RecordIt03;
import org.iata.bsplink.refund.loader.model.record.RecordIt05;
import org.iata.bsplink.refund.loader.model.record.RecordIt08;
import org.junit.Test;

public class RefundBuilderTest {

    @Test
    public void testBuild() {
        RefundBuilder builder = new RefundBuilder();
        builder.setRefundDocument(new RefundDocument());
        assertNotNull(builder.build());
    }


    @Test
    public void testBuildWithSettings() {

        RefundDocument refundDocument = new RefundDocument();

        RefundBuilder builder = new RefundBuilder();
        builder.setRefundDocument(refundDocument);
        RefundAmounts amounts = new RefundAmounts();
        builder.setAmounts(amounts);
        RefundCurrency currency = new RefundCurrency();
        builder.setCurrency(currency);
        List<FormOfPaymentAmount> formOfPaymentAmounts = new ArrayList<>();
        builder.setFormOfPaymentAmounts(formOfPaymentAmounts);
        List<RelatedDocument> relatedDocuments = new ArrayList<>();
        builder.setRelatedDocuments(relatedDocuments);
        String remark = new String();
        builder.setRemark(remark);
        List<TaxMiscellaneousFee> taxMiscellaneousFees = new ArrayList<>();
        builder.setTaxMiscellaneousFees(taxMiscellaneousFees);

        Refund refund = builder.build();
        assertNotNull(refund);
        assertThat(refund.getAmounts(), is(amounts));
        assertThat(refund.getCurrency(), is(currency));
        assertThat(refund.getFormOfPaymentAmounts(), is(formOfPaymentAmounts));
        assertNull(refund.getRelatedDocument());
        assertThat(refund.getAirlineRemark(), is(remark));
        assertThat(refund.getTaxMiscellaneousFees(), is(taxMiscellaneousFees));
    }


    @Test
    public void testBuildWithRelatedDocuments() {

        RefundDocument refundDocument = new RefundDocument();
        RefundBuilder builder = new RefundBuilder();
        builder.setRefundDocument(refundDocument);
        List<RelatedDocument> relatedDocuments = new ArrayList<>();

        relatedDocuments.add(new RelatedDocument() {
            {
                setRelatedTicketDocumentNumber("4567890123");
                setRelatedTicketCoupon1(true);
                setRelatedTicketCoupon2(true);
                setRelatedTicketCoupon3(true);
                setRelatedTicketCoupon4(true);
            }
        });

        relatedDocuments.add(new RelatedDocument() {
            {
                setRelatedTicketDocumentNumber("4567890124");
                setRelatedTicketCoupon1(true);
                setRelatedTicketCoupon2(false);
                setRelatedTicketCoupon3(false);
                setRelatedTicketCoupon4(false);
            }
        });

        builder.setRelatedDocuments(relatedDocuments);
        Refund refund = builder.build();
        assertNotNull(refund);
        assertThat(refund.getRelatedDocument(), is(relatedDocuments.get(0)));
        assertNotNull(refund.getConjunctions());
        assertThat(refund.getConjunctions(), hasSize(1));
        assertThat(refund.getConjunctions().get(0), is(relatedDocuments.get(1)));
    }


    @Test
    public void testBuildWithIt02() {
        RecordIt02 it02 = new RecordIt02();
        String agentNumericCode = "78200102";
        it02.setAgentNumericCode(agentNumericCode);
        String ticketingAirlineCodeNumber = "220";
        it02.setTicketingAirlineCodeNumber(ticketingAirlineCodeNumber);
        String isoCountryCode = "ES";
        it02.setIsoCountryCode(isoCountryCode);
        String passengerName = "ALFREDO LANDA";
        it02.setPassengerName(passengerName);
        String ticketDocumentNumber = "2202143658709";
        it02.setTicketDocumentNumber(ticketDocumentNumber);
        String statisticalCode = "DOM";
        it02.setStatisticalCode(statisticalCode);
        String settlementAuthorisationCode = "ESAC";
        it02.setSettlementAuthorisationCode(settlementAuthorisationCode);
        it02.setRefundApplicationStatus("U");

        RefundDocument refundDocument = new RefundDocument();
        refundDocument.setRecordIt02(it02);

        RefundBuilder builder = new RefundBuilder();
        builder.setRefundDocument(refundDocument);
        Refund refund = builder.build();
        assertNotNull(refund);
        assertThat(refund.getAgentCode(), is(agentNumericCode));
        assertThat(refund.getAirlineCode(), is(ticketingAirlineCodeNumber));
        assertThat(refund.getIsoCountryCode(), is(isoCountryCode));
        assertThat(refund.getPassenger(), is(passengerName));
        assertThat(refund.getTicketDocumentNumber(), is(ticketDocumentNumber));
        assertThat(refund.getStatisticalCode(), is(statisticalCode));
        assertThat(refund.getSettlementAuthorisationCode(), is(settlementAuthorisationCode));
        assertThat(refund.getStatus(), is(RefundStatus.UNDER_INVESTIGATION));
    }

    @Test
    public void testBuildWithBlankIt02() {
        RecordIt02 it02 = getBlankIt02();

        RefundDocument refundDocument = new RefundDocument();
        refundDocument.setRecordIt02(it02);

        RefundBuilder builder = new RefundBuilder();
        builder.setRefundDocument(refundDocument);
        Refund refund = builder.build();
        assertNotNull(refund);
        assertNotNull(refund);
        assertThat(refund.getAgentCode(), is(""));
        assertThat(refund.getAirlineCode(), is(""));
        assertThat(refund.getIsoCountryCode(), is(""));
        assertThat(refund.getPassenger(), is(""));
        assertThat(refund.getTicketDocumentNumber(), is(""));
        assertThat(refund.getStatisticalCode(), is(""));
        assertThat(refund.getSettlementAuthorisationCode(), is(""));
        assertNull(refund.getStatus());
    }

    @Test
    public void testBuildWithStatusAuthorized() {
        RecordIt02 it02 = getBlankIt02();
        it02.setRefundApplicationStatus("A");

        RefundDocument refundDocument = new RefundDocument();
        refundDocument.setRecordIt02(it02);

        RefundBuilder builder = new RefundBuilder();
        builder.setRefundDocument(refundDocument);
        String remark = "authorization remark";
        builder.setRemark(remark);
        Refund refund = builder.build();
        assertNotNull(refund);
        assertThat(refund.getStatus(), is(RefundStatus.AUTHORIZED));
        assertThat(refund.getAirlineRemark(), is(remark));
    }

    @Test
    public void testBuildWithStatusRejected() {
        RecordIt02 it02 = getBlankIt02();
        it02.setRefundApplicationStatus("R");

        RefundDocument refundDocument = new RefundDocument();
        refundDocument.setRecordIt02(it02);

        RefundBuilder builder = new RefundBuilder();
        builder.setRefundDocument(refundDocument);
        String remark = "rejection reason";
        builder.setRemark(remark);
        Refund refund = builder.build();
        assertNotNull(refund);
        assertThat(refund.getStatus(), is(RefundStatus.REJECTED));
        assertThat(refund.getRejectionReason(), is(remark));
    }


    @Test
    public void testBuildWithNetReporting() {
        RecordIt05 it05 = new RecordIt05();
        it05.setNetReportingIndicator("NR");

        RefundDocument refundDocument = new RefundDocument();
        refundDocument.addRecordIt05(it05);

        RefundBuilder builder = new RefundBuilder();
        builder.setRefundDocument(refundDocument);
        Refund refund = builder.build();
        assertNotNull(refund);
        assertTrue(refund.getNetReporting());
    }


    @Test
    public void testBuildWithoutNetReporting() {
        RecordIt05 it05 = new RecordIt05();
        it05.setNetReportingIndicator("");

        RefundDocument refundDocument = new RefundDocument();
        refundDocument.addRecordIt05(it05);

        RefundBuilder builder = new RefundBuilder();
        builder.setRefundDocument(refundDocument);
        Refund refund = builder.build();
        assertNotNull(refund);
        assertFalse(refund.getNetReporting());
    }

    @Test
    public void testBuildWithCustomerReference() {
        String ref1 = "REF1";
        RecordIt08 it08 = new RecordIt08();
        it08.setCustomerFileReference1(ref1);
        it08.setCustomerFileReference2("X");

        RefundDocument refundDocument = new RefundDocument();
        refundDocument.addRecordIt08(it08);

        RefundBuilder builder = new RefundBuilder();
        builder.setRefundDocument(refundDocument);
        Refund refund = builder.build();
        assertNotNull(refund);
        assertThat(refund.getCustomerFileReference(), is(ref1));
    }


    @Test
    public void testBuildWithRelatedDocumentData() {
        RecordIt03 it03 = new RecordIt03();
        it03.setCheckDigit1("1");

        int year = 2017;
        int dayOfMonth = 12;
        int month = 10;
        it03.setDateOfIssueRelatedDocument("" + (year % 100) + month + dayOfMonth);
        String airlineCodeRelatedDocument = "220";
        String relatedTicketDocumentNumber = "1114447770";
        it03.setRelatedTicketDocumentNumber1(
                airlineCodeRelatedDocument + relatedTicketDocumentNumber);

        RefundDocument refundDocument = new RefundDocument();
        refundDocument.addRecordIt03(it03);

        RefundBuilder builder = new RefundBuilder();
        builder.setRefundDocument(refundDocument);
        Refund refund = builder.build();
        assertNotNull(refund);
        assertThat(refund.getDateOfIssueRelatedDocument(),
                is(LocalDate.of(year, month, dayOfMonth)));
        assertThat(refund.getAirlineCodeRelatedDocument(), is(airlineCodeRelatedDocument));
    }


    @Test
    public void testBuildWithInvalidDateOfIssueRelatedDocument() {
        RecordIt03 it03 = new RecordIt03();
        it03.setCheckDigit1("1");
        it03.setDateOfIssueRelatedDocument("XXMMDD");
        it03.setRelatedTicketDocumentNumber1("2202024234123");

        RefundDocument refundDocument = new RefundDocument();
        refundDocument.addRecordIt03(it03);

        RefundBuilder builder = new RefundBuilder();
        builder.setRefundDocument(refundDocument);
        Refund refund = builder.build();
        assertNotNull(refund);
        assertNull(refund.getDateOfIssueRelatedDocument());
    }


    @Test
    public void testBuildWithCustomerReference2() {
        String ref2 = "REF2";
        RecordIt08 it08 = new RecordIt08();
        it08.setCustomerFileReference1("");
        it08.setCustomerFileReference2(ref2);

        RefundDocument refundDocument = new RefundDocument();
        refundDocument.addRecordIt08(it08);

        RefundBuilder builder = new RefundBuilder();
        builder.setRefundDocument(refundDocument);
        Refund refund = builder.build();
        assertThat(refund.getCustomerFileReference(), is(ref2));
    }


    private RecordIt02 getBlankIt02() {
        RecordIt02 it02 = new RecordIt02();
        it02.setAgentNumericCode("");
        it02.setTicketingAirlineCodeNumber("");
        it02.setIsoCountryCode("");
        it02.setPassengerName("");
        it02.setTicketDocumentNumber("");
        it02.setStatisticalCode("");
        it02.setSettlementAuthorisationCode("");
        it02.setRefundApplicationStatus("");
        return it02;
    }
}
