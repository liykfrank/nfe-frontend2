package org.iata.bsplink.refund.loader.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Contact {

    private String contactName;
    private String email;
    private String phoneFaxNumber;
}
