package org.iata.bsplink.agencymemo.dto;

import static org.iata.bsplink.agencymemo.validation.ValidationMessages.INCORRECT_SIZE;
import static org.iata.bsplink.agencymemo.validation.ValidationMessages.NON_NULL_MESSAGE;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class RelatedTicketDocumentRequest {

    @ApiModelProperty(
            value = "Related Ticket/Document Number,"
                    + " 3 character airline code + 10 character ticket number",
            required = true)
    @Size(min = 13, max = 13, message = INCORRECT_SIZE + 13)
    @NotNull(message = NON_NULL_MESSAGE)
    @Pattern(regexp = "^[A-Z0-9]{3}[0-9]*$")
    private String relatedTicketDocumentNumber;
}
