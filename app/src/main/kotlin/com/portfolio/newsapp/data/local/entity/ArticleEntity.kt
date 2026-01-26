package com.portfolio.newsapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String?,
    val content: String?,
    val imageUrl: String?,
    val publishedAt: String,
    val source: String,
    val url: String,
    val author: String?,
    val cachedAt: Long = System.currentTimeMillis()
)
