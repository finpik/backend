package com.loanpick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.loanpick")
public class LoanPickApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoanPickApplication.class, args);
    }
}
