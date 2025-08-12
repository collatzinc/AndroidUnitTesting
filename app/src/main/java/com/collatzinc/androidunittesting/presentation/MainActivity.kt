package com.collatzinc.androidunittesting.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowInsetsControllerCompat
import com.collatzinc.androidunittesting.presentation.screen.login.LoginScreen
import com.collatzinc.androidunittesting.presentation.screen.login.LoginViewModel
import com.collatzinc.androidunittesting.presentation.screen.profile.ProfileScreen
import com.collatzinc.androidunittesting.presentation.screen.profile.ProfileViewModel
import com.collatzinc.androidunittesting.presentation.theme.AndroidUnitTestingTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewmodel: MainViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
        setContent {
            AndroidUnitTestingTheme {
                val isLoggedIn by mainViewmodel.isLoggedIn.collectAsState()

                if (isLoggedIn) {
                    ProfileScreen(
                        modifier = Modifier,
                        profileViewModel = profileViewModel,
                        logout = {
                            mainViewmodel.onEvent(MainUiEvent.Logout)
                        }
                    )
                } else {
                    LoginScreen(
                        modifier = Modifier,
                        loginViewModel = loginViewModel,
                        goToProfile = {
                            mainViewmodel.onEvent(MainUiEvent.GotoProfile)
                        }
                    )
                }
            }
        }
    }
}