package org.iata.bsplink.agencymemo.model.repository;

import java.util.List;

import org.iata.bsplink.agencymemo.model.entity.BsplinkFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BsplinkFileRepository extends JpaRepository<BsplinkFile, Long> {
    List<BsplinkFile> findByAcdmId(Long id);
}