package com.portfolio.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.portfolio.newsapp.ui.navigation.NewsNavigation
import com.portfolio.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            NewsAppTheme {
                NewsNavigation()
            }
        }
    }
}
