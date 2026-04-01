package com.darshan_miskin.www.lib

import android.content.ClipboardManager
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.text.InputFilter.AllCaps
import android.text.InputFilter.LengthFilter
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.ContextMenu
import android.view.Gravity
import android.widget.EditText
import androidx.annotation.ColorInt
import androidx.annotation.ColorLong
import androidx.annotation.ColorRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.darshan_miskin.darshan_miskin.pinview.R
import kotlin.math.roundToInt

class PinView : LinearLayoutCompat {
    private val DEFAULT_PIN_TEXT_SIZE = 23f
    private val DEFAULT_PIN_COUNT = 6
    private val context: Context
    private var pinText: String = ""
    private var isToggleAdded = false
    private var background: Drawable? = null
    private val editTextsArrayList = ArrayList<EditText>()
    private val textWatcherArrayList = ArrayList<PinTextWatcher>()

    constructor(context: Context) : super(context) {
        this.context = context
        this.orientation = HORIZONTAL
        this.gravity = Gravity.CENTER

        addPins()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.context = context
        this.orientation = HORIZONTAL
        this.gravity = Gravity.CENTER

        setStyleAndPins(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this.context = context
        this.orientation = HORIZONTAL
        this.gravity = Gravity.CENTER

        setStyleAndPins(attrs)
    }

    /**
     * Color/Tint to be applied to the toggle view
     * @param color color resource
     */
    @ColorInt
    var passwordToggleColor = resources.getColor(android.R.color.black)
        set(value) {
            if (isToggleAdded) {
                field = value
                val drawable = editTextsArrayList[editTextsArrayList.size - 1].background
                drawable.setColorFilter(field, PorterDuff.Mode.SRC_IN)
                editTextsArrayList[editTextsArrayList.size - 1].background = drawable
            }
        }

    /**
     * Text color of the pins
     * @param pinTextColor
     */
    var pinTextColor = resources.getColor(android.R.color.black)
        set(value) {
            field = value
            for (i in 0..<this.pinCount) {
                editTextsArrayList[i].setTextColor(pinTextColor)
            }
        }
    /**
     * Size of the toggle view
     * @param passwordToggleSize size in pixels
     */
    var passwordToggleSize = resources.getDimensionPixelSize(R.dimen.password_toggle_size)
        set(value) {
            field = value
            if (isToggleAdded) {
                val height = (field - (field * .10)).roundToInt()
                val layoutParams = LayoutParams(
                    field,
                    height /*,1*/
                )
                editTextsArrayList[editTextsArrayList.size - 1].setLayoutParams(layoutParams)
            }
        }

    /**
     * Textsize of the pins
     * @param pinTextSizeSp size in sp
     */
    var pinTextSizeSp = DEFAULT_PIN_TEXT_SIZE
        set(value) {
            field = value
            for (i in 0..<this.pinCount) {
                editTextsArrayList[i].textSize = field
            }
        }

    //    private int pinCursorColor =getResources().getColor(android.R.color.holo_orange_dark);
    /**
     * Specify if the pinview is of the type password.
     * <br></br> Default is false
     * @param isPassword either true or false
     */
    var isPassword = false
        set(value) {
            field = value

            textWatcherArrayList[0].setProcessing(true)
            Log.d("asdf", "isPassword set(): $value \t field: $field")
//            if (value) {
//                for (i in 0..<this.pinCount) {
//                    if (inputType == InputType.TYPE_NUMBER)
//                        editTextsArrayList[i].setInputType(android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD)
//                    else
//                        editTextsArrayList[i].setInputType(android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD)
//                }
//            } else {
                this.inputType = inputType
//            }

            if (isToggleAdded) {
                if (value) {
                    val drawable = resources.getDrawable(R.drawable.ic_show)
                    drawable.setColorFilter(passwordToggleColor, PorterDuff.Mode.SRC_IN)
                    editTextsArrayList[editTextsArrayList.size - 1].background = drawable
                } else {
                    val drawable = resources.getDrawable(R.drawable.ic_hide)
                    drawable.setColorFilter(passwordToggleColor, PorterDuff.Mode.SRC_IN)
                    editTextsArrayList[editTextsArrayList.size - 1].background = drawable
                }
//                field = !field
            }
            textWatcherArrayList[this.pinCount - 1].setProcessing(false)
        }

    /**
     * Enable or disable password toggle
     * @param showPasswordToggle boolean value
     */
    var showPasswordToggle = false
        set(value) {
            field = value

            isPassword = value
            Log.d("asdf", "showPasswordToggle set() \t isPassword: $isPassword")

            if (value) {
//                isPassword = true
//                isPassword = !isPassword

                val editText = EditText(context)
                editText.setFocusable(false)
                editText.setInputType(android.text.InputType.TYPE_NULL)
                val drawable = resources.getDrawable(R.drawable.ic_show)
                drawable.setColorFilter(passwordToggleColor, PorterDuff.Mode.SRC_IN)
                editText.background = drawable
                val height = (passwordToggleSize - (passwordToggleSize * .10)).roundToInt()
                val layoutParams = LayoutParams(
                    passwordToggleSize,
                    height /*,1*/
                )
                layoutParams.setMargins(
                    convertDpToPixel(4f, context),
                    resources.getDimensionPixelSize(R.dimen.margin_pin_edit_text),
                    resources.getDimensionPixelSize(R.dimen.margin_pin_edit_text),
                    resources.getDimensionPixelSize(R.dimen.margin_pin_edit_text)
                )
                editText.setLayoutParams(layoutParams)
                editText.setPadding(4, 4, 4, 4)
                if (!isToggleAdded) {
                    editTextsArrayList.add(editText)

                    editTextsArrayList[this.pinCount.toInt()]
                        .setOnClickListener {
                            Log.d("asdf", "Icon Clicked!")
                            Log.d("asdf", "isPassword: $isPassword")
                            textWatcherArrayList[0].setProcessing(true)
//                            for (i in 0..<this@PinView.pinCount) {
//                                setPasswordType(editTextsArrayList[i])
//                            }
                            this.isPassword = !this.isPassword
                            Log.d("asdf", "isPassword: $isPassword")
                            textWatcherArrayList[this@PinView.pinCount - 1].setProcessing(false)
                            if (isPassword) {
                                val drawable = resources.getDrawable(R.drawable.ic_show)
                                drawable.setColorFilter(passwordToggleColor, PorterDuff.Mode.SRC_IN)
                                editTextsArrayList[editTextsArrayList.size - 1].background = drawable
                            } else {
                                val drawable = resources.getDrawable(R.drawable.ic_hide)
                                drawable.setColorFilter(passwordToggleColor, PorterDuff.Mode.SRC_IN)
                                editTextsArrayList[editTextsArrayList.size - 1].background = drawable
                            }
//                            isPassword = !isPassword
                        }
                    this.addView(editText)
                    isToggleAdded = true
                }
            } else {
                if (editTextsArrayList.size > this.pinCount) {
                    editTextsArrayList.removeAt(this.pinCount.toInt())
                    this.removeViewAt(this.pinCount.toInt())
                    isToggleAdded = false
                }
            }
        }

    /**
     * Total number of pins in the pinview
     * <br></br> Default is 6
     * @param pinCount integer
     */
    var pinCount = DEFAULT_PIN_COUNT.toShort()
        set(value) {
            field = value
            addPins()
        }

    /**
     * Default value is 50dp
     * @param pinSizeDp Width of the individual pin. Is also applied to height.
     */
    var pinSizeDp = resources.getDimensionPixelSize(R.dimen.pin_size)
        set(value) {
            field = value
            for (i in 0..<this.pinCount) {
                val layoutParams = LayoutParams(
                    convertDpToPixel(value.toFloat(), context),
                    convertDpToPixel(value.toFloat(), context) /*,1*/
                )
                layoutParams.setMargins(
                    resources.getDimensionPixelSize(R.dimen.margin_pin_edit_text),
                    resources.getDimensionPixelSize(R.dimen.margin_pin_edit_text),
                    resources.getDimensionPixelSize(R.dimen.margin_pin_edit_text),
                    resources.getDimensionPixelSize(R.dimen.margin_pin_edit_text)
                )
                editTextsArrayList[i].setLayoutParams(layoutParams)
            }
        }

    /**
     *
     * <br></br> Default is Number
     * @param inputType Takes input as InputType.TYPE_NUMBER or InputType.TYPE_TEXT
     */
    var inputType = InputType.TYPE_NUMBER
        set(value) {
            field = value
            Log.d("asdf", "inputType: $inputType \t isPassword: $isPassword")
            for (i in 0..<this.pinCount) {
                if (value == InputType.TYPE_TEXT) {
                    if (isPassword)
                        editTextsArrayList[i].setInputType(android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD)
                    else
                        editTextsArrayList[i].setInputType(android.text.InputType.TYPE_CLASS_TEXT)
                } else {
                    if (isPassword)
                        editTextsArrayList[i].setInputType(android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD)
                    else
                        editTextsArrayList[i].setInputType(android.text.InputType.TYPE_CLASS_NUMBER)
                }
            }
        }

    private fun setStyleAndPins(attrs: AttributeSet?) {
        context.withStyledAttributes(attrs, R.styleable.PinView) {
            pinCount = getInteger(R.styleable.PinView_pinCount, DEFAULT_PIN_COUNT).toShort()
            inputType =
                getInteger(R.styleable.PinView_inputType, InputType.TYPE_TEXT.toInt()).toShort()
            isPassword = getBoolean(R.styleable.PinView_isPassword, false)
            showPasswordToggle = getBoolean(R.styleable.PinView_showPasswordToggle, false)
            pinSizeDp = getDimensionPixelSize(R.styleable.PinView_pinSize, pinSizeDp)

            pinText = getString(R.styleable.PinView_pinText).toString()
            pinTextSizeSp = getDimension(R.styleable.PinView_pinTextSize, DEFAULT_PIN_TEXT_SIZE)
            passwordToggleSize =
                getDimensionPixelSize(R.styleable.PinView_passwordToggleSize, passwordToggleSize)
            passwordToggleColor =
                getColor(R.styleable.PinView_passwordToggleColor, passwordToggleColor)
            pinTextColor = getColor(R.styleable.PinView_textColor, pinTextColor)

            //        pinCursorColor =a.getColor(R.styleable.PinView_cursorColor,pinCursorColor);
            if (hasValue(R.styleable.PinView_pinBackground)) {
                background = getDrawable(R.styleable.PinView_pinBackground)
            }
        }

        addPins()
    }

    override fun onCreateContextMenu(menu: ContextMenu) {
        super.onCreateContextMenu(menu)

        menu.add(0, 0, 0, "Paste")
            .setOnMenuItemClickListener {
                val clipboardManager =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val text = clipboardManager.primaryClip?.getItemAt(0)?.text.toString()
                this@PinView.text = text
                true
            }
    }

    private fun addPins() {
        this.removeAllViews()
        editTextsArrayList.clear()
        for (i in 0..<this.pinCount) {
            val editText = EditText(context)
            editText.setGravity(Gravity.CENTER)
            val layoutParams = LayoutParams(pinSizeDp, pinSizeDp /*,1*/)
            layoutParams.setMargins(
                resources.getDimensionPixelSize(R.dimen.margin_pin_edit_text),
                resources.getDimensionPixelSize(R.dimen.margin_pin_edit_text),
                resources.getDimensionPixelSize(R.dimen.margin_pin_edit_text),
                resources.getDimensionPixelSize(R.dimen.margin_pin_edit_text)
            )
            editText.setLayoutParams(layoutParams)
            editText.textSize = pinTextSizeSp
            editText.setTextColor(pinTextColor)
            //            setCursorColor(editText,pinCursorColor);
            editText.setMaxLines(1)
            editText.setLines(1)
            editText.setPadding(0, 0, 0, 0)
            editText.setFilters(arrayOf(LengthFilter(1), AllCaps()))
            if (background != null) editText.background = background
            setPasswordType(editText)
            editTextsArrayList.add(editText)
            this.addView(editText)
        }
        textWatcherArrayList.clear()
        for (i in 0..<this.pinCount) {
            val pinTextWatcher = PinTextWatcher(i, editTextsArrayList)
            textWatcherArrayList.add(pinTextWatcher)
            editTextsArrayList.get(i).addTextChangedListener(pinTextWatcher)
            editTextsArrayList.get(i).setOnKeyListener(PinOnKeyListener(i, editTextsArrayList))
        }
        isToggleAdded = false

        if (!isInEditMode) this.text = pinText
        showPasswordToggle = showPasswordToggle

        //        requestPinFocus();
    }

    private fun setPasswordType(editText: EditText) {
        Log.d("asdf", "setPasswordType() called")
        Log.d("asdf", "isPassword: $isPassword")
        if (isPassword) {
            if (inputType == InputType.TYPE_TEXT)
                editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD)
            else
                editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD)
        } else {
            if (inputType == InputType.TYPE_TEXT)
                editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT)
            else
                editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER)
        }
    }

    var text: String
        /**
         * 
         * @return Collective text in the pinview
         */
        get() {
            var text = ""
            for (i in 0..<this.pinCount) {
                text += editTextsArrayList[i].getText().toString()
            }
            return text
        }
        /**
         * Set the pinview text
         * @param text string
         */
        set(text) {
            var validLength = text.length.toShort()
            if (validLength >= this.pinCount) validLength = this.pinCount
            for (i in 0..<validLength) {
                editTextsArrayList[i].setText(text.get(i).toString())
            }
        }

    /**
     * Sets the drawable background of individual pin
     * <br></br> Default is null
     * @param backgroundDrawable Drawable to use as the background
     */
    fun setPinBackground(backgroundDrawable: Drawable?) {
        background = backgroundDrawable
        for (i in 0..<this.pinCount) {
            editTextsArrayList[i].background = background
        }
    }

    /**
     * Set a listener to detect when all the pins are filled
     * @param onPinCompletionListener instance of OnPinCompletionListener
     */
    fun setOnPinCompletionListener(onPinCompleted: (entirePin: String) -> Unit) {
        Companion.onPinCompleted = onPinCompleted
    }

    private fun convertDpToPixel(dp: Float, context: Context): Int {
        return (dp * (context.resources
            .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

    /**
     * Requests focus on the pin at index zero
     */
    fun requestPinFocus() {
        editTextsArrayList[0].requestFocus()
    }

    /**
     * Request focus on the pin at specified index, if index is invalid defaults to zeroth pin
     * @param index index of the pin
     */
    fun requestPinFocus(index: Int) {
        if (index > 0 && index < this.pinCount) editTextsArrayList.get(index).requestFocus()
        else editTextsArrayList[0].requestFocus()
    }

    /**
     * Cursor color of the pins
     * @param cursorColor
     */
    //    public void setCursorColor(int cursorColor){
    //        pinCursorColor=cursorColor;
    //        for (int i=0; i<getPinCount(); i++){
    //            setCursorColor(editTextsArrayList.get(i),pinCursorColor);
    //        }
    //    }

    //    private void setCursorColor(EditText view, @ColorInt int color) {
    //        try {
    //            // Get the cursor resource id
    //            Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
    //            field.setAccessible(true);
    //            int drawableResId = field.getInt(view);
    //
    //            // Get the editor
    //            field = TextView.class.getDeclaredField("mEditor");
    //            field.setAccessible(true);
    //            Object editor = field.get(view);
    //
    //            // Get the drawable and set a color filter
    //            Drawable drawable = ContextCompat.getDrawable(view.getContext(), drawableResId);
    //            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    //            Drawable[] drawables = {drawable, drawable};
    //
    //            // Set the drawables
    //            field = editor.getClass().getDeclaredField("mCursorDrawable");
    //            field.setAccessible(true);
    //            field.set(editor, drawables);
    //        } catch (Exception ignored) {
    //        }
    //    }

    companion object {
        @JvmStatic
        var onPinCompleted: ((entirePin: String) -> Unit)? = null
    }
}
