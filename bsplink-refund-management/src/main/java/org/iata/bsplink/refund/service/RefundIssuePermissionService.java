package org.iata.bsplink.refund.service;

import java.util.List;
import java.util.Optional;

import org.iata.bsplink.refund.model.entity.RefundIssuePermission;
import org.iata.bsplink.refund.model.repository.RefundIssuePermissionRepository;
import org.springframework.stereotype.Service;

@Service
public class RefundIssuePermissionService {

    private RefundIssuePermissionRepository refundIssuePermissionRepository;

    public RefundIssuePermissionService(
            RefundIssuePermissionRepository refundIssuePermissionRepository) {
        this.refundIssuePermissionRepository = refundIssuePermissionRepository;
    }

    public RefundIssuePermission save(RefundIssuePermission permission) {
        return refundIssuePermissionRepository.save(permission);
    }

    public List<RefundIssuePermission> findAll() {
        return refundIssuePermissionRepository.findAll();
    }

    public void delete(RefundIssuePermission permission) {
        refundIssuePermissionRepository.delete(permission);
    }

    public Optional<RefundIssuePermission> findById(Long id) {
        return refundIssuePermissionRepository.findById(id);
    }

    public Optional<RefundIssuePermission> findByIsoCountryCodeAndAirlineCodeAndAgentCode(
            String isoCountryCode, String airlineCode, String agentCode) {
        return refundIssuePermissionRepository.findByIsoCountryCodeAndAirlineCodeAndAgentCode(
                isoCountryCode, airlineCode, agentCode);
    }

    public List<RefundIssuePermission> findByIsoCountryCodeAndAirlineCode(
            String isoCountryCode, String airlineCode) {
        return refundIssuePermissionRepository.findByIsoCountryCodeAndAirlineCode(
                isoCountryCode, airlineCode);
    }


}
