# 🎵 Music Score Editor

## 📌 Introduction

Music Score Editor 是一款簡單易用的五線譜音樂編輯軟體。
使用者可以透過圖形化介面選擇音符、編輯旋律，並即時播放與匯出樂譜。

本系統的設計目標是提供一個直觀、易上手的音樂創作工具，讓沒有專業背景的使用者也能快速完成簡單的鋼琴樂譜。

本專題獲獎為國立屏東大學校內專題競賽優勝
組員：CBB111238 李依中/CBB111201 黃啟綸/CBB111243 簡育誠

## 🚀 Features

* 🎼 五線譜音符編輯
* 🎹 即時鋼琴聲音播放
* 💾 樂譜儲存功能
* 🖼 樂譜匯出為圖片
* 🎤 音訊錄製功能（可選）
* 🤖 未來擴充：音訊自動轉譜功能

---

## 🛠 System Requirements

* 作業系統：macOS / Windows
* Java 版本：JDK 17+
* 或 Web 版本：Chrome / Edge / Safari

---

## 📥 Installation

### 方法一：執行已編譯版本

直接下載 release 版本並執行：

```bash
java -jar MusicEditor.jar
```

---

### 方法二：從原始碼執行

```bash
git clone <your-repository-url>
cd MusicEditor
javac Main.java
java Main
```

---

## 📖 Usage Guide

### 1️⃣ 新增音符

* 點擊工具列選擇音符種類（全音符、二分音符、四分音符等）
* 點擊五線譜對應位置即可放置音符

---

### 2️⃣ 播放樂譜

* 點擊「Play」按鈕
* 系統將依照樂譜順序播放旋律

---

### 3️⃣ 儲存檔案

* 點擊「Save」
* 選擇儲存位置
* 會產生 `.music` 專案檔

---

### 4️⃣ 匯出圖片

* 點擊「Export as Image」
* 產生 PNG 檔案

---

## 🧠 Technical Architecture

* Frontend: Java Swing / Web UI
* Backend: Java / Django (如為 Web 版本)
* 音訊處理：MIDI 技術
* 樂譜生成：自訂五線譜渲染引擎

---

## 🔮 Future Improvements

* 🎶 AI 自動作曲功能
* 🎧 音訊轉五線譜 (Audio-to-Score)
* ☁️ 雲端儲存
* 📱 iOS 版本

