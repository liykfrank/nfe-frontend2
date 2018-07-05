package org.iata.bsplink.refund.loader.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelatedDocument {

    private Boolean relatedTicketCoupon1;
    private Boolean relatedTicketCoupon2;
    private Boolean relatedTicketCoupon3;
    private Boolean relatedTicketCoupon4;
    private String relatedTicketDocumentNumber;
}
