package com.richard.gaming_trading_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GamingTradingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(GamingTradingSystemApplication.class, args);
    }

}
