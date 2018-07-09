package org.iata.bsplink.refund.model.entity;

import static org.iata.bsplink.refund.validation.ValidationMessages.INCORRECT_SIZE;
import static org.iata.bsplink.refund.validation.ValidationMessages.NON_NULL_MESSAGE;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(description = "Agent's permission to issue refunds for a specific airline.")
@Data
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"agentCode", "airlineCode"})})
//@RefundIssuePermissionConstraint
public class RefundIssuePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    Long id;

    @ApiModelProperty(
        value = "Agent Code",
        required = true)
    @NotNull(message = NON_NULL_MESSAGE)
    @Size(min = 8, max = 8, message = INCORRECT_SIZE + 8)
    @Column(length = 8)
    private String agentCode;

    @ApiModelProperty(
            value = "Airline Code",
            required = true)
    @Size(min = 3, max = 3, message = INCORRECT_SIZE + 3)
    @NotNull(message = NON_NULL_MESSAGE)
    @Column(length = 3)
    private String airlineCode;

    @ApiModelProperty(
            value = "ISO Country Code, two letter code",
            required = true)
    @Size(min = 2, max = 2, message = INCORRECT_SIZE + 2)
    @NotNull(message = NON_NULL_MESSAGE)
    private String isoCountryCode;
}
