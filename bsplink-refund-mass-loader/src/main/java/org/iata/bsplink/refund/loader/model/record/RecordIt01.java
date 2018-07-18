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
public class RecordIt01 implements Record {

    @Setter(AccessLevel.NONE)
    private RecordIdentifier recordIdentifier = RecordIdentifier.IT01;
    private int lineNumber;

    private String fileType;
    private String fileTypeSequenceNumber;
    private String handbookRevisionNumber;
    private String isoCountryCode;
    private String processingDate;
    private String processingTime;
    private String reportingSystemIdentifier;
    private String systemProviderReportingPeriodEndingDate;
    private String testProductionStatus;
}
