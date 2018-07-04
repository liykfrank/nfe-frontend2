package org.iata.bsplink.refund.loader.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iata.bsplink.refund.loader.dto.Refund;
import org.iata.bsplink.refund.loader.job.JobCompletionNotificationListener;
import org.iata.bsplink.refund.loader.model.RefundDocument;
import org.iata.bsplink.refund.loader.model.record.Record;
import org.iata.bsplink.refund.loader.model.record.RecordIt01;
import org.iata.bsplink.refund.loader.model.record.RecordIt01Layout;
import org.iata.bsplink.refund.loader.model.record.RecordIt02;
import org.iata.bsplink.refund.loader.model.record.RecordIt02Layout;
import org.iata.bsplink.refund.loader.model.record.RecordLayout;
import org.iata.bsplink.refund.loader.model.record.RecordRawLine;
import org.iata.bsplink.refund.loader.model.record.RecordRawLineLayout;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
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
    @StepScope
    public ItemReader<Record> recordReader(
            @Value("file:#{jobParameters[file]}")
            Resource resource, LineMapper<Record> lineMapper) {

        FlatFileItemReader<Record> reader = new FlatFileItemReader<>();

        reader.setLineMapper(lineMapper);
        reader.setResource(resource);
        reader.open(new ExecutionContext());

        return reader;
    }

    /**
     * Builds step1.
     */
    @Bean
    public Step step1(ItemReader<RefundDocument> reader,
            ItemProcessor<RefundDocument, Refund> processor, ItemWriter<Refund> writer) {

        return stepBuilderFactory.get("step1")
            .<RefundDocument, Refund>chunk(STEP_CHUNK_SIZE)
            .reader(reader)
            .processor(processor)
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

        RecordIt01Layout recordIt01Layout = new RecordIt01Layout();
        RecordIt02Layout recordIt02Layout = new RecordIt02Layout();
        RecordRawLineLayout recordRawLineLayout = new RecordRawLineLayout();

        Map<String, LineTokenizer> tokenizers = new HashMap<>();

        tokenizers.put(recordIt01Layout.getPattern(), getTokenizer(recordIt01Layout));
        tokenizers.put(recordIt02Layout.getPattern(), getTokenizer(recordIt02Layout));
        tokenizers.put(recordRawLineLayout.getPattern(), getTokenizer(recordRawLineLayout));

        // TODO: the prototype bean names of fieldSetMappers should be passed in a better way
        // because right now it is completely hardcoded and repeated in three places (having in
        // mind the bean creation).
        Map<String, FieldSetMapper<Record>> mappers = new HashMap<>();

        mappers.put(recordIt01Layout.getPattern(),
                fieldSetMappers.get(RECORD_IT01_BEAN_NAME));
        mappers.put(recordIt02Layout.getPattern(),
                fieldSetMappers.get(RECORD_IT02_BEAN_NAME));
        mappers.put(recordRawLineLayout.getPattern(),
                fieldSetMappers.get(RECORD_RAWLINE_BEAN_NAME));

        PatternMatchingCompositeLineMapper<Record> mapper =
                new PatternMatchingCompositeLineMapper<>();

        mapper.setTokenizers(tokenizers);
        mapper.setFieldSetMappers(mappers);

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
