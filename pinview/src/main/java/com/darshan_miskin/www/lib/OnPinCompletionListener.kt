package com.darshan_miskin.www.lib

/**
 * Called when the pin is completely entered. i.e all pin values are filled.
 *
 * Returns the entire pin.
 */
fun interface OnPinCompletionListener {
    fun onPinCompleted(entirePin: String)
}