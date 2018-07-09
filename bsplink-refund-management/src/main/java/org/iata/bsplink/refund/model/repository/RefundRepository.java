package org.iata.bsplink.refund.model.repository;

import org.iata.bsplink.refund.model.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund, Long> {
}
