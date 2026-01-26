package com.portfolio.newsapp.domain.model

data class Article(
    val id: String,
    val title: String,
    val description: String?,
    val content: String?,
    val imageUrl: String?,
    val publishedAt: String,
    val source: String,
    val url: String,
    val author: String?
)
