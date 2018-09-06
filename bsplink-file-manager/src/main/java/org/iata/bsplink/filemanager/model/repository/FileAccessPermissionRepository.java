package org.iata.bsplink.filemanager.model.repository;

import java.util.List;

import org.iata.bsplink.filemanager.model.entity.FileAccessPermission;
import org.iata.bsplink.filemanager.model.entity.FileAccessType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileAccessPermissionRepository extends JpaRepository<FileAccessPermission, Long> {

    public List<FileAccessPermission> findByUser(String user);

    public boolean existsByUserAndIsoCountryCodeAndFileTypeAndAccess(
            String user, String isoCountryCode, String fileType, FileAccessType access);
}
