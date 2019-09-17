package com.gne.www.lib;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.TypedArray;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.LinearLayoutCompat;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


import java.util.ArrayList;


public class PinView extends LinearLayoutCompat {

    private final float DEFAULT_PIN_TEXT_SIZE=23;
    private final int DEFAULT_PIN_COUNT=6;
    private Context context;
    private String pinText="";
    private int pinSize =getResources().getDimensionPixelSize(R.dimen.pin_size), passwordToggleSize=getResources().getDimensionPixelSize(R.dimen.password_toggle_size);
    private int passwordToggleColor=getResources().getColor(android.R.color.black);
    private float pinTextSize=DEFAULT_PIN_TEXT_SIZE;
    private short pinCount=DEFAULT_PIN_COUNT, inputType= com.gne.www.lib.InputType.TYPE_NUMBER;
    private boolean isPassword=false, showPasswordToggle=false, isToggleAdded=false;
    private Drawable background;
    private ArrayList<EditText> editTextsArrayList =new ArrayList<>();
    private ArrayList<PinTextWatcher> textWatcherArrayList =new ArrayList<>();
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

        setStyleAndPins(attrs);
    }

    public PinView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.CENTER);

        setStyleAndPins(attrs);
    }


    private void setStyleAndPins(AttributeSet attrs){

        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.PinView);
        pinCount=(short)a.getInteger(R.styleable.PinView_pinCount,DEFAULT_PIN_COUNT);
        inputType=(short)a.getInteger(R.styleable.PinView_inputType,InputType.TYPE_TEXT);
        isPassword=a.getBoolean(R.styleable.PinView_isPassword,false);
        showPasswordToggle=a.getBoolean(R.styleable.PinView_showPasswordToggle,false);
        pinSize =a.getDimensionPixelSize(R.styleable.PinView_pinSize,pinSize);

        pinText =a.getString(R.styleable.PinView_pinText);
        pinTextSize =a.getDimension(R.styleable.PinView_pinTextSize,DEFAULT_PIN_TEXT_SIZE);
        passwordToggleSize =a.getDimensionPixelSize(R.styleable.PinView_passwordToggleSize,passwordToggleSize);
        passwordToggleColor =a.getColor(R.styleable.PinView_passwordToggleColor,passwordToggleColor);

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
        editTextsArrayList.clear();
        for (int i=0; i<getPinCount(); i++){
            EditText editText=new EditText(context);
            editText.setGravity(Gravity.CENTER);
            LinearLayoutCompat.LayoutParams layoutParams=new LinearLayoutCompat.
                                        LayoutParams(pinSize, pinSize/*,1*/);
            layoutParams.setMargins(getResources().getDimensionPixelSize(R.dimen.margin_pin_edit_text),getResources().getDimensionPixelSize(R.dimen.margin_pin_edit_text),
                    getResources().getDimensionPixelSize(R.dimen.margin_pin_edit_text),getResources().getDimensionPixelSize(R.dimen.margin_pin_edit_text));
            editText.setLayoutParams(layoutParams);
            editText.setTextSize(pinTextSize);
            editText.setMaxLines(1);
            editText.setLines(1);
            editText.setPadding(0,0,0,0);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1), new InputFilter.AllCaps()});
            if(background!=null)
                editText.setBackground(background);
            setPasswordType(editText);
            editTextsArrayList.add(editText);
            this.addView(editText);
        }
        textWatcherArrayList.clear();
        for (int i = 0; i< getPinCount(); i++){
            PinTextWatcher pinTextWatcher=new PinTextWatcher(i, editTextsArrayList);
            textWatcherArrayList.add(pinTextWatcher);
            editTextsArrayList.get(i).addTextChangedListener(pinTextWatcher);
            editTextsArrayList.get(i).setOnKeyListener(new PinOnKeyListener(i, editTextsArrayList));
        }
        isToggleAdded=false;

        if(!isInEditMode() && pinText!=null)
            setText(pinText);
        setShowPasswordToggle(showPasswordToggle);

