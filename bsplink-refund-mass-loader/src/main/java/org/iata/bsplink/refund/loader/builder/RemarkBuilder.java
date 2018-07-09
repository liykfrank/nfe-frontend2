package org.iata.bsplink.refund.loader.builder;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Setter;

import org.iata.bsplink.refund.loader.model.record.RecordIt0h;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Setter
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RemarkBuilder {
    private List<RecordIt0h> it0hs;

    /**
     * Creates the text of the Remark.
     * @return The created text.
     */
    public String build() {
        return StringUtils.trimTrailingWhitespace(it0hs.stream()
                .map(it0h -> it0h.getReasonForMemoInformation1()
                        + it0h.getReasonForMemoInformation2() + it0h.getReasonForMemoInformation3()
                        + it0h.getReasonForMemoInformation4() + it0h.getReasonForMemoInformation5())
                .collect(Collectors.joining()));
    }
}
