package org.iata.bsplink.agencymemo.model.repository;

import java.util.List;

import org.iata.bsplink.agencymemo.model.entity.Reason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReasonRepository extends JpaRepository<Reason, Long> {

    public List<Reason> findByIsoCountryCode(String isoCountryCode);
}
