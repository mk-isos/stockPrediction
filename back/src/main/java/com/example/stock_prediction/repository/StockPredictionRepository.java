package com.example.stock_prediction.repository;

import com.example.stock_prediction.entity.Stock;
import com.example.stock_prediction.entity.StockPrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface StockPredictionRepository extends JpaRepository<StockPrediction, Long> {
    // 특정 주식에 대한 모든 예측을 찾는 메소드
    List<StockPrediction> findByStock(Stock stock);

    // 특정 주식과 특정 날짜에 대한 예측을 찾는 메소드
    List<StockPrediction> findByStockAndPredictionDate(Stock stock, LocalDate predictionDate);

    // 특정 날짜 범위에 대한 예측을 찾는 메소드
    List<StockPrediction> findByPredictionDateBetween(LocalDate startDate, LocalDate endDate);
}
