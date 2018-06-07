package org.iata.bsplink.airlinemgmt.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;
import java.util.Optional;

import org.iata.bsplink.airlinemgmt.model.entity.Airline;
import org.iata.bsplink.airlinemgmt.model.entity.AirlinesWrapper;
import org.iata.bsplink.airlinemgmt.service.AirlineService;
import org.iata.bsplink.commons.rest.exception.ApplicationValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/v1/airlines")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AirlineController {

    @Autowired
    AirlineService airlineService;

    @Autowired
    private Validator validator;

    /**
     * Loads an array of airlines.
     */
    @ApiOperation(value = "Loads an array of Airlines")
    @PostMapping()
    @ApiImplicitParams({@ApiImplicitParam(name = "body", value = "The airlines to load.",
            allowMultiple = true, paramType = "body", required = true, dataType = "Airline")})
    public ResponseEntity<String> load(@RequestBody List<Airline> airlines) {

        Errors errors = validateAirlines(airlines);

        if (errors.hasErrors()) {
            throw new ApplicationValidationException(errors);
        }

        airlineService.saveAll(airlines);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private Errors validateAirlines(List<Airline> airlines) {

        AirlinesWrapper wrapper = new AirlinesWrapper(airlines);

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(wrapper, "Airline");

        validator.validate(wrapper, errors);

        return errors;
    }

    @ApiOperation(value = "List of airlines")
    @GetMapping()
    public ResponseEntity<List<Airline>> getAirlines() {

        return ResponseEntity.status(HttpStatus.OK).body(airlineService.findAll());
    }

    /**
     * Gets an airline by code.
     */
    @ApiOperation(value = "Airline by code")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Airline")})
    @GetMapping("/{isoCountryCode}/{airlineCode}")
    public ResponseEntity<Airline> getAirline(
            @PathVariable("isoCountryCode") String isoCountryCode,
            @PathVariable("airlineCode") String airlineCode) {

        Optional<Airline> optionalAirline =
                airlineService.findByAirlineCodeAndIsoCountryCode(airlineCode, isoCountryCode);

        if (!optionalAirline.isPresent()) {

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(optionalAirline.get());
    }

    /**
     * Deletes an Airline.
     */
    @ApiOperation(value = "Deletes an Airline")
    @DeleteMapping("/{isoCountryCode}/{airlineCode}")
    public ResponseEntity<Airline> deleteAirline(
            @PathVariable("isoCountryCode") String isoCountryCode,
            @PathVariable("airlineCode") String airlineCode) {

        Optional<Airline> optionalAirline =
                airlineService.findByAirlineCodeAndIsoCountryCode(airlineCode, isoCountryCode);

        if (!optionalAirline.isPresent()) {

            return ResponseEntity.notFound().build();
        }

        airlineService.delete(optionalAirline.get());

        return ResponseEntity.status(HttpStatus.OK).body(optionalAirline.get());
    }
}
