package com.collatzinc.androidunittesting.presentation.screen.common

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.collatzinc.androidunittesting.presentation.theme.AndroidUnitTestingTheme


@Composable
fun AppTextField(
    value: String,
    label: String? = null,
    hint: String? = null,
    onValueChange: (value: String) -> Unit = {},
    isPasswordField: Boolean = false,
    allowedChars: (Char) -> Boolean = { true },
    isClickOnly: Boolean = false,
    isDisabled: Boolean = false,
    maxLine: Int = Int.MAX_VALUE,
    maxLength: Int = Int.MAX_VALUE,
    onClick: () -> Unit = {},
    @StringRes error: Int? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    isShowError: Boolean = true,
    trailingIconRes: Int? = null,
    trailingIconClick: (() -> Unit)? = null,
    trailingIconTint:Color = Color.Unspecified,
    onDone: () -> Unit = {},
    onNext: () -> Unit = {},
    onFocusChanged: (focusState: FocusState) -> Unit = {},
    labelStyle: TextStyle = AppTextFieldDefaults.levelStyle,
    hintStyle: TextStyle = AppTextFieldDefaults.hintStyle,
    errorStyle: TextStyle = AppTextFieldDefaults.errorStyle,
    inputStyle: TextStyle = AppTextFieldDefaults.inputStyle,

    ) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(error) {
        if (error != null) {
            focusRequester.requestFocus()
        }
    }

    Column {
        label?.let {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = label,
                style = labelStyle
            )
        }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester).then(
                if (isClickOnly && !isDisabled) {
                    Modifier.clickable { onClick() }
                } else Modifier
            ).onFocusChanged { focusState ->
                    onFocusChanged(focusState)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                errorTextColor = MaterialTheme.colorScheme.onBackground,
                disabledTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = if (isClickOnly) 1f else 0.5f),
                focusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = if (isClickOnly) 1f else 0.5f),
                errorPlaceholderColor = MaterialTheme.colorScheme.onBackground,
                focusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                disabledIndicatorColor = MaterialTheme.colorScheme.outline.copy(alpha = if (isClickOnly) 1f else 0.5f),
                errorIndicatorColor = MaterialTheme.colorScheme.error,
                errorSupportingTextColor = MaterialTheme.colorScheme.error,
                disabledSupportingTextColor = MaterialTheme.colorScheme.error

            ),
            textStyle = inputStyle,
            value = value,
            onValueChange = { newText ->
                val filteredText = newText.filter(allowedChars)
                if (!isClickOnly && !isDisabled && newText.length <= maxLength)
                    onValueChange(filteredText)
            },
            maxLines = maxLine,
            isError = error != null,
//            readOnly = isClickOnly || isDisabled,
            enabled = !isDisabled && !isClickOnly,
            supportingText = {
                error?.let { msg ->
                    if (isShowError) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = stringResource(msg),
                            style = errorStyle
                        )
                    }
                }
            },
            visualTransformation = if (isPasswordField) PasswordVisualTransformation() else VisualTransformation.None,

            placeholder = {
                hint?.let {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = hint,
                        style = hintStyle
                    )
                }

            },
            trailingIcon = if(trailingIconRes!=null) {
                {
                    IconButton(onClick = { trailingIconClick?.invoke() }) {
                        Icon(
                            painter = painterResource(id = trailingIconRes),
                            contentDescription = "Trailing Icon",
                            modifier = Modifier.size(24.dp),
                            tint = trailingIconTint
                        )
                    }
                }
            }else null,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onDone()
                },
                onNext = {
//                focusManager.moveFocus(FocusDirection.Down)
                    onNext()
                }
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType,
                imeAction = imeAction,
                capitalization = if (keyboardType == KeyboardType.Text)
                    KeyboardCapitalization.Sentences
                else
                    KeyboardCapitalization.Unspecified
            )

        )
    }

}




@AppPreview
@Composable
private fun PreviewAppTextField() {
    AndroidUnitTestingTheme {
        Column {
            AppTextField(
                value = "",
                hint = "Enter Phone Number",
                label = "Your Name",
                onValueChange = {},
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .width(2.dp)
                    .background(color = Color.Red)
            )


        }
    }
}