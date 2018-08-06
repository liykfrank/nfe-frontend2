package org.iata.bsplink.refund.loader.error;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.iata.bsplink.refund.loader.model.record.FieldLayout;
import org.iata.bsplink.refund.loader.model.record.RecordIdentifier;

@Getter
@Setter
@ToString
public class RefundLoaderError {

    private String field;
    private String message;
    private String description;
    private String transactionNumber;
    private ValidationPhase validationPhase = ValidationPhase.NONE;
    private Integer lineNumber;
    private RecordIdentifier recordIdentifier;
    private FieldLayout fieldLayout;
}
