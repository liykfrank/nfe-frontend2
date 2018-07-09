package org.iata.bsplink.refund.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.Valid;

import lombok.extern.java.Log;

import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
import org.iata.bsplink.refund.dto.CommentRequest;
import org.iata.bsplink.refund.dto.RefundStatusRequest;
import org.iata.bsplink.refund.model.entity.BsplinkFile;
import org.iata.bsplink.refund.model.entity.Comment;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.model.entity.RefundHistory;
import org.iata.bsplink.refund.model.entity.RefundStatus;
import org.iata.bsplink.refund.response.SimpleResponse;
import org.iata.bsplink.refund.service.BsplinkFileService;
import org.iata.bsplink.refund.service.CommentService;
import org.iata.bsplink.refund.service.RefundHistoryService;
import org.iata.bsplink.refund.service.RefundService;
import org.iata.bsplink.refund.validation.AgentValidator;
import org.iata.bsplink.refund.validation.AirlineValidator;
import org.iata.bsplink.refund.validation.CountryValidator;
import org.iata.bsplink.refund.validation.CurrencyValidator;
import org.iata.bsplink.refund.validation.IssuePermissionValidator;
import org.iata.bsplink.refund.validation.MassloadValidator;
import org.iata.bsplink.refund.validation.PartialRefundValidator;
import org.iata.bsplink.refund.validation.RefundCompositeValidator;
import org.iata.bsplink.refund.validation.RefundStatusValidator;
import org.iata.bsplink.refund.validation.RefundUpdateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Log
@RestController
@RequestMapping("/v1/refunds/indirects")
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
public class RefundController {

    @Autowired
    RefundService refundService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private BsplinkFileService fileService;

    @Autowired
    private AgentValidator agentValidator;

    @Autowired
    private AirlineValidator airlineValidator;

    @Autowired
    private RefundCompositeValidator refundValidator;

    @Autowired
    private CurrencyValidator currencyValidator;

    @Autowired
    private IssuePermissionValidator issuePermissionValidator;

    @Autowired
    private RefundHistoryService refundHistoryService;

    @Autowired
    private RefundUpdateValidator refundUpdateValidator;

    @Autowired
    private PartialRefundValidator partialRefundValidator;

    @Autowired
    private CountryValidator countryValidator;

    @Autowired
    private RefundStatusValidator refundStatusValidator;

    @Autowired
    private MassloadValidator massloadValidator;

