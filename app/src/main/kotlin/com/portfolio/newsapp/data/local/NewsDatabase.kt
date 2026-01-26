package com.portfolio.newsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.portfolio.newsapp.data.local.dao.ArticleDao
import com.portfolio.newsapp.data.local.entity.ArticleEntity

@Database(
    entities = [ArticleEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}
