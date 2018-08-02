package org.iata.bsplink.refund.loader.model.record;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.file.transform.Range;

public class RecordBaseLayoutTest {

    private static final String ANY_PATTERN = "1*";

    private Record record;

    @Before
    public void setUp() {

        record = new Record(ANY_PATTERN);
    }

    @Test
    public void testSetsPattern() {

        assertThat(record.getPattern(), equalTo(ANY_PATTERN));
    }

    @Test
    public void testSetGetFieldsLayouts() {

        assertThat(record.getFieldsLayouts(), hasSize(2));
    }

    @Test
    public void testGetFieldsNames() {

        String[] fieldsNames = record.getFieldsNames();

        assertThat(fieldsNames, equalTo(new String[] { "fileType", "fileTypeSequenceNumber" }));
    }

    @Test
    public void getFieldsRanges() {

        Range[] ranges = record.getFieldsRanges();

        assertThat(ranges, arrayWithSize(2));

        assertThat(ranges[0].getMin(), equalTo(37));
        assertThat(ranges[0].getMax(), equalTo(37));

        assertThat(ranges[1].getMin(), equalTo(38));
        assertThat(ranges[1].getMax(), equalTo(39));
    }

    @Test
    public void testGetFieldLayout() {

        assertThat(record.getFieldLayout("foo"), nullValue());

        FieldLayout fieldLayout = record.getFieldLayout("fileType");

        assertThat(fieldLayout, not(nullValue()));
        assertThat(fieldLayout.getField(), equalTo("fileType"));
    }

    private static class Record extends RecordBaseLayout {

        protected Record(String recordPattern) {
            super(recordPattern);
        }

        @Override
        protected void setFieldsLayouts(List<FieldLayout> fieldsLayouts) {

            fieldsLayouts.add(new FieldLayout(
                    "fileType", 10, "FTYP", FieldType.AN, 37, 1));
            fieldsLayouts.add(new FieldLayout(
                    "fileTypeSequenceNumber", 11, "FTSN", FieldType.AN, 38, 2));
        }

    }
}
