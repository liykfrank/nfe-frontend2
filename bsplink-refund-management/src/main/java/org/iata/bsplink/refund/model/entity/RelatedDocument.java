package org.iata.bsplink.refund.model.entity;

import static org.iata.bsplink.refund.validation.ValidationMessages.INCORRECT_SIZE;
import static org.iata.bsplink.refund.validation.ValidationMessages.NON_NULL_MESSAGE;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Embeddable
public class RelatedDocument {

    @ApiModelProperty(
            value = "Related Ticket/Document Number",
            required = true)
    @Size(min = 10, max = 10, message = INCORRECT_SIZE + 10)
    @NotNull(message = NON_NULL_MESSAGE)
    @Column(length = 10)
    private String relatedTicketDocumentNumber;

    @ApiModelProperty(
            value = "Related Ticket/Document first Coupon",
            required = true)
    @NotNull(message = NON_NULL_MESSAGE)
    private Boolean relatedTicketCoupon1 = Boolean.FALSE;

    @ApiModelProperty(
            value = "Related Ticket/Document second Coupon",
            required = true)
    @NotNull(message = NON_NULL_MESSAGE)
    private Boolean relatedTicketCoupon2 = Boolean.FALSE;

    @ApiModelProperty(
            value = "Related Ticket/Document third Coupon",
            required = true)
    @NotNull(message = NON_NULL_MESSAGE)
    private Boolean relatedTicketCoupon3 = Boolean.FALSE;

    @ApiModelProperty(
            value = "Related Ticket/Document fourth Coupon",
            required = true)
    @NotNull(message = NON_NULL_MESSAGE)
    private Boolean relatedTicketCoupon4 = Boolean.FALSE;
}
