package com.darshan_miskin.www.pinviewlibrary;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.darshan_miskin.www.lib.InputType;
import com.darshan_miskin.www.lib.PinView;

public class MainActivityJava extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_java);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        PinView pinView = findViewById(R.id.pinview);

        pinView.setPinCount((short) 6);                                 //set total number of pins
        pinView.setText("hello!");                                      //set text for the pinview
        pinView.setPinBackground(                                       //pin style
            ContextCompat.getDrawable(this, R.drawable.pin_background)
        );
        pinView.setPassword(false);                                     //password flag. If true dotted format will be visible
        pinView.getText();                                              //get text from pinview
        pinView.setPinSizeDp(40);                                       //set size of pin in dp
        pinView.setInputType(InputType.TEXT);                           // or InputType.NUMBER
        pinView.setShowPasswordToggle(true);                            //make the password toggle visible
        pinView.requestPinFocusAt(4);                             //request focus on pin at 4th index, defaults to zeroth if invalid index
        pinView.setPasswordToggleColor(                                 //set toggle drawable tint/color
            ContextCompat.getColor(this, R.color.colorPrimary)
        );
        pinView.setPinTextSizeSp(23);                                   //set pin text size in sp
        pinView.setPasswordToggleSizeDp(20);                            //set password toggle size in dp
        pinView.setPinTextColor(                                        //set text color of the pins
            ContextCompat.getColor(this, R.color.colorPrimary)
        );

//        pinView.setOnPinCompletionListener(new OnPinCompletedListener() {
//            @Override
//            public void onPinCompleted(String entirePin) {
//                Toast.makeText(MainActivityJava.this,"Text Entered",Toast.LENGTH_LONG).show();
//            }
//        });
    }
}