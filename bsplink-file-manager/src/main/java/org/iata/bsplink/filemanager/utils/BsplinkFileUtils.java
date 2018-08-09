package org.iata.bsplink.filemanager.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import lombok.Data;
import lombok.extern.apachecommons.CommonsLog;

import org.apache.commons.io.IOUtils;
import org.iata.bsplink.filemanager.configuration.BsplinkFileBasicConfig;
import org.iata.bsplink.filemanager.exception.BsplinkValidationException;
import org.iata.bsplink.filemanager.model.entity.BsplinkFile;
import org.iata.bsplink.filemanager.model.entity.BsplinkFileStatus;
import org.iata.bsplink.filemanager.service.BsplinkFileConfigService;
import org.iata.bsplink.yadeutils.YadeUtils;
import org.iata.bsplink.yadeutils.enums.YadeOperation;
import org.iata.bsplink.yadeutils.enums.YadeProtocol;
import org.iata.bsplink.yadeutils.model.YadeHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

@Component
@Data
@CommonsLog
public class BsplinkFileUtils {

    public static final String ERROR_MSG_NUMBER_EXCEEDED =
            "Maximal total number of files for download exceeded.";
    public static final String ERROR_MSG_SIZE_EXCEEDED =
            "Maximal total size of files for download exceeded.";

    @Value("${app.zip.name}")
    private String zipName;

    @Value("${app.yade.host.sftp.name}")
    private String yadeHostSftpName;

    @Value("${app.yade.host.sftp.user}")
    private String yadeHostSftpUser;

    @Value("${app.yade.host.sftp.password}")
    private String yadeHostSftpPassword;

    @Value("${app.yade.host.sftp.directory}")
    private String yadeHostSftpDirectory;

    @Value("${app.yade.host.sftp.protocol}")
    private String yadeHostSftpProtocol;

    @Value("${app.yade.host.sftp.port}")
    private String yadeHostSftpPort;

    @Value("${app.yade.host.local.name}")
    private String yadeHostLocalName;

    @Value("${app.yade.host.local.user}")
    private String yadeHostLocalUser;

    @Value("${app.yade.host.local.password}")
    private String yadeHostLocalPassword;

    @Value("${app.yade.host.local.directory}")
    private String yadeHostLocalDirectory;

    @Value("${app.yade.host.local.protocol}")
    private String yadeHostLocalProtocol;

    @Value("${app.yade.host.local.port}")
    private String yadeHostLocalPort;

    @Value("${app.local_uploaded_files_directory}")
    private String localUploadedFilesDirectory;

    @Value("${app.local_downloaded_files_directory}")
    private String localDownloadedFilesDirectory;

    @Value("${app.yade.inbox}")
    private String yadeInbox;

    @Value("${app.yade.outbox}")
    private String yadeOutbox;

    @Value("${app.file_name_rex}")
    private String fileNameRex;

    @Value("${app.file_name_outbox_replacement}")
    private String fileNameOutboxReplacement;

    @Value("${app.file_name_eliminated_replacement}")
    private String fileNameEliminatedReplacement;

    @Value("${app.file_name_type_replacement}")
    private String fileNameTypeReplacement;

    @Value("${app.yade.save_in_bbdd_and_user_outbox_enable}")
    private boolean saveInBbddAndUserOutboxEnable;

    @Autowired
    private BsplinkFileConfigService bsplinkFileConfigService;

    @Autowired
    private YadeUtils yadeUtils;

    /**
     * Checks maximum number and size of the requested files to download.
     *
     * @param bsFileList Files list.
     * @throws BsplinkValidationException If Max number or Max size is exceeded.
     */
    public void validate(List<BsplinkFile> bsFileList) throws BsplinkValidationException {

        List<String> errors = new ArrayList<>();

        BsplinkFileBasicConfig cfg = bsplinkFileConfigService.find();
        int maxNum = cfg.getMaxDownloadFilesNumber();
        if (maxNum >= 0 && bsFileList.size() > maxNum) {
            errors.add(ERROR_MSG_NUMBER_EXCEEDED);
        }

        long maxSize = cfg.getMaxDownloadTotalFileSizeForMultipleFiles();
        if (maxSize >= 0) {
            long reqSize = bsFileList.stream().filter(Objects::nonNull)
                    .mapToLong(BsplinkFile::getBytes).sum();

            if (reqSize > maxSize) {
                errors.add(ERROR_MSG_SIZE_EXCEEDED);
            }
        }

        if (!errors.isEmpty()) {
            throw new BsplinkValidationException(errors);
        }
    }

