package org.iata.bsplink.refund.loader;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableBatchProcessing
@EnableFeignClients
@CommonsLog
public class RefundLoaderApplication {

    /**
     * Runs the batch job.
     */
    public static void main(String[] args) {

        int exitCode =
                SpringApplication.exit(SpringApplication.run(RefundLoaderApplication.class, args));

        log.info("Batch exit code: " + exitCode);

        System.exit(exitCode);
    }
}
