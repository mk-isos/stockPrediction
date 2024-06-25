package com.example.stock_prediction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Setter
@Getter
@Entity
@Table(name = "stocks")
public class Stock {
    // getters and setters
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer epoch;

    // 기본 생성자
    public Stock() {
    }

    // 모든 필드를 포함하는 생성자
    public Stock(String name, Integer epoch) {
        this.name = name;
        this.epoch = epoch;
    }

    // toString 메소드 (디버깅 및 로깅 용도)
    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", epoch=" + epoch +
                '}';
    }
}
