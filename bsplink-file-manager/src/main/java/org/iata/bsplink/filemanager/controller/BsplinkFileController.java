package org.iata.bsplink.filemanager.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import lombok.extern.apachecommons.CommonsLog;

import org.iata.bsplink.filemanager.exception.BsplinkValidationException;
import org.iata.bsplink.filemanager.model.entity.BsplinkFile;
import org.iata.bsplink.filemanager.model.entity.BsplinkFileStatus;
import org.iata.bsplink.filemanager.pojo.BsplinkFileSearchCriteria;
import org.iata.bsplink.filemanager.response.EntityActionResponse;
import org.iata.bsplink.filemanager.response.SimpleResponse;
import org.iata.bsplink.filemanager.service.BsplinkFileService;
import org.iata.bsplink.filemanager.service.MultipartFileService;
import org.iata.bsplink.filemanager.utils.BsplinkFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

@RestController()
@RequestMapping("/v1/files")
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@CommonsLog
public class BsplinkFileController {

    @Autowired
    private BsplinkFileService bsplinkFileService;

    @Autowired
    private MultipartFileService multipartFileSaveService;

    @Autowired
    private BsplinkFileUtils bsplinkFileUtils;

    /**
     * Returns a list of files.
     */
    @GetMapping()
    @ApiOperation(value = "List of current user's files")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "Size of the page", required = false,
                    dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "Page number", required = false,
                    dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "minUploadDateTime",
                    value = "Minimum file upload date and time in UTC ISO format "
                            + "(e.g: 2018-01-01T00:00:00Z)",
                    required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "maxUploadDateTime",
                    value = "Maximum file upload date and time in UTC ISO format "
                            + "(e.g: 2018-01-01T00:00:00Z)",
                    required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sort",
                    value = "Sort by one or more fields (e.g. sort=name,asc&sort=id,desc)",
                    required = false, dataType = "string", paramType = "query")})
    public Page<BsplinkFile> files(BsplinkFileSearchCriteria searchCriteria, Pageable pageable,
            Sort sort) {

        return bsplinkFileService.find(searchCriteria, pageable, sort);
    }

    /**
     * Returns a file.
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "Downloads a file by its id and updates the status to downloaded",
            notes = "The file will only be downloaded if the id is present and exists. "
                    + "It will be downloaded as an attached file.")
    @ApiImplicitParam(name = "id", value = "The identificator of the file to be downloaded",
            required = true, type = "int")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "File found"),
            @ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "File not found")})
    public ResponseEntity<?> downloadFile(@PathVariable("id") BsplinkFile bsFile,
            HttpServletResponse response) {

        if (bsFile == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (bsFile.getStatus().equals(BsplinkFileStatus.TRASHED)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {

            bsplinkFileUtils.downloadSingleFile(response, bsFile);
            bsplinkFileService.updateStatusToDownloaded(bsFile);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error : " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Returns a zip file.
     *
     */
    @GetMapping("/zip")
    @ApiOperation(
            value = "Downloads a zip with files by its id and updates the status to downloaded",
            notes = "The files will be compressed in a zip file if the id is present and exists. "
                    + "The zip file will be downloaded as an attached file.")
    @ApiImplicitParam(name = "id", value = "List of file ids (e.g id=1&id=2&id=3)", required = true,
            type = "array")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Files founded"),
            @ApiResponse(code = 400, message = "Invalid ids supplied"),
            @ApiResponse(code = 404, message = "File not found")})
    public ResponseEntity<?> downloadZip(@RequestParam("id") List<BsplinkFile> bsFileList,
            HttpServletResponse response) {

        if (!bsplinkFileUtils.checkIfListIsNotEmpty(bsFileList)) {
            return new ResponseEntity<>("Files not found", HttpStatus.NOT_FOUND);
        }

        try {
            bsplinkFileUtils.validate(bsFileList);

            bsplinkFileUtils.generateZipFileInResponse(response, bsFileList);
            bsFileList.forEach(f -> bsplinkFileService.updateStatusToDownloaded(f));

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (BsplinkValidationException e) {
            log.error("BsplinkValidationException error : " + e.getMessage());
            return new ResponseEntity<>(e.getErrorMap(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            log.error("BsplinkValidationException error : " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a file.
     */
    @ApiOperation(value = "Deletes a single file")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "File deleted"),
            @ApiResponse(code = 404, message = "File not found"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOneFile(@PathVariable("id") BsplinkFile bsFile) {

        if (bsFile == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (BsplinkFileStatus.DELETED.equals(bsFile.getStatus())
                || BsplinkFileStatus.TRASHED.equals(bsFile.getStatus())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            bsplinkFileService.deleteOneFile(bsFile);
        } catch (Exception e) {
            log.error("Error : " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Deletes multiple files.
     */
    @ApiOperation(value = "Deletes multiple files at the same time")
    @ApiImplicitParam(name = "id", value = "List of file ids (e.g id=1&id=2&id=3)", required = true,
            type = "string")
    @ApiResponses(value = {@ApiResponse(code = 207,
            message = "An array with an HTTP status code per file representing the delete result")})
    @DeleteMapping()
    @Transactional
    public ResponseEntity<List<EntityActionResponse<Long>>> deleteMultipleFiles(
            @RequestParam(value = "id", required = true) List<Long> ids) {

        return new ResponseEntity<>(bsplinkFileService.deleteMultipleFiles(ids),
                HttpStatus.MULTI_STATUS);
    }

    /**
     * Upload of multiple files.
     */
    @PostMapping()
    @ApiOperation(value = "Upload of files")
    @ApiResponses(value = {@ApiResponse(code = 207,
            message = "An array with an HTTP status code per file representing the upload "
                    + "result.")})
    public ResponseEntity<List<SimpleResponse>> send(
            @RequestParam("file") List<MultipartFile> files, WebRequest webRequest) {


        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();

        if (files.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        }

        return ResponseEntity.status(HttpStatus.MULTI_STATUS)
                .body(multipartFileSaveService.saveFiles(request.getRequestURI(), files));
    }

    /**
     * If file is been overwritten then updates status to TRASHED otherwise it registers file in DB.
     */
    @PostMapping("/register")
    @ApiOperation(value = "If file is been overwritten then updates status to TRASHED otherwise "
            + "registers file in DB.")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "File registered")})
    public ResponseEntity<String> register(@Valid @RequestBody BsplinkFile bsFile) {

        bsplinkFileService.updateStatusToTrashed(bsFile);

        bsFile.setType(bsplinkFileUtils.getFileType(bsFile.getName()));
        bsplinkFileService.save(bsFile);

        return ResponseEntity.status(HttpStatus.CREATED).body("File registered");
    }
}
