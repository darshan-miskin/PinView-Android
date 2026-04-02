package com.darshan_miskin.www.lib

import android.view.KeyEvent
import android.view.View
import android.widget.EditText

internal class PinOnKeyListener(private val currentIndex: Int, pinEditTexts: ArrayList<EditText>) :
    View.OnKeyListener {
    private val pinEditTexts = ArrayList<EditText>()

    init {
        this.pinEditTexts.clear()
        this.pinEditTexts.addAll(pinEditTexts)
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
            if (pinEditTexts[currentIndex].getText().toString()
                    .isEmpty() && currentIndex != 0
            ) {
                PinTextWatcher.isDeleting = true
                pinEditTexts[currentIndex - 1].setFocusable(true)
                pinEditTexts[currentIndex - 1].setFocusableInTouchMode(true)
                pinEditTexts[currentIndex - 1].setText("")
                pinEditTexts[currentIndex - 1].requestFocus()
            }
        }
        return false
    }
}

