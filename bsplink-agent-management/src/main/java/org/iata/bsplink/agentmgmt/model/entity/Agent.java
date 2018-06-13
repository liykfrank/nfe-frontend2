package org.iata.bsplink.agentmgmt.model.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.iata.bsplink.agentmgmt.validation.constraints.ControlDigitConstraint;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@Entity
@ApiModel(description = "Agent")
@JsonPropertyOrder(alphabetic = true)
public class Agent {

    @ApiModelProperty(required = true)
    @Size(min = 3, max = 255)
    @NotNull
    private String name;

    @Id
    @ApiModelProperty(value = "The IATA code of the agency.", required = true)
    @NotNull
    @Size(min = 8, max = 8)
    @ControlDigitConstraint
    private String iataCode;

    @ApiModelProperty(
            value = "Accreditation Date. Date in format YYYY-MM-DD. MM and DD with padded 0",
            required = true)
    @NotNull
    private LocalDate accreditationDate;

    @ApiModelProperty(value = "Default Date. Date in format YYYY-MM-DD. MM and DD with padded 0")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate defaultDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "iata_code")
    @Valid
    private List<FormOfPayment> formOfPayment;

    @ApiModelProperty(value = "Location class of the agency")
    @Enumerated(EnumType.STRING)
    private LocationClass locationClass;

    @ApiModelProperty(value = "Agency Remittance Frequency", required = true)
    @NotNull
    @Enumerated(EnumType.STRING)
    private RemittanceFrequency remittanceFrequency;

    @Size(max = 20)
    private String taxId;

    @Size(max = 100)
    private String tradeName;

    @Size(max = 40)
    private String billingCity;

    @Size(max = 80)
    private String billingCountry;

    @Size(max = 20)
    private String billingPostalCode;

    @Size(max = 80)
    private String billingState;

    @Size(max = 255)
    private String billingStreet;

    @Size(max = 40)
    private String phone;

    @Email
    private String email;

    @Size(max = 40)
    private String fax;

    @ApiModelProperty(required = true)
    @Size(min = 2, max = 2)
    @NotNull
    private String isoCountryCode;

    @ApiModelProperty(
            value = "Whether the agency is a head entity (HE) or an associate entity (AE).")
    @Enumerated(EnumType.STRING)
    private LocationType locationType;

    @Size(min = 8, max = 8)
    @ControlDigitConstraint
    private String parentIataCode;

    @Size(min = 8, max = 8)
    @ControlDigitConstraint
    private String topParentIataCode;

    @Size(max = 30)
    private String vatNumber;
}
