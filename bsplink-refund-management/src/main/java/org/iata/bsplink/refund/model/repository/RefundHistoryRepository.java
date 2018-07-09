package org.iata.bsplink.refund.model.repository;

import java.util.List;
import org.iata.bsplink.refund.model.entity.RefundHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundHistoryRepository extends JpaRepository<RefundHistory, Long> {
    List<RefundHistory> findByRefundId(Long id);
}
