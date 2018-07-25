package org.iata.bsplink.agencymemo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(exclude = "id")
@Entity
public class Calculations {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @ApiModelProperty(value = "Fare", required = true)
    @NotNull
    @PositiveOrZero
    @Column(precision = 20, scale = 9)
    private BigDecimal fare = BigDecimal.ZERO;

    @ApiModelProperty(value = "Tax", required = true)
    @NotNull
    @PositiveOrZero
    @Column(precision = 20, scale = 9)
    private BigDecimal tax = BigDecimal.ZERO;

    @ApiModelProperty(value = "Commission", required = true)
    @NotNull
    @PositiveOrZero
    @Column(precision = 20, scale = 9)
    private BigDecimal commission = BigDecimal.ZERO;

    @ApiModelProperty(value = "SPAM", required = true)
    @NotNull
    @PositiveOrZero
    @Column(precision = 20, scale = 9)
    private BigDecimal spam = BigDecimal.ZERO;

    @ApiModelProperty(value = "Tax on Commission", required = true)
    @NotNull
    @PositiveOrZero
    @Column(precision = 20, scale = 9)
    private BigDecimal taxOnCommission = BigDecimal.ZERO;
}
