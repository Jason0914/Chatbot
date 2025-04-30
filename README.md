# 麥當勞智能聊天機器人

這是一個使用 Spring AI 和 MySQL 資料庫構建的智能點餐助手，可以用繁體中文回答顧客關於麥當勞菜單和食物的問題。


## 功能特色

- 🤖 **智能聊天**：使用 Spring AI 與 Ollama 模型整合，提供智能的繁體中文對話體驗
- 🍔 **動態菜單管理**：從 MySQL 資料庫讀取並能夠動態增刪改查菜單項目
- 💬 **流式回應**：實現即時流式文字回應
- 📱 **響應式設計**：適配桌面和移動設備的界面
- 🛠️ **完整管理界面**：方便的菜單管理功能，包括添加、編輯和刪除食物項目

## 技術棧

- **後端**：Spring Boot , Spring AI , Java
- **AI 模型**：Ollama (llama3)
- **資料庫**：MySQL
- **前端**：HTML, CSS, JavaScript 
- **通訊**：Server-Sent Events (SSE)

## 快速開始

### 先決條件

- Java 17 或更高版本
- Maven
- MySQL 數據庫
- Ollama 服務 (支持 llama3 模型)




## 使用指南

### 聊天功能

- 在聊天輸入框中輸入您的問題並按 Enter
- 系統會根據當前菜單內容提供有關麥當勞食品的信息
- 點擊範例問題可以快速提問

### 菜單管理

- **查看菜單**：所有菜單項目會顯示在右側的表格中
- **添加食物**：填寫名稱和價格，點擊"添加食物"按鈕
- **編輯價格**：點擊食物項目旁的"編輯"按鈕，修改價格
- **刪除食物**：點擊食物項目旁的"刪除"按鈕

## API 接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/chat` | GET | 接收聊天問題並返回 AI 回應 (SSE) |
| `/foods` | GET | 獲取所有食物項目 |
| `/foods` | POST | 添加新食物項目 |
| `/foods/update` | POST | 更新食物價格 |
| `/foods/delete` | POST | 刪除食物項目 |

## 系統架構

應用基於三層架構設計：

- **表示層**：HTML 前端和 REST 控制器
- **業務層**：包含服務邏輯和 AI 整合
- **持久層**：JPA/Hibernate 實體和存儲庫

數據流向如下：

1. 用戶發送問題到 `/chat` 端點
2. 控制器從數據庫獲取當前菜單
3. 系統將菜單和用戶問題組合成提示發送給 AI
4. AI 回應通過 SSE 流式返回給前端

## 定制化

### 修改提示模板

在 `ChatController.java` 中修改 `systemPrompt` 字符串，可以調整 AI 的行為和回應風格。

### 調整 AI 模型參數

在 `application.properties` 中修改以下配置：

```properties
spring.ai.ollama.chat.options.temperature=0.3
spring.ai.ollama.chat.options.top_p=0.9
```


