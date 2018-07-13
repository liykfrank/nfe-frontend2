package org.iata.bsplink.refund.loader.job;

import java.util.List;

import lombok.extern.java.Log;

import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    @Autowired
    List<RefundLoaderError> refundLoaderErrors;

    @Override
    public void afterJob(JobExecution jobExecution) {

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {

            log.info("Refund load finished");
            log.info("#Refund Loader Errors: " + refundLoaderErrors.size());
        }
    }
}
