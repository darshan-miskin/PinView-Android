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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import com.darshan_miskin.www.lib.compose.PinView
import com.darshan_miskin.www.pinviewlibrary.ui.theme.PinViewAndroidTheme

class MainActivityCompose : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PinViewAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .wrapContentHeight())
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    val keyboard = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    PinView(
        modifier = modifier,
        text = "1234",
        showPasswordToggle = true,
        onValueChange = {
            Log.d("asdf", "pin value: $it")
        },
    ) {
        Toast.makeText( context, "Entered pin is: $it", Toast.LENGTH_LONG).show()
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