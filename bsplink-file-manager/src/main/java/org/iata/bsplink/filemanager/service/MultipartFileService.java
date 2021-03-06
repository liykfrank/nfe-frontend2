package org.iata.bsplink.filemanager.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.apachecommons.CommonsLog;

import org.apache.commons.io.FilenameUtils;
import org.iata.bsplink.filemanager.configuration.ApplicationConfiguration;
import org.iata.bsplink.filemanager.configuration.BsplinkFileBasicConfig;
import org.iata.bsplink.filemanager.model.entity.BsplinkFile;
import org.iata.bsplink.filemanager.model.entity.BsplinkFileStatus;
import org.iata.bsplink.filemanager.model.repository.BsplinkFileRepository;
import org.iata.bsplink.filemanager.response.SimpleResponse;
import org.iata.bsplink.filemanager.utils.BsplinkFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@CommonsLog
public class MultipartFileService {

    public static final String BAD_REQUEST_MSG_EMPTY = "Empty files are not allowed";
    public static final String BAD_REQUEST_MSG_NAME_LENGTH = "Maximum File name length exceeded.";
    public static final String BAD_REQUEST_MSG_PATTERN = "File name not accepted.";
    public static final String BAD_REQUEST_MSG_EXTENSION = "File extension not accepted.";
    public static final String BAD_REQUEST_MSG_COUNT = "Maximum number of files exceeded.";
    public static final String BAD_REQUEST_MSG_TOTAL_SIZE =
            "Maximum total upload file size exceeded.";

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    @Autowired
    private BsplinkFileConfigService bsplinkFileConfigService;

    @Autowired
    private BsplinkFileUtils bsplinkFileUtils;
    
    @Autowired
    private BsplinkFileRepository bsplinkFileRepository;

    /**
     * Save MultipartFiles in FS.
     */
    public List<SimpleResponse> saveFiles(String path, List<MultipartFile> files) {
        List<SimpleResponse> simpleResponses = new ArrayList<>(files.size());
        BsplinkFileBasicConfig cfg = bsplinkFileConfigService.find();

        if (cfg.getMaxUploadFilesNumber() >= 0 && files.size() > cfg.getMaxUploadFilesNumber()) {
            simpleResponses.add(badRequestResponse(BAD_REQUEST_MSG_COUNT));
            return simpleResponses;
        }

        for (MultipartFile file : files) {
            SimpleResponse simpleResponse;

            String fileName = file.getOriginalFilename();

            if (file.isEmpty()) {
                simpleResponse = badRequestResponse(BAD_REQUEST_MSG_EMPTY);
            } else if (fileName.length() > 255) {
                simpleResponse = badRequestResponse(BAD_REQUEST_MSG_NAME_LENGTH);
            } else if (!matchesFilePatterns(fileName)) {
                simpleResponse = badRequestResponse(BAD_REQUEST_MSG_PATTERN);
            } else if (!isFileExtensionAllowed(fileName)) {
                simpleResponse = badRequestResponse(BAD_REQUEST_MSG_EXTENSION);
            } else {
                try {
                    saveFile(file);
                    simpleResponse = new SimpleResponse(null, HttpStatus.OK);
                } catch (Exception e) {
                    log.error("Error saving file: " + e.toString());
                    simpleResponse = new SimpleResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            simpleResponse.setSubject(file.getOriginalFilename());
            simpleResponses.add(simpleResponse);
        }
        return simpleResponses;
    }

    private SimpleResponse badRequestResponse(String message) {
        return new SimpleResponse(null, HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.name() + ": " + message);
    }

    private boolean isFileExtensionAllowed(String fileName) {
        String ext = FilenameUtils.getExtension(fileName);
        return bsplinkFileConfigService.find().getAllowedFileExtensions().stream()
                .anyMatch(ext::equalsIgnoreCase);
    }

    private boolean matchesFilePatterns(String fileName) {
        return bsplinkFileConfigService.find().getFileNamePatterns().stream()
                .anyMatch(fileName::matches);
    }

    private void saveFile(MultipartFile file) throws Exception {

        Path fileName = Paths.get(file.getOriginalFilename()).getFileName();
        Path uploadedFilesDirectory =
                Paths.get(applicationConfiguration.getLocalUploadedFilesDirectory());

        File dirUploadedFiles = new File(uploadedFilesDirectory.toString());
        if (!dirUploadedFiles.exists()) {
            if (dirUploadedFiles.mkdir()) {
                log.info("Directory " + dirUploadedFiles + " created.");
            }
        }

        Path path = uploadedFilesDirectory.resolve(fileName);
        Files.deleteIfExists(path);
        Files.write(path, file.getBytes(), StandardOpenOption.CREATE_NEW);
        
        // TEMPORARILY THE FILE WILL BE SAVED IN BBDD
        BsplinkFile bsplinkFile = new BsplinkFile();
        bsplinkFile.setName(file.getOriginalFilename());
        bsplinkFile.setBytes(file.getSize());
        bsplinkFile.setStatus(BsplinkFileStatus.SENT);
        bsplinkFile.setType(bsplinkFileUtils.getFileType(file.getOriginalFilename()));
        bsplinkFile.setUploadDateTime(Instant.now());
        bsplinkFileRepository.save(bsplinkFile);

        
        bsplinkFileUtils.uploadSingleFileFromLocalToRemote(file.getOriginalFilename());
    }
}
