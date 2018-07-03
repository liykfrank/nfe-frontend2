package org.iata.bsplink.airlinemgmt.service;

import java.util.List;
import java.util.Optional;

import org.iata.bsplink.airlinemgmt.model.entity.Airline;
import org.iata.bsplink.airlinemgmt.model.repository.AirlineRepository;
import org.springframework.stereotype.Service;

@Service
public class AirlineService {

    private AirlineRepository airlineRepository;

    public AirlineService(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    public List<Airline> saveAll(List<Airline> airlines) {

        return airlineRepository.saveAll(airlines);
    }

    public List<Airline> findAll() {

        return airlineRepository.findAll();
    }

    /**
     * Finds an airline by airline code and ISO country code.
     */
    public Optional<Airline> findByAirlineCodeAndIsoCountryCode(String airlineCode,
            String isoCountryCode) {

        return airlineRepository.findByAirlinePkAirlineCodeAndAirlinePkIsoCountryCode(airlineCode,
                isoCountryCode);
    }

    public void delete(Airline airline) {

        airlineRepository.delete(airline);
    }

}