    private static final String RESPONDING_WITH = "responding with response: ";

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        Stream.of(refundValidator, agentValidator, airlineValidator, currencyValidator,
                countryValidator, partialRefundValidator)
                .forEach(validator -> Optional.ofNullable(binder.getTarget())
                        .filter(o -> validator.supports(o.getClass()))
                        .ifPresent(o -> binder.addValidators(validator)));
    }

    /**
     * Get a Refund.
     */
    @ApiOperation(value = "Get a Refund")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Refund")})
    @GetMapping("/{id}")
    public ResponseEntity<Refund> getRefund(@PathVariable("id") Optional<Refund> optionalRefund) {
        if (!optionalRefund.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(optionalRefund.get());
    }


    @ApiOperation(value = "List of Refunds")
    @GetMapping()
    public ResponseEntity<List<Refund>> getRefunds() {
        List<Refund> refunds = refundService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(refunds);
    }

    /**
     * Returns all comments for Refund.
     *
     * @param refund Id of the refund.
     * @return Comment to save
     */
    @ApiOperation(value = "Get all comments by Refund id")
    @GetMapping(value = "/{id}/comments", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiImplicitParam(name = "id", value = "The identifier of the Refund", required = true,
            type = "Long")
    public ResponseEntity<List<Comment>> getAllCommentsForRefund(
            @PathVariable("id") Refund refund) {

        log.info("received request for getting comments with refund: " + refund);

        if (refund == null) {
            return ResponseEntity.notFound().build();
        }

        List<Comment> commentList = commentService.findByRefundId(refund.getId());

        log.info(RESPONDING_WITH + commentList);

        return ResponseEntity.status(HttpStatus.OK).body(commentList);
    }

    /**
     * Save comments for Refund.
     *
     * @param commentRequest Request to save.
     * @return Comment
     */
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Save Refund comments")
    @PostMapping(value = "/{id}/comments")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "body", value = "The comment to save.", paramType = "body",
                    required = true, dataType = "CommentRequest"),
            @ApiImplicitParam(name = "id", value = "The identifier of the Refund", required = true,
                    type = "Long")})
    public ResponseEntity<Comment> saveComment(
            @Valid @RequestBody(required = true) CommentRequest commentRequest,
            @PathVariable(value = "id", required = true) Long id) {

        log.info("received request for creating comment: " + commentRequest);

        if (!refundService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Comment comment = commentService.save(commentRequest, id);

        log.info(RESPONDING_WITH + comment);

        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }


    /**
     * Get all files by Refund id.
     */
    @ApiOperation(value = "Get all files by Refund id")
    @GetMapping(value = "/{id}/files")
    @ApiImplicitParam(name = "id", value = "The identifier of the Refund", required = true,
            type = "Long")
    public ResponseEntity<List<BsplinkFile>> getAllFilesForRefund(
            @PathVariable("id") Refund refund) {

        log.info("received request for getting files with refund: " + refund);

        if (refund == null) {
            return ResponseEntity.notFound().build();
        }

        List<BsplinkFile> listFiles = fileService.findByRefundId(refund.getId());

        log.info(RESPONDING_WITH + listFiles);

        return ResponseEntity.status(HttpStatus.OK).body(listFiles);
    }


    /**
     * Save Refund's files.
     */
    @ApiOperation(value = "Save Refund's files")
    @PostMapping(value = "/{id}/files", consumes = {"multipart/form-data"})
    @ApiResponses(value = {@ApiResponse(code = 207,
            message = "An array with an HTTP status code per file representing the upload "
                    + "result.")})
    @ApiImplicitParam(name = "id", value = "The identifier of the Refund", required = true,
            type = "Long")
    public ResponseEntity<List<SimpleResponse>> saveFiles(@PathVariable("id") Refund refund,
            @RequestParam("file") List<MultipartFile> files) {

        log.info("received request for getting comments with refund: " + refund);

        List<SimpleResponse> responses = new ArrayList<>();

        if (refund == null) {
            return ResponseEntity.notFound().build();
        }

        try {

            List<SimpleResponse> listResponses = fileService.saveFiles(refund, files);

            log.info(RESPONDING_WITH + listResponses);

            return ResponseEntity.status(HttpStatus.OK).body(listResponses);

        } catch (Exception e) {
            responses.add(
                    new SimpleResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responses);
        }

    }


    /**
     * Save a Indirect Refund.
     */
    @ApiOperation(value = "Save an Indirect Refund")
    @PostMapping()
    @ApiImplicitParams({@ApiImplicitParam(name = "body", value = "The Indirect Refund to save.",
            paramType = "body", required = true, dataType = "Refund")})
    public ResponseEntity<Refund> saveRefund(@Valid @RequestBody(required = true) Refund refund,
            Errors errors) {

        issuePermissionValidator.validate(refund, errors);

        if (refund.getStatus().equals(RefundStatus.PENDING)
                || refund.getStatus().equals(RefundStatus.PENDING_SUPERVISION)) {
            refundStatusValidator.validatePendingRefund(refund, errors);
        } else {
            if (!refund.getStatus().equals(RefundStatus.DRAFT)) {
                errors.rejectValue("status", "incorrect_status", "The refund status is incorrect.");
            }
        }

        if (errors.hasErrors()) {
            throw new ApplicationValidationException(errors);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(refundService.saveIndirectRefund(refund));
    }

    /**
     * Updates an Indirect Refund.
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "Updates an Indirect Refund")
    @ApiImplicitParams({@ApiImplicitParam(name = "body", value = "The Indirect Refund to update.",
            paramType = "body", required = true, dataType = "Refund")})
    public ResponseEntity<Refund> updateRefund(@PathVariable(value = "id", required = true) Long id,
            @Valid @RequestBody(required = true) Refund refund, Errors errors) {

        log.info("received request to update indirect refund: " + refund);

        Refund refundToSave = refundUpdateValidator.validate(id, refund, errors);

        if (errors.hasErrors() || refundToSave == null) {
            throw new ApplicationValidationException(errors);
        }

        Refund refundUpdated = refundService.updateIndirectRefund(refundToSave);

        log.info(RESPONDING_WITH + refundUpdated);

        return ResponseEntity.status(HttpStatus.OK).body(refundUpdated);
    }

    /**
     * Deletes a Refund by id.
     */
    @ApiOperation(value = "Deletes an Indirect Refund")
    @DeleteMapping("/{id}")
    public ResponseEntity<Refund> deleteRefund(
            @PathVariable("id") Optional<Refund> optionalRefund) {

        if (!optionalRefund.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        refundService.deleteIndirectRefund(optionalRefund.get());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Get a Refund history.
     */
    @ApiOperation(value = "Get Refund's history")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Refund history")})
    @GetMapping("/{id}/history")
    public ResponseEntity<List<RefundHistory>> getRefundHistory(
            @PathVariable("id") Optional<Refund> optionalRefund) {

        log.info("received to get refund's history: " + optionalRefund);

        if (!optionalRefund.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        List<RefundHistory> history =
                refundHistoryService.findByRefundId(optionalRefund.get().getId());

        log.info(RESPONDING_WITH + history);

        return ResponseEntity.status(HttpStatus.OK).body(history);
    }


    /**
     * Updates refund's status.
     *
     * @param refund To update
     * @param refundStatusRequest New status
     * @param errors list of errors
     * @return Refund updated refund
     */
    @ApiOperation("Changes refund's status")
    @PostMapping("/{id}/status")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "body", value = "New status", paramType = "body",
                    required = true, dataType = "RefundStatusRequest"),
            @ApiImplicitParam(name = "id", value = "The identifier of the refund", required = true,
                    type = "Long")})
    @ResponseBody
    public ResponseEntity<Refund> changeRefundStatus(@PathVariable(value = "id") Refund refund,
            @Valid @RequestBody RefundStatusRequest refundStatusRequest, Errors errors) {

        log.info("received request to change refund's status: " + refund);       

        if (refund == null) {
            return ResponseEntity.notFound().build();
        }
        
        if (refundStatusRequest.getStatus().equals(RefundStatus.PENDING)) {
            issuePermissionValidator.validate(refund, errors);
        }
        
        if (errors.hasErrors()) {
            throw new ApplicationValidationException(errors);
        }      

        refundStatusValidator.validate(refund, refundStatusRequest, errors);

        Refund updatedRefund = refundService.updateIndirectRefund(refund);

        log.info(RESPONDING_WITH + updatedRefund);

        return ResponseEntity.status(HttpStatus.OK).body(updatedRefund);
    }

    /**
     * Get a Refund.
     */
    @ApiOperation(
            value = "Get a Refund by ISO county code, airline code and ticket document number")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Refund")})
    @GetMapping(params = {"isoCountryCode", "airlineCode", "ticketDocumentNumber"})
    public ResponseEntity<Refund> getRefundByIsoCountryCodeAirlineCodeTicketDocumentNumber(
            @RequestParam String isoCountryCode, @RequestParam String airlineCode,
            @RequestParam String ticketDocumentNumber) {

        Optional<Refund> optionalRefund =
                refundService.findByIsoCountryCodeAndAirlineCodeAndTicketDocumentNumber(
                        isoCountryCode, airlineCode, ticketDocumentNumber);
        if (!optionalRefund.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(optionalRefund.get());

    }

    /**
     * Updates an Indirect Refund.
     */
    @PutMapping(value = "/{id}", params = "fileName")
    @ApiOperation(value = "Updates an Indirect Refund")
    @ApiImplicitParams({@ApiImplicitParam(name = "body", value = "The Indirect Refund to update.",
            paramType = "body", required = true, dataType = "Refund")})
    public ResponseEntity<Refund> updateRefundViaMassload(
            @PathVariable(value = "id", required = true) Long id,
            @RequestParam(required = true) String fileName,
            @Valid @RequestBody(required = true) Refund refund, Errors errors) {

        Refund newRefund = massloadValidator.validate(id, refund, fileName, errors);

        if (errors.hasErrors()) {
            throw new ApplicationValidationException(errors);
        }

        refundService.updateIndirectRefund(newRefund, fileName);
        return ResponseEntity.status(HttpStatus.OK).body(newRefund);
    }
}
