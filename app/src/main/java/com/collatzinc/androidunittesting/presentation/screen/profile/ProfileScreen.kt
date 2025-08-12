package com.collatzinc.androidunittesting.presentation.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.collatzinc.androidunittesting.R
import com.collatzinc.androidunittesting.presentation.screen.common.HandleApiError
import com.collatzinc.androidunittesting.presentation.theme.AndroidUnitTestingTheme
import com.collatzinc.tokenrefreshapp.ui.screen.common.FullScreenLoading


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel,
    logout: () -> Unit
) {

    val uiState = profileViewModel.profileUiState.collectAsStateWithLifecycle()

    uiState.value.userUiData?.let {
        Profile(modifier = modifier, user = it, logout = logout)
    }


    if (uiState.value.isLoading) {
        FullScreenLoading()
    }
    uiState.value.apiError?.let { error ->
        HandleApiError(
            error = error,
            onPopupDismiss = {
                profileViewModel.onEvent(ProfileUiEvent.DismissApiError)
            }
        )
    }


}

@Composable
fun Profile(modifier: Modifier = Modifier, user: UserUiData, logout: () -> Unit) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF6A11CB), Color(0xFF2575FC))
                    )
                )
                .safeDrawingPadding()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Profile Image
            AsyncImage(
                model = user.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(4.dp, Color.White, CircleShape)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = user.fullName ?: "",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )

            Text(
                text = user.username ?: "",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(
                        alpha = 0.7f
                    )
                )
            )
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Info Card
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProfileInfoRow(Icons.Default.Email, "Email", user.email)
                ProfileInfoRow(Icons.Default.Phone, "Phone", user.phone)
                ProfileInfoRow(Icons.Default.Person, "Gender", user.gender)
                ProfileInfoRow(Icons.Default.Cake, "Birth Date", user.birthDate)
                ProfileInfoRow(Icons.Default.Bloodtype, "Blood Group", user.bloodGroup)
                ProfileInfoRow(Icons.Default.Numbers, "Age", user.age?.toString())
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            modifier = Modifier.clickable(onClick = {
                logout()
            }),
            text = stringResource(R.string.logout),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(20.dp))

    }
}

@Composable
fun ProfileInfoRow(icon: ImageVector, label: String, value: String?) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF2575FC),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
            )
            Text(
                text = value ?: "N/A",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    AndroidUnitTestingTheme {
        Profile(
            user = UserUiData(
                "Milan Maji",
                29,
                "Male",
                "milan.maji@991@gmail.com",
                "1234567890",
                "@Milan991",
                "27th May, 1996",
                "",
                "O+"
            ),
            logout = {}
        )
    }
}