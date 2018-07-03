package org.iata.bsplink.agencymemo.restclient;

import org.iata.bsplink.agencymemo.dto.Airline;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "airlines", url = "${feign.airlines.url}", decode404 = true)
public interface AirlineClient {

    @GetMapping("/{isoCountryCode}/{airlineCode}")
    ResponseEntity<Airline> findAirline(
            @PathVariable("isoCountryCode") String isoCountryCode,
            @PathVariable("airlineCode") String airlineCode);
}