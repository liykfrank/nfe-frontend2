package org.iata.bsplink.filemanager.model.repository;

import org.iata.bsplink.filemanager.model.entity.BsplinkConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BsplinkConfigRepository
        extends JpaRepository<BsplinkConfig, String>, CustomBsplinkFileRepository {

}
