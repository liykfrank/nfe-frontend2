package org.iata.bsplink.refund.service;

import java.util.List;

import lombok.extern.java.Log;

import org.iata.bsplink.refund.model.entity.RefundHistory;
import org.iata.bsplink.refund.model.repository.RefundHistoryRepository;
import org.springframework.stereotype.Service;

@Log
@Service
public class RefundHistoryService {

    private RefundHistoryRepository refundHistoryRepository;

    public RefundHistoryService(RefundHistoryRepository refundHistoryRepository) {
        this.refundHistoryRepository = refundHistoryRepository;
    }

    /**
     * Save refund history.
     * @param refundHistory RefundHistory
     * @return RefundHistory
     */
    public RefundHistory save(RefundHistory refundHistory) {

        log.info("Saving refund history: " + refundHistory);

        return refundHistoryRepository.save(refundHistory);
    }

    /**
     * Get history by refund id.
     * @param id Long
     * @return RefundHistory
     */
    public List<RefundHistory> findByRefundId(Long id) {

        log.info("Getting refund history by id: " + id);

        return refundHistoryRepository.findByRefundId(id);
    }

}
