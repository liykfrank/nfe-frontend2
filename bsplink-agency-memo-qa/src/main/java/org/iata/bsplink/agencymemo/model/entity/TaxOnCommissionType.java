package org.iata.bsplink.agencymemo.model.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Data
public class TaxOnCommissionType {

    @EmbeddedId
    @JsonUnwrapped
    private TaxOnCommissionTypePk pk;

    public String getCode() {
        return pk == null ? null : pk.getCode();
    }

    public String getIsoCountryCode() {
        return pk == null ? null : pk.getIsoCountryCode();
    }

    @ApiModelProperty(value = "Description for Tax On Commission Type", required = true)
    @NotNull
    @Size(min = 1, max = 50)
    @Pattern(
            regexp = "^[A-Za-z0-9 ]*$",
            message = "Only alphanumeric characters and blank spaces accepted.")
    private String description;

}
