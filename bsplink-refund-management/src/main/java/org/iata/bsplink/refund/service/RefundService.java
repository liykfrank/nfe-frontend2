package org.iata.bsplink.refund.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.java.Log;
import org.iata.bsplink.refund.exception.ActionNotAllowedForRefundStatus;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.model.entity.RefundAction;
import org.iata.bsplink.refund.model.entity.RefundHistory;
import org.iata.bsplink.refund.model.entity.RefundStatus;
import org.iata.bsplink.refund.model.repository.RefundRepository;
import org.springframework.stereotype.Service;

@Log
@Service
public class RefundService {

    private RefundRepository refundRepository;
    private RefundHistoryService refundHistoryService;

    /**
     * Constructor with RefundRepository and RefundHistoryService.
     *
     * @param refundRepository RefundRepository
     * @param refundHistoryService RefundHistoryService
     */
    public RefundService(RefundRepository refundRepository,
            RefundHistoryService refundHistoryService) {
        this.refundRepository = refundRepository;
        this.refundHistoryService = refundHistoryService;
    }

    public Optional<Refund> findById(Long id) {
        return refundRepository.findById(id);
    }

    /**
     * Retrieves refund by Ticket Document Number, Country and Airline.
     */
    public Optional<Refund> findByIsoCountryCodeAndAirlineCodeAndTicketDocumentNumber(
            String isoCountryCode, String airlineCode, String ticketDocumentNumber) {
        if (!ticketDocumentNumber.matches("^\\d{1,10}$")) {
            return Optional.empty();
        }
        Long id = Long.parseLong(ticketDocumentNumber);
        Optional<Refund> refund = findById(id);
        if (refund.isPresent()) {
            if (!refund.get().getIsoCountryCode().equals(isoCountryCode)) {
                return Optional.empty();
            }
            if (!refund.get().getAirlineCode().equals(airlineCode)) {
                return Optional.empty();
            }
        }
        return refund;
    }

    public List<Refund> findAll() {
        return refundRepository.findAll();
    }

    /**
     * Persists a Refund.
     */
    public Refund saveIndirectRefund(Refund refund) {

        log.info("Saving refund : " + refund);

        Refund refundSaved = refundRepository.save(refund);

        refundSaved.setHistory(saveRefundHistory(refundSaved, RefundAction.REFUND_ISSUE));

        return refundSaved;

    }


    /**
     * Updates a Refund.
     */
    public Refund updateIndirectRefund(Refund refund) {
        return updateIndirectRefund(refund, null);
    }

    /**
     * Updates a Refund.
     */
    public Refund updateIndirectRefund(Refund refund, String fileName) {

        log.info("Updating refund : " + refund);

        refund.setHistory(getRefundHistoryToUpdate(refund, RefundAction.MODIFY));

        if (fileName != null) {
            saveRefundHistory(refund, RefundAction.MASSLOAD_UPDATE, fileName);
        } else {
            saveRefundHistory(refund, RefundAction.MODIFY);
        }

        log.info("Getting last updated Refund to retrieve");

        return refundRepository.save(refund);
    }

    /**
     * Deletes a refund, if the status permits deletion.
     */
    public void deleteIndirectRefund(Refund refund) {
        if (!refund.getStatus().equals(RefundStatus.DRAFT)
                && !refund.getStatus().equals(RefundStatus.PENDING)) {

            throw new ActionNotAllowedForRefundStatus();
        }
        refundRepository.delete(refund);
    }

    private List<RefundHistory> saveRefundHistory(Refund refundSaved, RefundAction action) {
        return saveRefundHistory(refundSaved, action, null);
    }


    private List<RefundHistory> saveRefundHistory(Refund refundSaved, RefundAction action,
            String fileName) {

        log.info("Saving refund history for: " + refundSaved + " Action: " + action);

        RefundHistory refundHistory = new RefundHistory();
        refundHistory.setAction(action);
        refundHistory.setInsertDateTime(Instant.now());
        refundHistory.setRefundId(refundSaved.getId());
        refundHistory.setFileName(fileName);

        RefundHistory refundHistorysaved = refundHistoryService.save(refundHistory);

        log.info("Refund history saved: " + refundHistorysaved);

        return refundHistoryService.findByRefundId(refundSaved.getId());
    }

    private List<RefundHistory> getRefundHistoryToUpdate(Refund refundSaved, RefundAction action) {

        log.info("Prepare refund history for: " + refundSaved + " Action: " + action);

        RefundHistory refundHistory = new RefundHistory();
        refundHistory.setAction(action);
        refundHistory.setInsertDateTime(Instant.now());
        refundHistory.setRefundId(refundSaved.getId());

        if (null == refundSaved.getHistory()) {
            List<RefundHistory> historyList = new ArrayList<>();
            historyList.add(refundHistory);
            refundSaved.setHistory(historyList);
        } else {
            refundSaved.getHistory().add(refundHistory);
        }

        return refundSaved.getHistory();
    }
}
