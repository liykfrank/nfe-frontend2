package org.iata.bsplink.agencymemo.fake.restclient;

import static org.iata.bsplink.agencymemo.fake.Country.ISO_COUNTRY_CODE_AA;
import static org.iata.bsplink.agencymemo.fake.Country.ISO_COUNTRY_CODE_BB;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.iata.bsplink.agencymemo.dto.Agent;
import org.iata.bsplink.agencymemo.dto.Airline;

public class ClientMockFixtures {

    private static final String AIRLINE_CODE_1 = "123";
    private static final String AIRLINE_CODE_2 = "456";

    private static final String AGENT_CODE_1 = "78200010";
    private static final String AGENT_CODE_2 = "78200021";

    private ClientMockFixtures() {
        // hides implicit public constructor
    }

    /**
     * Returns a map of mocked airlines.
     */
    public static Map<String, Airline> getAirlines() {

        Map<String, Airline> airlines = new HashMap<>();

        airlines.put(getMapKey(ISO_COUNTRY_CODE_AA, AIRLINE_CODE_1),
                getAirlineMock(ISO_COUNTRY_CODE_AA, AIRLINE_CODE_1));

        airlines.put(getMapKey(ISO_COUNTRY_CODE_BB, AIRLINE_CODE_2),
                getAirlineMock(ISO_COUNTRY_CODE_BB, AIRLINE_CODE_2));

        return airlines;
    }

    public static String getMapKey(String val1, String val2) {

        return String.format("%s-%s", val1, val2);
    }

    private static Airline getAirlineMock(String isoCountryCode, String airlineCode) {

        Airline airline = new Airline();
        airline.setAirlineCode(airlineCode);
        airline.setIsoCountryCode(isoCountryCode);
        airline.setCity("CITY_" + airlineCode);
        airline.setCountry(isoCountryCode);
        airline.setGlobalName("GLOBAL_NAME_" + airlineCode);
        airline.setPostalCode("PC_" + airlineCode);
        airline.setTaxNumber("TAX_NUMBER_" + airlineCode);
        airline.setToDate(LocalDate.of(2999, 1, 1));

        return airline;
    }

    /**
     * Returns a map of mocked agents.
     */
    public static Map<String, Agent> getAgents() {

        Map<String, Agent> agents = new HashMap<>();

        agents.put(AGENT_CODE_1, getAgentMock(ISO_COUNTRY_CODE_AA, AGENT_CODE_1));
        agents.put(AGENT_CODE_2, getAgentMock(ISO_COUNTRY_CODE_BB, AGENT_CODE_2));

        return agents;
    }

    private static Agent getAgentMock(String isoCountryCode, String agentCode) {

        Agent agent = new Agent();

        agent.setIsoCountryCode(isoCountryCode);
        agent.setIataCode(agentCode);
        agent.setBillingCity("CITY_" + agentCode);
        agent.setBillingCountry(isoCountryCode);
        agent.setBillingPostalCode("PC_" + agentCode);
        agent.setBillingStreet("STREET_" + agentCode);
        agent.setDefaultDate(LocalDate.of(2999,  1, 1));
        agent.setName("NAME_" + agentCode);
        agent.setVatNumber("VAT_NUMBER_" + agentCode);

        return agent;
    }
}
