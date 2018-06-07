package org.iata.bsplink.airlinemgmt.model.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@Entity
@ApiModel(description = "Airline")
@JsonPropertyOrder(alphabetic = true)
public class Airline {

    @EmbeddedId
    @NotNull
    @JsonUnwrapped
    private AirlinePk airlinePk;

    @ApiModelProperty(value = "Airline  designator code.", required = true)
    @NotNull
    @Size(min = 2, max = 2)
    private String designator;

    @ApiModelProperty(value = "The airline global name.", required = true)
    @NotNull
    @Size(max = 255)
    private String globalName;

    @ApiModelProperty(value = "Effective Date (Status). Format YYYY-MM-DD.", required = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate fromDate;

    @ApiModelProperty(value = "Effective to (Status). Format YYYY-MM-DD.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate toDate;

    @Embedded
    @JsonUnwrapped
    private LocalAddress localAddress;

    @Embedded
    @JsonUnwrapped
    private LocalContact localContact;

    @Embedded
    @JsonUnwrapped
    private TaxInfo taxInfo;
}
