package org.iata.bsplink.filemanager.service;

import java.util.List;

import org.iata.bsplink.filemanager.model.entity.FileAccessPermission;
import org.iata.bsplink.filemanager.model.repository.FileAccessPermissionRepository;
import org.springframework.stereotype.Service;

@Service
public class FileAccessPermissionService  {

    private FileAccessPermissionRepository repository;

    public FileAccessPermissionService(FileAccessPermissionRepository repository) {

        this.repository = repository;
    }


    public List<FileAccessPermission> findByUser(String user) {

        return repository.findByUser(user);
    }


    /**
     * Returns true if the user has an Access Permission
     *  of the indicated file type and access type in the indicated country.
     */
    public boolean existsByUserAndIsoCountryCodeAndFileTypeAndAccess(
            FileAccessPermission fileAccessPermission) {

        return repository.existsByUserAndIsoCountryCodeAndFileTypeAndAccess(
                fileAccessPermission.getUser(),
                fileAccessPermission.getIsoCountryCode(),
                fileAccessPermission.getFileType(),
                fileAccessPermission.getAccess());
    }


    /**
     * Creates a File Access Permission.
     */
    public FileAccessPermission create(FileAccessPermission fileAccessPermission) {

        fileAccessPermission.setId(null);
        return repository.save(fileAccessPermission);
    }


    public void delete(FileAccessPermission fileAccessPermission) {

        repository.delete(fileAccessPermission);
    }



    public List<FileAccessPermission> findAll() {

        return repository.findAll();
    }
}
