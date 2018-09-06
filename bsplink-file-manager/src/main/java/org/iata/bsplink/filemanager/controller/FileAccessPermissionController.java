package org.iata.bsplink.filemanager.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
import org.iata.bsplink.filemanager.model.entity.FileAccessPermission;
import org.iata.bsplink.filemanager.service.FileAccessPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/v1/fileAccessPermissions")
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
public class FileAccessPermissionController {

    @Autowired
    private FileAccessPermissionService service;


    /**
     * XXX.
     */
    @ApiOperation(value = "XXX")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "XXX")})
    @GetMapping(params = "user")
    public ResponseEntity<List<FileAccessPermission>> getByUser(
            @RequestParam(required = true) String user) {

        return ResponseEntity.status(HttpStatus.OK).body(service.findByUser(user));
    }


    /**
     * XXX.
     */
    @ApiOperation(value = "XXX")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "XXX")})
    @GetMapping()
    public ResponseEntity<List<FileAccessPermission>> getAll() {

        return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
    }


    /**
     * XXX.
     */
    @ApiOperation(value = "XXX")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "XXX")})
    @GetMapping("/{id}")
    public ResponseEntity<FileAccessPermission> get(
            @PathVariable("id") Optional<FileAccessPermission> optionalfileAccessPermission) {

        if (!optionalfileAccessPermission.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(optionalfileAccessPermission.get());
    }


    /**
     * XXX.
     */
    @ApiOperation(value = "XXX")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "XXX")})
    @DeleteMapping("/{id}")
    public ResponseEntity<FileAccessPermission> delete(
            @NotBlank @PathVariable("id") Optional<FileAccessPermission> fileAccessPermission) {

        if (!fileAccessPermission.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        service.delete(fileAccessPermission.get());

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    /**
     * XXX.
     */
    @ApiOperation(value = "XXX")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "XXX")})
    @PostMapping()
    public ResponseEntity<Object> create(
            @Valid @RequestBody FileAccessPermission fileAccessPermission, Errors errors) {

        if (errors.hasErrors()) {

            throw new ApplicationValidationException(errors);
        }

        if (service.existsByUserAndIsoCountryCodeAndFileTypeAndAccess(fileAccessPermission)) {

            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(fileAccessPermission));
    }
}
