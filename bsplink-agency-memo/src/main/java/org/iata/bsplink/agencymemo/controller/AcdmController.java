package org.iata.bsplink.agencymemo.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.iata.bsplink.agencymemo.dto.AcdmRequest;
import org.iata.bsplink.agencymemo.dto.CommentRequest;
import org.iata.bsplink.agencymemo.model.entity.Acdm;
import org.iata.bsplink.agencymemo.model.entity.BsplinkFile;
import org.iata.bsplink.agencymemo.model.entity.Comment;
import org.iata.bsplink.agencymemo.response.SimpleResponse;
import org.iata.bsplink.agencymemo.service.AcdmService;
import org.iata.bsplink.agencymemo.service.BsplinkFileService;
import org.iata.bsplink.agencymemo.service.CommentService;
import org.iata.bsplink.agencymemo.validation.FreeStatValidator;
import org.iata.bsplink.commons.rest.exception.ApplicationException;
import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController()
@RequestMapping("/v1/acdms")
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
public class AcdmController {

    @Autowired
    AcdmService acdmService;

    @Autowired
    private BsplinkFileService fileService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private FreeStatValidator freeStatValidator;

    /**
     * Get an ACDM.
     */
    @ApiOperation(value = "Get an ADM or ACM")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "ADM / ACM")})
    @GetMapping("/{id}")
    public ResponseEntity<Acdm> getAcdm(@PathVariable("id") Acdm acdm) {
        if (acdm == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(acdm);
    }

    @ApiOperation(value = "List of ADM / ACM")
    @GetMapping()
    public ResponseEntity<List<Acdm>> getAcdm() {
        List<Acdm> acdms = acdmService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(acdms);
    }


    /**
     * Save an ADM / ACM.
     */
    @ApiOperation(value = "Save an ADM / ACM")
    @PostMapping()
    @ApiImplicitParams({@ApiImplicitParam(name = "body", value = "The ADM / ACM to save.",
            paramType = "body", required = true, dataType = "AcdmRequest")})
    public ResponseEntity<Acdm> save(@Valid @RequestBody(required = true) AcdmRequest acdm,
            Errors errors) {

        freeStatValidator.validate(acdm, errors);

        if (errors.hasErrors()) {
            throw new ApplicationValidationException(errors);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(acdmService.save(acdm));
    }


    /**
     * Get all files by Acdm id.
     */
    @ApiOperation(value = "Get all files by Acdm id")
    @GetMapping(value = "/{id}/files")
    @ApiImplicitParam(name = "id", value = "The identificator of the Acdm", required = true,
            type = "Long")
    public ResponseEntity<List<BsplinkFile>> getAllFilesForAcdm(@PathVariable("id") Acdm acdm) {

        if (acdm == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        }

        return ResponseEntity.status(HttpStatus.OK).body(fileService.findByAcdmId(acdm.getId()));
    }


    /**
     * Save ADM/ACM's files.
     */
    @ApiOperation(value = "Save ADM/ACM's files")
    @PostMapping(value = "/{id}/files", consumes = {"multipart/form-data"})
    @ApiResponses(value = {@ApiResponse(code = 207,
            message = "An array with an HTTP status code per file representing the upload "
                    + "result.")})
    @ApiImplicitParam(name = "id", value = "The identificator of the Acdm", required = true,
            type = "Long")
    public ResponseEntity<List<SimpleResponse>> saveFiles(@PathVariable("id") Acdm acdm,
            @RequestParam("file") List<MultipartFile> files) {

        List<SimpleResponse> responses = new ArrayList<>();

        if (acdm == null) {

            responses.add(new SimpleResponse(null, HttpStatus.BAD_REQUEST, "Acdm not found"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responses);
        }

        try {

            return ResponseEntity.status(HttpStatus.OK).body(fileService.saveFiles(acdm, files));

        } catch (Exception e) {
            responses.add(
                    new SimpleResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responses);
        }
    }

    /**
     * Returns all comments for Acdm.
     *
     * @param acdm The acdm identificador.
     * @return Comment
     */
    @ApiOperation(value = "Get all comments by Acdm id")
    @GetMapping(value = "/{id}/comments")
    @ApiImplicitParam(name = "id", value = "The identificator of the Acdm", required = true,
            type = "Long")
    public ResponseEntity<List<Comment>> getAllCommentsForAcdm(@PathVariable("id") Acdm acdm) {

        if (acdm == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        }

        return ResponseEntity.status(HttpStatus.OK).body(commentService.findByAcdmId(acdm.getId()));
    }

    /**
     * Save comments for Acdm.
     *
     * @param commentRequest Request to save.
     * @return Comment
     */
    @ApiOperation(value = "Save ADM/ACM's comments")
    @PostMapping(value = "/{id}/comments")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "body", value = "The comment to save.", paramType = "body",
                    required = true, dataType = "CommentRequest"),
            @ApiImplicitParam(name = "id", value = "The identificator of the Acdm", required = true,
                    type = "Long")})
    public ResponseEntity<Comment> saveComment(
            @Valid @RequestBody(required = true) CommentRequest commentRequest,
            @PathVariable(value = "id", required = true) Long id) {

        if (!acdmService.findById(id).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        try {

            return ResponseEntity.status(HttpStatus.OK)
                    .body(commentService.save(commentRequest, id));

        } catch (Exception e) {
            throw new ApplicationException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
