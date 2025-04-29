package com.example.ai.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.ai.entity.Food;
import com.example.ai.repository.FoodRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FoodService {

    private final FoodRepository foodRepository;
    
    /**
     * 獲取所有食物列表
     */
    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }
    
    /**
     * 構建食物列表字符串用於AI提示
     * 改進了格式化以提供更清晰的數據結構
     */
    public String buildFoodList() {
        try {
            List<Food> foods = getAllFoods();
            
            if (foods.isEmpty()) {
                log.warn("食物列表為空");
                return "目前沒有可用的食物";
            }
            
            // 改進的格式化方式，每個食物項目一行，更易於AI模型理解
            String foodList = foods.stream()
                .map(food -> String.format("- %s: %d元", food.getName(), food.getPrice()))
                .collect(Collectors.joining("\n"));
            
            log.debug("構建的食物列表: {}", foodList);
            return foodList;
        } catch (Exception e) {
            log.error("構建食物列表時出錯", e);
            return "獲取食物列表失敗";
        }
    }
    
    /**
     * 添加新食物
     */
    public Food addFood(String name, Integer price) {
        log.info("添加新食物: {}, 價格: {}", name, price);
        Food food = new Food(name, price);
        return foodRepository.save(food);
    }
    
    /**
     * 更新食物價格
     */
    public Food updateFoodPrice(Long id, Integer newPrice) {
        log.info("更新食物價格: ID={}, 新價格={}", id, newPrice);
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到ID為" + id + "的食物"));
        food.setPrice(newPrice);
        return foodRepository.save(food);
    }
    
    /**
     * 刪除食物
     */
    public void deleteFood(Long id) {
        log.info("刪除食物: ID={}", id);
        foodRepository.deleteById(id);
    }
}