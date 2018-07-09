package org.iata.bsplink.refund.model.repository;

import java.util.List;
import java.util.Optional;

import org.iata.bsplink.refund.model.entity.RefundIssuePermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundIssuePermissionRepository
    extends JpaRepository<RefundIssuePermission, Long> {

    public Optional<RefundIssuePermission> findByIsoCountryCodeAndAirlineCodeAndAgentCode(
            String isoCountryCode, String airlineCode, String agentCode);

    public List<RefundIssuePermission> findByIsoCountryCodeAndAirlineCode(
            String isoCountryCode, String airlineCode);
}
