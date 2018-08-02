package org.iata.bsplink.refund.loader.configuration;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.iata.bsplink.refund.loader.model.record.FieldLayout;
import org.junit.Test;

public class FieldNameToFieldLayoutConfigurationTest {

    private Map<String, FieldLayout> configuration =
            new FieldNameToFieldLayoutConfiguration().fieldNameToFieldLayoutMap();

    @Test
    public void testFieldsAreMappedToExpectedLayouts() {

        assertExpectedLayout("agentCode", "AGTN", 3);
        assertExpectedLayout("passenger", "PXNM", 16);
        assertExpectedLayout("settlementAuthorisationCode", "ESAC", 32);
        assertExpectedLayout("statisticalCode", "STAT", 7);
        assertExpectedLayout("status", "RFAS", 37);
        assertExpectedLayout("airlineCodeRelatedDocument", "RTDN", 4);
        assertExpectedLayout("dateOfIssueRelatedDocument", "DIRD", 24);
        assertExpectedLayout("waiverCode", "WAVR", 26);

        assertExpectedLayout("relatedDocument.relatedTicketDocumentNumber", "RTDN", 4);
        assertExpectedLayout("relatedDocument.relatedTicketCoupon1", "RCPN", 3);
        assertExpectedLayout("relatedDocument.relatedTicketCoupon2", "RCPN", 3);
        assertExpectedLayout("relatedDocument.relatedTicketCoupon3", "RCPN", 3);
        assertExpectedLayout("relatedDocument.relatedTicketCoupon4", "RCPN", 3);

        assertExpectedLayout("conjunctions[0].relatedTicketDocumentNumber", "RTDN", 7);
        assertExpectedLayout("conjunctions[0].relatedTicketCoupon1", "RCPN", 6);
        assertExpectedLayout("conjunctions[0].relatedTicketCoupon2", "RCPN", 6);
        assertExpectedLayout("conjunctions[0].relatedTicketCoupon3", "RCPN", 6);
        assertExpectedLayout("conjunctions[0].relatedTicketCoupon4", "RCPN", 6);

        assertExpectedLayout("conjunctions[1].relatedTicketDocumentNumber", "RTDN", 10);
        assertExpectedLayout("conjunctions[1].relatedTicketCoupon1", "RCPN", 9);
        assertExpectedLayout("conjunctions[1].relatedTicketCoupon2", "RCPN", 9);
        assertExpectedLayout("conjunctions[1].relatedTicketCoupon3", "RCPN", 9);
        assertExpectedLayout("conjunctions[1].relatedTicketCoupon4", "RCPN", 9);

        assertExpectedLayout("conjunctions[2].relatedTicketDocumentNumber", "RTDN", 13);
        assertExpectedLayout("conjunctions[2].relatedTicketCoupon1", "RCPN", 12);
        assertExpectedLayout("conjunctions[2].relatedTicketCoupon2", "RCPN", 12);
        assertExpectedLayout("conjunctions[2].relatedTicketCoupon3", "RCPN", 12);
        assertExpectedLayout("conjunctions[2].relatedTicketCoupon4", "RCPN", 12);

        assertExpectedLayout("conjunctions[3].relatedTicketDocumentNumber", "RTDN", 16);
        assertExpectedLayout("conjunctions[3].relatedTicketCoupon1", "RCPN", 15);
        assertExpectedLayout("conjunctions[3].relatedTicketCoupon2", "RCPN", 15);
        assertExpectedLayout("conjunctions[3].relatedTicketCoupon3", "RCPN", 15);
        assertExpectedLayout("conjunctions[3].relatedTicketCoupon4", "RCPN", 15);

        assertExpectedLayout("conjunctions[4].relatedTicketDocumentNumber", "RTDN", 19);
        assertExpectedLayout("conjunctions[4].relatedTicketCoupon1", "RCPN", 18);
        assertExpectedLayout("conjunctions[4].relatedTicketCoupon2", "RCPN", 18);
        assertExpectedLayout("conjunctions[4].relatedTicketCoupon3", "RCPN", 18);
        assertExpectedLayout("conjunctions[4].relatedTicketCoupon4", "RCPN", 18);

        assertExpectedLayout("conjunctions[5].relatedTicketDocumentNumber", "RTDN", 22);
        assertExpectedLayout("conjunctions[5].relatedTicketCoupon1", "RCPN", 21);
        assertExpectedLayout("conjunctions[5].relatedTicketCoupon2", "RCPN", 21);
        assertExpectedLayout("conjunctions[5].relatedTicketCoupon3", "RCPN", 21);
        assertExpectedLayout("conjunctions[5].relatedTicketCoupon4", "RCPN", 21);

        assertExpectedLayout("currency.code", "CUTP", 12);
        assertExpectedLayout("currency.decimals", "CUTP", 12);
        assertExpectedLayout("netReporting", "NRID", 20);

        assertExpectedLayout("amounts.cancellationPenalty", "TMFA", 6);
        assertExpectedLayout("amounts.commissionAmount", "COAM", 23);
        assertExpectedLayout("amounts.commissionOnCpAndMfAmount", "COAM", 26);
        assertExpectedLayout("amounts.commissionOnCpAndMfRate", "CORT", 25);
        assertExpectedLayout("amounts.commissionRate", "CORT", 22);
        assertExpectedLayout("amounts.grossFare", "TDAM", 11);
        assertExpectedLayout("amounts.miscellaneousFee", "TMFA", 6);
        assertExpectedLayout("amounts.refundToPassenger", "TDAM", 11);
        assertExpectedLayout("amounts.spam", "COAM", 26);
        assertExpectedLayout("amounts.tax", "TMFA", 6);

        assertExpectedLayout("taxMiscellaneousFees[0].amount", "TMFA", 6);
        assertExpectedLayout("taxMiscellaneousFees[1].amount", "TMFA", 8);
        assertExpectedLayout("taxMiscellaneousFees[2].amount", "TMFA", 10);
        assertExpectedLayout("taxMiscellaneousFees[3].amount", "TMFA", 16);
        assertExpectedLayout("taxMiscellaneousFees[4].amount", "TMFA", 18);
        assertExpectedLayout("taxMiscellaneousFees[5].amount", "TMFA", 20);

        assertExpectedLayout("taxMiscellaneousFees[0].type", "TMFT", 5);
        assertExpectedLayout("taxMiscellaneousFees[1].type", "TMFT", 7);
        assertExpectedLayout("taxMiscellaneousFees[2].type", "TMFT", 9);
        assertExpectedLayout("taxMiscellaneousFees[3].type", "TMFT", 15);
        assertExpectedLayout("taxMiscellaneousFees[4].type", "TMFT", 17);
        assertExpectedLayout("taxMiscellaneousFees[5].type", "TMFT", 19);

        assertExpectedLayout("customerFileReference", "CSTF", 10);

        assertExpectedLayout("formOfPaymentAmounts[0].amount", "FPAM", 4);
        assertExpectedLayout("formOfPaymentAmounts[1].amount", "FPAM", 17);
        assertExpectedLayout("formOfPaymentAmounts[0].number", "FPAC", 3);
        assertExpectedLayout("formOfPaymentAmounts[1].number", "FPAC", 16);
        assertExpectedLayout("formOfPaymentAmounts[0].type", "FPTP", 8);
        assertExpectedLayout("formOfPaymentAmounts[1].type", "FPTP", 21);
        assertExpectedLayout("formOfPaymentAmounts[0].vendorCode", "FPTP", 8);
        assertExpectedLayout("formOfPaymentAmounts[1].vendorCode", "FPTP", 21);

        assertExpectedLayout("airlineRemark", "RMIN", 4);
    }

    private void assertExpectedLayout(String fieldName, String abbreviation, int elementNumber) {

        assertEquals(abbreviation, configuration.get(fieldName).getAbbreviation());
        assertEquals(elementNumber, (int)configuration.get(fieldName).getElementNumber());
    }

}
