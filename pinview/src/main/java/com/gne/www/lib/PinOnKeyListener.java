package com.gne.www.lib;

import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class PinOnKeyListener implements View.OnKeyListener {

    private int currentIndex;
    private ArrayList<EditText> pinEditTexts=new ArrayList<>();

    public PinOnKeyListener(int currentIndex, ArrayList<EditText> pinEditTexts) {
        this.currentIndex = currentIndex;
        this.pinEditTexts.clear();
        this.pinEditTexts.addAll(pinEditTexts);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (pinEditTexts.get(currentIndex).getText().toString().isEmpty() && currentIndex != 0)
                pinEditTexts.get(currentIndex - 1).requestFocus();
        }
        return false;
    }

}

