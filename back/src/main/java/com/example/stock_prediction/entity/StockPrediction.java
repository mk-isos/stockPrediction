package com.example.stock_prediction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "stock_predictions")
public class StockPrediction {

    // getters and setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, insertable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false)
    private Stock stock;

    @Column(nullable = false)
    private LocalDate predictionDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal predictedPrice;

    // 기본 생성자
    public StockPrediction() {
    }

    // 모든 필드를 포함하는 생성자
    public StockPrediction(Stock stock, LocalDate predictionDate, BigDecimal predictedPrice) {
        this.stock = stock;
        this.predictionDate = predictionDate;
        this.predictedPrice = predictedPrice;
    }

    // toString 메소드 (디버깅 및 로깅 용도)
    @Override
    public String toString() {
        return "StockPrediction{" +
                "id=" + id +
                ", stock=" + stock +
                ", predictionDate=" + predictionDate +
                ", predictedPrice=" + predictedPrice +
                '}';
    }
}
