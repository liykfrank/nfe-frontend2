package org.iata.bsplink.refund.loader.configuration;

import java.util.HashMap;
import java.util.Map;

import org.iata.bsplink.refund.loader.model.record.FieldLayout;
import org.iata.bsplink.refund.loader.model.record.RecordIt02Layout;
import org.iata.bsplink.refund.loader.model.record.RecordIt03Layout;
import org.iata.bsplink.refund.loader.model.record.RecordIt05Layout;
import org.iata.bsplink.refund.loader.model.record.RecordIt08Layout;
import org.iata.bsplink.refund.loader.model.record.RecordIt0hLayout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FieldNameToFieldLayoutConfiguration {

    /**
     * Returns the mapping of field names to field layouts used to create the error report file.
     */
    @Bean
    public Map<String, FieldLayout> fieldNameToFieldLayoutMap() {

        RecordIt02Layout recordIt02Layout = new RecordIt02Layout();
        RecordIt03Layout recordIt03Layout = new RecordIt03Layout();
        RecordIt05Layout recordIt05Layout = new RecordIt05Layout();
        RecordIt08Layout recordIt08Layout = new RecordIt08Layout();
        RecordIt0hLayout recordIt0hLayout = new RecordIt0hLayout();

        Map<String, FieldLayout> fieldLayouts = new HashMap<>();

        fieldLayouts.put("agentCode",
                recordIt02Layout.getFieldLayout("agentNumericCode"));
        fieldLayouts.put("passenger",
                recordIt02Layout.getFieldLayout("passengerName"));
        fieldLayouts.put("settlementAuthorisationCode",
                recordIt02Layout.getFieldLayout("settlementAuthorisationCode"));
        fieldLayouts.put("statisticalCode",
                recordIt02Layout.getFieldLayout("statisticalCode"));
        fieldLayouts.put("status",
                recordIt02Layout.getFieldLayout("refundApplicationStatus"));

        fieldLayouts.put("airlineCodeRelatedDocument",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentNumber1"));
        fieldLayouts.put("dateOfIssueRelatedDocument",
                recordIt03Layout.getFieldLayout("dateOfIssueRelatedDocument"));
        fieldLayouts.put("waiverCode",
                recordIt03Layout.getFieldLayout("waiverCode"));

        // relatedDocument
        fieldLayouts.put("relatedDocument.relatedTicketDocumentNumber",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentNumber1"));
        fieldLayouts.put("relatedDocument.relatedTicketCoupon1",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier1"));
        fieldLayouts.put("relatedDocument.relatedTicketCoupon2",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier1"));
        fieldLayouts.put("relatedDocument.relatedTicketCoupon3",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier1"));
        fieldLayouts.put("relatedDocument.relatedTicketCoupon4",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier1"));

        // conjunctions
        fieldLayouts.put("conjunctions[0].relatedTicketDocumentNumber",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentNumber2"));
        fieldLayouts.put("conjunctions[0].relatedTicketCoupon1",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier2"));
        fieldLayouts.put("conjunctions[0].relatedTicketCoupon2",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier2"));
        fieldLayouts.put("conjunctions[0].relatedTicketCoupon3",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier2"));
        fieldLayouts.put("conjunctions[0].relatedTicketCoupon4",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier2"));
        fieldLayouts.put("conjunctions[1].relatedTicketDocumentNumber",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentNumber3"));
        fieldLayouts.put("conjunctions[1].relatedTicketCoupon1",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier3"));
        fieldLayouts.put("conjunctions[1].relatedTicketCoupon2",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier3"));
        fieldLayouts.put("conjunctions[1].relatedTicketCoupon3",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier3"));
        fieldLayouts.put("conjunctions[1].relatedTicketCoupon4",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier3"));
        fieldLayouts.put("conjunctions[2].relatedTicketDocumentNumber",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentNumber4"));
        fieldLayouts.put("conjunctions[2].relatedTicketCoupon1",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier4"));
        fieldLayouts.put("conjunctions[2].relatedTicketCoupon2",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier4"));
        fieldLayouts.put("conjunctions[2].relatedTicketCoupon3",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier4"));
        fieldLayouts.put("conjunctions[2].relatedTicketCoupon4",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier4"));
        fieldLayouts.put("conjunctions[3].relatedTicketDocumentNumber",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentNumber5"));
        fieldLayouts.put("conjunctions[3].relatedTicketCoupon1",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier5"));
        fieldLayouts.put("conjunctions[3].relatedTicketCoupon2",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier5"));
        fieldLayouts.put("conjunctions[3].relatedTicketCoupon3",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier5"));
        fieldLayouts.put("conjunctions[3].relatedTicketCoupon4",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier5"));
        fieldLayouts.put("conjunctions[4].relatedTicketDocumentNumber",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentNumber6"));
        fieldLayouts.put("conjunctions[4].relatedTicketCoupon1",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier6"));
        fieldLayouts.put("conjunctions[4].relatedTicketCoupon2",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier6"));
        fieldLayouts.put("conjunctions[4].relatedTicketCoupon3",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier6"));
        fieldLayouts.put("conjunctions[4].relatedTicketCoupon4",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier6"));
        fieldLayouts.put("conjunctions[5].relatedTicketDocumentNumber",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentNumber7"));
        fieldLayouts.put("conjunctions[5].relatedTicketCoupon1",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier7"));
        fieldLayouts.put("conjunctions[5].relatedTicketCoupon2",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier7"));
        fieldLayouts.put("conjunctions[5].relatedTicketCoupon3",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier7"));
        fieldLayouts.put("conjunctions[5].relatedTicketCoupon4",
                recordIt03Layout.getFieldLayout("relatedTicketDocumentCouponNumberIdentifier7"));

        fieldLayouts.put("currency.code",
                recordIt05Layout.getFieldLayout("currencyType"));
        fieldLayouts.put("currency.decimals",
                recordIt05Layout.getFieldLayout("currencyType"));
        fieldLayouts.put("netReporting",
                recordIt05Layout.getFieldLayout("netReportingIndicator"));

        // amounts
        fieldLayouts.put("amounts.cancellationPenalty",
                recordIt05Layout.getFieldLayout("taxMiscellaneousFeeAmount1"));
        fieldLayouts.put("amounts.commissionAmount",
                recordIt05Layout.getFieldLayout("commissionAmount1"));
        fieldLayouts.put("amounts.commissionOnCpAndMfAmount",
                recordIt05Layout.getFieldLayout("commissionAmount2"));
        fieldLayouts.put("amounts.commissionOnCpAndMfRate",
                recordIt05Layout.getFieldLayout("commissionRate2"));
        fieldLayouts.put("amounts.commissionRate",
                recordIt05Layout.getFieldLayout("commissionRate1"));
        fieldLayouts.put("amounts.grossFare",
                recordIt05Layout.getFieldLayout("ticketDocumentAmount"));
        fieldLayouts.put("amounts.miscellaneousFee",
                recordIt05Layout.getFieldLayout("taxMiscellaneousFeeAmount1"));
        fieldLayouts.put("amounts.refundToPassenger",
                recordIt05Layout.getFieldLayout("ticketDocumentAmount"));
        fieldLayouts.put("amounts.spam",
                recordIt05Layout.getFieldLayout("commissionAmount2"));
        fieldLayouts.put("amounts.tax",
                recordIt05Layout.getFieldLayout("taxMiscellaneousFeeAmount1"));

        // taxMiscellaneousFees
        fieldLayouts.put("taxMiscellaneousFees[0].amount",
                recordIt05Layout.getFieldLayout("taxMiscellaneousFeeAmount1"));
        fieldLayouts.put("taxMiscellaneousFees[1].amount",
                recordIt05Layout.getFieldLayout("taxMiscellaneousFeeAmount2"));
        fieldLayouts.put("taxMiscellaneousFees[2].amount",
                recordIt05Layout.getFieldLayout("taxMiscellaneousFeeAmount3"));
        fieldLayouts.put("taxMiscellaneousFees[3].amount",
                recordIt05Layout.getFieldLayout("taxMiscellaneousFeeAmount4"));
        fieldLayouts.put("taxMiscellaneousFees[4].amount",
                recordIt05Layout.getFieldLayout("taxMiscellaneousFeeAmount5"));
        fieldLayouts.put("taxMiscellaneousFees[5].amount",
                recordIt05Layout.getFieldLayout("taxMiscellaneousFeeAmount6"));
        fieldLayouts.put("taxMiscellaneousFees[0].type",
                recordIt05Layout.getFieldLayout("taxMiscellaneousFeeType1"));
        fieldLayouts.put("taxMiscellaneousFees[1].type",
                recordIt05Layout.getFieldLayout("taxMiscellaneousFeeType2"));
        fieldLayouts.put("taxMiscellaneousFees[2].type",
                recordIt05Layout.getFieldLayout("taxMiscellaneousFeeType3"));
        fieldLayouts.put("taxMiscellaneousFees[3].type",
                recordIt05Layout.getFieldLayout("taxMiscellaneousFeeType4"));
        fieldLayouts.put("taxMiscellaneousFees[4].type",
                recordIt05Layout.getFieldLayout("taxMiscellaneousFeeType5"));
        fieldLayouts.put("taxMiscellaneousFees[5].type",
                recordIt05Layout.getFieldLayout("taxMiscellaneousFeeType6"));

        fieldLayouts.put("customerFileReference",
                recordIt08Layout.getFieldLayout("customerFileReference1"));

        // formOfPaymentAmounts
        fieldLayouts.put("formOfPaymentAmounts[0].amount",
                recordIt08Layout.getFieldLayout("formOfPaymentAmount1"));
        fieldLayouts.put("formOfPaymentAmounts[1].amount",
                recordIt08Layout.getFieldLayout("formOfPaymentAmount2"));
        fieldLayouts.put("formOfPaymentAmounts[0].number",
                recordIt08Layout.getFieldLayout("formOfPaymentAccountNumber1"));
        fieldLayouts.put("formOfPaymentAmounts[1].number",
                recordIt08Layout.getFieldLayout("formOfPaymentAccountNumber2"));
        fieldLayouts.put("formOfPaymentAmounts[0].type",
                recordIt08Layout.getFieldLayout("formOfPaymentType1"));
        fieldLayouts.put("formOfPaymentAmounts[1].type",
                recordIt08Layout.getFieldLayout("formOfPaymentType2"));
        fieldLayouts.put("formOfPaymentAmounts[0].vendorCode",
                recordIt08Layout.getFieldLayout("formOfPaymentType1"));
        fieldLayouts.put("formOfPaymentAmounts[1].vendorCode",
                recordIt08Layout.getFieldLayout("formOfPaymentType2"));

        fieldLayouts.put("airlineRemark",
                recordIt0hLayout.getFieldLayout("reasonForMemoInformation1"));

        return fieldLayouts;

    }

}
