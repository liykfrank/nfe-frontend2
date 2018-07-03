package org.iata.bsplink.refund.loader;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class RefundLoaderApplication {

    public static void main(String[] args) {
        SpringApplication.run(RefundLoaderApplication.class, args);
    }
}
