package com.clickandsave.saawanshah.dispatchnews.Network

import com.clickandsave.saawanshah.dispatchnews.Data.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for all NewsData.io /api/1/latest endpoints.
 *
 * Base URL : https://newsdata.io/
 * Endpoint : api/1/latest
 */
interface ApiInterface {

    // ------------------------------------------------------------------
    // 1. All News — Home Feed
    // GET api/1/latest?apikey=...&country=in&language=en
    // ------------------------------------------------------------------
    @GET("api/1/latest")
    suspend fun getLatestNews(
        @Query("apikey")   apiKey   : String = NewsConstants.API_KEY,
        @Query("country")  country  : String = NewsConstants.COUNTRY,
        @Query("language") language : String = NewsConstants.LANGUAGE
    ): Response<NewsResponse>

    // ------------------------------------------------------------------
    // 2. Category-wise News
    // GET api/1/latest?...&category=business   (one category per call on free plan)
    // ------------------------------------------------------------------
    @GET("api/1/latest")
    suspend fun getNewsByCategory(
        @Query("apikey")    apiKey   : String = NewsConstants.API_KEY,
        @Query("country")   country  : String = NewsConstants.COUNTRY,
        @Query("language")  language : String = NewsConstants.LANGUAGE,
        @Query("category")  category : String
    ): Response<NewsResponse>

    // ------------------------------------------------------------------
    // 3a. Keyword Search  (searches title + body)
    // GET api/1/latest?...&q=sony
    // ------------------------------------------------------------------
    @GET("api/1/latest")
    suspend fun searchNews(
        @Query("apikey")   apiKey   : String = NewsConstants.API_KEY,
        @Query("country")  country  : String = NewsConstants.COUNTRY,
        @Query("language") language : String = NewsConstants.LANGUAGE,
        @Query("q")        query    : String
    ): Response<NewsResponse>

    // ------------------------------------------------------------------
    // 3b. Title-only Search
    // GET api/1/latest?...&qInTitle=election
    // ------------------------------------------------------------------
    @GET("api/1/latest")
    suspend fun searchInTitle(
        @Query("apikey")    apiKey   : String = NewsConstants.API_KEY,
        @Query("country")   country  : String = NewsConstants.COUNTRY,
        @Query("language")  language : String = NewsConstants.LANGUAGE,
        @Query("qInTitle")  query    : String
    ): Response<NewsResponse>

    // ------------------------------------------------------------------
    // 4. Pagination — pass the nextPage token from the previous response
    // GET api/1/latest?...&page=<nextPage_token>
    // ------------------------------------------------------------------
    @GET("api/1/latest")
    suspend fun getNextPage(
        @Query("apikey")   apiKey   : String = NewsConstants.API_KEY,
        @Query("country")  country  : String = NewsConstants.COUNTRY,
        @Query("language") language : String = NewsConstants.LANGUAGE,
        @Query("page")     page     : String
    ): Response<NewsResponse>

    // ------------------------------------------------------------------
    // 5. Category + Keyword combined search
    // GET api/1/latest?...&category=technology&q=iphone
    // ------------------------------------------------------------------
    @GET("api/1/latest")
    suspend fun searchInCategory(
        @Query("apikey")   apiKey   : String = NewsConstants.API_KEY,
        @Query("country")  country  : String = NewsConstants.COUNTRY,
        @Query("language") language : String = NewsConstants.LANGUAGE,
        @Query("category") category : String,
        @Query("q")        query    : String
    ): Response<NewsResponse>
}