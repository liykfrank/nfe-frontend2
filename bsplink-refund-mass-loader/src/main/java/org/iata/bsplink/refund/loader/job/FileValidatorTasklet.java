package org.iata.bsplink.refund.loader.job;

import org.iata.bsplink.refund.loader.validation.RefundFileValidator;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class FileValidatorTasklet implements Tasklet {

    private RefundFileValidator validator;
    private Resource resource;

    /**
     * Constructs the validation task for a refund file.
     */
    public FileValidatorTasklet(@Value("file:#{jobParameters[file]}") Resource resource,
            RefundFileValidator validator) {

        this.resource = resource;
        this.validator = validator;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
            throws Exception {

        validator.validate(resource.getFile());

        return RepeatStatus.FINISHED;
    }

}
