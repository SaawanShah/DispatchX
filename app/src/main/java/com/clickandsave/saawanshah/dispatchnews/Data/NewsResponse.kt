package com.clickandsave.saawanshah.dispatchnews.Data

/**
 * Top-level response from NewsData.io /api/1/latest
 *
 * {
 *   "status": "success",
 *   "totalResults": 10,
 *   "results": [...],
 *   "nextPage": "1720..."   // null when no more pages
 * }
 */
data class NewsResponse(
    val status       : String,
    val totalResults : Int,
    val results      : List<NewsArticle>,
    val nextPage     : String?
)

/**
 * Individual news article as returned by the API.
 */
data class NewsArticle(
    val article_id  : String,
    val title       : String,
    val description : String?,
    val image_url   : String?,
    val category    : List<String>?,
    val pubDate     : String?,
    val source_id   : String?,

    // Additional fields available in the API response
    val source_name : String?,   // human-readable source name e.g. "Times of India"
    val link        : String?,   // original article URL — used for deep-linking & bookmarks
    val creator     : List<String>? // author name(s)
)
