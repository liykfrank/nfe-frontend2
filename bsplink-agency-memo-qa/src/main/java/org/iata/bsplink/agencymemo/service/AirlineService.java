package org.iata.bsplink.agencymemo.service;

import org.iata.bsplink.agencymemo.dto.Airline;
import org.iata.bsplink.agencymemo.exception.FeignClientException;
import org.iata.bsplink.agencymemo.restclient.AirlineClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AirlineService {

    private AirlineClient airlineClient;

    public AirlineService(AirlineClient airlineClient) {
        this.airlineClient = airlineClient;
    }

    public ResponseEntity<Airline> findAirlineResponse(String isoCountryCode, String airlineCode) {
        return airlineClient.findAirline(isoCountryCode, airlineCode);
    }

    /**
     * Returns an Airline.
     */
    public Airline findAirline(String isoCountryCode, String airlineCode) {
        ResponseEntity<Airline> respAirline = findAirlineResponse(isoCountryCode, airlineCode);

        if (!respAirline.getStatusCode().isError()) {
            return respAirline.getBody();
        }
        if (respAirline.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            return null;
        }

        throw new FeignClientException(respAirline.getStatusCode());
    }

}
