package org.iata.bsplink.agencymemo.dto;

import static org.iata.bsplink.agencymemo.validation.ValidationMessages.INCORRECT_FORMAT;
import static org.iata.bsplink.agencymemo.validation.ValidationMessages.INCORRECT_SIZE;
import static org.iata.bsplink.agencymemo.validation.ValidationMessages.NON_NULL_MESSAGE;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ContactRequest {
    private static final String PATTERN = "^[\\x20-\\x7E]*[\\x21-\\x7E][\\x20-\\x7E]*$";

    @ApiModelProperty(value = "Contact Name, pattern: " + PATTERN, required = true)
    @Size(max = 49, message = INCORRECT_SIZE)
    @NotNull(message = NON_NULL_MESSAGE)
    @Pattern(regexp = PATTERN, message = INCORRECT_FORMAT)
    private String contactName;

    @ApiModelProperty(value = "Phone/Fax Number, pattern: " + PATTERN, required = true)
    @Size(max = 30, message = INCORRECT_SIZE + "max 30")
    @NotNull(message = NON_NULL_MESSAGE)
    @Pattern(regexp = PATTERN, message = INCORRECT_FORMAT)
    private String phoneFaxNumber;

    @ApiModelProperty(value = "E-Mail Address, pattern: " + PATTERN, required = true)
    @Size(max = 100, message = INCORRECT_SIZE + "max 100")
    @NotNull(message = NON_NULL_MESSAGE)
    @Pattern(regexp = PATTERN, message = INCORRECT_FORMAT)
    private String email;
}
