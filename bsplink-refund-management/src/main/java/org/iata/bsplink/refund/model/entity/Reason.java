package org.iata.bsplink.refund.model.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(exclude = "id")
@NoArgsConstructor
@Entity
@ApiModel(description = "Reasons")
@JsonPropertyOrder(alphabetic = true)
public class Reason {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(length = 20)
    private ReasonType type;

    @Size(min = 3, max = 255)
    @NotNull
    private String title;

    @Size(min = 3, max = 500)
    @NotNull
    private String detail;

    @Size(min = 2, max = 2)
    @NotNull
    private String isoCountryCode;
}
