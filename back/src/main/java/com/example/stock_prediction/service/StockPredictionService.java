package com.example.stock_prediction.service;

import com.example.stock_prediction.entity.Stock;
import com.example.stock_prediction.entity.StockPrediction;
import com.example.stock_prediction.repository.StockPredictionRepository;
import com.example.stock_prediction.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StockPredictionService {

    private final StockPredictionRepository stockPredictionRepository;
    private final StockRepository stockRepository;

    @Autowired
    public StockPredictionService(StockPredictionRepository stockPredictionRepository,
                                  StockRepository stockRepository) {
        this.stockPredictionRepository = stockPredictionRepository;
        this.stockRepository = stockRepository;
    }

    // 특정 주식 심볼에 대한 예측을 찾는 메소드
    public List<StockPrediction> findPredictionsByStockName(String stockName) {
        Optional<Stock> stock = stockRepository.findByName(stockName);
        return stock.map(stockPredictionRepository::findByStock).orElse(List.of());
    }

    // 특정 날짜 범위에 대한 예측을 찾는 메소드
    public List<StockPrediction> findPredictionsByDateRange(LocalDate startDate, LocalDate endDate) {
        return stockPredictionRepository.findByPredictionDateBetween(startDate, endDate);
    }

    // 추가적인 비즈니스 로직 메소드를 여기에 구현할 수 있습니다...
}
