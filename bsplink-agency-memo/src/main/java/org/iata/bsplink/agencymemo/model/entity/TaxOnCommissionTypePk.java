package org.iata.bsplink.agencymemo.model.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Embeddable
public class TaxOnCommissionTypePk implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ISO Country Code", required = true)
    @NotNull
    @Size(min = 2, max = 2)
    @Column(length = 2)
    private String isoCountryCode;

    @ApiModelProperty(
            value = "Code for Tax On Commission Type, an alphanumeric code",
            required = true)
    @NotNull
    @Size(min = 1, max = 6)
    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Only alphanumeric characters accepted.")
    @Column(length = 6)
    private String code;
}
