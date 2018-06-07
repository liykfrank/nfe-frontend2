package org.iata.bsplink.airlinemgmt.model.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class TaxInfo {

    @ApiModelProperty(value = "Tax number.")
    @Size(max = 60)
    private String taxNumber;
}
