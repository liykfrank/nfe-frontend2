package org.iata.bsplink.refund.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

import javax.validation.Valid;

import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
import org.iata.bsplink.refund.model.entity.Config;
import org.iata.bsplink.refund.service.ConfigService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/v1/configurations")
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
public class ConfigController {

    @Autowired
    ConfigService configService;

    /**
     * Configurations for refunds.
     */
    @ApiOperation(value = "Configuration for refunds")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Config")})
    @GetMapping("/{isoc}")
    public ResponseEntity<Config> getConfig(@PathVariable String isoc) {

        return ResponseEntity.status(HttpStatus.OK).body(configService.find(isoc));
    }

    /**
     * Retrieves all configurations.
     */
    @ApiOperation(value = "List of Configurations")
    @GetMapping
    public ResponseEntity<List<Config>> getConfigs() {

        return ResponseEntity.status(HttpStatus.OK).body(configService.findAll());
    }

    /**
     * Save a configuration.
     */
    @ApiOperation(value = "Save a configuration")
    @PostMapping
    @ApiImplicitParams({@ApiImplicitParam(name = "body", value = "The configuration to save.",
            paramType = "body", required = true, dataType = "Config")})
    public ResponseEntity<Config> save(@Valid @RequestBody Config config, Errors errors) {

        if (errors.hasErrors()) {
            throw new ApplicationValidationException(errors);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(configService.save(config));
    }

    /**
     * Get all ISO Country Codes.
     */
    @ApiOperation(value = "List of ISOCs")
    @GetMapping("/isocs")
    public ResponseEntity<List<String>> getIsocs() {

        return ResponseEntity.status(HttpStatus.OK).body(configService.findAllIsoCountryCodes());
    }
}
