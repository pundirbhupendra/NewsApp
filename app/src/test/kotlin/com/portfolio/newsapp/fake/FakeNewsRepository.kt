package com.portfolio.newsapp.fake

import com.portfolio.newsapp.data.repository.NewsRepository
import com.portfolio.newsapp.domain.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Fake implementation of NewsRepository for testing
 * Preferred over mocking for simplicity and maintainability
 */
class FakeNewsRepository : NewsRepository {
    
    private var articlesData: List<Article> = emptyList()
    private var shouldReturnError = false
    
    fun setArticles(articles: List<Article>) {
        articlesData = articles
    }
    
    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }
    
    override fun getTopHeadlines(): Flow<Result<List<Article>>> = flow {
        if (shouldReturnError) {
            emit(Result.failure(Exception("Network error")))
        } else {
            emit(Result.success(articlesData))
        }
    }
    
    override suspend fun getArticleById(id: String): Article? {
        return articlesData.find { it.id == id }
    }
    
    override suspend fun refreshArticles(): Result<Unit> {
        return if (shouldReturnError) {
            Result.failure(Exception("Network error"))
        } else {
            Result.success(Unit)
        }
    }
}
