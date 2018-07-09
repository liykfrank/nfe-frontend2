package org.iata.bsplink.refund.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import lombok.extern.java.Log;

import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.yadeutils.YadeUtils;
import org.iata.bsplink.yadeutils.enums.YadeOperation;
import org.iata.bsplink.yadeutils.enums.YadeProtocol;
import org.iata.bsplink.yadeutils.model.YadeHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Log
@Service
public class FilesUtility {

    @Value("${app.host.sftp.name}")
    private String hostSftpName;

    @Value("${app.host.sftp.username}")
    private String hostSftpUsername;

    @Value("${app.host.sftp.password}")
    private String hostSftpPassword;

    @Value("${app.host.sftp.port}")
    private String hostSftpPort;

    @Value("${app.host.sftp.protocol}")
    private String hostSftpProtocol;

    @Value("${app.host.local.name}")
    private String hostLocalName;

    @Value("${app.host.local.protocol}")
    private String hostLocalProtocol;

    @Value("${app.local_attached_files_directory}")
    private String localAttachedFilesDirectory;

    @Value("${app.remote_attached_files_directory}")
    private String remoteAttachedFilesDirectory;

    @Value("${app.remote_full_path_file}")
    private String remoteFullPathFile;

    @Autowired
    private YadeUtils yadeUtils;

    /**
     * Upload single file from local to remote.
     */
    public boolean uploadSingleFileFromLocalToRemote(Refund refund, String fileName)
            throws Exception {

        log.info("upload file: " + fileName + " from local to remote");

        YadeHost sourceHost = new YadeHost(hostLocalName, localAttachedFilesDirectory,
                YadeProtocol.valueOf(hostLocalProtocol.toUpperCase()));

        YadeHost targetHost = new YadeHost(hostSftpName, hostSftpUsername, hostSftpPassword,
                getRemotePathForFile(refund), YadeProtocol.valueOf(hostSftpProtocol.toUpperCase()),
                hostSftpPort);

        return yadeUtils.transfer(sourceHost, targetHost, YadeOperation.MOVE, fileName);
    }

    /**
     * Save file in temp attached files directory.
     *
     * @param file MultipartFile to save.
     * @return Saved file path.
     * @throws IOException When no file is found.
     */
    public String saveFileInTempAttachedDirectory(MultipartFile file) throws IOException {

        log.info("save file " + file.getOriginalFilename() + " in " + localAttachedFilesDirectory
                + " directory");

        Path fileName = Paths.get(file.getOriginalFilename()).getFileName();
        Path attachedFilesDirectory = Paths.get(localAttachedFilesDirectory);

        File dirAttachedFiles = new File(attachedFilesDirectory.toString());

        if (!dirAttachedFiles.exists() && dirAttachedFiles.mkdir()) {
            log.info("directory " + dirAttachedFiles + " created");
        }

        Path path = attachedFilesDirectory.resolve(fileName);
        Files.deleteIfExists(path);
        Files.write(path, file.getBytes(), StandardOpenOption.CREATE_NEW);

        log.info("file " + fileName + " saved." + " Path: " + path);

        return path.toString();
    }

    /**
     * Save MultiparFile from local to Remote SFTP Server.
     *
     * @param refund Refund
     * @param file File to save
     */
    public void saveMultipartFileFromLocalToRemote(Refund refund, MultipartFile file)
            throws Exception {

        log.info("save multipart file from local to remote. File name: "
                + file.getOriginalFilename() + ". Refund: " + refund);

        saveFileInTempAttachedDirectory(file);
        uploadSingleFileFromLocalToRemote(refund, file.getOriginalFilename());
    }

    /**
     * Save MultiparFile from local to Remote SFTP Server.
     *
     * @param files List of Multipart files to save.
     */
    public void saveMultipartFilesFromLocalToRemote(Refund refund, List<MultipartFile> files)
            throws Exception {

        for (MultipartFile file : files) {
            log.info("save multipart file from local to remote. File name: "
                    + file.getOriginalFilename() + " with refund: " + refund);
            uploadSingleFileFromLocalToRemote(refund, file.getOriginalFilename());
        }
    }

    /**
     * Gets remote path for file.
     *
     * @param refund Object
     */
    public String getRemotePathForFile(Refund refund) {
        log.info("get remote path for refund: " + refund);
        return String.format(remoteAttachedFilesDirectory, refund.getIsoCountryCode(),
                refund.getId());
    }

    /**
     * Gets remote path for file.
     *
     * @param refund Refund
     * @param fileName File name to save
     */
    public String getRemoteFullPathForFile(Refund refund, String fileName) {
        log.info("get remote full path for file: " + fileName + " with refund: " + refund);
        return String.format(remoteFullPathFile, refund.getIsoCountryCode(), refund.getId(),
                fileName);
    }

}
