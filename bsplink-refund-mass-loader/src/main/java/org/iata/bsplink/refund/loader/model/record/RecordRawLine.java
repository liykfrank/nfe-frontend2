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
public class RecordRawLine implements Record {

    @Setter(AccessLevel.NONE)
    private RecordIdentifier recordIdentifier = RecordIdentifier.UNKNOWN;
    private int lineNumber;

    private String line;

}
