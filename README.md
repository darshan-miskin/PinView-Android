# PinView-Android
[![](https://jitpack.io/v/darshan-miskin/PinView-Android.svg)](https://jitpack.io/#darshan-miskin/PinView-Android)


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
