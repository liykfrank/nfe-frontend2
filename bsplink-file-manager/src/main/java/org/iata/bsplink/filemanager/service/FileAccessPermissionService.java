package org.iata.bsplink.filemanager.service;

import java.util.List;

import org.iata.bsplink.filemanager.model.entity.BsplinkFile;
import org.iata.bsplink.filemanager.model.entity.FileAccessPermission;
import org.iata.bsplink.filemanager.model.entity.FileAccessType;
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


    /**
     * Checks if user has permission to access the file.
     */
    public boolean isFileAccessPermittedForUser(
            String fileName,
            FileAccessType fileAccess,
            String user) {

        if (fileName == null || user == null || fileName.length() < 4) {

            return false;
        }

        FileAccessPermission fap = new FileAccessPermission();
        fap.setAccess(fileAccess);
        fap.setFileType(fileName.substring(2, 4));
        fap.setIsoCountryCode(fileName.substring(0, 2));
        fap.setUser(user);
        return existsByUserAndIsoCountryCodeAndFileTypeAndAccess(fap);
    }


    public boolean isBsplinkFileAccessPermittedForUser(
            BsplinkFile file,
            FileAccessType fileAccess,
            String user) {

        return isFileAccessPermittedForUser(file.getName(), fileAccess, user);
    }


    /**
     * Checks if user has access permission for all the files.
     */
    public boolean isBsplinkFilesAccessPermittedForUser(
            List<BsplinkFile> files,
            FileAccessType fileAccess,
            String user) {

        return files.stream().allMatch(file
            -> isBsplinkFileAccessPermittedForUser(file, fileAccess, user));
    }
}
