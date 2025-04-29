package com.example.ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ai.entity.Food;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    // 可以添加自定義查詢方法
}