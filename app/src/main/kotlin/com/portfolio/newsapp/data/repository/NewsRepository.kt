package com.portfolio.newsapp.data.repository

import com.portfolio.newsapp.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getTopHeadlines(): Flow<Result<List<Article>>>

    suspend fun getArticleById(id: String): Article?
    suspend fun refreshArticles(): Result<Unit>
}
