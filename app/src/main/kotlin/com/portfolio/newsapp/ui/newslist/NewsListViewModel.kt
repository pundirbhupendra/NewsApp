package com.portfolio.newsapp.ui.newslist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portfolio.newsapp.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(NewsListUiState())
    val uiState: StateFlow<NewsListUiState> = _uiState.asStateFlow()
    
    init {
        loadNews()
    }
    
    fun loadNews() {
        Log.d("NewsListViewModel", "loadNews: Starting to load news")
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            repository.getTopHeadlines().collect { result ->
                result.fold(
                    onSuccess = { articles ->
                        Log.d("NewsListViewModel", "loadNews: Successfully loaded ${articles.size} articles")
                        _uiState.update {
                            it.copy(
                                articles = articles,
                                isLoading = false,
                                error = null
                            )
                        }
                    },
                    onFailure = { error ->
                        Log.e("NewsListViewModel", "loadNews: Failed to load news", error)
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = error.toUserMessage()
                            )
                        }
                    }
                )
            }
        }
    }
    
    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            
            val result = repository.refreshArticles()
            
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isRefreshing = false) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            error = error.toUserMessage()
                        )
                    }
                }
            )
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

private fun Throwable.toUserMessage(): String {
    return when {
        this is java.net.UnknownHostException -> "No internet connection"
        this is java.net.SocketTimeoutException -> "Connection timeout"
        message?.contains("401") == true -> "Invalid API key"
        message?.contains("429") == true -> "Too many requests. Please try again later"
        else -> "Something went wrong. Please try again"
    }
}
