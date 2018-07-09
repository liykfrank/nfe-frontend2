package org.iata.bsplink.refund.dto;

import lombok.Getter;
import lombok.Setter;

import org.iata.bsplink.refund.model.entity.FormOfPaymentType;

@Getter
@Setter
public class FormOfPayment {

    private FormOfPaymentStatus status;
    private FormOfPaymentType type;
}
