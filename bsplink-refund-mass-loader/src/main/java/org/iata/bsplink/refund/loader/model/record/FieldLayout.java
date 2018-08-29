package org.iata.bsplink.refund.loader.model.record;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.springframework.batch.item.file.transform.Range;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class FieldLayout {

    @NonNull
    private RecordIdentifier recordIdentifier;
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

    @JsonIgnore
    public Range getRange() {

        return new Range(startPosition, startPosition + length - 1);
    }
}
