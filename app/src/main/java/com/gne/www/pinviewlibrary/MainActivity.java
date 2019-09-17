package com.gne.www.pinviewlibrary;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.gne.www.lib.InputType;
import com.gne.www.lib.OnPinCompletedListener;
import com.gne.www.lib.PinView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final PinView pinView=findViewById(R.id.pinview);

        pinView.setPinCount(6);
        pinView.setText("helllooo");
//        pinView.setPinBackground(getResources().getDrawable(R.drawable.pin_background));
        pinView.getText();
        pinView.setPinSize(50);
        pinView.setInputType(InputType.TYPE_NUMBER);
        pinView.setShowPasswordToggle(true);
        pinView.setPassword(true);
        pinView.requestPinFocus();
        pinView.requestPinFocus(4);
        pinView.setPasswordToggleColor(getResources().getColor(R.color.colorPrimary));
//        pinView.setPinSize(20);
        pinView.setPinTextSize(13);
        pinView.setPasswordToggleSize(20);


        pinView.setOnPinCompletionListener(new OnPinCompletedListener() {
            @Override
            public void onPinCompleted(String entirePin) {
                Toast.makeText(MainActivity.this,"Text Entered",Toast.LENGTH_LONG).show();
                hideKeyboard();
//                pinView.setShowPasswordToggle(false);
//                pinView.setInputType(InputType.TYPE_NUMBER);
            }
        });

    }

    private void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
