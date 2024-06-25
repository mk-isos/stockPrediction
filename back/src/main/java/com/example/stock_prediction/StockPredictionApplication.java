package com.example.stock_prediction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.example.stock_prediction.entity")
public class StockPredictionApplication {
    public static void main(String[] args) {
        SpringApplication.run(StockPredictionApplication.class, args);
    }
}
