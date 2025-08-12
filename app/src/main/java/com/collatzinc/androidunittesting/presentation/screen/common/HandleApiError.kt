package com.collatzinc.androidunittesting.presentation.screen.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.collatzinc.androidunittesting.R
import com.collatzinc.androidunittesting.data.network.ApiError
import com.collatzinc.tokenrefreshapp.ui.screen.common.ErrorDialog


@Composable
fun HandleApiError(
    error: ApiError,
    logout: () -> Unit = {},
    onPopupDismiss: () -> Unit,
) {
    when(error){
        is ApiError.Forbidden -> {
            ErrorDialog( stringResource(R.string.something_went_wrong)) {
                onPopupDismiss()
            }
        }
        ApiError.Unauthorized -> {
            logout()
        }
        ApiError.Network -> {
            ErrorDialog(errorMessage = stringResource(R.string.network_error)) {
               onPopupDismiss()
            }
        }
        is ApiError.Unknown -> {
            ErrorDialog(error.message ?: stringResource(R.string.something_went_wrong)) {
                onPopupDismiss()
            }
        }
    }

}