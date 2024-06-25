package com.example.stock_prediction.controller;

import com.example.stock_prediction.entity.StockPrediction;
import com.example.stock_prediction.service.StockPredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/predictions")
public class StockPredictionController {

    private final StockPredictionService stockPredictionService;

    @Autowired
    public StockPredictionController(StockPredictionService stockPredictionService) {
        this.stockPredictionService = stockPredictionService;
    }

    // 특정 주식 심볼에 대한 예측 조회
    @GetMapping("/symbol/{stockSymbol}")
    public ResponseEntity<List<StockPrediction>> getPredictionsByStockSymbol(@PathVariable String stockSymbol) {
        List<StockPrediction> predictions = stockPredictionService.findPredictionsByStockName(stockSymbol);
        if (predictions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(predictions);
    }

    // 추가적인 요청 처리 메소드를 여기에 구현할 수 있습니다...
}
