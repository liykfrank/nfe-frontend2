package org.iata.bsplink.refund.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.iata.bsplink.refund.dto.Airline;
import org.iata.bsplink.refund.service.AirlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/v1/airlines")
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
public class AirlineController {

    @Autowired
    AirlineService airlineService;

    /**
     * Get basic Agent Data.
     */
    @ApiOperation(value = "Get basic Airline Data")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Airline Data")})
    @GetMapping("/{isoCountryCode}/{airlineCode}")
    public ResponseEntity<Airline> getAirline(@PathVariable("isoCountryCode") String isoCountryCode,
            @PathVariable("airlineCode") String airlineCode) {

        return airlineService.findAirlineResponse(isoCountryCode, airlineCode);
    }
}
