package com.clickandsave.saawanshah.dispatchnews.Network

/**
 * Central configuration for NewsData.io API.
 *
 * ⚠️ Security Note:
 * For production, move API_KEY to local.properties → expose via BuildConfig.
 * Never ship the raw key in source code for a public-facing app.
 */
object NewsConstants {
    const val API_KEY  = "pub_ed72eb6eeb354e4184f90e74e63fea12"
    const val BASE_URL = "https://newsdata.io/"
    const val COUNTRY  = "in"
    const val LANGUAGE = "en"
}
