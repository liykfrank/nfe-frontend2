package org.iata.bsplink.refund.loader.model.record;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.iata.bsplink.refund.loader.configuration.BatchConfiguration;
import org.junit.Before;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public abstract class RecordFieldMappingTestCase<T extends Record> {

    private static final String FIXTURES_DIR = "fixtures/records";

    private String recordLine;
    private LineMapper<Record> lineMapper;

    /**
     * Returns the name of the file which contains the record fixture.
     */
    abstract String getRecordFileName();

    /**
     * Tests that the fields are read and mapped to object as expected.
     */
    abstract void testFieldsAreMappedCorrectly() throws Exception;

    @Before
    public void setUp() throws Exception {

        if (recordLine == null) {

            // @BeforeClass is not used because the static properties will be the same in subclases
            recordLine = readRecordLine();
        }

        BeanFactory beanFactory = getBeanFactoryMock();
        BatchConfiguration batchConfiguration = new BatchConfiguration();

        lineMapper = batchConfiguration.lineMapper(batchConfiguration.fieldSetMappers(beanFactory));
    }

    /**
     * Reads the line and returns the mapped Record object.
     */
    @SuppressWarnings("unchecked")
    protected T readRecord() throws Exception {

        return (T) lineMapper.mapLine(recordLine, 1);
    }

    private String readRecordLine() throws Exception {

        Resource resource = new ClassPathResource(FIXTURES_DIR + "/" + getRecordFileName());

        return FileUtils.readFileToString(resource.getFile(), StandardCharsets.ISO_8859_1);
    }

    private BeanFactory getBeanFactoryMock() {

        BeanFactory beanFactory = mock(BeanFactory.class);

        when(beanFactory.getBean(Mockito.argThat(new RequestBeanWithName("recordIt01"))))
                .thenReturn(new RecordIt01());
        when(beanFactory.getBean(Mockito.argThat(new RequestBeanWithName("recordIt02"))))
                .thenReturn(new RecordIt02());
        when(beanFactory.getBean(Mockito.argThat(new RequestBeanWithName("recordIt03"))))
                .thenReturn(new RecordIt03());
        when(beanFactory.getBean(Mockito.argThat(new RequestBeanWithName("recordIt05"))))
                .thenReturn(new RecordIt05());
        when(beanFactory.getBean(Mockito.argThat(new RequestBeanWithName("recordIt08"))))
                .thenReturn(new RecordIt08());
        when(beanFactory.getBean(Mockito.argThat(new RequestBeanWithName("recordIt0y"))))
                .thenReturn(new RecordIt0y());
        when(beanFactory.getBean(Mockito.argThat(new RequestBeanWithName("recordIt0h"))))
                .thenReturn(new RecordIt0h());
        when(beanFactory.getBean(Mockito.argThat(new RequestBeanWithName("recordRawLine"))))
                .thenReturn(new RecordRawLine());

        return beanFactory;
    }

    private static class RequestBeanWithName implements ArgumentMatcher<String> {

        private final String beanName;

        public RequestBeanWithName(String beanName) {

            this.beanName = beanName;
        }

        @Override
        public boolean matches(String arg) {

            return beanName.equals(arg);
        }
    }
}
