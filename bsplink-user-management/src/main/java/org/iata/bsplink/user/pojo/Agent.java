package org.iata.bsplink.user.pojo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDate;

import lombok.Data;

@Data
@JsonPropertyOrder(alphabetic = true)
public class Agent {
    private String name;
    private String iataCode;
    private LocalDate defaultDate;
    private String billingCity;
    private String billingCountry;
    private String billingPostalCode;
    private String billingStreet;
    private String isoCountryCode;
    private String vatNumber;
}
