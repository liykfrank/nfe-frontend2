package org.iata.bsplink.agencymemo.model.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Embeddable
public class Contact {
    @ApiModelProperty(value = "Contact Name", required = true)
    @Size(max = 49)
    @NotNull
    @Column(length = 49)
    private String contactName;

    @ApiModelProperty(value = "Phone/Fax Number", required = true)
    @Size(max = 30)
    @NotNull
    @Column(length = 30)
    private String phoneFaxNumber;

    @ApiModelProperty(value = "E-Mail Address", required = true)
    @Size(max = 100)
    @NotNull
    @Column(length = 100)
    private String email;
}
