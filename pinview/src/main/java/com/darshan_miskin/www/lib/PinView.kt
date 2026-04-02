package com.darshan_miskin.www.lib

import android.content.ClipboardManager
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.ContextMenu
import android.view.Gravity
import android.widget.EditText
import androidx.annotation.ColorInt
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.withStyledAttributes
import com.darshan_miskin.darshan_miskin.pinview.R
import kotlin.math.roundToInt

class PinView : LinearLayoutCompat {

    internal companion object {
        internal const val DEFAULT_PIN_TEXT_SIZE = 23f
        internal const val DEFAULT_PIN_COUNT: Short = 4
    }

    private val context: Context
    private var pinText: String = ""
    private var isToggleAdded = false
    private val editTextsArrayList = ArrayList<EditText>()
    private val textWatcherArrayList = ArrayList<PinTextWatcher>()
    private var onPinCompletionListener: OnPinCompletionListener = OnPinCompletionListener {}
    private var onPinChangedListener: OnPinChangedListener = OnPinChangedListener {}

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

    private fun setStyleAndPins(attrs: AttributeSet?) {
        context.withStyledAttributes(attrs, R.styleable.PinView) {
            pinCount = getInteger(R.styleable.PinView_pinCount, DEFAULT_PIN_COUNT.toInt()).toShort()
            inputType =
                InputType.entries.toTypedArray()[getInteger(
                    R.styleable.PinView_inputType,
                    InputType.TEXT.id.toInt()
                )]
            isPassword = getBoolean(R.styleable.PinView_isPassword, false)
            showPasswordToggle = getBoolean(R.styleable.PinView_showPasswordToggle, false)
            pinSizeDp = getDimensionPixelSize(R.styleable.PinView_pinSize, pinSizeDp)

            pinText = getString(R.styleable.PinView_pinText).toString()
            pinTextSizeSp = getDimension(R.styleable.PinView_pinTextSize, DEFAULT_PIN_TEXT_SIZE)
            passwordToggleSizeDp =
                getDimensionPixelSize(R.styleable.PinView_passwordToggleSize, passwordToggleSizeDp)
            passwordToggleColor =
                getColor(R.styleable.PinView_passwordToggleColor, passwordToggleColor)
            pinTextColor = getColor(R.styleable.PinView_textColor, pinTextColor)

//            pinCursorColor =a.getColor(R.styleable.PinView_cursorColor,pinCursorColor);
            if (hasValue(R.styleable.PinView_pinBackground)) {
                pinBackground = getDrawable(R.styleable.PinView_pinBackground)
            }
        }

        addPins()
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
            editText.setFilters(arrayOf(LengthFilter(1)))
            if (pinBackground != null) editText.background = pinBackground
            setPasswordType(editText)
            editTextsArrayList.add(editText)
            this.addView(editText)
        }
        textWatcherArrayList.clear()
        for (i in 0..<this.pinCount) {
            val pinTextWatcher = PinTextWatcher(i, editTextsArrayList)
            textWatcherArrayList.add(pinTextWatcher)
            editTextsArrayList[i].addTextChangedListener(pinTextWatcher)
            editTextsArrayList[i].setOnKeyListener(PinOnKeyListener(i, editTextsArrayList))
        }
        isToggleAdded = false

