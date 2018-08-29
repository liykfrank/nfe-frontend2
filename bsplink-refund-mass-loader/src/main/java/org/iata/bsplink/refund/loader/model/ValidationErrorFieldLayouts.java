package org.iata.bsplink.refund.loader.model;

import java.util.HashMap;
import java.util.Map;

import org.iata.bsplink.refund.loader.model.record.FieldLayout;
import org.iata.bsplink.refund.loader.model.record.RecordIt02Layout;
import org.iata.bsplink.refund.loader.model.record.RecordIt03Layout;
import org.iata.bsplink.refund.loader.model.record.RecordIt05Layout;
import org.iata.bsplink.refund.loader.model.record.RecordIt08Layout;
import org.iata.bsplink.refund.loader.model.record.RecordIt0hLayout;
import org.iata.bsplink.refund.loader.model.record.RecordLayout;

/**
 * Relation between the field names received in validation errors from the REST API and its layouts.
 */
public class ValidationErrorFieldLayouts {

    protected static final Map<String, FieldLayout> FIELD_LAYOUTS = new HashMap<>();

    protected static final RecordIt02Layout RECORD_IT02_LAYOUT = new RecordIt02Layout();
    protected static final RecordIt03Layout RECORD_IT03_LAYOUT = new RecordIt03Layout();
    protected static final RecordIt05Layout RECORD_IT05_LAYOUT = new RecordIt05Layout();
    protected static final RecordIt08Layout RECORD_IT08_LAYOUT = new RecordIt08Layout();
    protected static final RecordIt0hLayout RECORD_IT0H_LAYOUT = new RecordIt0hLayout();

    private static final String RCPN_BASE_FIELD_NAME =
            "relatedTicketDocumentCouponNumberIdentifier";

    private static final String RCPN1_FIELD_NAME = RCPN_BASE_FIELD_NAME + 1;
    private static final String RCPN2_FIELD_NAME = RCPN_BASE_FIELD_NAME + 2;
    private static final String RCPN3_FIELD_NAME = RCPN_BASE_FIELD_NAME + 3;
    private static final String RCPN4_FIELD_NAME = RCPN_BASE_FIELD_NAME + 4;
    private static final String RCPN5_FIELD_NAME = RCPN_BASE_FIELD_NAME + 5;
    private static final String RCPN6_FIELD_NAME = RCPN_BASE_FIELD_NAME + 6;
    private static final String RCPN7_FIELD_NAME = RCPN_BASE_FIELD_NAME + 7;

    private static final String TMFA1_FIELD_NAME = "taxMiscellaneousFeeAmount1";

