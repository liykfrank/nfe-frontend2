package org.iata.bsplink.agencymemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BsplinkAgencyMemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BsplinkAgencyMemoApplication.class, args);
    }
}