        if (!isInEditMode) this.text = pinText
        showPasswordToggle = showPasswordToggle

//        requestPinFocus();
    }

    /**
     * Color/Tint to be applied to the toggle view. Default is black.
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
     * Text color of the pins.
     */
    var pinTextColor = resources.getColor(android.R.color.system_accent3_600)
        set(value) {
            field = value
            for (i in 0..<this.pinCount) {
                editTextsArrayList[i].setTextColor(pinTextColor)
            }
        }

    /**
     * Size of the toggle view in dp.
     */
    var passwordToggleSizeDp = resources.getDimensionPixelSize(R.dimen.password_toggle_size)
        set(value) {
            field = value
            if (isToggleAdded) {
                val layoutParams = LayoutParams(
                    convertDpToPixel(value.toFloat(), context),
                    convertDpToPixel(value.toFloat(), context),
                    /*,1*/
                )
                layoutParams.setMargins(
                    resources.getDimensionPixelSize(R.dimen.margin_pin_edit_text),
                    resources.getDimensionPixelSize(R.dimen.margin_pin_edit_text),
                    resources.getDimensionPixelSize(R.dimen.margin_pin_edit_text),
                    resources.getDimensionPixelSize(R.dimen.margin_pin_edit_text)
                )
                editTextsArrayList[editTextsArrayList.size - 1].setLayoutParams(layoutParams)
            }
        }

    /**
     * Text size of the pins in sp.
     */
    var pinTextSizeSp = DEFAULT_PIN_TEXT_SIZE
        set(value) {
            field = value
            for (i in 0..<this.pinCount) {
                editTextsArrayList[i].textSize = field
            }
        }

    /**
     * Specify if the pinview is of the type password. Default value is false.
     *
     * Setting a value to this variable is independent of [showPasswordToggle]. However, setting a value to [showPasswordToggle] updates this variable.
     *
     * @param isPassword either true or false
     */
    var isPassword = false
        set(value) {
            field = value

            textWatcherArrayList[0].setProcessing(true)
//            if (value) {
//                for (i in 0..<this.pinCount) {
//                    if (inputType == InputType.NUMBER)
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
            }
            textWatcherArrayList[this.pinCount - 1].setProcessing(false)
        }

    /**
     * Enable or disable password toggle. Default value is false.
     *
     * Enabling this, simultaneously also enables [isPassword]
     *
     * @param showPasswordToggle boolean value
     */
    var showPasswordToggle = false
        set(value) {
            field = value

            isPassword = value

            if (value) {
//                isPassword = true
//                isPassword = !isPassword

                val editText = EditText(context)
                editText.setFocusable(false)
                editText.setInputType(android.text.InputType.TYPE_NULL)
                val drawable = resources.getDrawable(R.drawable.ic_show)
                drawable.setColorFilter(passwordToggleColor, PorterDuff.Mode.SRC_IN)
                editText.background = drawable
                val layoutParams = LayoutParams(
                    passwordToggleSizeDp,
                    passwordToggleSizeDp /*,1*/
                )
                layoutParams.setMargins(
                    resources.getDimensionPixelSize(R.dimen.margin_pin_edit_text),
                    resources.getDimensionPixelSize(R.dimen.margin_pin_edit_text),
                    resources.getDimensionPixelSize(R.dimen.margin_pin_edit_text),
                    resources.getDimensionPixelSize(R.dimen.margin_pin_edit_text)
                )
                editText.setLayoutParams(layoutParams)
                editText.setPadding(4, 4, 4, 4)
                if (!isToggleAdded && showPasswordToggle) {
                    editTextsArrayList.add(editText)

                    editTextsArrayList[this.pinCount.toInt()]
                        .setOnClickListener {
                            textWatcherArrayList[0].setProcessing(true)
                            this.isPassword = !this.isPassword
                            textWatcherArrayList[this@PinView.pinCount - 1].setProcessing(false)
                            if (isPassword) {
                                val drawable = resources.getDrawable(R.drawable.ic_show)
                                drawable.setColorFilter(passwordToggleColor, PorterDuff.Mode.SRC_IN)
                                editTextsArrayList[editTextsArrayList.size - 1].background =
                                    drawable
                            } else {
                                val drawable = resources.getDrawable(R.drawable.ic_hide)
                                drawable.setColorFilter(passwordToggleColor, PorterDuff.Mode.SRC_IN)
                                editTextsArrayList[editTextsArrayList.size - 1].background =
                                    drawable
                            }
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
     * Total number of pins in the pinview. Default is 4.
     */
    var pinCount = DEFAULT_PIN_COUNT
        set(value) {
            field = value
            addPins()
        }

    /**
     * Width of the individual pin in dp. Is also applied to height. Default value is 50dp.
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
     * Set the input type of the pinview. Default is [InputType.NUMBER].
     *
     * Can either be [InputType.NUMBER] or [InputType.TEXT]
     */
    var inputType = InputType.NUMBER
        set(value) {
            field = value
            for (i in 0..<this.pinCount) {
                if (value == InputType.TEXT) {
                    if (isPassword)
                        editTextsArrayList[i].setInputType(android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD)
                    else
                        editTextsArrayList[i].setInputType(android.text.InputType.TYPE_CLASS_TEXT)
                    editTextsArrayList[i].filters = arrayOf(
                        LengthFilter(1),
                        InputFilter { source, start, end, dest, dstart, dend ->
                            for (i in start until end) {
                                if (Character.isDigit(source[i])) {
                                    return@InputFilter ""
                                }
                            }
                            null
                        }
                    )
                } else {
                    if (isPassword)
                        editTextsArrayList[i].setInputType(android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD)
                    else
                        editTextsArrayList[i].setInputType(android.text.InputType.TYPE_CLASS_NUMBER)

                    editTextsArrayList[i].filters = arrayOf(
                        LengthFilter(1),
                        InputFilter { source, start, end, dest, dstart, dend ->
                            for (i in start until end) {
                                if (Character.isLetter(source[i])) {
                                    return@InputFilter ""
                                }
                            }
                            null
                        }
                    )
                }
            }
        }

    override fun onCreateContextMenu(menu: ContextMenu) {
        super.onCreateContextMenu(menu)

        menu.add(0, 0, 0, "Paste")
            .setOnMenuItemClickListener {
                val clipboardManager =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val text: CharSequence? = clipboardManager.primaryClip?.getItemAt(0)?.text
                if(text!=null && text.trim().isNotEmpty()) this@PinView.text = text.toString()
                true
            }
    }

    private fun setPasswordType(editText: EditText) {
        if (isPassword) {
            if (inputType == InputType.TEXT)
                editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD)
            else
                editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD)
        } else {
            if (inputType == InputType.TEXT)
                editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT)
            else
                editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER)
        }
    }

    /**
     * Set the pinview text
     *
     * @return Collective text in the pinview
     */
    var text: String
        get() {
            var text = ""
            for (i in 0..<this.pinCount) {
                text += editTextsArrayList[i].getText().toString()
            }
            return text
        }
        set(text) {
            if (text.trim().isEmpty()) return
            var validLength = text.length.toShort()
            if (validLength >= this.pinCount) validLength = this.pinCount
            for (i in 0..<validLength) {
                editTextsArrayList[i].setText(text[i].toString())
            }
        }

    /**
     * Sets the drawable background of individual pin.
     *
     * If no value is assigned, pins will have default [EditText] or [androidx.compose.material3.TextField] background accordingly.
     */
    var pinBackground: Drawable? = null
        set(value) {
            field = value
            for (i in 0..<this.pinCount) {
                editTextsArrayList[i].background = pinBackground
            }
        }

    private fun convertDpToPixel(dp: Float, context: Context): Int {
        return (dp * (context.resources
            .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

//    /**
//     * Requests focus on the pin at index zero
//     */
//    fun requestPinFocus() {
//        editTextsArrayList[0].requestFocus()
//    }

    /**
     * Request focus on the pin at specified index.
     *
     * If index is invalid defaults to zero.
     * @param index index of the pin
     */
    fun requestPinFocusAt(index: Int) {
        if (index > 0 && index < this.pinCount) editTextsArrayList[index].requestFocus()
        else editTextsArrayList[0].requestFocus()
    }

//    private int pinCursorColor =getResources().getColor(android.R.color.holo_orange_dark);
//    /**
//     * Cursor color of the pins
//     * @param cursorColor
//     */
//    public void setCursorColor(int cursorColor){
//        pinCursorColor=cursorColor;
//        for (int i=0; i<getPinCount(); i++){
//            setCursorColor(editTextsArrayList.get(i),pinCursorColor);
//        }
//    }
//
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

    /**
     * Set a listener to get notified when all the pins are entered
     * @param onPinCompletionListener instance of [OnPinCompletionListener]
     */
    fun setOnPinCompletionListener(onPinCompletionListener: OnPinCompletionListener) {
        this.onPinCompletionListener = onPinCompletionListener
        textWatcherArrayList.forEach {
            it.setOnPinCompletionListener(this.onPinCompletionListener)
        }
    }

    /**
     * Set a listener to get notified when the pin changes.
     * @param onPinChangedListener instance of [OnPinChangedListener]
     */
    fun setOnPinChangedListener(onPinChangedListener: OnPinChangedListener) {
        this.onPinChangedListener = onPinChangedListener
        textWatcherArrayList.forEach {
            it.setOnPinChangedListener(this.onPinChangedListener)
        }
    }
}
