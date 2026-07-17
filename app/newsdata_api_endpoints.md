# NewsData.io API — Endpoint Documentation
### (Android News App Integration)

**Base URL:** `https://newsdata.io/api/1/latest`
**API Key:** `pub_ed72eb6eeb354e4184f90e74e63fea12`
**Country:** `in` (India)
**Language:** `en` (English)

---

## 1. All News (Home Feed)
```
https://newsdata.io/api/1/latest?apikey=pub_ed72eb6eeb354e4184f90e74e63fea12&country=in&language=en
```

---

## 2. Category-wise News Endpoints

Add the `category` parameter to the base URL. Supported values used below: `business`, `entertainment`, `health`, `science`, `sports`, `technology`.

### 🔹 Business News
```
https://newsdata.io/api/1/latest?apikey=pub_ed72eb6eeb354e4184f90e74e63fea12&country=in&language=en&category=business
```

### 🔹 Entertainment News
```
https://newsdata.io/api/1/latest?apikey=pub_ed72eb6eeb354e4184f90e74e63fea12&country=in&language=en&category=entertainment
```

### 🔹 Health News
```
https://newsdata.io/api/1/latest?apikey=pub_ed72eb6eeb354e4184f90e74e63fea12&country=in&language=en&category=health
```

### 🔹 Science News
```
https://newsdata.io/api/1/latest?apikey=pub_ed72eb6eeb354e4184f90e74e63fea12&country=in&language=en&category=science
```

### 🔹 Sports News
```
https://newsdata.io/api/1/latest?apikey=pub_ed72eb6eeb354e4184f90e74e63fea12&country=in&language=en&category=sports
```

### 🔹 Technology News
```
https://newsdata.io/api/1/latest?apikey=pub_ed72eb6eeb354e4184f90e74e63fea12&country=in&language=en&category=technology
```

> ⚠️ Note: NewsData.io category param accepts only **one** category per call in the free plan. Multiple categories comma-separated (`category=business,sports`) work only on paid plans — check your plan limits before using multiple values.

---

## 3. Search News (Keyword Search)

Use the `q` parameter for keyword search. Example: searching for "Sony".

```
https://newsdata.io/api/1/latest?apikey=pub_ed72eb6eeb354e4184f90e74e63fea12&country=in&language=en&q=sony
```

### Search within a specific category
```
https://newsdata.io/api/1/latest?apikey=pub_ed72eb6eeb354e4184f90e74e63fea12&country=in&language=en&category=technology&q=iphone
```

### Search in title only (qInTitle)
```
https://newsdata.io/api/1/latest?apikey=pub_ed72eb6eeb354e4184f90e74e63fea12&country=in&language=en&qInTitle=election
```

---

## 4. Pagination (Next Page)

Response JSON contains a `nextPage` token. Pass it as `page` param to load more results.

```
https://newsdata.io/api/1/latest?apikey=pub_ed72eb6eeb354e4184f90e74e63fea12&country=in&language=en&page=<nextPage_token_from_response>
```

---

## 5. Android Implementation Notes

### Retrofit Interface Example
```kotlin
interface NewsApiService {

    @GET("api/1/latest")
    suspend fun getNewsByCategory(
        @Query("apikey") apiKey: String = "pub_ed72eb6eeb354e4184f90e74e63fea12",
        @Query("country") country: String = "in",
        @Query("language") language: String = "en",
        @Query("category") category: String
    ): NewsResponse

    @GET("api/1/latest")
    suspend fun searchNews(
        @Query("apikey") apiKey: String = "pub_ed72eb6eeb354e4184f90e74e63fea12",
        @Query("country") country: String = "in",
        @Query("language") language: String = "en",
        @Query("q") query: String
    ): NewsResponse

    @GET("api/1/latest")
    suspend fun getNextPage(
        @Query("apikey") apiKey: String = "pub_ed72eb6eeb354e4184f90e74e63fea12",
        @Query("country") country: String = "in",
        @Query("language") language: String = "en",
        @Query("page") page: String
    ): NewsResponse
}
```

### Base URL for Retrofit Builder
```kotlin
Retrofit.Builder()
    .baseUrl("https://newsdata.io/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
```

### Category Enum (for tabs like Business / Entertainment / Health / Science / Sports / Technology)
```kotlin
enum class NewsCategory(val value: String) {
    BUSINESS("business"),
    ENTERTAINMENT("entertainment"),
    HEALTH("health"),
    SCIENCE("science"),
    SPORTS("sports"),
    TECHNOLOGY("technology")
}
```

---

## ⚠️ Security Reminder
This API key is currently visible in plain text in your requests. For a production Android app:
- Do **not** hardcode the API key directly in app source (it can be extracted from the APK).
- Store it in `local.properties` / `BuildConfig` field, or better, proxy requests through your own backend server so the key never ships inside the app.
