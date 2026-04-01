package com.darshan_miskin.www.lib

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.darshan_miskin.www.lib.PinView.Companion.onPinCompleted
import com.darshan_miskin.www.lib.PinView.Companion.onPinChanged


internal class PinTextWatcher(private val currentIndex: Int, pinEditTexts: ArrayList<EditText>) :
    TextWatcher {
    private var isFirst = false
    private var isLast = false
    private var newTypedString = ""
    private var entirePin = ""
    private val pinEditTexts = ArrayList<EditText>()

    init {
        this.pinEditTexts.clear()
        this.pinEditTexts.addAll(pinEditTexts)

        if (currentIndex == 0) this.isFirst = true
        else if (currentIndex == pinEditTexts.size - 1) this.isLast = true
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        newTypedString = s.subSequence(start, start + count).toString().trim { it <= ' ' }
    }

    override fun afterTextChanged(s: Editable) {
        val text = newTypedString

        pinEditTexts[currentIndex].removeTextChangedListener(this)
        pinEditTexts[currentIndex].setText(text)
        if (!isProcessing) onPinChanged.invoke()
        pinEditTexts[currentIndex].setSelection(text.length)
        pinEditTexts[currentIndex].addTextChangedListener(this)

        if (!isProcessing) if (text.length >= 1) moveToNext()
        //            else {
//                if(!isDeleting)
//                    moveToPrevious();
//                isDeleting=false;
//            }
    }

    private fun moveToNext() {
        if (!isLast) {
            pinEditTexts[currentIndex].setFocusable(false)
            pinEditTexts[currentIndex + 1].requestFocus()
        }

        if (this.isAllEditTextsFilled && !isProcessing) {
            onPinCompleted?.invoke(entirePin)
        }
    }

    private fun moveToPrevious() {
        isDeleting = true

        if (!isFirst) {
            pinEditTexts[currentIndex - 1].setFocusable(true)
            pinEditTexts[currentIndex - 1].setText("")
            pinEditTexts[currentIndex - 1].setFocusableInTouchMode(true)
            pinEditTexts[currentIndex - 1].requestFocus()
        }
    }

    private val isAllEditTextsFilled: Boolean
        get() {
            entirePin = ""
            for (editText in pinEditTexts) {
                entirePin += editText.getText().toString()
                if (editText.getText().toString().trim { it <= ' ' }.isEmpty()) return false
            }
            return true
        }

    fun setProcessing(isProcessing: Boolean) {
        Companion.isProcessing = isProcessing
    }

    companion object {
        @JvmField
        var isDeleting: Boolean = false
        private var isProcessing = false
    }
}
