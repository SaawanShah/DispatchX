package com.clickandsave.saawanshah.dispatchnews.Repository

import com.clickandsave.saawanshah.dispatchnews.Data.NewsArticle
import com.clickandsave.saawanshah.dispatchnews.Network.RetrofitInstance

/**
 * NewsRepository — single source of truth for all news API calls.
 *
 * Every function returns a [Result] so callers can handle success/failure
 * without try-catch boilerplate.
 *
 * Pagination-aware functions return a [Pair]:
 *   first  = list of articles
 *   second = nextPage token (null when no further pages exist)
 */
class NewsRepository {
    private val api = RetrofitInstance.api

    // -------------------------------------------------------------------------
    // 1. Home feed — all latest news (no category filter)
    // -------------------------------------------------------------------------
    suspend fun getLatestNews(): Result<Pair<List<NewsArticle>, String?>> {
        return safeApiCall { api.getLatestNews() }
    }

    // -------------------------------------------------------------------------
    // 2. Category-wise news
    //    category values: "business" | "entertainment" | "health" |
    //                     "science"  | "sports"        | "technology"
    // -------------------------------------------------------------------------
    suspend fun getNewsByCategory(category: String): Result<Pair<List<NewsArticle>, String?>> {
        return safeApiCall { api.getNewsByCategory(category = category) }
    }

    // -------------------------------------------------------------------------
    // 3a. Full keyword search (title + body)
    // -------------------------------------------------------------------------
    suspend fun searchNews(query: String): Result<Pair<List<NewsArticle>, String?>> {
        return safeApiCall { api.searchNews(query = query) }
    }

    // -------------------------------------------------------------------------
    // 3b. Title-only keyword search
    // -------------------------------------------------------------------------
    suspend fun searchInTitle(query: String): Result<Pair<List<NewsArticle>, String?>> {
        return safeApiCall { api.searchInTitle(query = query) }
    }

    // -------------------------------------------------------------------------
    // 4. Pagination — pass the nextPage token from a previous response
    // -------------------------------------------------------------------------
    suspend fun getNextPage(pageToken: String): Result<Pair<List<NewsArticle>, String?>> {
        return safeApiCall { api.getNextPage(page = pageToken) }
    }

    // -------------------------------------------------------------------------
    // 5. Category + keyword combined search
    // -------------------------------------------------------------------------
    suspend fun searchInCategory(
        category: String,
        query: String
    ): Result<Pair<List<NewsArticle>, String?>> {
        return safeApiCall { api.searchInCategory(category = category, query = query) }
    }

    // -------------------------------------------------------------------------
    // Private helper — wraps any Retrofit suspend call in a safe Result
    // -------------------------------------------------------------------------
    private suspend fun safeApiCall(
        call: suspend () -> retrofit2.Response<com.clickandsave.saawanshah.dispatchnews.Data.NewsResponse>
    ): Result<Pair<List<NewsArticle>, String?>> {
        return try {
            val response = call()
            if (response.isSuccessful && response.body() != null) {
                val body     = response.body()!!
                val articles = body.results
                val nextPage = body.nextPage
                Result.success(Pair(articles, nextPage))
            } else {
                Result.failure(
                    Exception("API error ${response.code()}: ${response.message()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
