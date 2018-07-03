package org.iata.bsplink.airlinemgmt.model.repository;

import java.util.Optional;

import org.iata.bsplink.airlinemgmt.model.entity.Airline;
import org.iata.bsplink.airlinemgmt.model.entity.AirlinePk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirlineRepository extends JpaRepository<Airline, AirlinePk> {

    public Optional<Airline> findByAirlinePkAirlineCodeAndAirlinePkIsoCountryCode(
            String airlineCode, String isoCountryCode);
}
