package org.iata.bsplink.agencymemo.fake.restclient;

import static org.iata.bsplink.agencymemo.fake.restclient.ClientMockFixtures.getAirlines;
import static org.iata.bsplink.agencymemo.fake.restclient.ClientMockFixtures.getMapKey;

import java.util.Map;

import org.iata.bsplink.agencymemo.dto.Airline;
import org.iata.bsplink.agencymemo.restclient.AirlineClient;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Mock client with develop purposes.
 */
@Component
@Profile("mock")
public class AirlineClientMock implements AirlineClient {

    Map<String, Airline> airlines = getAirlines();

    @Override
    public ResponseEntity<Airline> findAirline(String isoCountryCode, String airlineCode) {

        String mapKey = getMapKey(isoCountryCode, airlineCode);

        if (!airlines.containsKey(mapKey)) {

            return ResponseEntity.notFound().build();
        }

        return new ResponseEntity<>(airlines.get(mapKey), HttpStatus.OK);
    }

}
