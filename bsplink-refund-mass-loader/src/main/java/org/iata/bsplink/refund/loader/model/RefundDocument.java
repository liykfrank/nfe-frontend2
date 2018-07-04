package org.iata.bsplink.refund.loader.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import org.iata.bsplink.refund.loader.model.record.RecordIt02;
import org.iata.bsplink.refund.loader.model.record.RecordIt03;
import org.iata.bsplink.refund.loader.model.record.RecordIt05;
import org.iata.bsplink.refund.loader.model.record.RecordIt08;
import org.iata.bsplink.refund.loader.model.record.RecordIt0h;
import org.iata.bsplink.refund.loader.model.record.RecordIt0y;

@Data
public class RefundDocument {

    private RecordIt02 recordIt02;
    private List<RecordIt03> recordsIt03 = new ArrayList<>();
    private List<RecordIt05> recordsIt05 = new ArrayList<>();
    private List<RecordIt08> recordsIt08 = new ArrayList<>();
    private List<RecordIt0y> recordsIt0y = new ArrayList<>();
    private List<RecordIt0h> recordsIt0h = new ArrayList<>();

    public void addrecordIt03(RecordIt03 record) {
        recordsIt03.add(record);
    }

    public void addrecordIt05(RecordIt05 record) {
        recordsIt05.add(record);
    }

    public void addrecordIt08(RecordIt08 record) {
        recordsIt08.add(record);
    }

    public void addrecordIt0y(RecordIt0y record) {
        recordsIt0y.add(record);
    }

    public void addrecordIt0h(RecordIt0h record) {
        recordsIt0h.add(record);
    }
}
