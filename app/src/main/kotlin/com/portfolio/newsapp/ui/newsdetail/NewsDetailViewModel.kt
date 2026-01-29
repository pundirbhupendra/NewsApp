package com.portfolio.newsapp.ui.newsdetail

import androidx.lifecycle.SavedStateHandle
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
class NewsDetailViewModel @Inject constructor(
    private val repository: NewsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val articleId: String = checkNotNull(savedStateHandle["articleId"])
    
    private val _uiState = MutableStateFlow(NewsDetailUiState())
    val uiState: StateFlow<NewsDetailUiState> = _uiState.asStateFlow()
    
    init {
        loadArticle()
    }
    
    private fun loadArticle() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val article = repository.getArticleById(articleId)
            
            if (article != null) {
                _uiState.update {
                    it.copy(
                        article = article,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Article not found"
                    )
                }
            }
        }
    }
}
