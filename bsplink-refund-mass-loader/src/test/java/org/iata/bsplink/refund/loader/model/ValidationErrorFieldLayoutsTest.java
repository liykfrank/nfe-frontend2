package org.iata.bsplink.refund.loader.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.iata.bsplink.refund.loader.model.record.FieldLayout;
import org.iata.bsplink.refund.loader.model.record.RecordIdentifier;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class ValidationErrorFieldLayoutsTest {

    @Test
    @Parameters
    public void testGetsFieldLayout(String validationFieldName, RecordIdentifier recordIdentifier,
            String abbreviation, Integer elementNumber) {

        FieldLayout fieldLayout = ValidationErrorFieldLayouts.get(validationFieldName);

        assertThat(fieldLayout.getRecordIdentifier(), equalTo(recordIdentifier));
        assertThat(fieldLayout.getAbbreviation(), equalTo(abbreviation));
        assertThat(fieldLayout.getElementNumber(), equalTo(elementNumber));
    }

    @SuppressWarnings("unused")
    private Object[][] parametersForTestGetsFieldLayout() {

        return new Object[][] {

            /** IT02 **/

            { "agentCode", RecordIdentifier.IT02, "AGTN", 3 },
            { "passenger", RecordIdentifier.IT02, "PXNM", 16 },
            { "settlementAuthorisationCode", RecordIdentifier.IT02, "ESAC", 32 },
            { "statisticalCode", RecordIdentifier.IT02, "STAT", 7 },
            { "status", RecordIdentifier.IT02, "RFAS", 37 },

            /** IT03 **/

            { "airlineCodeRelatedDocument", RecordIdentifier.IT03, "RTDN", 4 },
            { "dateOfIssueRelatedDocument", RecordIdentifier.IT03, "DIRD", 24 },
            { "waiverCode", RecordIdentifier.IT03, "WAVR", 26 },
            { "relatedDocument.relatedTicketDocumentNumber", RecordIdentifier.IT03, "RTDN", 4 },
            { "relatedDocument.relatedTicketCoupon1", RecordIdentifier.IT03, "RCPN", 3 },
            { "relatedDocument.relatedTicketCoupon2", RecordIdentifier.IT03, "RCPN", 3 },
            { "relatedDocument.relatedTicketCoupon3", RecordIdentifier.IT03, "RCPN", 3 },
            { "relatedDocument.relatedTicketCoupon4", RecordIdentifier.IT03, "RCPN", 3 },
            { "conjunctions", RecordIdentifier.IT03, "RCPN", 3 },
            { "conjunctions[0].relatedTicketDocumentNumber", RecordIdentifier.IT03, "RTDN", 7 },
            { "conjunctions[0].relatedTicketCoupon1", RecordIdentifier.IT03, "RCPN", 6 },
            { "conjunctions[0].relatedTicketCoupon2", RecordIdentifier.IT03, "RCPN", 6 },
            { "conjunctions[0].relatedTicketCoupon3", RecordIdentifier.IT03, "RCPN", 6 },
            { "conjunctions[0].relatedTicketCoupon4", RecordIdentifier.IT03, "RCPN", 6 },
            { "conjunctions[1].relatedTicketDocumentNumber", RecordIdentifier.IT03, "RTDN", 10 },
            { "conjunctions[1].relatedTicketCoupon1", RecordIdentifier.IT03, "RCPN", 9 },
            { "conjunctions[1].relatedTicketCoupon2", RecordIdentifier.IT03, "RCPN", 9 },
            { "conjunctions[1].relatedTicketCoupon3", RecordIdentifier.IT03, "RCPN", 9 },
            { "conjunctions[1].relatedTicketCoupon4", RecordIdentifier.IT03, "RCPN", 9 },
            { "conjunctions[2].relatedTicketDocumentNumber", RecordIdentifier.IT03, "RTDN", 13 },
            { "conjunctions[2].relatedTicketCoupon1", RecordIdentifier.IT03, "RCPN", 12 },
            { "conjunctions[2].relatedTicketCoupon2", RecordIdentifier.IT03, "RCPN", 12 },
            { "conjunctions[2].relatedTicketCoupon3", RecordIdentifier.IT03, "RCPN", 12 },
            { "conjunctions[2].relatedTicketCoupon4", RecordIdentifier.IT03, "RCPN", 12 },
            { "conjunctions[3].relatedTicketDocumentNumber", RecordIdentifier.IT03, "RTDN", 16 },
            { "conjunctions[3].relatedTicketCoupon1", RecordIdentifier.IT03, "RCPN", 15 },
            { "conjunctions[3].relatedTicketCoupon2", RecordIdentifier.IT03, "RCPN", 15 },
            { "conjunctions[3].relatedTicketCoupon3", RecordIdentifier.IT03, "RCPN", 15 },
            { "conjunctions[3].relatedTicketCoupon4", RecordIdentifier.IT03, "RCPN", 15 },
            { "conjunctions[4].relatedTicketDocumentNumber", RecordIdentifier.IT03, "RTDN", 19 },
            { "conjunctions[4].relatedTicketCoupon1", RecordIdentifier.IT03, "RCPN", 18 },
            { "conjunctions[4].relatedTicketCoupon2", RecordIdentifier.IT03, "RCPN", 18 },
            { "conjunctions[4].relatedTicketCoupon3", RecordIdentifier.IT03, "RCPN", 18 },
            { "conjunctions[4].relatedTicketCoupon4", RecordIdentifier.IT03, "RCPN", 18 },
            { "conjunctions[5].relatedTicketDocumentNumber", RecordIdentifier.IT03, "RTDN", 22 },
            { "conjunctions[5].relatedTicketCoupon1", RecordIdentifier.IT03, "RCPN", 21 },
            { "conjunctions[5].relatedTicketCoupon2", RecordIdentifier.IT03, "RCPN", 21 },
            { "conjunctions[5].relatedTicketCoupon3", RecordIdentifier.IT03, "RCPN", 21 },
            { "conjunctions[5].relatedTicketCoupon4", RecordIdentifier.IT03, "RCPN", 21 },

            /** IT05 **/

            { "currency.code", RecordIdentifier.IT05, "CUTP", 12 },
            { "currency.decimals", RecordIdentifier.IT05, "CUTP", 12 },
            { "netReporting", RecordIdentifier.IT05, "NRID", 20 },
            { "amounts.cancellationPenalty", RecordIdentifier.IT05, "TMFA", 6 },
            { "amounts.commissionAmount", RecordIdentifier.IT05, "COAM", 23 },
            { "amounts.commissionOnCpAndMfAmount", RecordIdentifier.IT05, "COAM", 26 },
            { "amounts.commissionOnCpAndMfRate", RecordIdentifier.IT05, "CORT", 25 },
            { "amounts.commissionRate", RecordIdentifier.IT05, "CORT", 22 },
            { "amounts.grossFare", RecordIdentifier.IT05, "TDAM", 11 },
            { "amounts.miscellaneousFee", RecordIdentifier.IT05, "TMFA", 6 },
            { "amounts.refundToPassenger", RecordIdentifier.IT05, "TDAM", 11 },
            { "amounts.spam", RecordIdentifier.IT05, "COAM", 26 },
            { "amounts.tax", RecordIdentifier.IT05, "TMFA", 6 },
            { "taxMiscellaneousFees[0].amount", RecordIdentifier.IT05, "TMFA", 6 },
            { "taxMiscellaneousFees[1].amount", RecordIdentifier.IT05, "TMFA", 8 },
            { "taxMiscellaneousFees[2].amount", RecordIdentifier.IT05, "TMFA", 10 },
            { "taxMiscellaneousFees[3].amount", RecordIdentifier.IT05, "TMFA", 16 },
            { "taxMiscellaneousFees[4].amount", RecordIdentifier.IT05, "TMFA", 18 },
            { "taxMiscellaneousFees[5].amount", RecordIdentifier.IT05, "TMFA", 20 },
            { "taxMiscellaneousFees[0].type", RecordIdentifier.IT05, "TMFT", 5 },
            { "taxMiscellaneousFees[1].type", RecordIdentifier.IT05, "TMFT", 7 },
            { "taxMiscellaneousFees[2].type", RecordIdentifier.IT05, "TMFT", 9 },
            { "taxMiscellaneousFees[3].type", RecordIdentifier.IT05, "TMFT", 15 },
            { "taxMiscellaneousFees[4].type", RecordIdentifier.IT05, "TMFT", 17 },
            { "taxMiscellaneousFees[5].type", RecordIdentifier.IT05, "TMFT", 19 },

            /** IT08 **/

            { "customerFileReference", RecordIdentifier.IT08, "CSTF", 10 },
            { "formOfPaymentAmounts[0].amount", RecordIdentifier.IT08, "FPAM", 4 },
            { "formOfPaymentAmounts[1].amount", RecordIdentifier.IT08, "FPAM", 17 },
            { "formOfPaymentAmounts[0].number", RecordIdentifier.IT08, "FPAC", 3 },
            { "formOfPaymentAmounts[1].number", RecordIdentifier.IT08, "FPAC", 16 },
            { "formOfPaymentAmounts[0].type", RecordIdentifier.IT08, "FPTP", 8 },
            { "formOfPaymentAmounts[1].type", RecordIdentifier.IT08, "FPTP", 21 },
            { "formOfPaymentAmounts[0].vendorCode", RecordIdentifier.IT08, "FPTP", 8 },
            { "formOfPaymentAmounts[1].vendorCode", RecordIdentifier.IT08, "FPTP", 21 },

            /** IT0h **/

            { "airlineRemark", RecordIdentifier.IT0H, "RMIN", 4 }
        };
    }

    @Test
    public void testExists() {

        assertThat(ValidationErrorFieldLayouts.exists("agentCode"), equalTo(true));
        assertThat(ValidationErrorFieldLayouts.exists("foo"), equalTo(false));
    }

}
