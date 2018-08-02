package org.iata.bsplink.refund.loader.model.record;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.springframework.batch.item.file.transform.Range;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class FieldLayout {

    @NonNull
    private String field;
    @NonNull
    private Integer elementNumber;
    @NonNull
    private String abbreviation;
    @NonNull
    private FieldType type;
    @NonNull
    private Integer startPosition;
    @NonNull
    private Integer length;

    public Range getRange() {

        return new Range(startPosition, startPosition + length - 1);
    }
}
