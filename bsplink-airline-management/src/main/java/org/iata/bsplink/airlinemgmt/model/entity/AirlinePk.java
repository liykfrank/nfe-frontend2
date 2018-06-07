package org.iata.bsplink.airlinemgmt.model.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Embeddable
public class AirlinePk implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "The IATA code of the airline.", required = true)
    @NotNull
    @Size(min = 3, max = 3)
    @Column(length = 3)
    private String airlineCode;

    @ApiModelProperty(value = "ISO country code.", required = true)
    @NotNull
    @Size(min = 2, max = 2)
    @Column(length = 2)
    private String isoCountryCode;

    public AirlinePk() { }

    /**
     * Creates an AirlinePk using the airline code and the ISO country code.
     */
    public AirlinePk(String airlineCode, String isoCountryCode) {

        this.airlineCode = airlineCode;
        this.isoCountryCode = isoCountryCode;
    }
}
