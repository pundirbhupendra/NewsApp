package com.portfolio.newsapp.feature.newslist

import com.portfolio.newsapp.domain.model.Article

data class NewsListUiState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false
)
