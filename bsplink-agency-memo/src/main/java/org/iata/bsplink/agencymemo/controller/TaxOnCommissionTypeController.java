package org.iata.bsplink.agencymemo.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.iata.bsplink.agencymemo.model.entity.TaxOnCommissionType;
import org.iata.bsplink.agencymemo.model.entity.TaxOnCommissionTypePk;
import org.iata.bsplink.agencymemo.service.TaxOnCommissionTypeService;
import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
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
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/v1/tctps")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TaxOnCommissionTypeController {

    @Autowired
    TaxOnCommissionTypeService taxOnCommissionTypeService;

    /**
     * Tax On Commission Types.
     */
    @ApiOperation(value = "Tax On Commission Types")
    @ApiResponses(
            value = {@ApiResponse(code = 200,
            message = "Array of Tax On Commission Types")})
    @GetMapping("/{isoc}")
    public ResponseEntity<List<TaxOnCommissionType>> getTctps(
            @PathVariable("isoc") String isoc) {

        return ResponseEntity.status(HttpStatus.OK).body(
                taxOnCommissionTypeService.findByIsoCountryCode(isoc));
    }

    /**
     * Save an Tax On Commission Type.
     */
    @ApiOperation(value = "Save a Tax On Commission Type")
    @PostMapping()
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "body",
                value = "The Tax On Commission Type to save.",
            paramType = "body", required = true, dataType = "TaxOnCommissionType")})
    public ResponseEntity<TaxOnCommissionType> save(
            @Valid
            @RequestBody
            TaxOnCommissionType taxOnCommissionType,
            Errors errors) {
        if (errors.hasErrors()) {
            throw new ApplicationValidationException(errors);
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taxOnCommissionTypeService.save(taxOnCommissionType));
    }

    /**
     * Deletes an Tax On Commission Type.
     */
    @ApiOperation(value = "Deletes an Tax On Commission Type")
    @DeleteMapping("/{isoc}/{code}")
    public ResponseEntity<TaxOnCommissionType> delete(
            @PathVariable("isoc") String isoc,
            @PathVariable("code") String code) {

        TaxOnCommissionTypePk pk = new TaxOnCommissionTypePk();
        pk.setIsoCountryCode(isoc);
        pk.setCode(code);

        Optional<TaxOnCommissionType> optionalTaxOnCommissionType
            = taxOnCommissionTypeService.find(pk);

        if (!optionalTaxOnCommissionType.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        taxOnCommissionTypeService.delete(optionalTaxOnCommissionType.get());

        return ResponseEntity.status(HttpStatus.OK).body(optionalTaxOnCommissionType.get());
    }
}
