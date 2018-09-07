package org.iata.bsplink.filemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BsplinkFileManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BsplinkFileManagerApplication.class, args);
    }
}
