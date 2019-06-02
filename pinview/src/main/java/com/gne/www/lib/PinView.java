package com.gne.www.lib;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import java.util.ArrayList;

public class PinView extends LinearLayoutCompat{

    private Context context;
    private short pinCount=6, inputType= com.gne.www.lib.InputType.TYPE_NUMBER;
    private boolean isPassword=false;
    private Drawable background;
    private ArrayList<EditText> pinEditTexts=new ArrayList<>();
    static OnPinCompletedListener onPinCompletionListener;

    public PinView(Context context) {
        super(context);
        this.context=context;
        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.CENTER);
        addPins();
    }

    public PinView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.CENTER);

        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.PinView);
        pinCount=(short)a.getInteger(R.styleable.PinView_pinCount,pinCount);
        inputType=(short)a.getInteger(R.styleable.PinView_inputType,pinCount);
        isPassword=a.getBoolean(R.styleable.PinView_isPassword,false);

        if(a.hasValue(R.styleable.PinView_pinBackground)){
            background=a.getDrawable(R.styleable.PinView_pinBackground);
        }
        a.recycle();
        addPins();
    }

    public PinView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.CENTER);

        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.PinView);
        pinCount=(short)a.getInteger(R.styleable.PinView_pinCount,pinCount);
        inputType=(short)a.getInteger(R.styleable.PinView_inputType,pinCount);
        isPassword=a.getBoolean(R.styleable.PinView_isPassword,false);

        if(a.hasValue(R.styleable.PinView_pinBackground)){
            background=a.getDrawable(R.styleable.PinView_pinBackground);
        }
        a.recycle();

        addPins();
    }

    @Override
    protected void onCreateContextMenu(ContextMenu menu) {
        super.onCreateContextMenu(menu);

        menu.add(0, 0, 0, "Paste").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ClipboardManager clipboardManager=(ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                String text=clipboardManager.getPrimaryClip().getItemAt(0).getText().toString();
                setText(text);
                return true;
            }
        });
    }

    private void addPins(){
        this.removeAllViews();
        pinEditTexts.clear();
        for (int i=0; i<getPinCount(); i++){
            EditText pinEditText=new EditText(context);
            pinEditText.setGravity(Gravity.CENTER);
            LinearLayoutCompat.LayoutParams layoutParams=new LinearLayoutCompat.
                                        LayoutParams(getResources().getDimensionPixelOffset(R.dimen.pin_size),
                                        getResources().getDimensionPixelOffset(R.dimen.pin_size)/*,1*/);
            layoutParams.setMargins(getResources().getDimensionPixelSize(R.dimen.margin_pin_edit_text),getResources().getDimensionPixelSize(R.dimen.margin_pin_edit_text),
                    getResources().getDimensionPixelSize(R.dimen.margin_pin_edit_text),getResources().getDimensionPixelSize(R.dimen.margin_pin_edit_text));
            pinEditText.setLayoutParams(layoutParams);
            pinEditText.setMaxLines(1);
            pinEditText.setLines(1);
            pinEditText.setPadding(0,0,0,0);
            pinEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1), new InputFilter.AllCaps()});
            if(background!=null)
                pinEditText.setBackground(background);
            if(isPassword){
                if(inputType==InputType.TYPE_TEXT)
                    pinEditText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                else
                    pinEditText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            }
            else {
                if (inputType == InputType.TYPE_TEXT)
                    pinEditText.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
                else
                    pinEditText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            }
            pinEditTexts.add(pinEditText);
            this.addView(pinEditText);
        }
        for (int i=0; i<pinEditTexts.size(); i++){
            pinEditTexts.get(i).addTextChangedListener(new PinTextWatcher((AppCompatActivity) context,i, pinEditTexts));
            pinEditTexts.get(i).setOnKeyListener(new PinOnKeyListener(i,pinEditTexts));
        }
    }

    private short getPinCount(){
        return pinCount;
    }

    /**
     *
     * @return Collective text in the pinview
     */
    public String getText(){
        String text="";
        for (int i=0; i<getPinCount(); i++){
            text+=pinEditTexts.get(i).getText().toString();
        }
        return text;
    }

    /**
     * Set the pinview text
     * @param text string
     */
    public void setText(String text){
        short validLength=(short) text.length();
        if(validLength>=getPinCount()) validLength=getPinCount();
        for (int i=0; i<validLength; i++){
            pinEditTexts.get(i).setText(Character.toString(text.charAt(i)));
        }
    }

    /**
     * Total number of pins in the pinview
     * <br> Default is 6
     * @param pinCount integer
     */
    public void setPinCount(int pinCount){
        this.pinCount=(short) pinCount;
        addPins();
    }

    /**
     *
     * <br> Default is Number
     * @param inputType Takes input as InputType.TYPE_NUMBER or InputType.TYPE_TEXT
     */
    public void setInputType(short inputType){
        this.inputType= inputType;
        for (int i=0; i<getPinCount(); i++){
            if(inputType==InputType.TYPE_TEXT) {
                if(isPassword)
                    pinEditTexts.get(i).setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                else
                    pinEditTexts.get(i).setInputType(android.text.InputType.TYPE_CLASS_TEXT);
            }
            else {
                if(isPassword)
                    pinEditTexts.get(i).setInputType(android.text.InputType.TYPE_CLASS_NUMBER| android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                else
                    pinEditTexts.get(i).setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            }
        }
    }

    /**
     * Specifiy if the pinview is of the type password.
     * <br> Default is false
     * @param isPassword either true or false
     */
    public void setPassword(boolean isPassword){
        this.isPassword=isPassword;

        if(isPassword){
            for (int i=0; i<getPinCount(); i++) {
                if (inputType == InputType.TYPE_NUMBER)
                    pinEditTexts.get(i).setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                else
                    pinEditTexts.get(i).setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        }
        else {
            setInputType(inputType);
        }
    }

    /**
     * Sets the drawable background of individual pin
     * <br> Default is null
     * @param backgroundDrawable Drawable to use as the background
     */
    public void setPinBackground(Drawable backgroundDrawable){
        background=backgroundDrawable;
        for (int i=0; i<getPinCount(); i++) {
            pinEditTexts.get(i).setBackground(background);
        }
    }

    /**
     * Default value is 50dp
     * @param sizeInDp Width of the individual pin. Is also applied to height.
     */
    public void setPinSize(int sizeInDp){

        for (int i=0; i<getPinCount(); i++) {
            LinearLayoutCompat.LayoutParams layoutParams=new LinearLayoutCompat.
                LayoutParams(convertDpToPixel(sizeInDp,context),
                    convertDpToPixel(sizeInDp,context)/*,1*/);
            layoutParams.setMargins(getResources().getDimensionPixelSize(R.dimen.margin_pin_edit_text),getResources().getDimensionPixelSize(R.dimen.margin_pin_edit_text),
                    getResources().getDimensionPixelSize(R.dimen.margin_pin_edit_text),getResources().getDimensionPixelSize(R.dimen.margin_pin_edit_text));
            pinEditTexts.get(i).setLayoutParams(layoutParams);
        }
    }

    public void setOnPinCompletionListener(OnPinCompletedListener onPinCompletionListener){
        this.onPinCompletionListener=onPinCompletionListener;
    }

    private int convertDpToPixel(float dp, Context context) {
        return Math.round(dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static void onPinCompleted(String entirePin) {
        onPinCompletionListener.onPinCompleted(entirePin);
    }
}
