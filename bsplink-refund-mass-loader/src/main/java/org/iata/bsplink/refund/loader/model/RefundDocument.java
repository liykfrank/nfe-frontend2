package org.iata.bsplink.refund.loader.model;

import lombok.Data;

import org.iata.bsplink.refund.loader.model.record.RecordIt01;
import org.iata.bsplink.refund.loader.model.record.RecordIt02;

@Data
public class RefundDocument {

    private RecordIt01 recordIt01;
    private RecordIt02 recordIt02;
}