    /**
     * Generates a zip file.
     *
     * @param response HttpServletResponse.
     * @param bsFileList A list of files.
     * @throws Exception Yade Exceptions.
     */
    public void generateZipFileInResponse(HttpServletResponse response,
            List<BsplinkFile> bsFileList) throws Exception {

        bsFileList.removeAll(Collections.singleton(null));

        response.setContentType(URLConnection.guessContentTypeFromName(zipName));
        response.setHeader("Content-Disposition",
                String.format("attachment; filename=\"%s\"", zipName));

        ZipOutputStream zout =
                new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));

        bsFileList.removeAll(Collections.singleton(null));

        // All files with trashed status are deleted from list.
        List<BsplinkFile> bsFileListWihoutTrashedFiles =
                bsFileList.stream().filter(f -> !f.getStatus().equals(BsplinkFileStatus.TRASHED))
                        .collect(Collectors.toList());

        for (BsplinkFile bsFile : bsFileListWihoutTrashedFiles) {

            yadeGetFileFromRemoteHost(bsFile);

            File file = new File(localDownloadedFilesDirectory + File.separator + bsFile.getName());

            try (FileInputStream fin = new FileInputStream(file)) {

                zout.putNextEntry(new ZipEntry(bsFile.getName()));

                IOUtils.copy(fin, zout);

                zout.closeEntry();

            } catch (IOException e) {
                log.error("Error creating FileInputStream " + e.getMessage());
                throw e;
            } finally {
                if (null != response.getOutputStream()) {
                    zout.closeEntry();
                }
                Files.delete(file.toPath());
            }
        }

        if (null != response.getOutputStream()) {
            zout.close();
        }

    }

    /**
     * Downloads a single file. Also with yade if is enabled.
     *
     * @param response Sets header to write in buffer.
     * @param bsFile File.
     * @throws Exception YadeException is something occurs in the transfer.
     */
    public void downloadSingleFile(HttpServletResponse response, BsplinkFile bsFile)
            throws Exception {

        response.setContentType(URLConnection.guessContentTypeFromName(bsFile.getName()));
        response.setHeader("Content-Disposition",
                String.format("attachment; filename=\"%s\"", bsFile.getName()));

        yadeGetFileFromRemoteHost(bsFile);

        File file = new File(localDownloadedFilesDirectory + File.separator + bsFile.getName());

        try (FileInputStream input = new FileInputStream(file);
                ServletOutputStream sos = response.getOutputStream()) {

            FileCopyUtils.copy(input, sos);

            Files.delete(file.toPath());

        } catch (IOException e) {
            log.error("Error creating FileInputStream " + e.getMessage());
            throw e;
        }
    }


    /**
     * Build source and target host to transfer files using yade-utils.
     *
     * @param bsFile File.
     * @throws Exception Yade throws exceptions.
     */
    public void yadeGetFileFromRemoteHost(BsplinkFile bsFile) throws Exception {

        // Creates the remote sftp source host to download files from
        // the user outbox directory

        String pathToFile = getOutboxPathFromBsplinkFile(bsFile);

        YadeHost sourcehost =
                new YadeHost(yadeHostSftpName, yadeHostSftpUser, yadeHostSftpPassword, pathToFile,
                        YadeProtocol.valueOf(yadeHostSftpProtocol.toUpperCase()), yadeHostSftpPort);

        // Creates the downloaded_files directory (if not exists) to save the file temporarily.
        Path uploadedFilesDirectory = Paths.get(localDownloadedFilesDirectory);
        File dirUploadedFiles = new File(uploadedFilesDirectory.toString());

        if (!dirUploadedFiles.exists() && dirUploadedFiles.mkdir()) {
            log.info("Directory " + dirUploadedFiles + " created.");
        }

        // Creates the local destination source where the files will be saved
        YadeHost targetHost = new YadeHost(yadeHostLocalName, localDownloadedFilesDirectory,
                YadeProtocol.valueOf(yadeHostLocalProtocol.toUpperCase()));

        // Execute the transfer from remote host to local host
        yadeUtils.transfer(sourcehost, targetHost, YadeOperation.COPY, bsFile.getName());
    }

    /**
     * Uploads a file to a remote sftp server using yade-utils if yade is enabled.
     *
     * @param fileName name of file to save.
     * @throws Exception Yade throws exceptions.
     */
    public boolean uploadSingleFileFromLocalToRemote(String fileName) throws Exception {

        // Creates the local source where the files will be pulled of
        // with the uploaded_files_directory
        YadeHost sourceHost = new YadeHost(yadeHostLocalName, localUploadedFilesDirectory,
                YadeProtocol.valueOf(yadeHostLocalProtocol.toUpperCase()));

        // TEMPORARILY THE FILE WILL BE SAVED IN THE USER OUTBOX DIRECTORY
        String pathToFile = yadeInbox;
        if (saveInBbddAndUserOutboxEnable) {
            pathToFile = getOutboxPathFromFileName(fileName);
        }

        // Creates the remote sftp source host where files will be saved
        // The file will be move to the user outbox directory
        YadeHost targetHost =
                new YadeHost(yadeHostSftpName, yadeHostSftpUser, yadeHostSftpPassword, pathToFile,
                        YadeProtocol.valueOf(yadeHostSftpProtocol.toUpperCase()), yadeHostSftpPort);

        // Moves the file from local uploaded_files_directory to the
        // user outbox
        return yadeUtils.transfer(sourceHost, targetHost, YadeOperation.MOVE, fileName);
    }

    /**
     * Moves a file to a yade-utils from outbox to outbox/eliminated.
     *
     * @param fileName name of file to move.
     * @throws Exception Yade throws exceptions.
     * 
     */
    public void moveFileToEliminated(String fileName) throws Exception {
        String sourceDirectory = getOutboxPathFromFileName(fileName);
        String targetDirectory = getOutboxEliminatedPathFromFileName(fileName);

        YadeHost sourceHost = new YadeHost(yadeHostSftpName, yadeHostSftpUser, yadeHostSftpPassword,
                sourceDirectory, YadeProtocol.valueOf(yadeHostSftpProtocol.toUpperCase()),
                yadeHostSftpPort);

        YadeHost targetHost = new YadeHost(yadeHostSftpName, yadeHostSftpUser, yadeHostSftpPassword,
                targetDirectory, YadeProtocol.valueOf(yadeHostSftpProtocol.toUpperCase()),
                yadeHostSftpPort);

        yadeUtils.transfer(sourceHost, targetHost, YadeOperation.MOVE, fileName);
    }

    /**
     * Checks if list is not empty.
     *
     * @param bsFileList A list of files.
     * @return Returns true if list is not empty.
     */
    public boolean checkIfListIsNotEmpty(List<BsplinkFile> bsFileList) {
        return bsFileList.stream().anyMatch(Objects::nonNull);
    }


    /**
     * Obtains the user outbox path from BsplinkFile.
     *
     * @param file Information of the file.
     * @return String with path to file.
     */
    public String getOutboxPathFromBsplinkFile(BsplinkFile file) {

        if (file.getStatus() == BsplinkFileStatus.DELETED) {
            return getOutboxEliminatedPathFromFileName(file.getName());
        }

        return getOutboxPathFromFileName(file.getName());
    }


    /**
     * Obtains the user outbox path from file name.
     *
     * @param fileName Information of the file.
     * @return String with path to file.
     */
    public String getOutboxPathFromFileName(String fileName) {
        return fileName.replaceFirst(fileNameRex, fileNameOutboxReplacement);
    }

    /**
     * Obtains the user outbox eliminated path from file name.
     *
     * @param fileName Information of the file.
     * @return String with path to file.
     */
    public String getOutboxEliminatedPathFromFileName(String fileName) {
        return fileName.replaceFirst(fileNameRex, fileNameEliminatedReplacement);
    }

    /**
     * Obtains the file type.
     *
     * @param fileName The file Name.
     * @return String with path to file.
     */
    public String getFileType(String fileName) {
        return fileName.replaceFirst(fileNameRex, fileNameTypeReplacement);
    }

}
