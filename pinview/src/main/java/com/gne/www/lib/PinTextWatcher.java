package com.gne.www.lib;


import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import java.util.ArrayList;

import static android.content.Context.INPUT_METHOD_SERVICE;

class PinTextWatcher implements TextWatcher {

    private AppCompatActivity activity;
    private int currentIndex;
    private boolean isFirst = false, isLast = false;
    private String newTypedString = "", entirePin="";
    private ArrayList<EditText> pinEditTexts=new ArrayList<>();

    public PinTextWatcher(AppCompatActivity activity, int currentIndex,ArrayList<EditText> pinEditTexts) {
        this.currentIndex = currentIndex;
        this.activity=activity;
        this.pinEditTexts.clear();
        this.pinEditTexts.addAll(pinEditTexts);

        if (currentIndex == 0)
            this.isFirst = true;
        else if (currentIndex == pinEditTexts.size()- 1)
            this.isLast = true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        newTypedString = s.subSequence(start, start + count).toString().trim();
    }

    @Override
    public void afterTextChanged(Editable s) {

        String text = newTypedString;

        pinEditTexts.get(currentIndex).removeTextChangedListener(this);
        pinEditTexts.get(currentIndex).setText(text);
        pinEditTexts.get(currentIndex).setSelection(text.length());
        pinEditTexts.get(currentIndex).addTextChangedListener(this);

        if (text.length() >= 1)
            moveToNext();
        else
            moveToPrevious();
    }

    private void moveToNext() {
        if (!isLast)
            pinEditTexts.get(currentIndex + 1).requestFocus();

        if (isAllEditTextsFilled()) {
            pinEditTexts.get(currentIndex).clearFocus();
            if(PinView.onPinCompletionListener!=null){
                PinView.onPinCompleted(entirePin);
            }
            hideKeyboard();
        }
    }

    private void moveToPrevious() {
        if (!isFirst)
            pinEditTexts.get(currentIndex - 1).requestFocus();
    }

    private boolean isAllEditTextsFilled() {
        entirePin="";
        for (EditText editText : pinEditTexts) {
            entirePin+=editText.getText().toString();
            if (editText.getText().toString().trim().length() == 0)
                return false;
        }
        return true;
    }

    private void hideKeyboard() {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

}
