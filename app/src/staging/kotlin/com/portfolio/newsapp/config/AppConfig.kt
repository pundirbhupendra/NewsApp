package com.portfolio.newsapp.config

import com.portfolio.newsapp.BuildConfig

object AppConfig {
    const val BASE_URL = "https://newsapi.org/v2/"
    val API_KEY = BuildConfig.NEWS_API_KEY
    const val ENVIRONMENT = "STAGING"
}
