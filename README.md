# Travel
獲取航班資訊和貨幣匯率

這是一個使用 **MVVM 架構** 並結合 **Clean Architecture 分層設計** 的 Android 範例專案。
專案使用 **Jetpack Compose** 建立 UI，並透過 **Koin** 進行依賴注入。

---

## 🚀 功能說明

- APP 啟動後自動進入航班頁面(預設為國際線, 起飛班機)
- 航班頁面：可切換國際線、國內線、起飛航班、抵達班機
- 貨幣匯率頁面：點擊貨幣項目可切換不同貨幣匯率

---

## 🧱 專案架構

本專案依照 **Clean Architecture** 進行模組分層，並使用 MVVM 模式處理 UI 狀態與事件：

```plaintext
📦 app/
├── data
│   ├── data_source         # 數據來源（遠程數據源）
│   ├── repositories        # 資料庫操作邏輯
│   └── entities            # 資料實體（Entity）定義
│
├── viewModel           
│   ├── FlightViewModel     # 航班業務邏輯處理
│   └── CurrencyViewModel   # 貨幣業務邏輯處理
│
├── view
│   ├── CurrencyPage        # 貨幣頁面 UI 畫面與事件定義（顯示資料、觸發事件）
│   └── FlightPage          # 航班頁面 UI 畫面與事件定義（顯示資料、觸發事件）
│                           
├── ui.theme                # 自定義主題與樣式
│                           
├── MainActivity            # APP 進入點，設定 Navigation
├── MainApplication         # Application 類，初始化設定（例如：Koin DI）
└── Module                  # 管理和定義應用程式中的依賴注入，便於各層之間的協作和測試。
```
---

## 🛠️ 使用技術與套件

- **Jetpack Compose** - 建立聲明式 UI
- **Koin** - 輕量級依賴注入框架
- **retrofit** - 網路請求框架
- **okhttp** - 網路請求框架
- **navigation** - 頁面導航
- **Gson** - JSON 處理（如有假資料序列化/反序列化需求）

---

## 📷 畫面展示

[damo.mp4](damo/damo.mp4)

---

## 👨‍💻 作者
- This project was created by [Hsieh Jia Shiuan]