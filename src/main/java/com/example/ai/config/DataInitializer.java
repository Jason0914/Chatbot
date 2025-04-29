package com.example.ai.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.ai.entity.Food;
import com.example.ai.repository.FoodRepository;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeData(FoodRepository foodRepository) {
        return args -> {
            // 檢查數據庫是否為空
            if (foodRepository.count() == 0) {
                log.info("初始化食物數據...");
                
                // 初始化食物列表
                List<Food> foods = Arrays.asList(
                    new Food("麥香魚", 58),
                    new Food("大麥克", 72),
                    new Food("薯餅", 29),
                    new Food("可樂", 20),
                    new Food("沙拉", 35),
                    new Food("雞塊", 45),
                    new Food("蘋果派", 30),
                    new Food("冰淇淋", 25),
                    new Food("咖啡", 40),
                    new Food("熱巧克力", 50),
                    new Food("雞腿堡", 65),
                    new Food("花生奶昔", 55),
                    new Food("玉米湯", 28),
                    new Food("炸雞", 85),
                    new Food("牛肉堡", 68),
                    new Food("海鮮湯", 60),
                    new Food("豬排堡", 62),
                    new Food("柳橙汁", 25),
                    new Food("奶昔", 50),
                    new Food("巧克力蛋糕", 45),
                    new Food("草莓派", 30),
                    new Food("炸薯條", 35),
                    new Food("雞肉卷", 55),
                    new Food("雞肉沙拉", 48),
                    new Food("飲料套餐", 75),
                    new Food("起司蛋堡", 70),
                    new Food("蜜汁雞翅", 90)
                );
                
                // 保存到數據庫
                foodRepository.saveAll(foods);
                log.info("成功初始化 {} 個食物項目", foods.size());
            } else {
                log.info("數據庫已有數據，跳過初始化");
            }
        };
    }
}