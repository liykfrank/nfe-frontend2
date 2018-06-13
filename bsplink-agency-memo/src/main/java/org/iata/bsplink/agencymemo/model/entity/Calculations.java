package org.iata.bsplink.agencymemo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(exclude = "id")
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Calculations {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @ApiModelProperty(value = "Fare", required = true)
    @NotNull
    @PositiveOrZero
    private BigDecimal fare = BigDecimal.ZERO;

    @ApiModelProperty(value = "Tax", required = true)
    @NotNull
    @PositiveOrZero
    private BigDecimal tax = BigDecimal.ZERO;

    @ApiModelProperty(value = "Commission", required = true)
    @NotNull
    @PositiveOrZero
    private BigDecimal commission = BigDecimal.ZERO;

    @ApiModelProperty(value = "SPAM", required = true)
    @NotNull
    @PositiveOrZero
    private BigDecimal spam = BigDecimal.ZERO;

    @ApiModelProperty(value = "Tax on Commission", required = true)
    @NotNull
    @PositiveOrZero
    private BigDecimal taxOnCommission = BigDecimal.ZERO;
}
