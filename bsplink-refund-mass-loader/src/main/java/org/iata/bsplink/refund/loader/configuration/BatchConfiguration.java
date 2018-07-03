package org.iata.bsplink.refund.loader.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iata.bsplink.refund.loader.listener.JobCompletionNotificationListener;
import org.iata.bsplink.refund.loader.model.Refund;
import org.iata.bsplink.refund.loader.model.record.Record;
import org.iata.bsplink.refund.loader.model.record.RecordIt01;
import org.iata.bsplink.refund.loader.model.record.RecordIt01Layout;
import org.iata.bsplink.refund.loader.model.record.RecordIt02;
import org.iata.bsplink.refund.loader.model.record.RecordIt02Layout;
import org.iata.bsplink.refund.loader.model.record.RecordLayout;
import org.iata.bsplink.refund.loader.model.record.RecordRawLine;
import org.iata.bsplink.refund.loader.model.record.RecordRawLineLayout;
import org.iata.bsplink.refund.loader.reader.RefundReader;
import org.iata.bsplink.refund.loader.writer.RefundConsoleWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;

@Configuration
public class BatchConfiguration {

    /**
     * Chunk size of the step, have in mind that the writing is done to a REST service
     * without transactionality so it must be always 1.
     */
    private static final int STEP_CHUNK_SIZE = 1;

    private static final String RECORD_IT01_BEAN_NAME = "recordIt01";
    private static final String RECORD_IT02_BEAN_NAME = "recordIt02";
    private static final String RECORD_RAWLINE_BEAN_NAME = "recordRawLine";

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    /**
     * Flat file reader which will act as delegate of the refund reader.
     */
    @Bean
    public ItemReader<Record> recordReader(
            @Value("classpath:fixtures/files/ESe9EARS_20160331_2203_107")
            Resource resource, LineMapper<Record> lineMapper) {

        FlatFileItemReader<Record> reader = new FlatFileItemReader<>();

        reader.setLineMapper(lineMapper);
        reader.setResource(resource);
        reader.open(new ExecutionContext());

        return reader;
    }

    @Bean
    public ItemReader<Refund> refundReader(ItemReader<Record> recordReader) {

        return new RefundReader(recordReader);
    }

    @Bean
    public RefundConsoleWriter writer() {

        return new RefundConsoleWriter();
    }

    /**
     * Builds step1.
     */
    @Bean
    public Step step1(ItemReader<Refund> reader, ItemWriter<Refund> writer) {

        return stepBuilderFactory.get("step1")
            .<Refund, Refund>chunk(STEP_CHUNK_SIZE)
            .reader(reader)
            .writer(writer)
            .build();
    }

    /**
     * Builds the refund massive loader job.
     */
    @Bean
    public Job refundMassLoaderJob(
            JobCompletionNotificationListener jobCompletionNotificationListener, Step step1) {

        return jobBuilderFactory.get("refundMassLoaderJob")
            .incrementer(new RunIdIncrementer())
            .listener(jobCompletionNotificationListener)
            .flow(step1)
            .end()
            .build();
    }

    /**
     * Builds the refund line mapper.
     */
    @Bean
    public LineMapper<Record> lineMapper(Map<String, FieldSetMapper<Record>> fieldSetMappers) {

        Map<String, LineTokenizer> tokenizers = new HashMap<>();

        tokenizers.put(RecordIt01Layout.getRecordLayout().getPattern(),
                getTokenizer(RecordIt01Layout.getRecordLayout()));
        tokenizers.put(RecordIt02Layout.getRecordLayout().getPattern(),
                getTokenizer(RecordIt02Layout.getRecordLayout()));
        tokenizers.put(RecordRawLineLayout.getRecordLayout().getPattern(),
                getTokenizer(RecordRawLineLayout.getRecordLayout()));

        // TODO: the prototype bean names of fieldSetMappers should be passed in a better way
        // because right now it is completely hardcoded and repeated in three places (having in
        // mind the bean creation).
        fieldSetMappers.put(RecordIt01Layout.getRecordLayout().getPattern(),
                fieldSetMappers.get(RECORD_IT01_BEAN_NAME));
        fieldSetMappers.put(RecordIt02Layout.getRecordLayout().getPattern(),
                fieldSetMappers.get(RECORD_IT02_BEAN_NAME));
        fieldSetMappers.put(RecordRawLineLayout.getRecordLayout().getPattern(),
                fieldSetMappers.get(RECORD_RAWLINE_BEAN_NAME));

        PatternMatchingCompositeLineMapper<Record> mapper =
                new PatternMatchingCompositeLineMapper<>();

        mapper.setTokenizers(tokenizers);
        mapper.setFieldSetMappers(fieldSetMappers);

        return mapper;
    }

    private LineTokenizer getTokenizer(RecordLayout recordLayout) {

        List<Range> ranges = new ArrayList<>();

        for (String fieldPosition : recordLayout.getFieldsLayout().values()) {

            String[] limits = fieldPosition.split("-");

            Range range = new Range(Integer.parseInt(limits[0]), Integer.parseInt(limits[1]));
            ranges.add(range);
        }

        FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
        tokenizer.setNames(recordLayout.getFieldsLayout().keySet().toArray(new String[] {}));
        tokenizer.setColumns(ranges.toArray(new Range[] {}));
        tokenizer.setStrict(false);

        return tokenizer;
    }

    /**
     * Refund field set mappers.
     */
    @Bean
    public Map<String, FieldSetMapper<Record>> fieldSetMappers(BeanFactory beanFactory) {

        Map<String, FieldSetMapper<Record>> fieldSetMappers = new HashMap<>();

        List<String> beanNames = Arrays.asList(
                RECORD_IT01_BEAN_NAME,
                RECORD_IT02_BEAN_NAME,
                RECORD_RAWLINE_BEAN_NAME);

        for (String beanName : beanNames) {

            BeanWrapperFieldSetMapper<Record> mapper = new BeanWrapperFieldSetMapper<>();
            mapper.setBeanFactory(beanFactory);
            mapper.setPrototypeBeanName(beanName);

            fieldSetMappers.put(beanName, mapper);
        }

        return fieldSetMappers;
    }

    @Bean
    @Qualifier(RECORD_IT01_BEAN_NAME)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Record recordIt01() {

        return new RecordIt01();
    }

    @Bean
    @Qualifier(RECORD_IT02_BEAN_NAME)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Record recordIt02() {

        return new RecordIt02();
    }

    @Bean
    @Qualifier(RECORD_RAWLINE_BEAN_NAME)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Record recordRawLine() {

        return new RecordRawLine();
    }
}
