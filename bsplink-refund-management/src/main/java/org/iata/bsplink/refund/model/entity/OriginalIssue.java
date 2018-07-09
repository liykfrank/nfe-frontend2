package org.iata.bsplink.refund.model.entity;

import static org.iata.bsplink.refund.validation.ValidationMessages.INCORRECT_FORMAT;
import static org.iata.bsplink.refund.validation.ValidationMessages.INCORRECT_SIZE;

import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

import org.iata.bsplink.refund.validation.constraints.OriginalIssueAgentConstraint;

@Data
@Embeddable
public class OriginalIssue {

    @ApiModelProperty(value = "Original Issue Agent Code")
    @Size(min = 8, max = 8, message = INCORRECT_SIZE + 8)
    @Column(length = 8)
    @OriginalIssueAgentConstraint
    private String originalAgentCode;

    @ApiModelProperty(value = "Original Issue Airline Code")
    @Size(min = 3, max = 3, message = INCORRECT_SIZE + 3)
    @Pattern(regexp = "^[A-Z0-9]{3}$", message = INCORRECT_FORMAT)
    @Column(length = 3)
    private String originalAirlineCode;

    @ApiModelProperty(value =
            "Original Issue Ticket/Document Number (10 digits: Form Code + Serial Number)")
    @Size(min = 10, max = 10, message = INCORRECT_SIZE + 10)
    @Column(length = 10)
    private String originalTicketDocumentNumber;

    @ApiModelProperty(value = "Original Issue Date")
    private LocalDate originalDateOfIssue;

    @ApiModelProperty(value = "Original Issue Location City Code")
    @Size(min = 3, max = 3, message = INCORRECT_SIZE + 3)
    @Column(length = 3)
    private String originalLocationCityCode;
}
