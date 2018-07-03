package org.iata.bsplink.filemanager.model.repository;

import java.util.List;

import org.iata.bsplink.filemanager.model.entity.BsplinkFile;
import org.iata.bsplink.filemanager.model.entity.BsplinkFileStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BsplinkFileRepository
        extends JpaRepository<BsplinkFile, Long>, CustomBsplinkFileRepository {

    List<BsplinkFile> findByName(String name);

    List<BsplinkFile> findByNameAndStatus(String name, BsplinkFileStatus status);
}
