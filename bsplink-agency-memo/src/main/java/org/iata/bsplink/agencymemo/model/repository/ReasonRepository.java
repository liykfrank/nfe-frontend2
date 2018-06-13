package org.iata.bsplink.agencymemo.model.repository;

import org.iata.bsplink.agencymemo.model.entity.Reason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReasonRepository extends JpaRepository<Reason, Long> {
}
