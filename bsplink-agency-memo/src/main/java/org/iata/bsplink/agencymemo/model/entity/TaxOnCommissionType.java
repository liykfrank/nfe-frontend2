package org.iata.bsplink.agencymemo.model.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Data
public class TaxOnCommissionType {

    public static final String DESCRIPTION_FORMAT = "";

    @EmbeddedId
    @JsonUnwrapped
    @Valid
    private TaxOnCommissionTypePk pk;


    @ApiModelProperty(value = "Description for Tax On Commission Type", required = true)
    @NotNull
    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[A-Za-z0-9 ]*$", message = DESCRIPTION_FORMAT)
    private String description;

}
