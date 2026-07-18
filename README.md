# 📰 DispatchX

DispatchX is a modern Android News Application built using **Kotlin**, **MVVM Architecture**, **Coroutines**, and **NewsData.io API**. The application provides real-time news across multiple categories with a clean Material Design UI, Firebase Authentication, and efficient offline-friendly architecture.

---

## 📱 Screenshots

| Home | Search | Article |
|------|--------|---------|
| ![](screenshots/home.png) | ![](screenshots/search.png) | ![](screenshots/details.png) |

---

# ✨ Features

- 📰 Latest News Headlines
- 🔍 Search News by Keyword
- 📂 Category-wise News
- 🔥 Breaking News Section
- ❤️ Save Favorite Articles
- 🌐 Real-Time News Updates
- 👤 Firebase Authentication
- 📱 Material Design UI
- ⚡ Fast Loading using Coroutines
- 📖 Detailed News Screen
- 🌙 Responsive UI
- 📡 REST API Integration

---

# 🛠 Tech Stack

- Kotlin
- MVVM Architecture
- Android Jetpack
- Navigation Component
- ViewModel
- LiveData
- Coroutines
- Retrofit
- Gson
- Glide
- Firebase Authentication
- RecyclerView
- Material Design Components
- View Binding
- Git & GitHub

---

# 📂 Project Structure

```
DispatchX
│
├── data
│   ├── api
│   ├── repository
│   └── model
│
├── ui
│   ├── auth
│   ├── home
│   ├── search
│   ├── details
│   └── adapter
│
├── viewmodel
│
├── utils
│
└── MainActivity
```

---

# 📡 API

News is fetched using the **NewsData.io API**.

Example Endpoint

```
https://newsdata.io/api/1/latest
```

---

# 🚀 Getting Started

### Clone the Repository

```bash
git clone https://github.com/SaawanShah/DispatchX.git
```

Open the project in Android Studio.

---

### Add API Key

Create your API key from NewsData.io and add it to:

```
Constants.kt
```

```kotlin
const val API_KEY = "YOUR_API_KEY"
```

---

### Build & Run

- Sync Gradle
- Run the application on an emulator or Android device.

---

# 🏗 Architecture

The application follows **MVVM Architecture**.

```
UI (Fragment)
      │
      ▼
 ViewModel
      │
      ▼
 Repository
      │
      ▼
 Retrofit API
      │
      ▼
 NewsData API
```

---

# 📚 Libraries Used

- Retrofit
- Coroutines
- Glide
- Firebase Auth
- Navigation Component
- Lifecycle
- RecyclerView
- Material Components

---

# 🎯 Future Improvements

- Bookmark with Room Database
- Dark Mode
- Offline Reading
- Push Notifications
- Pagination
- Profile Screen
- Multiple Languages
- Share Articles
- Settings Screen

---

# 👨‍💻 Developer

**Saawan Shah**

Android Developer

- Kotlin
- MVVM
- Coroutines
- Retrofit
- Firebase
- REST APIs
- Material Design

GitHub:
https://github.com/SaawanShah

LinkedIn:
https://www.linkedin.com/in/saawan_shah_tech/

---

# 🤝 Contributing

Contributions, issues, and feature requests are welcome.

Feel free to fork this repository and submit a pull request.

---

# ⭐ Support

If you found this project helpful, don't forget to give it a ⭐ on GitHub.

---

## 📄 License

This project is licensed under the MIT License.