    static {

        /** IT02 **/

        addFieldLayoutIt02("agentCode", "agentNumericCode");
        addFieldLayoutIt02("passenger", "passengerName");
        addFieldLayoutIt02("settlementAuthorisationCode", "settlementAuthorisationCode");
        addFieldLayoutIt02("statisticalCode", "statisticalCode");
        addFieldLayoutIt02("status", "refundApplicationStatus");

        /** IT03 **/

        addFieldLayoutIt03("airlineCodeRelatedDocument", "relatedTicketDocumentNumber1");
        addFieldLayoutIt03("dateOfIssueRelatedDocument", "dateOfIssueRelatedDocument");
        addFieldLayoutIt03("waiverCode", "waiverCode");

        // relatedDocument
        addFieldLayoutIt03("relatedDocument.relatedTicketDocumentNumber",
                "relatedTicketDocumentNumber1");
        addFieldLayoutIt03("relatedDocument.relatedTicketCoupon1", RCPN1_FIELD_NAME);
        addFieldLayoutIt03("relatedDocument.relatedTicketCoupon2", RCPN1_FIELD_NAME);
        addFieldLayoutIt03("relatedDocument.relatedTicketCoupon3", RCPN1_FIELD_NAME);
        addFieldLayoutIt03("relatedDocument.relatedTicketCoupon4", RCPN1_FIELD_NAME);

        // conjunctions
        addFieldLayoutIt03("conjunctions", RCPN1_FIELD_NAME);

        addFieldLayoutIt03("conjunctions[0].relatedTicketDocumentNumber",
                "relatedTicketDocumentNumber2");
        addFieldLayoutIt03("conjunctions[0].relatedTicketCoupon1", RCPN2_FIELD_NAME);
        addFieldLayoutIt03("conjunctions[0].relatedTicketCoupon2", RCPN2_FIELD_NAME);
        addFieldLayoutIt03("conjunctions[0].relatedTicketCoupon3", RCPN2_FIELD_NAME);
        addFieldLayoutIt03("conjunctions[0].relatedTicketCoupon4", RCPN2_FIELD_NAME);
        addFieldLayoutIt03("conjunctions[1].relatedTicketDocumentNumber",
                "relatedTicketDocumentNumber3");
        addFieldLayoutIt03("conjunctions[1].relatedTicketCoupon1", RCPN3_FIELD_NAME);
        addFieldLayoutIt03("conjunctions[1].relatedTicketCoupon2", RCPN3_FIELD_NAME);
        addFieldLayoutIt03("conjunctions[1].relatedTicketCoupon3", RCPN3_FIELD_NAME);
        addFieldLayoutIt03("conjunctions[1].relatedTicketCoupon4", RCPN3_FIELD_NAME);
        addFieldLayoutIt03("conjunctions[2].relatedTicketDocumentNumber",
                "relatedTicketDocumentNumber4");
        addFieldLayoutIt03("conjunctions[2].relatedTicketCoupon1", RCPN4_FIELD_NAME);
        addFieldLayoutIt03("conjunctions[2].relatedTicketCoupon2", RCPN4_FIELD_NAME);
        addFieldLayoutIt03("conjunctions[2].relatedTicketCoupon3", RCPN4_FIELD_NAME);
        addFieldLayoutIt03("conjunctions[2].relatedTicketCoupon4", RCPN4_FIELD_NAME);
        addFieldLayoutIt03("conjunctions[3].relatedTicketDocumentNumber",
                "relatedTicketDocumentNumber5");
        addFieldLayoutIt03("conjunctions[3].relatedTicketCoupon1", RCPN5_FIELD_NAME);
        addFieldLayoutIt03("conjunctions[3].relatedTicketCoupon2", RCPN5_FIELD_NAME);
        addFieldLayoutIt03("conjunctions[3].relatedTicketCoupon3", RCPN5_FIELD_NAME);
        addFieldLayoutIt03("conjunctions[3].relatedTicketCoupon4", RCPN5_FIELD_NAME);
        addFieldLayoutIt03("conjunctions[4].relatedTicketDocumentNumber",
                "relatedTicketDocumentNumber6");
        addFieldLayoutIt03("conjunctions[4].relatedTicketCoupon1", RCPN6_FIELD_NAME);
        addFieldLayoutIt03("conjunctions[4].relatedTicketCoupon2", RCPN6_FIELD_NAME);
        addFieldLayoutIt03("conjunctions[4].relatedTicketCoupon3", RCPN6_FIELD_NAME);
        addFieldLayoutIt03("conjunctions[4].relatedTicketCoupon4", RCPN6_FIELD_NAME);
        addFieldLayoutIt03("conjunctions[5].relatedTicketDocumentNumber",
                "relatedTicketDocumentNumber7");
        addFieldLayoutIt03("conjunctions[5].relatedTicketCoupon1", RCPN7_FIELD_NAME);
        addFieldLayoutIt03("conjunctions[5].relatedTicketCoupon2", RCPN7_FIELD_NAME);
        addFieldLayoutIt03("conjunctions[5].relatedTicketCoupon3", RCPN7_FIELD_NAME);
        addFieldLayoutIt03("conjunctions[5].relatedTicketCoupon4", RCPN7_FIELD_NAME);

        /** IT05 **/

        addFieldLayoutIt05("currency.code", "currencyType");
        addFieldLayoutIt05("currency.decimals", "currencyType");
        addFieldLayoutIt05("netReporting", "netReportingIndicator");

        // amounts
        addFieldLayoutIt05("amounts.cancellationPenalty", TMFA1_FIELD_NAME);
        addFieldLayoutIt05("amounts.commissionAmount", "commissionAmount1");
        addFieldLayoutIt05("amounts.commissionOnCpAndMfAmount", "commissionAmount2");
        addFieldLayoutIt05("amounts.commissionOnCpAndMfRate", "commissionRate2");
        addFieldLayoutIt05("amounts.commissionRate", "commissionRate1");
        addFieldLayoutIt05("amounts.grossFare", "ticketDocumentAmount");
        addFieldLayoutIt05("amounts.miscellaneousFee", TMFA1_FIELD_NAME);
        addFieldLayoutIt05("amounts.refundToPassenger", "ticketDocumentAmount");
        addFieldLayoutIt05("amounts.spam", "commissionAmount2");
        addFieldLayoutIt05("amounts.tax", TMFA1_FIELD_NAME);

        // taxMiscellaneousFees
        addFieldLayoutIt05("taxMiscellaneousFees[0].amount", TMFA1_FIELD_NAME);
        addFieldLayoutIt05("taxMiscellaneousFees[1].amount", "taxMiscellaneousFeeAmount2");
        addFieldLayoutIt05("taxMiscellaneousFees[2].amount", "taxMiscellaneousFeeAmount3");
        addFieldLayoutIt05("taxMiscellaneousFees[3].amount", "taxMiscellaneousFeeAmount4");
        addFieldLayoutIt05("taxMiscellaneousFees[4].amount", "taxMiscellaneousFeeAmount5");
        addFieldLayoutIt05("taxMiscellaneousFees[5].amount", "taxMiscellaneousFeeAmount6");
        addFieldLayoutIt05("taxMiscellaneousFees[0].type", "taxMiscellaneousFeeType1");
        addFieldLayoutIt05("taxMiscellaneousFees[1].type", "taxMiscellaneousFeeType2");
        addFieldLayoutIt05("taxMiscellaneousFees[2].type", "taxMiscellaneousFeeType3");
        addFieldLayoutIt05("taxMiscellaneousFees[3].type", "taxMiscellaneousFeeType4");
        addFieldLayoutIt05("taxMiscellaneousFees[4].type", "taxMiscellaneousFeeType5");
        addFieldLayoutIt05("taxMiscellaneousFees[5].type", "taxMiscellaneousFeeType6");

        /** IT08 **/

        addFieldLayoutIt08("customerFileReference", "customerFileReference1");

        // formOfPaymentAmounts
        addFieldLayoutIt08("formOfPaymentAmounts[0].amount", "formOfPaymentAmount1");
        addFieldLayoutIt08("formOfPaymentAmounts[1].amount", "formOfPaymentAmount2");
        addFieldLayoutIt08("formOfPaymentAmounts[0].number", "formOfPaymentAccountNumber1");
        addFieldLayoutIt08("formOfPaymentAmounts[1].number", "formOfPaymentAccountNumber2");
        addFieldLayoutIt08("formOfPaymentAmounts[0].type", "formOfPaymentType1");
        addFieldLayoutIt08("formOfPaymentAmounts[1].type", "formOfPaymentType2");
        addFieldLayoutIt08("formOfPaymentAmounts[0].vendorCode", "formOfPaymentType1");
        addFieldLayoutIt08("formOfPaymentAmounts[1].vendorCode", "formOfPaymentType2");

        /** IT0h **/

        addFieldLayoutIt0h("airlineRemark", "reasonForMemoInformation1");
    }

