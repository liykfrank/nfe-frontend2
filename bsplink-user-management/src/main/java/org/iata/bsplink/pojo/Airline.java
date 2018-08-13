package org.iata.bsplink.pojo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;

import java.time.LocalDate;

import lombok.Data;

@ApiModel(description = "Airline")
@Data
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
