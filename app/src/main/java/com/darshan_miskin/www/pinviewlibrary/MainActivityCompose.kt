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
                val keyboard = LocalSoftwareKeyboardController.current
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PinView(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .wrapContentHeight(),
                        text = "123456",
                        showPasswordToggle = true,
                        onValueChange = {
                            Log.d("asdf", "pin value: $it")
                        },
                    ) {
                        Toast.makeText(this, "Entered pin is: $it", Toast.LENGTH_LONG).show()
                        keyboard?.hide()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PinViewAndroidTheme {
        Greeting("Android")
    }
}