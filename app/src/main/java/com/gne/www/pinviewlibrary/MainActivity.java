package com.gne.www.pinviewlibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.gne.www.lib.InputType;
import com.gne.www.lib.OnPinCompletedListener;
import com.gne.www.lib.PinView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PinView pinView=findViewById(R.id.pinview);

        pinView.setPinCount(6);
        pinView.setText("helllooo");
        pinView.setPinBackground(getResources().getDrawable(R.drawable.pin_background));
        pinView.setPassword(false);
        pinView.getText();
        pinView.setPinSize(40);
        pinView.setInputType(InputType.TYPE_TEXT);

        pinView.setOnPinCompletionListener(new OnPinCompletedListener() {
            @Override
            public void onPinCompleted(String entirePin) {
                Toast.makeText(MainActivity.this,"Text Entered",Toast.LENGTH_LONG).show();
            }
        });

    }
}
