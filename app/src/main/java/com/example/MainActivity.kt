package com.example

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.MainScreen
import com.example.ui.theme.MidnightBg
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppViewModel
import com.example.util.AppLanguage

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        setContent {
            val viewModel: AppViewModel = viewModel()
            val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
            val currentLang by viewModel.currentLanguage.collectAsStateWithLifecycle()

            MyApplicationTheme(darkTheme = isDarkTheme) {
                val layoutDirection = if (currentLang == AppLanguage.PERSIAN) {
                    LayoutDirection.Rtl
                } else {
                    LayoutDirection.Ltr
                }
                CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MidnightBg
                    ) {
                        MainScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}
