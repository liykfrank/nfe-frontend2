package org.iata.bsplink.refund.model.repository;

import java.util.List;

import org.iata.bsplink.refund.model.entity.BsplinkFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BsplinkFileRepository extends JpaRepository<BsplinkFile, Long> {
    List<BsplinkFile> findByRefundId(Long id);
}
