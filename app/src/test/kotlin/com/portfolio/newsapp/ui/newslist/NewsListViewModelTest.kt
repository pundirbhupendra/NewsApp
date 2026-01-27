package com.portfolio.newsapp.ui.newslist

import app.cash.turbine.test
import com.portfolio.newsapp.domain.model.Article
import com.portfolio.newsapp.fake.FakeNewsRepository
import com.portfolio.newsapp.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NewsListViewModelTest {
    
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    
    private lateinit var viewModel: NewsListViewModel
    private lateinit var fakeRepository: FakeNewsRepository
    
    private val sampleArticles = listOf(
        Article(
            id = "1",
            title = "Test Article 1",
            description = "Description 1",
            content = "Content 1",
            imageUrl = "https://example.com/image1.jpg",
            publishedAt = "2026-01-25T10:00:00Z",
            source = "Test Source",
            url = "https://example.com/article1",
            author = "John Doe"
        ),
        Article(
            id = "2",
            title = "Test Article 2",
            description = "Description 2",
            content = "Content 2",
            imageUrl = "https://example.com/image2.jpg",
            publishedAt = "2026-01-25T11:00:00Z",
            source = "Test Source",
            url = "https://example.com/article2",
            author = "Jane Smith"
        )
    )
    
    @Before
    fun setup() {
        fakeRepository = FakeNewsRepository()
        viewModel = NewsListViewModel(fakeRepository)
    }
    
    @Test
    fun `initial state shows loading`() = runTest {
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertTrue(initialState.isLoading)
            assertNull(initialState.error)
            assertTrue(initialState.articles.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }
    
    @Test
    fun `loads articles successfully`() = runTest {
        fakeRepository.setArticles(sampleArticles)
        
        viewModel = NewsListViewModel(fakeRepository)
        
        viewModel.uiState.test {

            skipItems(1)
            
            // Check success state
            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertNull(successState.error)
            assertEquals(2, successState.articles.size)
            assertEquals("Test Article 1", successState.articles[0].title)
            
            cancelAndIgnoreRemainingEvents()
        }
    }
    
    @Test
    fun `shows error when repository fails`() = runTest {
        fakeRepository.setShouldReturnError(true)
        
        viewModel = NewsListViewModel(fakeRepository)
        
        viewModel.uiState.test {

            skipItems(1)
            
            // Check error state
            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertNotNull(errorState.error)
            assertTrue(errorState.articles.isEmpty())
            
            cancelAndIgnoreRemainingEvents()
        }
    }
    
    @Test
    fun `refresh updates articles`() = runTest {
        fakeRepository.setArticles(sampleArticles)
        viewModel = NewsListViewModel(fakeRepository)
        
        viewModel.uiState.test {
            skipItems(2) // Skip initial loading and success
            
            // Trigger refresh
            viewModel.refresh()
            
            // Check refreshing state
            val refreshingState = awaitItem()
            assertTrue(refreshingState.isRefreshing)
            
            // Check after refresh complete
            val afterRefreshState = awaitItem()
            assertFalse(afterRefreshState.isRefreshing)
            
            cancelAndIgnoreRemainingEvents()
        }
    }
    
    @Test
    fun `clearError removes error message`() = runTest {
        fakeRepository.setShouldReturnError(true)
        viewModel = NewsListViewModel(fakeRepository)
        
        viewModel.uiState.test {
            skipItems(2)
            
            viewModel.clearError()
            
            val clearedState = awaitItem()
            assertNull(clearedState.error)
            
            cancelAndIgnoreRemainingEvents()
        }
    }
}
