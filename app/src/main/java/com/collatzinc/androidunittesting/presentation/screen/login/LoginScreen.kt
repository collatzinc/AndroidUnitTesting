package com.collatzinc.androidunittesting.presentation.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.collatzinc.androidunittesting.R
import com.collatzinc.androidunittesting.presentation.screen.common.AppTextField
import com.collatzinc.androidunittesting.presentation.screen.common.AppTextFieldDefaults
import com.collatzinc.androidunittesting.presentation.screen.common.HandleApiError
import com.collatzinc.androidunittesting.presentation.theme.AndroidUnitTestingTheme
import com.collatzinc.tokenrefreshapp.ui.screen.common.FullScreenLoading


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel,
    goToProfile: () -> Unit
) {

    val uiState = loginViewModel.loginUiState.collectAsStateWithLifecycle()

    when (val state = uiState.value) {
        is LoginUiState.Initial -> {
            Login(modifier = modifier, uiState = state, onEvent = { loginViewModel.onEvent(it) })

            if (state.isLoading) {
                FullScreenLoading()
            }
            state.apiError?.let { error ->
                HandleApiError(
                    error = error,
                    onPopupDismiss = {
                        loginViewModel.onEvent(LoginUiEvent.DismissApiError)
                    }
                )
            }
        }

        LoginUiState.LoggedIn -> {
            LaunchedEffect(Unit) {
                goToProfile()
            }
        }
    }


}

@Composable
fun Login(
    modifier: Modifier = Modifier,
    uiState: LoginUiState.Initial,
    onEvent: (LoginUiEvent) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF6A11CB), Color(0xFF2575FC))
            )),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome Back 👋",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Login to your account",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )

                Spacer(modifier = Modifier.height(24.dp))


                AppTextField(
                    value = uiState.username,
                    label = stringResource(R.string.username),
                    labelStyle = AppTextFieldDefaults.levelStyle.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold
                    ),
                    inputStyle = AppTextFieldDefaults.inputStyle.copy(
                        textAlign = TextAlign.Start
                    ),
                    error = uiState.usernameError,
                    maxLine = 1,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    onValueChange = {
                        onEvent(LoginUiEvent.UsernameChanged(it))
                    },
                    onNext = {
                        onEvent(LoginUiEvent.ClickOnKeyboardNext)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                AppTextField(
                    value = uiState.password,
                    label = stringResource(R.string.password),
                    labelStyle = AppTextFieldDefaults.levelStyle.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold
                    ),
                    inputStyle = AppTextFieldDefaults.inputStyle.copy(
                        textAlign = TextAlign.Start
                    ),
                    error = uiState.passwordError,
                    maxLine = 1,
                    isPasswordField = uiState.isPasswordVisible.not(),
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    trailingIconRes = if (uiState.isPasswordVisible)
                        R.drawable.ic_eye_on
                    else
                        R.drawable.ic_eye_off,
                    trailingIconTint = MaterialTheme.colorScheme.onSurface,
                    trailingIconClick = {
                        onEvent(LoginUiEvent.PasswordEyeToggleClick)
                    },
                    onValueChange = {
                        onEvent(LoginUiEvent.PasswordChanged(it))
                    },
                    onNext = {
                        onEvent(LoginUiEvent.ClickOnKeyboardDone)
                    }

                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onEvent(LoginUiEvent.ClickOnLogin) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2575FC))
                ) {
                    Text("Login", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    AndroidUnitTestingTheme {
        Login(
            uiState = LoginUiState.Initial(),
            onEvent = {})
    }
}