package com.portfolio.newsapp.data.repository

import android.util.Log
import com.portfolio.newsapp.config.AppConfig
import com.portfolio.newsapp.data.local.dao.ArticleDao
import com.portfolio.newsapp.data.mapper.toDomainModel
import com.portfolio.newsapp.data.mapper.toEntity
import com.portfolio.newsapp.data.remote.NewsApiService
import com.portfolio.newsapp.domain.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val apiService: NewsApiService,
    private val articleDao: ArticleDao
) : NewsRepository {
    
    companion object {
        private const val TAG = "NewsRepository"
    }

    override fun getTopHeadlines(): Flow<Result<List<Article>>> = flow {
        Log.d(TAG, "getTopHeadlines: Starting...")

        // First, emit cached data for immediate display
        val cachedArticles = articleDao.getAllArticles()
            .map { entities -> entities.map { it.toDomainModel() } }
            .firstOrNull() ?: emptyList()

        if (cachedArticles.isNotEmpty()) {
            Log.d(TAG, "getTopHeadlines: Emitting ${cachedArticles.size} cached articles")
            emit(Result.success(cachedArticles))
        } else {
            Log.d(TAG, "getTopHeadlines: No cached articles found")
        }

        // Then fetch fresh data from network
        try {
            Log.d(TAG, "getTopHeadlines: Fetching from API...")
            Log.d(TAG, "getTopHeadlines: Using API key: ${AppConfig.API_KEY.take(10)}...")
            Log.d(TAG, "getTopHeadlines: Base URL: ${AppConfig.BASE_URL}")

            val response = apiService.getTopHeadlines(
                country = "us",
                apiKey = AppConfig.API_KEY
            )

            Log.d(TAG, "getTopHeadlines: API response received with ${response.articles.size} articles")

            val freshArticles = response.articles.map { it.toDomainModel() }

            // Update cache
            articleDao.insertArticles(freshArticles.map { it.toEntity() })
            Log.d(TAG, "getTopHeadlines: Cache updated")

            // Emit fresh data
            emit(Result.success(freshArticles))
            Log.d(TAG, "getTopHeadlines: Fresh articles emitted")

        } catch (e: Exception) {
            Log.e(TAG, "getTopHeadlines: Error fetching articles", e)
            Log.e(TAG, "getTopHeadlines: Error message: ${e.message}")
            // If network fails and we have no cache, emit error
            emit(Result.failure(e))
        }
    }.catch { e ->
        Log.e(TAG, "getTopHeadlines: Flow error", e)
        emit(Result.failure(e))
    }

    override suspend fun getArticleById(id: String): Article? {
        return articleDao.getArticleById(id)?.toDomainModel()
    }

    override suspend fun refreshArticles(): Result<Unit> {
        return try {
            val response = apiService.getTopHeadlines(
                country = "us",
                apiKey = AppConfig.API_KEY
            )

            val articles = response.articles.map { it.toDomainModel() }

            // Clear old cache and insert fresh data
            articleDao.deleteAllArticles()
            articleDao.insertArticles(articles.map { it.toEntity() })

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
