package com.example.stock_prediction.repository;

import com.example.stock_prediction.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    // 주식 이름으로 주식 엔티티를 찾는 메소드
    Optional<Stock> findByName(String name);
}
