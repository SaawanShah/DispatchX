package com.clickandsave.saawanshah.dispatchnews.Data

/**
 * Supported NewsData.io category values (free plan).
 *
 * Usage:
 *   newsRepository.getNewsByCategory(NewsCategory.SPORTS.value)
 */
enum class NewsCategory(val value: String, val displayName: String) {
    ALL          ("",              "All"),
    BUSINESS     ("business",      "Business"),
    ENTERTAINMENT("entertainment", "Entertainment"),
    HEALTH       ("health",        "Health"),
    SCIENCE      ("science",       "Science"),
    SPORTS       ("sports",        "Sports"),
    TECHNOLOGY   ("technology",    "Technology")
}
