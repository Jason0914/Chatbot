# 麥當勞智能聊天機器人

## 專案簡介

這是一個基於 Spring Boot 和 OpenAI 的智能麥當勞餐廳聊天機器人系統。該應用程式提供了一個互動式的網頁介面,允許使用者與 AI 聊天,查詢菜單,並管理食物項目。

## 主要功能

- 🤖 AI 聊天助手：提供即時的麥當勞菜單查詢和建議
- 📋 食物管理：新增、編輯和刪除食物項目
- 💬 範例問題：提供常見的互動問題模板
- 📱 響應式設計：支持桌面和移動裝置

## 技術棧

- **後端**
  - Spring Boot
  - Spring AI (OpenAI)
  - Spring Data JPA
  - MySQL

- **前端**
  - HTML5
  - CSS3
  - JavaScript
  - Server-Sent Events (SSE)

- **開發工具**
  - Maven
  - Lombok
  - Java 17

## 環境需求

- JDK 17
- MySQL 8.0+
- Maven 3.6+

. 訪問應用
   - 打開瀏覽器，訪問 `http://localhost:8080`

## 主要組件

- `ChatController`: 處理 AI 聊天交互
- `FoodService`: 管理食物項目的業務邏輯
- `DataInitializer`: 初始化食物數據
- `index.html`: 前端互動介面

## API 端點

- `GET /foods`: 獲取所有食物
- `POST /foods`: 添加新食物
- `POST /foods/update`: 更新食物價格
- `POST /foods/delete`: 刪除食物
- `GET /chat`: AI 聊天端點 (使用 SSE)


