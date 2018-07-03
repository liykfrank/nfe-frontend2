package org.iata.bsplink.airlinemgmt.test.fixtures;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.iata.bsplink.airlinemgmt.model.entity.Airline;
import org.iata.bsplink.airlinemgmt.model.entity.AirlinePk;
import org.iata.bsplink.airlinemgmt.model.entity.LocalAddress;
import org.iata.bsplink.airlinemgmt.model.entity.LocalContact;
import org.iata.bsplink.airlinemgmt.model.entity.TaxInfo;

public class AirlineFixtures {

    public static final String AIRLINE_1_CODE = "001";
    public static final String AIRLINE_1_COUNTRY = "C1";

    public static final String AIRLINE_2_CODE = "002";
    public static final String AIRLINE_2_COUNTRY = "C2";

    private static final LocalDate ANY_DATE =
            LocalDate.parse("2016-01-01", DateTimeFormatter.ISO_LOCAL_DATE);

    public static List<Airline> getAirlinesFixture() {

        return Arrays.asList(getAirlineFixture(AIRLINE_1_CODE), getAirlineFixture(AIRLINE_2_CODE));
    }

    private static Airline getAirlineFixture(String airlineCode) {

        Airline airline = new Airline();

        airline.setAirlinePk(new AirlinePk(airlineCode, getAirlineCountry(airlineCode)));
        airline.setFromDate(ANY_DATE);
        airline.setGlobalName("NAME_" + airlineCode);
        airline.setLocalAddress(getLocalAddressFixture(airlineCode));
        airline.setLocalContact(getLocalContactFixture(airlineCode));
        airline.setTaxInfo(getTaxInfoFixture(airlineCode));
        airline.setToDate(ANY_DATE);
        airline.setDesignator(
                airlineCode.substring(airlineCode.length() - 2, airlineCode.length()));

        return airline;
    }

    private static String getAirlineCountry(String airlineCode) {

        if (airlineCode.equals(AIRLINE_1_CODE)) {
            return AIRLINE_1_COUNTRY;
        } else if (airlineCode.equals(AIRLINE_2_CODE)) {
            return AIRLINE_2_COUNTRY;
        }

        return null;
    }

    /**
     * Creates a LocalAddress for an airline code.
     */
    public static LocalAddress getLocalAddressFixture(String airlineCode) {

        return new LocalAddress() {
            {
                setAddress1("ADDRESS_" + airlineCode);
                setCity("CITY_" + airlineCode);
                setCountry("C" + airlineCode.substring(airlineCode.length() - 1));
                setPostalCode("PC_" + airlineCode);
                setState("STATE_" + airlineCode);
            }
        };
    }

    /**
     * Creates a LocalContact for an airline code.
     */
    public static LocalContact getLocalContactFixture(String airlineCode) {

        return new LocalContact() {
            {
                setFirstName("FIRST_NAME_" + airlineCode);
                setLastName("LAST_NAME_" + airlineCode);
                setEmail(String.format("airline%s@example.com", airlineCode));
                setTelephone("000-000-" + airlineCode);
            }
        };
    }

    /**
     * Creates a TaxInfo for an airline code.
     */
    public static TaxInfo getTaxInfoFixture(String airlineCode) {

        return new TaxInfo() {
            {
                setTaxNumber("000000" + airlineCode);
            }
        };
    }
}
