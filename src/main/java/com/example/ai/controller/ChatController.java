package com.example.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.ai.entity.Food;
import com.example.ai.service.FoodService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatClient chatClient;
    private final FoodService foodService;
    
    @GetMapping("/foods")
    public List<Food> getAllFoods() {
        return foodService.getAllFoods();
    }
    
    @PostMapping("/foods")
    public Food addFood(@RequestBody Map<String, Object> foodData) {
        String name = (String) foodData.get("name");
        Integer price = Integer.valueOf(foodData.get("price").toString());
        return foodService.addFood(name, price);
    }
    
    @PostMapping("/foods/update")
    public Food updateFoodPrice(@RequestBody Map<String, Object> updateData) {
        Long id = Long.valueOf(updateData.get("id").toString());
        Integer price = Integer.valueOf(updateData.get("price").toString());
        return foodService.updateFoodPrice(id, price);
    }
    
    @PostMapping("/foods/delete")
    public void deleteFood(@RequestBody Map<String, Object> deleteData) {
        Long id = Long.valueOf(deleteData.get("id").toString());
        foodService.deleteFood(id);
    }
   
    @RequestMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chat(@RequestParam String message) {
        try {
            // 從數據庫獲取食物列表
            String foods = foodService.buildFoodList();
            log.info("獲取到的食物列表: {}", foods);
            
            // 構建提示內容
            String systemPrompt = 
                "你是一個專業的麥當勞點餐助手，可以用繁體中文回答顧客關於菜單和食物的問題。\n" +
                "請始終使用繁體中文回應。\n\n" +
                "以下是可用的菜單項目：\n" +
                foods + "\n\n" +
                "回答時請遵循以下規則：\n" +
                "1. 回答必須簡潔精準\n" +
                "2. 如果有人詢問不在菜單上的食物，請告知該項目目前沒有供應\n" +
                "3. 如果有人問到價格區間的食物，請列出該區間內的所有選項\n" +
                "4. 如果有人詢問套餐或優惠組合，請根據菜單項目提供合理的建議\n" +
                "5. 不要使用表情符號或過於活潑的語氣\n" +
                "6. 菜單上的所有價格單位都是新台幣(元)\n" +
                "7. 不要提供不在菜單上的食品資訊\n\n" +
                "請直接回答用戶問題，不需要額外的問候語或解釋。";
                
            // 創建 SSE 發射器，設置更長的超時時間
            SseEmitter emitter = new SseEmitter(120000L); // 2分鐘超時
            
            // 使用兼容的方式獲取串流回應
            Flux<String> responseFlux = chatClient.prompt()
                                                .system(systemPrompt)
                                                .user(message)
                                                .stream()
                                                .content();

            // 訂閱 Flux，逐字傳送給前端
            responseFlux.subscribe(
                content -> {
                    try {
                        emitter.send(content);
                    } catch (IOException e) {
                        log.error("發送SSE訊息時出錯", e);
                        emitter.completeWithError(e);
                    }
                },
                error -> {
                    log.error("生成回應時出錯", error);
                    emitter.completeWithError(error);
                },
                () -> {
                    // 在完成時發送特殊事件，告知前端已正常完成
                    try {
                        log.info("回應生成完成");
                        // 發送一個特殊的完成事件
                        emitter.send(SseEmitter.event()
                                .name("complete")
                                .data("回應已完成"));
                        
                        // 延遲關閉連接，確保前端有時間處理完成事件
                        CompletableFuture.delayedExecutor(500, java.util.concurrent.TimeUnit.MILLISECONDS)
                                         .execute(() -> emitter.complete());
                    } catch (IOException e) {
                        log.error("發送完成事件時出錯", e);
                        emitter.complete();
                    }
                }
            );

            // 設置超時處理
            emitter.onTimeout(() -> {
                log.warn("SSE連接超時");
                emitter.complete();
            });

            // 設置完成處理
            emitter.onCompletion(() -> {
                log.info("SSE連接已完成");
            });

            return emitter;
        } catch (Exception e) {
            log.error("處理聊天請求時出錯", e);
            SseEmitter emitter = new SseEmitter();
            emitter.completeWithError(e);
            return emitter;
        }
    }
}