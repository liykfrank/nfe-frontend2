package org.iata.bsplink.refund.loader.model.record;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RecordIt0h implements TransactionRecord {

    @Setter(AccessLevel.NONE)
    private RecordIdentifier recordIdentifier = RecordIdentifier.IT0H;
    private int lineNumber;

    private String reasonForMemoInformation1;
    private String reasonForMemoInformation2;
    private String reasonForMemoInformation3;
    private String reasonForMemoInformation4;
    private String reasonForMemoInformation5;
    private String reasonForMemoIssuanceCode;
    private String reasonForMemoLineIdentifier1;
    private String reasonForMemoLineIdentifier2;
    private String reasonForMemoLineIdentifier3;
    private String reasonForMemoLineIdentifier4;
    private String reasonForMemoLineIdentifier5;
    private String transactionNumber;
}
