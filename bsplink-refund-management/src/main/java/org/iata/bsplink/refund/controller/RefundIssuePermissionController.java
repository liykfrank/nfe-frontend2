package org.iata.bsplink.refund.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
import org.iata.bsplink.refund.model.entity.RefundIssuePermission;
import org.iata.bsplink.refund.service.RefundIssuePermissionService;
import org.iata.bsplink.refund.validation.RefundIssuePermissionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/refunds/indirects/permissions")
public class RefundIssuePermissionController {

    @Autowired
    private RefundIssuePermissionService refundIssuePermissionService;

    @Autowired
    private RefundIssuePermissionValidator validator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        Optional.ofNullable(binder.getTarget()).filter(o -> validator.supports(o.getClass()))
                .ifPresent(o -> binder.addValidators(validator));
    }

    /**
     * Creates a new refundIssuePermission and returns it.
     */
    @ApiOperation(value = "Create permission to an agency to issue indirect refunds "
            + "for a specific airline")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "RefundIssuePermission")})
    @PostMapping
    public ResponseEntity<RefundIssuePermission> createRefundIssuePermission(
            @Valid @RequestBody RefundIssuePermission refundIssuePermission, Errors errors) {

        if (errors.hasErrors()) {
            throw new ApplicationValidationException(errors);
        }

        if (refundIssuePermissionService.findByIsoCountryCodeAndAirlineCodeAndAgentCode(
                refundIssuePermission.getIsoCountryCode(), refundIssuePermission.getAirlineCode(),
                refundIssuePermission.getAgentCode()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(refundIssuePermissionService.save(refundIssuePermission));
    }

    @ApiOperation(
            value = "Returns the list of permissions to issue indirect refunds of all agencies")
    @GetMapping
    public ResponseEntity<List<RefundIssuePermission>> getRefundIssuePermissions() {
        return ResponseEntity.status(HttpStatus.OK).body(refundIssuePermissionService.findAll());
    }

    /**
     * Deletes a RefundIssuePermission by id.
     */
    @ApiOperation(
            value = "Deletes the permission of an agency to "
                    + "issue indirect refunds for a specific airline")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "RefundIssuePermission")})
    @DeleteMapping("/{id}")
    public ResponseEntity<RefundIssuePermission> deleteRefundIssuePermission(
            @PathVariable("id") Optional<RefundIssuePermission> optionalRefundIssuePermission) {
        if (!optionalRefundIssuePermission.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        refundIssuePermissionService.delete(optionalRefundIssuePermission.get());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Retrieves a RefundIssuePermission by id.
     */
    @ApiOperation(value = "Returns the permission to issue indirect refunds")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "RefundIssuePermission")})
    @GetMapping("/{id}")
    public ResponseEntity<RefundIssuePermission> getRefundIssuePermission(
            @PathVariable("id") Optional<RefundIssuePermission> optionalRefundIssuePermission) {
        if (!optionalRefundIssuePermission.isPresent()) {

            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(optionalRefundIssuePermission.get());
    }

    /**
     * Retrieves a RefundIssuePermission by Airline Code and Agent Code.
     */
    @ApiOperation(value = "Returns the permission to an agency to issue "
            + "indirect refunds for a specific airline")
    @GetMapping(params = {"isoCountryCode", "airlineCode", "agentCode"})
    public ResponseEntity<RefundIssuePermission> 
        getRefundIssuePermissionByIsoCountryCodeAndAirlineCodeAndAgentCode(
            @RequestParam String isoCountryCode, @RequestParam String airlineCode,
            @RequestParam String agentCode) {
        Optional<RefundIssuePermission> optionalRefundIssuePermission =
                refundIssuePermissionService.findByIsoCountryCodeAndAirlineCodeAndAgentCode(
                        isoCountryCode, airlineCode, agentCode);
        if (!optionalRefundIssuePermission.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(optionalRefundIssuePermission.get());
    }

    /**
     * Retrieves a RefundIssuePermission by ISO Country Code and Airline Code.
     */
    @ApiOperation(value = "Returns the list of permission to issue indirect refunds"
            + " of all agencies for a specific airline")
    @GetMapping(params = {"isoCountryCode", "airlineCode"})
    public ResponseEntity<List<RefundIssuePermission>> 
        getRefundIssuePermissionByIsoCountryCodeAndAirlineCode(
            @RequestParam String isoCountryCode, @RequestParam String airlineCode) {
        List<RefundIssuePermission> permissions = refundIssuePermissionService
                .findByIsoCountryCodeAndAirlineCode(isoCountryCode, airlineCode);
        return ResponseEntity.status(HttpStatus.OK).body(permissions);
    }
}
