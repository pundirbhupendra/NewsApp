package com.portfolio.newsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.portfolio.newsapp.data.local.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    
    @Query("SELECT * FROM articles ORDER BY publishedAt DESC")
    fun getAllArticles(): Flow<List<ArticleEntity>>
    
    @Query("SELECT * FROM articles WHERE id = :articleId")
    suspend fun getArticleById(articleId: String): ArticleEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)
    
    @Query("DELETE FROM articles")
    suspend fun deleteAllArticles()
    
    @Query("DELETE FROM articles WHERE cachedAt < :timestamp")
    suspend fun deleteOldArticles(timestamp: Long)
}
