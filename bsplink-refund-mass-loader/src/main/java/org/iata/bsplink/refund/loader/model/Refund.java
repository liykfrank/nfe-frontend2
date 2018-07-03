package org.iata.bsplink.refund.loader.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.iata.bsplink.refund.loader.model.record.RecordIt01;
import org.iata.bsplink.refund.loader.model.record.RecordIt02;

@Getter
@Setter
@ToString
public class Refund {

    private RecordIt01 recordIt01;
    private RecordIt02 recordIt02;
}
