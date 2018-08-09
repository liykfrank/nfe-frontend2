package org.iata.bsplink.refund.model.repository;

import java.util.List;

import org.iata.bsplink.refund.model.entity.Reason;
import org.iata.bsplink.refund.model.entity.ReasonType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReasonRepository extends JpaRepository<Reason, Long> {

    public List<Reason> findByIsoCountryCode(String isoCountryCode);

    public List<Reason> findByIsoCountryCodeAndType(String isoCountryCode, ReasonType type);
}
