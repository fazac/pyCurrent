package com.stock.pycurrent;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PyCurrentApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(PyCurrentApplication.class);
        builder.headless(false);
        builder.run(args);
//        SpringApplication.run(PyCurrentApplication.class, args);
    }

}
