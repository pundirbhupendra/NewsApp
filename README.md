# 📰 Android News App

A **production‑ready Android News application** built with **Kotlin** and **Jetpack Compose**, designed to demonstrate **modern Android architecture**, **offline‑first data strategies**, and **industry‑standard testing practices**.

This project reflects how a real‑world Android app is structured, built, and tested

## ✨ Key Features

* 📱 **Single‑Activity Architecture** using Jetpack Compose
* 🤖 **AI Features** - Smart article summaries powered by **Vertex AI (Gemini 1.5 Flash)**
* 🏗️ **MVVM Architecture** with strict separation of concerns
* 🔄 **Reactive UI** powered by `StateFlow`
* 🌐 **Offline‑First Design** with Room caching and network refresh
* 🎨 **Material 3 UI** with dynamic colors, animations, & dark mode support
* 📦 **Product Flavors** – `dev`, `staging`, and `prod`
* 🔒 **Secure Configuration** - API keys properly managed via `local.properties` & `BuildConfig`
* 🧪 **Robust Unit Testing** for ViewModels and Repositories

## 🏛️ Architecture Overview

The app follows **Clean MVVM architecture**, ensuring scalability, testability, and maintainability.

```
UI (Jetpack Compose)
┌─────────────────────────────────────────┐
│ NewsListScreen | NewsDetailScreen        │
└──────────────┬──────────────────────────┘
               │ StateFlow
               ▼
┌─────────────────────────────────────────┐
│ ViewModels                               │
│ NewsListViewModel                        │
│ NewsDetailViewModel                      │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│ Repository                               │
│ NewsRepositoryImpl                       │
└──────┬──────────────────┬───────────────┘
       │                  │
       ▼                  ▼
┌─────────────┐     ┌──────────────┐
│ Remote Data │     │ Local Data   │
│ Retrofit    │     │ Room DB      │
└─────────────┘     └──────────────┘
```

📸 Screenshots
<p align="left">
  <img src="screenshots/screen1.png" alt="Top Headlines Screen" width="300"/>
  &nbsp;&nbsp;&nbsp;
  <img src="screenshots/screen2.png" alt="Article Details Screen" width="300"/>
</p>

### Tools & Libraries
* **Jetpack Compose** – Modern UI toolkit
* **Firebase Vertex AI** – For on-device smart Gemini integration
* **Hilt** – Dependency Injection
* **Retrofit / OkHttp** – Networking
* **Room** – Local caching
* **Coil** – Image loading
* **JUnit 4 / Turbine / MockK** – Unit testing framework

### Testing Approach

* ViewModels tested with controlled coroutine dispatchers
* Repositories tested using **fake data sources**, not heavy mocks
* Deterministic tests with `runTest`

## 🚀 Getting Started

### 1. Prerequisites
You need two API keys for this project to fully work:
1. **NewsAPI Key:** Get one at [https://newsapi.org](https://newsapi.org)
2. **Firebase Setup:** Connect the app to Firebase and set up Vertex AI to enable the Gemini summary features.

### 2. Configuration
Create a `local.properties` file in the root directory (if it doesn't exist) and add your NewsAPI key:
```properties
NEWS_API_KEY="YOUR_API_KEY_HERE"
```
Place your `google-services.json` inside the `app/` directory.

### 3. Running tests
```bash
# Run all unit tests
./gradlew test

# Run flavor‑specific tests
./gradlew testDevDebugUnitTest

# Generate coverage report
./gradlew testDebugUnitTest jacocoTestReport
```

## 📦 Product Flavors

The project is configured with multiple environments to mirror real‑world workflows:

| Flavor  | Purpose             |
| ------- | ------------------- |
| dev     | Local development   |
| staging | QA / pre‑production |
| prod    | Production release  |

### Build Commands

```bash
# Install dev build
./gradlew installDevDebug

# Install staging build
./gradlew installStagingDebug

# Assemble production release
./gradlew assembleProdRelease

./gradlew installDebug  # Defaults to dev
```

**Built with ❤️ using Kotlin & Jetpack Compose**
