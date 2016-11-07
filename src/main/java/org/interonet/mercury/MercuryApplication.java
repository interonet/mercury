package org.interonet.mercury;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableAsync
@EnableScheduling
public class MercuryApplication {

    public static void main(String[] args) {
        SpringApplication.run(MercuryApplication.class, args);
    }
}

