package com.darshan_miskin.www.lib.compose

import android.view.ContextThemeWrapper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.viewinterop.AndroidView
import com.darshan_miskin.darshan_miskin.pinview.R
import com.darshan_miskin.www.lib.InputType
import com.darshan_miskin.www.lib.PinView

@Composable
fun PinView(
    modifier: Modifier = Modifier,
    text: String = "",
    onValueChange: (String) -> Unit = {},
    pinCount: Short = PinView.DEFAULT_PIN_COUNT,
    inputType: InputType = InputType.NUMBER,
    isPassword: Boolean = false,
    showPasswordToggle: Boolean = false,
    pinSize: Dp = dimensionResource(R.dimen.pin_size),
    pinTextSize: TextUnit = TextUnit(PinView.DEFAULT_PIN_TEXT_SIZE, TextUnitType.Sp),
    passwordToggleSize: Dp = dimensionResource(R.dimen.password_toggle_size),
    pinTextColor: Color = if (isSystemInDarkTheme()) Color.White else Color.Black,
    passwordToggleColor: Color = if (isSystemInDarkTheme()) Color.White else Color.Black,
    onPinCompletionListener: (String) -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    AndroidView(
        factory = { context ->
            val themedContext = ContextThemeWrapper(
                context,
                if (isDarkTheme) android.R.style.Theme_Material else android.R.style.Theme_Material_Light
            )
            val pinView = PinView(themedContext)
            pinView.setOnPinCompletionListener(onPinCompletionListener)
            pinView.setOnPinChanged {
                onValueChange(pinView.text)
            }
            pinView
        },
        update = {
            it.pinCount = pinCount
            it.text = text
            it.inputType = inputType
            it.showPasswordToggle = showPasswordToggle
            it.isPassword = isPassword
            it.passwordToggleColor = passwordToggleColor.toArgb()
            it.pinTextSizeSp = pinTextSize.value
            it.pinTextColor = pinTextColor.toArgb()
            it.pinSizeDp = pinSize.value.toInt()
            it.passwordToggleSizeDp = passwordToggleSize.value.toInt()
        },
        modifier = modifier,
    )
}