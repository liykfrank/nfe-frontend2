package org.iata.bsplink.refund.test.fixtures;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.iata.bsplink.refund.model.entity.Contact;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.model.entity.RefundAction;
import org.iata.bsplink.refund.model.entity.RefundHistory;
import org.iata.bsplink.refund.model.entity.RefundStatus;
import org.iata.bsplink.refund.model.entity.RelatedDocument;

public class RefundFixtures {

    /**
     * Returns a list of Refunds.
     */
    public static List<Refund> getRefunds() {

        Refund refund;
        refund = new Refund();
        refund.setStatus(RefundStatus.PENDING);
        refund.setIsoCountryCode("AA");
        refund.setAgentCode("78200011");
        refund.setAirlineCode("220");
        refund.setDateOfIssue(LocalDate.of(2018, 6, 19));
        refund.getCurrency().setDecimals(0);
        refund.getCurrency().setCode("ABC");
        refund.setStatisticalCode("I");
        refund.setDateOfIssueRelatedDocument(LocalDate.of(2000, 1, 2));
        refund.setAirlineCodeRelatedDocument("220");
        refund.setRelatedDocument(new RelatedDocument());
        refund.getRelatedDocument().setRelatedTicketDocumentNumber("1234567890");
        refund.getRelatedDocument().setRelatedTicketCoupon1(true);
        refund.setPassenger("PEPE");
        refund.setIssueReason("Reason for Issuance");
        refund.setTicketDocumentNumber("1234567890");

        List<Refund> refunds = new ArrayList<>();
        refunds.add(refund);

        refund = new Refund();
        refund.setStatus(RefundStatus.PENDING);
        refund.setIsoCountryCode("AA");
        refund.setAgentCode("78200022");
        refund.setAirlineCode("220");
        refund.setDateOfIssue(LocalDate.of(2018, 7, 19));
        refund.getCurrency().setDecimals(0);
        refund.getCurrency().setCode("ABC");
        refund.setStatisticalCode("I");
        refund.setDateOfIssueRelatedDocument(LocalDate.of(2000, 3, 4));
        refund.setAirlineCodeRelatedDocument("220");
        refund.setRelatedDocument(new RelatedDocument());
        refund.getRelatedDocument().setRelatedTicketDocumentNumber("1111111111");
        refund.getRelatedDocument().setRelatedTicketCoupon1(true);
        refund.getRelatedDocument().setRelatedTicketCoupon2(true);
        refund.setPassenger("JUAN");
        refund.setIssueReason("Reason for Issuance");

        refunds.add(refund);

        refund = new Refund();
        refund.setId(1L);
        refund.setStatus(RefundStatus.PENDING);
        refund.setIsoCountryCode("AA");
        refund.setAgentCode("78200022");
        refund.setAirlineCode("220");
        refund.setDateOfIssue(LocalDate.of(2018, 7, 19));
        refund.getCurrency().setDecimals(0);
        refund.getCurrency().setCode("ABC");
        refund.setStatisticalCode("I");
        refund.setDateOfIssueRelatedDocument(LocalDate.of(2000, 3, 4));
        refund.setAirlineCodeRelatedDocument("220");
        refund.setRelatedDocument(new RelatedDocument());
        refund.getRelatedDocument().setRelatedTicketDocumentNumber("1111111111");
        refund.getRelatedDocument().setRelatedTicketCoupon1(true);
        refund.getRelatedDocument().setRelatedTicketCoupon2(true);
        refund.setPassenger("JUAN");
        refund.setIssueReason("Reason for Issuance");
        refund.setTicketDocumentNumber("0000000001");
        refund.setTourCode("tourCode");
        Contact contact = new Contact();
        contact.setContactName("contact");
        contact.setEmail("email@email.com");
        contact.setPhoneFaxNumber("654321654");
        refund.setAgentContact(contact);
        refunds.add(refund);

        return refunds;
    }

    /**
     * Returns a list of history.
     *
     * @return RefundHistory
     */
    public static List<RefundHistory> getRefundHistoryList() {

        RefundHistory historyOne = new RefundHistory();
        historyOne.setId(1L);
        historyOne.setAction(RefundAction.REFUND_ISSUE);
        historyOne.setInsertDateTime(Instant.now());
        historyOne.setRefundId(1L);

        List<RefundHistory> historyList = new ArrayList<>();
        historyList.add(historyOne);

        RefundHistory historyTwo = new RefundHistory();
        historyTwo.setId(2L);
        historyTwo.setAction(RefundAction.ATTACH_FILE);
        historyTwo.setInsertDateTime(Instant.now());
        historyTwo.setRefundId(1L);
        historyList.add(historyTwo);

        return historyList;
    }
}
