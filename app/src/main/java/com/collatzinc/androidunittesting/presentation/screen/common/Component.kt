package com.collatzinc.tokenrefreshapp.ui.screen.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun FullScreenLoading() {
    Dialog(onDismissRequest = { }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

}

@Composable
fun ErrorDialog(
    errorMessage: String,
    onDismiss: () -> Unit = {}
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .wrapContentWidth()
                .padding(5.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        )
        {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Error",
                    style = LocalTextStyle.current.copy(
                        fontFeatureSettings = "pnum, lnum",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp,
                    )

                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = errorMessage,
                    modifier = Modifier.padding(vertical = 5.dp, horizontal = 15.dp),
                    style = LocalTextStyle.current.copy(
                        fontFeatureSettings = "pnum, lnum",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline)
                Button(
                    onClick = {
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                    )
                ) {
                    Text(
                        text = "Ok",
                        style = LocalTextStyle.current.copy(
                            fontFeatureSettings = "pnum, lnum",
                            fontSize = 14.sp,
                        )
                    )
                }
            }

        }
    }

}