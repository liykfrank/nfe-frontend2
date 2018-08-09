package org.iata.bsplink.refund.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
    private List<FormOfPayment> formOfPayment;
}