    private static void addFieldLayoutIt02(String validationFieldName, String layoutFieldName) {

        addFieldLayout(RECORD_IT02_LAYOUT, validationFieldName, layoutFieldName);
    }

    private static void addFieldLayoutIt03(String validationFieldName, String layoutFieldName) {

        addFieldLayout(RECORD_IT03_LAYOUT, validationFieldName, layoutFieldName);
    }

    private static void addFieldLayoutIt05(String validationFieldName, String layoutFieldName) {

        addFieldLayout(RECORD_IT05_LAYOUT, validationFieldName, layoutFieldName);
    }

    private static void addFieldLayoutIt08(String validationFieldName, String layoutFieldName) {

        addFieldLayout(RECORD_IT08_LAYOUT, validationFieldName, layoutFieldName);
    }

    private static void addFieldLayoutIt0h(String validationFieldName, String layoutFieldName) {

        addFieldLayout(RECORD_IT0H_LAYOUT, validationFieldName, layoutFieldName);
    }

    private static void addFieldLayout(RecordLayout recordLayout, String validationFieldName,
            String layoutFieldName) {

        FIELD_LAYOUTS.put(validationFieldName, recordLayout.getFieldLayout(layoutFieldName));
    }

    /**
     * Gets the fieldLayout related to the validation field name.
     */
    public static FieldLayout get(String fieldName) {

        return FIELD_LAYOUTS.get(fieldName);
    }

    /**
     * Returns true if the field name exists.
     */
    public static boolean exists(String fieldName) {

        return FIELD_LAYOUTS.containsKey(fieldName);
    }

    private ValidationErrorFieldLayouts() {
        // class only contains static values
    }
}
