package org.iata.bsplink.airlinemgmt.model.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Embeddable;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class LocalContact {

    @ApiModelProperty(value = "Airline local contact first name")
    @Size(max = 255)
    private String firstName;

    @ApiModelProperty(value = "Airline local contact last name")
    @Size(max = 255)
    private String lastName;

    @ApiModelProperty(value = "Airline local contact e-mail")
    @Email
    private String email;

    @ApiModelProperty(value = "Airline local contact telephone number")
    @Size(max = 15)
    private String telephone;
}
