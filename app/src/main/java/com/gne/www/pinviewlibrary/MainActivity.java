package com.gne.www.pinviewlibrary;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gne.www.lib.PinView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PinView pinView=findViewById(R.id.pinview);

        pinView.setPinCount(6);
//        pinView.setText("helllooo");

//        pinView.setPinBackground(getResources().getDrawable(R.drawable.pin_background));
    }
}
