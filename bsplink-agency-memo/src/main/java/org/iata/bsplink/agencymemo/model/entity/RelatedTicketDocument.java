package org.iata.bsplink.agencymemo.model.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Embeddable
public class RelatedTicketDocument {

    @ApiModelProperty(
            value = "Related Ticket/Document Number,"
                    + " 3 character airline code + 10 character ticket number",
            required = true)
    @Size(min = 13, max = 13)
    @NotNull
    @Column(length = 13)
    private String relatedTicketDocumentNumber;

    @ApiModelProperty(
            value = "Check-Digit, check digit for Related Ticket/Document Number",
            allowableValues = "0, 1, 2, 3, 4, 5, 6, 9",
            required = false)
    @NotNull
    private Integer checkDigit = 9;
}
