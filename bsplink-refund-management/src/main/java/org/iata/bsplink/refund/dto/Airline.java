package org.iata.bsplink.refund.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder(alphabetic = true)
public class Airline {

    private String address1;
    private String airlineCode;
    private String city;
    private String country;
    private String globalName;
    private String isoCountryCode;
    private String postalCode;
    private String taxNumber;
    private LocalDate toDate;
}
