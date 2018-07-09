package org.iata.bsplink.refund.loader.builder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import lombok.Setter;

import org.apache.commons.lang.StringUtils;
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
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Setter
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RefundBuilder {
    private RefundDocument refundDocument;
    private RefundAmounts amounts;
    private RefundCurrency currency;
    private List<FormOfPaymentAmount> formOfPaymentAmounts;
    private List<TaxMiscellaneousFee> taxMiscellaneousFees;
    private List<RelatedDocument> relatedDocuments;
    private String remark;

    /**
     * Creates a Refund.
     * @return The created Refund.
     */
    public Refund build() {
        Refund refund = new Refund();

        assignRefundStatus(refund);

        if (refundDocument.getRecordIt02() != null) {
            RecordIt02 it02 = refundDocument.getRecordIt02();
            refund.setAgentCode(it02.getAgentNumericCode());
            refund.setAirlineCode(it02.getTicketingAirlineCodeNumber());
            refund.setIsoCountryCode(it02.getIsoCountryCode());
            refund.setPassenger(it02.getPassengerName());
            refund.setTicketDocumentNumber(it02.getTicketDocumentNumber());
            refund.setStatisticalCode(it02.getStatisticalCode());
            refund.setSettlementAuthorisationCode(it02.getSettlementAuthorisationCode());
        }

        assignRelatedDocuments(refund);
        assignRelatedDocumentData(refund);
        assignCustomerReference(refund);
        assignRemark(refund);
        assignNetReporting(refund);

        refund.setCurrency(currency);
        refund.setFormOfPaymentAmounts(formOfPaymentAmounts);
        refund.setTaxMiscellaneousFees(taxMiscellaneousFees);
        refund.setAmounts(amounts);
        return refund;
    }



    private void assignRelatedDocuments(Refund refund) {
        if (relatedDocuments != null && !relatedDocuments.isEmpty()) {
            refund.setRelatedDocument(relatedDocuments.get(0));
            if (relatedDocuments.size() > 1) {
                refund.setConjunctions(relatedDocuments.subList(1, relatedDocuments.size()));
            }
        }
    }


    private void assignNetReporting(Refund refund) {
        if (!refundDocument.getRecordsIt05().isEmpty()) {
            RecordIt05 it05 = refundDocument.getRecordsIt05().get(0);
            refund.setNetReporting("NR".equals(it05.getNetReportingIndicator()));
        }
    }


    private void assignRemark(Refund refund) {
        if (RefundStatus.REJECTED.equals(refund.getStatus())) {
            refund.setRejectionReason(remark);
        } else {
            refund.setAirlineRemark(remark);
        }
    }


    private void assignCustomerReference(Refund refund) {
        if (refundDocument.getRecordsIt08().isEmpty()) {
            return;
        }
        RecordIt08 it08 = refundDocument.getRecordsIt08().get(0);
        if (StringUtils.isBlank(it08.getCustomerFileReference1())) {
            refund.setCustomerFileReference(it08.getCustomerFileReference2().trim());
        } else {
            refund.setCustomerFileReference(it08.getCustomerFileReference1().trim());
        }
    }


    private void assignRefundStatus(Refund refund) {
        if (refundDocument.getRecordIt02() == null
                || refundDocument.getRecordIt02().getRefundApplicationStatus().length() == 0) {
            return;
        }

        switch (refundDocument.getRecordIt02().getRefundApplicationStatus().charAt(0)) {
            case 'A':
                refund.setStatus(RefundStatus.AUTHORIZED);
                break;
            case 'U': refund.setStatus(RefundStatus.UNDER_INVESTIGATION);
                break;
            case 'R': refund.setStatus(RefundStatus.REJECTED);
                break;
            default:
        }
    }


    private void assignRelatedDocumentData(Refund refund) {
        if (refundDocument.getRecordsIt03().isEmpty()) {
            return;
        }

        RecordIt03 it03 = refundDocument.getRecordsIt03().get(0);
        String rtdn = it03.getRelatedTicketDocumentNumber1();
        refund.setAirlineCodeRelatedDocument(rtdn.substring(0, 3));
        refund.setWaiverCode(it03.getWaiverCode());

        try {
            String dird = it03.getDateOfIssueRelatedDocument();
            if (!StringUtils.isBlank(dird)) {
                refund.setDateOfIssueRelatedDocument(
                        LocalDate.parse("20" + dird, DateTimeFormatter.BASIC_ISO_DATE));
            }
        } catch (DateTimeParseException e) {
            refund.setDateOfIssueRelatedDocument(null);
        }
    }
}
