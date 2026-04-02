package com.darshan_miskin.www.lib.compose

import android.graphics.drawable.Drawable
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
import com.darshan_miskin.www.lib.OnPinCompletionListener
import com.darshan_miskin.www.lib.PinView

@Composable
fun PinView(
    modifier: Modifier = Modifier,
    /**
     * Total number of pins in the pinview. Default is 4.
     */
    pinCount: Short = PinView.DEFAULT_PIN_COUNT,
    /**
     * Set the pinview text
     */
    text: String = "",
    /**
     * Sets the drawable background of individual pin.
     *
     * If no value is assigned, pins will have default [androidx.compose.material3.TextField] background.
     */
    pinBackground: Drawable? = null,
    /**
     * Specify if the pinview is of the type password. Default value is false.
     *
     * Setting a value to this variable is independent of [showPasswordToggle]. However, setting a value to [showPasswordToggle] updates this variable.
     *
     */
    isPassword: Boolean = false,
    /**
     * Set a listener to get notified when the pin value changes.
     */
    onValueChange: (String) -> Unit = {},
    /**
     * Width of the individual pin in dp. Is also applied to height. Default value is 50dp.
     */
    pinSize: Dp = dimensionResource(R.dimen.pin_size),
    /**
     *
     * Set the input type of the pinview. Default is [InputType.NUMBER].
     *
     * Can either be [InputType.NUMBER] or [InputType.TEXT]
     */
    inputType: InputType = InputType.NUMBER,
    /**
     * Enable or disable password toggle. Default value is false.
     *
     * Enabling this, simultaneously also enables [isPassword]
     */
    showPasswordToggle: Boolean = false,
//    /**
//     * Request focus on the pin at specified index.
//     *
//     * If index is invalid, defaults to zero.
//     */
//    requestFocus: Int = 0,
    /**
     * Color/Tint to be applied to the toggle view. Default value is [Color.Black] for Light Theme & [Color.White] for Dark Theme.
     */
    passwordToggleColor: Color = if (isSystemInDarkTheme()) Color.White else Color.Black,
    /**
     * Text size of the pins in sp. Default value is 23sp
     */
    pinTextSize: TextUnit = TextUnit(PinView.DEFAULT_PIN_TEXT_SIZE, TextUnitType.Sp),
    /**
     * Size of the toggle view in dp. Default value is 25dp
     */
    passwordToggleSize: Dp = dimensionResource(R.dimen.password_toggle_size),
    /**
     * Text color of the pins. Default value is [Color.Black] for Light Theme & [Color.White] for Dark Theme.
     */
    pinTextColor: Color = if (isSystemInDarkTheme()) Color.White else Color.Black,
    /**
     * Set a listener to get notified when all the pins are entered
     * @param onPinCompletionListener instance of [OnPinCompletionListener]
     */
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
            pinView
        },
        update = {
            it.pinCount = pinCount
            it.text = text
            it.inputType = inputType
            it.showPasswordToggle = showPasswordToggle
            it.isPassword = isPassword
            it.pinBackground = pinBackground
            it.passwordToggleColor = passwordToggleColor.toArgb()
            it.pinTextSizeSp = pinTextSize.value
            it.pinTextColor = pinTextColor.toArgb()
            it.pinSizeDp = pinSize.value.toInt()
            it.passwordToggleSizeDp = passwordToggleSize.value.toInt()

            it.setOnPinCompletionListener(onPinCompletionListener)
            it.setOnPinChangedListener {
                onValueChange(it.text)
            }
        },
        modifier = modifier,
    )
}