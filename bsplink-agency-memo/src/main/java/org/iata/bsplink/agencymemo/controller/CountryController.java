package org.iata.bsplink.agencymemo.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

import org.iata.bsplink.agencymemo.fake.Country;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/v1/countries")
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
public class CountryController {

    /**
     * Get all countries.
     */
    @ApiOperation(value = "Returns the list of countries.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "List of Countries")})
    @GetMapping()
    public ResponseEntity<List<Country>> getCountries() {
        return ResponseEntity.status(HttpStatus.OK).body(Country.findAllCountries());
    }
}
