package org.iata.bsplink.agencymemo.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import lombok.extern.apachecommons.CommonsLog;

import org.iata.bsplink.agencymemo.model.entity.Acdm;
import org.iata.bsplink.yadeutils.YadeUtils;
import org.iata.bsplink.yadeutils.enums.YadeOperation;
import org.iata.bsplink.yadeutils.enums.YadeProtocol;
import org.iata.bsplink.yadeutils.model.YadeHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@CommonsLog
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
    public boolean uploadSingleFileFromLocalToRemote(Acdm acdm, String fileName) throws Exception {

        // Creates the local source where the files will be pulled of
        // with the uploaded_files_directory
        YadeHost sourceHost = new YadeHost(hostLocalName, localAttachedFilesDirectory,
                YadeProtocol.valueOf(hostLocalProtocol.toUpperCase()));

        // Creates the remote sftp source host where files will be saved
        // The file will be move to the user outbox directory
        YadeHost targetHost = new YadeHost(hostSftpName, hostSftpUsername, hostSftpPassword,
                getRemotePathForFile(acdm), YadeProtocol.valueOf(hostSftpProtocol.toUpperCase()),
                hostSftpPort);

        // Moves the file from local uploaded_files_directory to the
        // user outbox
        return yadeUtils.transfer(sourceHost, targetHost, YadeOperation.MOVE, fileName);
    }


    /** Save file in temp attached files directory.
     *
     * @param file MultipartFile to save.
     * @return Saved file path.
     * @throws IOException When no file is found.
     */
    public String saveFileInTempAttachedDirectory(MultipartFile file) throws IOException {

        Path fileName = Paths.get(file.getOriginalFilename()).getFileName();
        Path attachedFilesDirectory = Paths.get(localAttachedFilesDirectory);

        File dirAttachedFiles = new File(attachedFilesDirectory.toString());

        if (!dirAttachedFiles.exists() && dirAttachedFiles.mkdir()) {
            log.info("Directory " + dirAttachedFiles + " created.");
        }

        Path path = attachedFilesDirectory.resolve(fileName);
        Files.deleteIfExists(path);
        Files.write(path, file.getBytes(), StandardOpenOption.CREATE_NEW);

        return path.toString();

    }

    /**
     * Save MultiparFile from local to Remote SFTP Server.
     *
     * @param acdm Acdm
     * @param file File to save
     */
    public void saveMultipartFileFromLocalToRemote(Acdm acdm, MultipartFile file) throws Exception {
        saveFileInTempAttachedDirectory(file);
        uploadSingleFileFromLocalToRemote(acdm, file.getOriginalFilename());
    }

    /**
     * Save MultiparFile from local to Remote SFTP Server.
     *
     * @param files List of Multipart files to save.
     */
    public void saveMultipartFilesFromLocalToRemote(Acdm acdm, List<MultipartFile> files)
            throws Exception {

        for (MultipartFile file : files) {
            uploadSingleFileFromLocalToRemote(acdm, file.getOriginalFilename());
        }
    }

    /**
     * Gets remote path for file.
     *
     * @param acdm Acdm
     */
    public String getRemotePathForFile(Acdm acdm) {
        return String.format(remoteAttachedFilesDirectory, acdm.getIsoCountryCode(), acdm.getId());
    }

    /**
     * Gets remote path for file.
     *
     * @param acdm Acdm
     * @param fileName File name to save
     */
    public String getRemoteFullPathForFile(Acdm acdm, String fileName) {
        return String.format(remoteFullPathFile, acdm.getIsoCountryCode(), acdm.getId(), fileName);
    }
}
