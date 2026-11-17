package com.portfolio.newsapp.feature.newsdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.vertexai.vertexAI
import com.portfolio.newsapp.domain.repository.NewsRepository
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

    private val generativeModel = Firebase.vertexAI.generativeModel("gemini-3.8-flash")

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

                // Generate AI summary after loading article
                val textToSummarize = article.content ?: article.description ?: article.title
                if (textToSummarize.isNotBlank()) {
                    generateSummary(textToSummarize)
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

    private fun generateSummary(textToSummarize: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSummarizing = true) }
            try {
                val prompt = "Provide a concise 2-3 sentence summary of the following news article:\n\n$textToSummarize"
                val response = generativeModel.generateContent(prompt)
                _uiState.update { 
                    it.copy(
                        isSummarizing = false,
                        aiSummary = response.text
                    ) 
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { 
                    it.copy(
                        isSummarizing = false,
                        aiSummary = "Failed to generate summary: ${e.message}"
                    ) 
                }
            }
        }
    }
}
