# PinView-Android
[![](https://jitpack.io/v/darshan-miskin/PinView-Android.svg)](https://jitpack.io/#darshan-miskin/PinView-Android)

<!--![alt text](https://github.com/darshan-miskin/PinView-Android/blob/master/imgs/GIF_1559495949954.gif?&v=1024&s=576)
![alt text](https://github.com/darshan-miskin/PinView-Android/blob/master/imgs/GIF_1559496256548.gif?&=1024×576)-->
<img src="https://github.com/darshan-miskin/PinView-Android/blob/master/imgs/GIF_1559495949954.gif??" width="300" height="600">
<img src="https://github.com/darshan-miskin/PinView-Android/blob/master/imgs/GIF_1559496256548.gif?" width="300" height="600">

# Gradle Dependency 
Add the following in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
 
 Add this in your module's build.gradle 
 
	dependencies {
	        implementation 'com.github.darshan-miskin:PinView-Android:v1.0.1'
	}
  
  # Features
  - Supports Paste via clipboard flawlessly.
  - Easy getText() and setText() methods like a regular EditText.
  - Listener to detect when the pin is completely entered.
  - Styleable pins.
  - Flag to set password input type. 
  
  # Usage
  
  XML:
  
    <com.gne.www.lib.PinView
        android:id="@+id/pinview"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:pinCount="4"
        app:inputType="number"
        app:isPassword="false"
        app:pinBackground="@drawable/pin_background"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"/>
        
Java: 


        PinView pinView=findViewById(R.id.pinview);
        
        pinView.setPinCount(6);                      //set total number of pins
        pinView.setText("helllooo");                 //set text for the pinview
        pinView.setPinBackground(getResources().getDrawable(R.drawable.pin_background));  //pin style
        pinView.setPassword(false);                  //password flag. If true dotted format will be visible
        pinView.getText();                           //get text from pinview
        pinView.setPinSize(40);                      //set size of pinview in dp
        pinView.setInputType(InputType.TYPE_TEXT);   // or InputType.TYPE_NUMBER
        
        pinView.setOnPinCompletionListener(new OnPinCompletedListener() {
            @Override
            public void onPinCompleted(String entirePin) {
                Toast.makeText(MainActivity.this,"Text Entered",Toast.LENGTH_LONG).show();
            }
        });
	
	
# License

    MIT License

    Copyright (c) 2019 Darshan Miskin

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
 
