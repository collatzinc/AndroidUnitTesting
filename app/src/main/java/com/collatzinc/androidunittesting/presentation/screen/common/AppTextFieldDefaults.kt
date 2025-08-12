package com.collatzinc.androidunittesting.presentation.screen.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

object AppTextFieldDefaults {


    val levelStyle: TextStyle
        @Composable
        get() = TextStyle(
//            fontFamily = FontFamily(Font(R.font.raleway_regular)),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            fontFeatureSettings = "pnum, lnum",
            lineHeight = 1.2.em
        )
    val hintStyle: TextStyle
        @Composable
        get() = TextStyle(
//            fontFamily = FontFamily(Font(R.font.raleway_regular)),
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            fontFeatureSettings = "pnum, lnum",
            lineHeight = 1.2.em
        )
    val errorStyle: TextStyle
        @Composable
        get() = TextStyle(
            textAlign = TextAlign.Center,
//            fontFamily = FontFamily(Font(R.font.raleway_regular)),
            fontFeatureSettings = "pnum, lnum",
            fontSize = 13.sp,
            lineHeight = 1.2.em
        )
    val inputStyle: TextStyle
        @Composable
        get() = TextStyle(
            textAlign = TextAlign.Center,
            fontSize = 13.sp,
//            fontFamily = FontFamily(Font(R.font.raleway_regular)),
            fontFeatureSettings = "pnum, lnum",
            lineHeight = 1.2.em
        )

}