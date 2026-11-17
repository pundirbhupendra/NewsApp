package com.portfolio.newsapp.feature.newsdetail

import com.portfolio.newsapp.domain.model.Article

/**
 * UI state for News Detail screen
 */
data class NewsDetailUiState(
    val article: Article? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val aiSummary: String? = null,
    val isSummarizing: Boolean = false
)
