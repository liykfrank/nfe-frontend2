package org.iata.bsplink.refund.loader.configuration;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iata.bsplink.refund.loader.dto.Refund;
import org.iata.bsplink.refund.loader.job.FileValidatorTasklet;
import org.iata.bsplink.refund.loader.job.JobCompletionNotificationListener;
import org.iata.bsplink.refund.loader.job.JobExitCodeGeneratorListener;
import org.iata.bsplink.refund.loader.job.ReportPrinterStepListener;
import org.iata.bsplink.refund.loader.mapper.LineNumberAwarePatternMatchingCompositeLineMapper;
import org.iata.bsplink.refund.loader.model.RefundDocument;
import org.iata.bsplink.refund.loader.model.record.Record;
import org.iata.bsplink.refund.loader.model.record.RecordIdentifier;
import org.iata.bsplink.refund.loader.model.record.RecordIt01Layout;
import org.iata.bsplink.refund.loader.model.record.RecordIt02Layout;
import org.iata.bsplink.refund.loader.model.record.RecordIt03Layout;
import org.iata.bsplink.refund.loader.model.record.RecordIt05Layout;
import org.iata.bsplink.refund.loader.model.record.RecordIt08Layout;
import org.iata.bsplink.refund.loader.model.record.RecordIt0hLayout;
import org.iata.bsplink.refund.loader.model.record.RecordIt0yLayout;
import org.iata.bsplink.refund.loader.model.record.RecordIt0zLayout;
import org.iata.bsplink.refund.loader.model.record.RecordLayout;
import org.iata.bsplink.refund.loader.model.record.RecordRawLineLayout;
import org.iata.bsplink.refund.loader.validation.RefundLoaderParametersValidator;
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
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    private static final String RECORD_IT03_BEAN_NAME = "recordIt03";
    private static final String RECORD_IT05_BEAN_NAME = "recordIt05";
    private static final String RECORD_IT08_BEAN_NAME = "recordIt08";
    private static final String RECORD_IT0Y_BEAN_NAME = "recordIt0y";
    private static final String RECORD_IT0H_BEAN_NAME = "recordIt0h";
    private static final String RECORD_IT0Z_BEAN_NAME = "recordIt0z";
    private static final String RECORD_RAWLINE_BEAN_NAME = "recordRawLine";

    public static final String VALIDATION_STEP_NAME = "validationStep";
    public static final String LOADER_STEP_NAME = "refundLoaderStep";
    public static final String LOADER_JOB_NAME = "refundMassLoaderJob";

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
     * Builds the validation step.
     */
    @Bean
    public Step validationStep(FileValidatorTasklet validatorTasklet,
            ReportPrinterStepListener errorReportPrinterStepListener) {

        return stepBuilderFactory.get(VALIDATION_STEP_NAME)
                .tasklet(validatorTasklet)
                // TODO: add test that ensures that this step executes always
                .allowStartIfComplete(true)
                .listener(errorReportPrinterStepListener)
                .build();
    }

    /**
     * Builds refundLoaderStep.
     */
    @Bean
    public Step refundLoaderStep(ItemReader<RefundDocument> reader,
            ItemProcessor<RefundDocument, Refund> processor, ItemWriter<Refund> writer,
            ReportPrinterStepListener errorReportPrinterStepListener) {

        return stepBuilderFactory.get(LOADER_STEP_NAME)
            .<RefundDocument, Refund>chunk(STEP_CHUNK_SIZE)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .listener(errorReportPrinterStepListener)
            .build();
    }

    /**
     * Builds the refund massive loader job.
     */
    @Bean
    public Job refundMassLoaderJob(
            JobCompletionNotificationListener jobCompletionNotificationListener,
            JobExitCodeGeneratorListener jobExitCodeGeneratorListener,
            Step refundLoaderStep, Step validationStep) {

        return jobBuilderFactory.get(LOADER_JOB_NAME)
            .listener(jobCompletionNotificationListener)
            .listener(jobExitCodeGeneratorListener)
            .validator(new RefundLoaderParametersValidator())
            .start(validationStep).next(refundLoaderStep)
            .build();
    }

    /**
     * Returns a map indexed by record identifier with the record layouts.
     */
    @Bean
    public Map<RecordIdentifier, RecordLayout> recordLayouts() {

        EnumMap<RecordIdentifier, RecordLayout> recordLayouts =
                new EnumMap<>(RecordIdentifier.class);

        recordLayouts.put(RecordIdentifier.IT01, new RecordIt01Layout());
        recordLayouts.put(RecordIdentifier.IT02, new RecordIt02Layout());
        recordLayouts.put(RecordIdentifier.IT03, new RecordIt03Layout());
        recordLayouts.put(RecordIdentifier.IT05, new RecordIt05Layout());
        recordLayouts.put(RecordIdentifier.IT08, new RecordIt08Layout());
        recordLayouts.put(RecordIdentifier.IT0Y, new RecordIt0yLayout());
        recordLayouts.put(RecordIdentifier.IT0H, new RecordIt0hLayout());
        recordLayouts.put(RecordIdentifier.IT0Z, new RecordIt0zLayout());
        recordLayouts.put(RecordIdentifier.UNKNOWN, new RecordRawLineLayout());

        return recordLayouts;
    }

    /**
     * Builds the refund line mapper.
     */
    @Bean
    public LineMapper<Record> lineMapper(Map<RecordIdentifier, RecordLayout> recordLayouts,
            Map<String, FieldSetMapper<Record>> fieldSetMappers) {

        RecordLayout recordIt01Layout = recordLayouts.get(RecordIdentifier.IT01);
        RecordLayout recordIt02Layout = recordLayouts.get(RecordIdentifier.IT02);
        RecordLayout recordIt03Layout = recordLayouts.get(RecordIdentifier.IT03);
        RecordLayout recordIt05Layout = recordLayouts.get(RecordIdentifier.IT05);
        RecordLayout recordIt08Layout = recordLayouts.get(RecordIdentifier.IT08);
        RecordLayout recordIt0yLayout = recordLayouts.get(RecordIdentifier.IT0Y);
        RecordLayout recordIt0hLayout = recordLayouts.get(RecordIdentifier.IT0H);
        RecordLayout recordIt0zLayout = recordLayouts.get(RecordIdentifier.IT0Z);
        RecordLayout recordRawLineLayout = recordLayouts.get(RecordIdentifier.UNKNOWN);

        Map<String, LineTokenizer> tokenizers = new HashMap<>();

        tokenizers.put(recordIt01Layout.getPattern(), getTokenizer(recordIt01Layout));
        tokenizers.put(recordIt02Layout.getPattern(), getTokenizer(recordIt02Layout));
        tokenizers.put(recordIt03Layout.getPattern(), getTokenizer(recordIt03Layout));
        tokenizers.put(recordIt05Layout.getPattern(), getTokenizer(recordIt05Layout));
        tokenizers.put(recordIt08Layout.getPattern(), getTokenizer(recordIt08Layout));
        tokenizers.put(recordIt0yLayout.getPattern(), getTokenizer(recordIt0yLayout));
        tokenizers.put(recordIt0hLayout.getPattern(), getTokenizer(recordIt0hLayout));
        tokenizers.put(recordIt0zLayout.getPattern(), getTokenizer(recordIt0zLayout));
        tokenizers.put(recordRawLineLayout.getPattern(), getTokenizer(recordRawLineLayout));

        // TODO: the prototype bean names of fieldSetMappers should be passed in a better way
        // because right now it is completely hardcoded and repeated in three places (having in
        // mind the bean creation).
        Map<String, FieldSetMapper<Record>> mappers = new HashMap<>();

        mappers.put(recordIt01Layout.getPattern(),
                fieldSetMappers.get(RECORD_IT01_BEAN_NAME));
        mappers.put(recordIt02Layout.getPattern(),
                fieldSetMappers.get(RECORD_IT02_BEAN_NAME));
        mappers.put(recordIt03Layout.getPattern(),
                fieldSetMappers.get(RECORD_IT03_BEAN_NAME));
        mappers.put(recordIt05Layout.getPattern(),
                fieldSetMappers.get(RECORD_IT05_BEAN_NAME));
        mappers.put(recordIt08Layout.getPattern(),
                fieldSetMappers.get(RECORD_IT08_BEAN_NAME));
        mappers.put(recordIt0yLayout.getPattern(),
                fieldSetMappers.get(RECORD_IT0Y_BEAN_NAME));
        mappers.put(recordIt0hLayout.getPattern(),
                fieldSetMappers.get(RECORD_IT0H_BEAN_NAME));
        mappers.put(recordIt0zLayout.getPattern(),
                fieldSetMappers.get(RECORD_IT0Z_BEAN_NAME));
        mappers.put(recordRawLineLayout.getPattern(),
                fieldSetMappers.get(RECORD_RAWLINE_BEAN_NAME));

        LineNumberAwarePatternMatchingCompositeLineMapper mapper =
                new LineNumberAwarePatternMatchingCompositeLineMapper();

        mapper.setTokenizers(tokenizers);
        mapper.setFieldSetMappers(mappers);

        return mapper;
    }

    private LineTokenizer getTokenizer(RecordLayout recordLayout) {

        FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
        tokenizer.setNames(recordLayout.getFieldsNames());
        tokenizer.setColumns(recordLayout.getFieldsRanges());
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
                RECORD_IT03_BEAN_NAME,
                RECORD_IT05_BEAN_NAME,
                RECORD_IT08_BEAN_NAME,
                RECORD_IT0Y_BEAN_NAME,
                RECORD_IT0H_BEAN_NAME,
                RECORD_IT0Z_BEAN_NAME,
                RECORD_RAWLINE_BEAN_NAME);

        for (String beanName : beanNames) {

            BeanWrapperFieldSetMapper<Record> mapper = new BeanWrapperFieldSetMapper<>();
            mapper.setBeanFactory(beanFactory);
            mapper.setPrototypeBeanName(beanName);

            fieldSetMappers.put(beanName, mapper);
        }

        return fieldSetMappers;
    }

}
