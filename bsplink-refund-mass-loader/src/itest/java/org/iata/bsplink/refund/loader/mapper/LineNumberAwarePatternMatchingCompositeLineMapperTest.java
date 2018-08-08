package org.iata.bsplink.refund.loader.mapper;

import static org.iata.bsplink.refund.loader.test.fixtures.FixtureLoader.readRecordFixtureToString;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.iata.bsplink.refund.loader.configuration.BatchConfiguration;
import org.iata.bsplink.refund.loader.model.record.Record;
import org.iata.bsplink.refund.loader.model.record.RecordIt01;
import org.junit.Test;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.beans.factory.BeanFactory;

public class LineNumberAwarePatternMatchingCompositeLineMapperTest {

    private static final String RECORD_FIXTURE_FILE_NAME = "IT01";

    @Test
    public void testSetsLineNumber() throws Exception {

        Record record = getMapper().mapLine(getLine(), 1);

        assertEquals(1, record.getLineNumber());
    }

    private String getLine() throws Exception {

        return readRecordFixtureToString(RECORD_FIXTURE_FILE_NAME);
    }

    private LineMapper<Record> getMapper() {

        BeanFactory beanFactory = mock(BeanFactory.class);
        when(beanFactory.getBean(anyString())).thenReturn(new RecordIt01());

        BatchConfiguration batchConfiguration = new BatchConfiguration();

        return batchConfiguration.lineMapper(batchConfiguration.fieldSetMappers(beanFactory));
    }

}
