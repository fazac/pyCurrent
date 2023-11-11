package com.stock.pycurrent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PyCurrentApplication {

    public static void main(String[] args) {
        SpringApplication.run(PyCurrentApplication.class, args);
    }

}
