package org.iata.bsplink.filemanager.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.iata.bsplink.filemanager.exception.BsplinkValidationException;
import org.iata.bsplink.filemanager.pojo.BsplinkFileConfiguration;
import org.iata.bsplink.filemanager.service.BsplinkFileConfigService;
import org.iata.bsplink.filemanager.validation.ErrorsConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/v1/configurations")
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
public class BsplinkFileConfigurationController {

    @Autowired
    private BsplinkFileConfigService fileConfigurationService;

    @Autowired
    private MultipartProperties multipartProperties;

    /**
     * BSPlink file configurations.
     */
    @ApiOperation(value = "Configurations for Filemanager")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Configuration attributes")})
    @GetMapping()
    public ResponseEntity<BsplinkFileConfiguration> getConfig() {
        BsplinkFileConfiguration cfg = new BsplinkFileConfiguration();
        cfg.setBsplinkFileBasicConfig(fileConfigurationService.find());
        cfg.setMaxUploadFileSize(multipartProperties.getMaxFileSize());
        return ResponseEntity.status(HttpStatus.OK).body(cfg);
    }

    /**
     * Updates.
     */
    @ApiOperation(value = "Update the Filemanager Configuration")
    @ApiResponses(value = {@ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 400, message = "Error message")})
    @PutMapping()
    public ResponseEntity<Object> updateConfig(@ApiParam(name = "fileConfiguration",
            value = "the new File Configuration including all configuration attributes",
            required = true) @Valid @RequestBody BsplinkFileConfiguration bsplinkFileConfiguration,
            Errors errors) {

        if (errors.hasErrors()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", new ErrorsConverter(errors).toString());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        try {
            fileConfigurationService.update(bsplinkFileConfiguration.getBsplinkFileBasicConfig());
        } catch (BsplinkValidationException e) {
            return new ResponseEntity<>(e.getErrorMap(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
