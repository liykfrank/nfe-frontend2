package org.iata.bsplink.test.fixtures;

import java.util.Arrays;
import java.util.List;

import org.iata.bsplink.user.pojo.Airline;


public class AirlineFixtures {

    /**
     * Returns a list of Airlines.
     */
    public static List<Airline> getAirlines() {
        Airline a1 = new Airline();
        a1.setAirlineCode("220");
        a1.setIsoCountryCode("AA");
        a1.setGlobalName("Lufthansa");

        Airline a2 = new Airline();
        a2.setAirlineCode("075");
        a2.setIsoCountryCode("AA");
        a2.setGlobalName("Iberia");

        Airline a3 = new Airline();
        a3.setAirlineCode("075");
        a3.setIsoCountryCode("BB");
        a3.setGlobalName("Iberia");

        return Arrays.asList(a1, a2, a3);
    }

}
