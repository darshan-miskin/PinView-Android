package com.darshan_miskin.www.pinviewlibrary

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.darshan_miskin.www.lib.InputType
import com.darshan_miskin.www.lib.compose.PinView
import com.darshan_miskin.www.pinviewlibrary.ui.theme.PinViewAndroidTheme

class MainActivityCompose : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PinViewAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .wrapContentHeight()
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    PinView(
        modifier = modifier,
        pinCount = 5,
        text = "1234",
        pinBackground = ContextCompat.getDrawable(context, R.drawable.pin_background),
        isPassword = true,
        onValueChange = {
            Toast.makeText(context, "pin value: $it", Toast.LENGTH_LONG).show()
            Log.d("asdf", "pin value: $it")
        },
        pinSize = 40.dp,
        inputType = InputType.NUMBER,
        showPasswordToggle = true,
        requestFocus = 2,
        passwordToggleColor = Color.Blue,
        pinTextSize = 20.sp,
        passwordToggleSize = 40.dp,
        pinTextColor = Color.LightGray,
    ) { entirePin ->
        Toast.makeText(context, "Entire pin is: $entirePin", Toast.LENGTH_LONG).show()
        focusManager.clearFocus()
        keyboard?.hide()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PinViewAndroidTheme {
        Greeting()
    }
}