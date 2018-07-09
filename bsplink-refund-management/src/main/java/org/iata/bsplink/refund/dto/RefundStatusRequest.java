package org.iata.bsplink.refund.dto;

import static org.iata.bsplink.refund.validation.ValidationMessages.INCORRECT_SIZE;
import static org.iata.bsplink.refund.validation.ValidationMessages.NON_NULL_MESSAGE;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.iata.bsplink.refund.model.entity.RefundStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundStatusRequest {

    @ApiModelProperty(
            value = "Refund Status")
    @Enumerated(EnumType.STRING)
    @NotNull(message = NON_NULL_MESSAGE)
    private RefundStatus status;

    @ApiModelProperty(value = "Airline Remark")
    @Size(max = 500, message = INCORRECT_SIZE + "max 500")
    @Valid
    private String airlineRemark;

    @ApiModelProperty(value = "Rejection Reason")
    @Size(max = 500, message = INCORRECT_SIZE + "max 500")
    @Valid
    private String rejectionReason;
}
