package org.iata.bsplink.airlinemgmt.model.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class LocalAddress {

    @ApiModelProperty(value = "Airline local address 1.")
    @Size(max = 50)
    private String address1;

    @ApiModelProperty(value = "Airline local address city.")
    @Size(max = 50)
    private String city;

    @ApiModelProperty(value = "Airline local address state.")
    @Size(max = 50)
    private String state;

    @ApiModelProperty(value = "Airline local address country.")
    @Size(min = 2, max = 2)
    private String country;

    @ApiModelProperty(value = "Airline local ZIP/Postal code.")
    @Size(max = 10)
    private String postalCode;
}
