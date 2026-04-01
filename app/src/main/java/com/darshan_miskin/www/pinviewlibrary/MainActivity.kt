package com.darshan_miskin.www.pinviewlibrary

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.darshan_miskin.www.lib.InputType
import com.darshan_miskin.www.lib.PinView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pinView = findViewById<PinView>(R.id.pinview)

        pinView.pinCount = 4
        pinView.text = "hello!!"
//        pinView.setPinBackground(getResources().getDrawable(R.drawable.pin_background));
        pinView.text
//        pinView.pinSizeDp = 40
        pinView.inputType = InputType.TEXT
        pinView.showPasswordToggle = true
        pinView.isPassword = false
        pinView.requestPinFocus()
        pinView.requestPinFocusAt(4)
        pinView.passwordToggleColor = ContextCompat.getColor(this, R.color.colorPrimary)
//        pinView.setPinSize(20);
        pinView.pinTextSizeSp = 13f
        pinView.passwordToggleSizeDp = 30


//        pinView.setTextColor(getResources().getColor(R.color.colorPrimary));
        pinView.setOnPinCompletionListener { entirePin ->
            Toast.makeText(this@MainActivity, "Text Entered: $entirePin", Toast.LENGTH_LONG).show()
            hideKeyboard()
//            pinView.setShowPasswordToggle(false);
//            pinView.setInputType(InputType.TYPE_NUMBER);
        }
    }

    private fun hideKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }
}
