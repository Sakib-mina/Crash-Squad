<div align="center">

# 🎮 Crash Squad — Island Strike
### *An Action-Packed Physics Slingshot Adventure for Android*

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0+-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-UI-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Material 3](https://img.shields.io/badge/Material_3-Design-757575?style=for-the-badge&logo=materialdesign&logoColor=white)](https://m3.material.io)
[![Google Play Billing](https://img.shields.io/badge/Google_Play_Billing-v9.1.0-34A853?style=for-the-badge&logo=googleplay&logoColor=white)](https://developer.android.com/google/play/billing)
[![Android SDK](https://img.shields.io/badge/Min_SDK-24_%7C_Target_SDK-36-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com)

</div>

---

## 🌟 Overview

**Crash Squad** is a modern, feature-rich Android mobile game built with **Jetpack Compose** and **Kotlin**. Experience thrilling physics-based slingshot gameplay, unlock and upgrade powerful heroes, tackle daily missions, and conquer challenging island stages!

---

## ✨ Core Features

* **🎯 Physics Slingshot Gameplay:** Aim, pull back, and launch your heroes to demolish wooden structures, stone pillars, and TNT caches to defeat all enemies.
* **🦸 Unique Heroes System:** Collect and upgrade distinct heroes (Blaze, Aqua, Rocky, etc.) with special combat abilities and stats.
* **💎 In-App Shop & Google Play Billing v9:** Purchase gems, chests, and special hero bundles securely via Google Play Billing Library v9.1.0 with repeatable consumable support.
* **💾 Local Persistence (Room Database):** Securely save level progress, high scores, hero upgrades, and inventory offline using Android Room.
* **📜 Daily Missions & Rewards:** Complete daily tasks, earn login streak bonuses, and claim chest rewards.
* **🎵 Immersive Sound & Music:** Full audio manager supporting sound effects and background music toggles.
* **🌍 Multi-Language Support:** Switch seamlessly between English, Bengali, Spanish, Portuguese, French, German, and Japanese.
* **⚙️ Settings & Legal Compliance:** Direct in-app access to privacy policy and terms of service hosted securely on Google Sites.

---

## 🛠️ Tech Stack & Architecture

* **UI Framework:** Jetpack Compose & Material 3
* **Architecture:** MVVM (Model-View-ViewModel) with Kotlin Coroutines & StateFlow
* **Local Database:** Android Room (SQLite persistence)
* **Monetization:** Google Play Billing Library v9.1.0
* **Package Name:** `com.arovastudio.crashsquad`
* **Min SDK / Target SDK:** API 24 / API 36

---

## 📄 Privacy Policy & Terms of Service

* **Privacy Policy:** [View Privacy Policy](https://sites.google.com/view/crashsquad-privacy-policy/home)
* **Terms & Conditions:** [View Terms & Conditions](https://sites.google.com/view/crashsquad-terms-condition/home)

---

## 🚀 Getting Started

### Prerequisites
* [Android Studio](https://developer.android.com/studio) (Koala / Ladybug or newer)
* Android SDK Platform 36

### Local Setup
1. Clone or open this repository in Android Studio.
2. Allow Gradle sync to complete and resolve dependencies.
3. Connect an Android device or start an emulator.
4. Run the app:
   ```bash
   ./gradlew assembleDebug
   ```

---

## 📦 In-App Purchase Product IDs

Configured in Google Play Console (`Monetize > In-app products`):
* `gems_100` — 100 Gems (Consumable)
* `gems_500` — 500 Gems + 250 Coins (Consumable)
* `gems_1200` — 1,200 Gems + 1,000 Coins (Consumable)
* `hero_bundle` — Hero Bundle (Consumable / Repeatable)
* `special_pack` — Special Pack (Consumable / Repeatable)

---

## © License

Developed by **Arova Studio**. All rights reserved.
