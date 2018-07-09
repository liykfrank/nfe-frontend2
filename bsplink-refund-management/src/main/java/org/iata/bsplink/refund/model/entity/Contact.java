package org.iata.bsplink.refund.model.entity;

import static org.iata.bsplink.refund.validation.ValidationMessages.NON_NULL_MESSAGE;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
public class Contact {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @ApiModelProperty(value = "Contact Name", required = true)
    @Size(max = 49)
    @NotNull(message = NON_NULL_MESSAGE)
    @Column(length = 49)
    private String contactName;

    @ApiModelProperty(value = "Phone/Fax Number", required = true)
    @Size(max = 30)
    @NotNull(message = NON_NULL_MESSAGE)
    @Column(length = 30)
    private String phoneFaxNumber;

    @ApiModelProperty(value = "E-Mail Address", required = true)
    @Size(max = 100)
    @NotNull(message = NON_NULL_MESSAGE)
    @Column(length = 100)
    @Email
    private String email;
}
