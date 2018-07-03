package org.iata.bsplink.agentmgmt.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data()
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Entity
public class FormOfPayment {

    @JsonIgnore
    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @Column(name = "iata_code")
    @Size(min = 8, max = 8)
    private String iataCode;

    @ApiModelProperty(required = true)
    @NotNull
    @Enumerated(EnumType.STRING)
    private FormOfPaymentType type;

    @ApiModelProperty(required = true)
    @NotNull
    @Enumerated(EnumType.STRING)
    private FormOfPaymentStatus status;

    /**
     * Creates a form of payment from a type and a status.
     */
    public FormOfPayment(FormOfPaymentType type, FormOfPaymentStatus status) {

        this.type = type;
        this.status = status;
    }
}