//        requestPinFocus();
    }

    private void setPasswordType(EditText editText){
        if(isPassword){
            if(inputType==InputType.TYPE_TEXT)
                editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            else
                editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        }
        else {
            if (inputType == InputType.TYPE_TEXT)
                editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
            else
                editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
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
            text+= editTextsArrayList.get(i).getText().toString();
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
            editTextsArrayList.get(i).setText(Character.toString(text.charAt(i)));
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
                    editTextsArrayList.get(i).setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                else
                    editTextsArrayList.get(i).setInputType(android.text.InputType.TYPE_CLASS_TEXT);
            }
            else {
                if(isPassword)
                    editTextsArrayList.get(i).setInputType(android.text.InputType.TYPE_CLASS_NUMBER| android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                else
                    editTextsArrayList.get(i).setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
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

        textWatcherArrayList.get(0).setProcessing(true);
        if(isPassword){
            for (int i=0; i<getPinCount(); i++) {
                if (inputType == InputType.TYPE_NUMBER)
                    editTextsArrayList.get(i).setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                else
                    editTextsArrayList.get(i).setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        }
        else {
            setInputType(inputType);
        }

        if(isToggleAdded) {
            if (isPassword) {
                Drawable drawable=getResources().getDrawable(R.drawable.ic_show);
                drawable.setColorFilter(passwordToggleColor, PorterDuff.Mode.SRC_IN);
                editTextsArrayList.get(editTextsArrayList.size() - 1).setBackground(drawable);
            } else {
                Drawable drawable=getResources().getDrawable(R.drawable.ic_hide);
                drawable.setColorFilter(passwordToggleColor, PorterDuff.Mode.SRC_IN);
                editTextsArrayList.get(editTextsArrayList.size() - 1).setBackground(drawable);
            }
            this.isPassword=!this.isPassword;

        }
        textWatcherArrayList.get(getPinCount()-1).setProcessing(false);
    }

    /**
     * Sets the drawable background of individual pin
     * <br> Default is null
     * @param backgroundDrawable Drawable to use as the background
     */
    public void setPinBackground(Drawable backgroundDrawable){
        background=backgroundDrawable;
        for (int i=0; i<getPinCount(); i++) {
            editTextsArrayList.get(i).setBackground(background);
        }
    }

    /**
     * Default value is 50dp
     * @param sizeInDp Width of the individual pin. Is also applied to height.
     */
    public void setPinSize(int sizeInDp){
        this.pinSize =sizeInDp;

        for (int i=0; i<getPinCount(); i++) {
            LinearLayoutCompat.LayoutParams layoutParams=new LinearLayoutCompat.
                LayoutParams(convertDpToPixel(sizeInDp,context),
                    convertDpToPixel(sizeInDp,context)/*,1*/);
            layoutParams.setMargins(getResources().getDimensionPixelSize(R.dimen.margin_pin_edit_text),getResources().getDimensionPixelSize(R.dimen.margin_pin_edit_text),
                    getResources().getDimensionPixelSize(R.dimen.margin_pin_edit_text),getResources().getDimensionPixelSize(R.dimen.margin_pin_edit_text));
            editTextsArrayList.get(i).setLayoutParams(layoutParams);
        }
    }

    /**
     * Enable or disable password toggle
     * @param showPasswordToggle boolean value
     */
    public void setShowPasswordToggle(boolean showPasswordToggle){
        this.showPasswordToggle=showPasswordToggle;

        if(showPasswordToggle)isPassword=showPasswordToggle;

        if(showPasswordToggle){

            setPassword(isPassword);
            isPassword=!isPassword;

            EditText editText=new EditText(context);
            editText.setFocusable(false);
            editText.setInputType(android.text.InputType.TYPE_NULL);
            Drawable drawable=getResources().getDrawable(R.drawable.ic_show);
            drawable.setColorFilter(passwordToggleColor, PorterDuff.Mode.SRC_IN);
            editText.setBackground(drawable);
            int height=(int)Math.round(passwordToggleSize-(passwordToggleSize*.10));
            LinearLayoutCompat.LayoutParams layoutParams=new LinearLayoutCompat.
                    LayoutParams(passwordToggleSize,
                    height/*,1*/);
            layoutParams.setMargins(convertDpToPixel(4,context),getResources().getDimensionPixelSize(R.dimen.margin_pin_edit_text),
                    getResources().getDimensionPixelSize(R.dimen.margin_pin_edit_text),getResources().getDimensionPixelSize(R.dimen.margin_pin_edit_text));
            editText.setLayoutParams(layoutParams);
            editText.setPadding(4,4,4,4);
            if(!isToggleAdded) {
                editTextsArrayList.add(editText);

                editTextsArrayList.get(getPinCount()).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textWatcherArrayList.get(0).setProcessing(true);
                        for (int i = 0; i < getPinCount(); i++) {
                            setPasswordType(editTextsArrayList.get(i));
                        }
                        textWatcherArrayList.get(getPinCount() - 1).setProcessing(false);
                        if (isPassword) {
                            Drawable drawable=getResources().getDrawable(R.drawable.ic_show);
                            drawable.setColorFilter(passwordToggleColor, PorterDuff.Mode.SRC_IN);
                            editTextsArrayList.get(editTextsArrayList.size() - 1).setBackground(drawable);
                        } else {
                            Drawable drawable=getResources().getDrawable(R.drawable.ic_hide);
                            drawable.setColorFilter(passwordToggleColor, PorterDuff.Mode.SRC_IN);
                            editTextsArrayList.get(editTextsArrayList.size() - 1).setBackground(drawable);
                        }
                        isPassword = !isPassword;
                    }
                });
                this.addView(editText);
                isToggleAdded=true;
            }
        }
        else {
            if(editTextsArrayList.size()>getPinCount()) {
                editTextsArrayList.remove(getPinCount());
                this.removeViewAt(getPinCount());
                isToggleAdded=false;
            }
        }
    }

    /**
     * Set a listener to detect when all the pins are filled
     * @param onPinCompletionListener instance of OnPinCompletionListener
     */
    public void setOnPinCompletionListener(OnPinCompletedListener onPinCompletionListener){
        this.onPinCompletionListener=onPinCompletionListener;
    }

    private int convertDpToPixel(float dp, Context context) {
        return Math.round(dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    static void onPinCompleted(String entirePin) {
        onPinCompletionListener.onPinCompleted(entirePin);
    }

    /**
     * Requests focus on the pin at index zero
     */
    public void requestPinFocus(){
        editTextsArrayList.get(0).requestFocus();
    }

    /**
     * Request focus on the pin at specified index, if index is invalid defaults to zeroth pin
     * @param index index of the pin
     */
    public void requestPinFocus(int index){
        if(index>0 && index<getPinCount())
            editTextsArrayList.get(index).requestFocus();
        else
            editTextsArrayList.get(0).requestFocus();
    }

    /**
     * Textsize of the pins
     * @param textSize size in sp
     */
    public void setPinTextSize(float textSize){
        pinTextSize=textSize;
        for (int i=0; i<getPinCount(); i++){
            editTextsArrayList.get(i).setTextSize(pinTextSize);
        }
    }

    /**
     * Size of the toggle view
     * @param toggleSize size in pixels
     */
    public void setPasswordToggleSize(int toggleSize){
        if(isToggleAdded) {
            passwordToggleSize=passwordToggleSize;
            int height = (int) Math.round(passwordToggleSize - (passwordToggleSize * .10));
            LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.
                    LayoutParams(passwordToggleSize,
                    height/*,1*/);
            editTextsArrayList.get(editTextsArrayList.size() - 1).setLayoutParams(layoutParams);
        }
    }

    /**
     * Color/Tint to be applied to the toggle view
     * @param color color resource
     */
    public void setPasswordToggleColor(int color){
        if(isToggleAdded) {
            passwordToggleColor=color;
            Drawable drawable= editTextsArrayList.get(editTextsArrayList.size() - 1).getBackground();
            drawable.setColorFilter(passwordToggleColor, PorterDuff.Mode.SRC_IN);
            editTextsArrayList.get(editTextsArrayList.size() - 1).setBackground(drawable);
        }
    }


}
