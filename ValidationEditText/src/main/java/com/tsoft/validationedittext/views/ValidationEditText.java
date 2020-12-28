package com.tsoft.validationedittext.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.tsoft.validationedittext.utils.PatternValidator;
import com.tsoft.validationedittext.R;


/**
 * Created by Tsur Yohananov on 14/10/2020.
 * <p>
 * A <code>ValidationEditText</code> - View that handles validation ui,<br>
 * showing error nicely with animations, hints, input, colors, etc.<br>
 * Handles validation logic as individual ValidationEditText
 * as well {@link ValidationEditText#validate()}
 * <p>
 *
 * @see com.tsoft.validationedittext.utils.EditTextValidator
 */
public class ValidationEditText extends ConstraintLayout {
    private String mErrorText;
    private String mHint;
    private int mInputType;
    private int mTextSize;
    private int mTextColor;

    private ImmutableTextInputLayout mTextInput;
    private EditText mEditText;
    private ValidationType mValidationType;
    private Listener mListener;
    private int mEtBg;
    private int mTextColorHint;
    private String mPatternRegex;
    private String mText;

    public ValidationEditText(Context context) {
        super(context);
    }

    public ValidationEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.validation_edit_text, this, true);

        mTextInput = findViewById(R.id.til);
        mEditText = findViewById(R.id.et);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.ValidationEditText, 0, 0);

        parseStyledAttributes(typedArray);
        setFont(context, typedArray);
        setWillNotDraw(false);
    }

    private void parseStyledAttributes(TypedArray typedArray) {
        mTextColor = typedArray.getColor(R.styleable.ValidationEditText_text_color,
                getResources().getColor(R.color.black));
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.
                ValidationEditText_android_textSize, 0);
        mHint = typedArray.getString(R.styleable.ValidationEditText_android_hint);
        mInputType = typedArray.getInt(R.styleable.ValidationEditText_android_inputType,
                EditorInfo.TYPE_NULL);
        mErrorText = typedArray.getString(R.styleable.ValidationEditText_error_text);
        mValidationType = ValidationType.values()
                [typedArray.getInt(R.styleable.ValidationEditText_pattern, 0)];
        mEtBg = typedArray.getResourceId(R.styleable.ValidationEditText_input_background,
                -1);
        mTextColorHint = typedArray.getResourceId(
                R.styleable.ValidationEditText_android_textColorHint, -1);
        mText = typedArray.getString(R.styleable.ValidationEditText_android_text);

        parseCustomPattern(typedArray);
    }


    private void parseCustomPattern(TypedArray typedArray) {
        mPatternRegex = typedArray
                .getString(R.styleable.ValidationEditText_custom_pattern);
        if (mPatternRegex != null)
            setPattern(ValidationType.CUSTOM);
    }

    private void setFont(Context context, TypedArray typedArray) {
        int font = typedArray.getResourceId(
                R.styleable.ValidationEditText_android_fontFamily, -1);
        if (font != -1)
            setBothTypeFace(ResourcesCompat.getFont(context, font));
    }

    private void setBothTypeFace(Typeface typeFace) {
        mEditText.setTypeface(typeFace);
        mTextInput.setTypeface(typeFace);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mEditText.setTextColor(mTextColor);
        setEditTextBg();
        setHintColor();
        mTextInput.setHint(mHint);

        setTextSize();
        setInputType();
        setText();
    }

    private void setEditTextBg() {
        if (mEtBg != -1) {
            Drawable drawable = getResources().getDrawable(mEtBg);
            if (drawable != null)
                mEditText.setBackground(drawable);
            else
                mEditText.setBackgroundColor(mEtBg);
        }
    }

    private void setHintColor() {
        mTextInput.setDefaultHintTextColor(mEditText.getHintTextColors());
        if (mTextColorHint != -1) {
            mEditText.setHintTextColor(getResources().getColor(mTextColorHint));
            mTextInput.setHintTextColor(mEditText.getHintTextColors());
        }
    }

    private void setTextSize() {
        if (mTextSize > 0)
            mEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                    mTextSize);
    }

    private void setInputType() {
        if (mInputType != EditorInfo.TYPE_NULL)
            mEditText.setInputType(mInputType);
    }

    private void setText() {
        if (mText != null)
            mEditText.setText(mText);
    }


    /**
     * Use this method to start the validation.
     * Provided {@link Listener} will be notified.
     *
     * <p>
     *
     * @see ValidationEditText#setListener(Listener)
     * </p>
     */
    public void validate() {
        PatternValidator validator = new PatternValidator(mValidationType);
        if (mPatternRegex != null)
            validator = new PatternValidator(mValidationType, mPatternRegex);
        check(validator.validate(mEditText.getText()));
    }

    private void check(boolean valid) {
        if (!valid) {
            mTextInput.setError(mErrorText);
            mTextInput.setErrorEnabled(true);
            if (mListener != null)
                mListener.onFailure(mErrorText);
        } else {
            mTextInput.setError(null);
            mListener.onValidated();
        }
    }

    public void setError(String error) {
        mTextInput.setError(error);
        refresh();
    }

    /**
     * Use this method to set the input {@link ValidationEditText#mEditText}
     * background
     *
     * @param resourceId - reference|color resource id
     */
    public void setInputBackground(int resourceId) {
        mEtBg = resourceId;
        refresh();
    }

    /**
     * Use this method to set the input {@link ValidationEditText#mEditText}
     * background
     *
     * @param color - color int.
     */
    public void setTextColor(int color) {
        mTextColor = color;
        refresh();
    }

    /**
     * Use this method to set the input {@link ValidationEditText#mEditText}
     * background
     *
     * <p>
     * From {@link Color#parseColor(String)}
     * </p>Parse the color string, and return the corresponding color-int.
     * cannot be parsed, throws an IllegalArgumentException
     * pported formats are:</p>
     *
     * <ul>
     *   <li><code>#RRGGBB</code></li>
     *   <li><code>#AARRGGBB</code></li>
     * </ul>
     *
     * <p>The following names are also accepted: <code>red</code>, <code>blue</code>,
     * <code>green</code>, <code>black</code>, <code>white</code>, <code>gray</code>,
     * <code>cyan</code>, <code>magenta</code>, <code>yellow</code>, <code>lightgray</code>,
     * <code>darkgray</code>, <code>grey</code>, <code>lightgrey</code>, <code>darkgrey</code>,
     * <code>aqua</code>, <code>fuchsia</code>, <code>lime</code>, <code>maroon</code>,
     * <code>navy</code>, <code>olive</code>, <code>purple</code>, <code>silver</code>,
     * and <code>teal</code>.</p>
     *
     * @param color - color String.
     * @see Color#parseColor(String)
     */
    public void setTextColor(String color) {
        mTextColor = Color.parseColor(color);
        refresh();
    }

    /**
     * Set the error message to show on validation failure
     */
    public void setErrorText(String text) {
        mErrorText = text;
        refresh();
    }

    /**
     * Set one of the provided pattern {@link ValidationType}
     */
    public void setPattern(ValidationType validationType) {
        mValidationType = validationType;
    }

    /**
     * Use this method to set your own custom regex.
     *
     * @param regex- this string  will compile to {@link java.util.regex.Pattern},
     *               using {@link java.util.regex.Pattern#compile(String)}
     *               This will be the  validation pattern.
     */
    public void setCustomPattern(String regex) {
        mPatternRegex = regex;
        setPattern(ValidationType.CUSTOM);
    }


    /**
     * Use this method to set the wanted input type.
     *
     * @see android.text.InputType
     */
    public void setInputType(int inputType) {
        if (mInputType != EditorInfo.TYPE_NULL)
            mEditText.setInputType(mInputType);
        refresh();
    }

    /**
     * Sets the typeface and style in which the text should be displayed.
     *
     * @see Typeface
     */
    public void setTypeFace(Typeface typeFace) {
        setBothTypeFace(typeFace);
        refresh();
    }

    /**
     * Use this method to set hint text.
     */
    public void setHint(String hint) {
        mHint = hint;
        refresh();
    }

    /**
     * Use this method to set the input {@link ValidationEditText#mEditText}
     * hint text color.
     *
     * @param color - color int.
     */
    public void setHintColor(int color) {
        mTextColorHint = color;
        refresh();
    }

    /**
     * Use this method to set the input {@link ValidationEditText#mEditText}
     * hint text color.
     *
     * @param color - color String.
     * @see Color#parseColor(String)
     */
    public void setHintColor(String color) {
        mTextColorHint = Color.parseColor(color);
        refresh();
    }

    /**
     * Use this method to set the input {@link ValidationEditText#mEditText}
     * text size.
     *
     * @param size - int size in sp
     * @see TypedValue#COMPLEX_UNIT_SP
     */
    public void setTextSize(int size) {
        mTextSize = size;
        refresh();
    }

    /**
     * Use this method to set the input {@link ValidationEditText#mEditText}
     * text.
     *
     * @param text - text to show
     */
    public void setText(String text) {
        mText = text;
        refresh();
    }

    /**
     * Use this to listen to text changes
     *
     * @param watcher - TextWatcher
     * @see EditText#addTextChangedListener(TextWatcher)
     */
    public void addTextChangedListener(TextWatcher watcher) {
        mEditText.addTextChangedListener(watcher);
    }

    public String getText() {
        return mEditText.getText().toString();
    }

    /**
     * Set {@link Listener} to get notified with the validation results
     */
    public void setListener(Listener listener) {
        mListener = listener;
    }

    private void refresh() {
        invalidate();
        requestLayout();
    }

    /**
     * Implement this listener to get notified with validation results
     */
    public interface Listener {
        void onValidated();

        void onFailure(String msg);
    }


    /**
     * Use this enum with {@link ValidationEditText#setPattern(ValidationType)}
     */
    public enum ValidationType {
        EMAIL(0), PASSWORD(1), PHONE(2),
        NAME(3), CUSTOM(4), WEB_URL(5),
        IP_ADDRESS(6), FULL_NAME(7);

        private final Integer mInt;

        ValidationType(Integer i) {
            mInt = i;
        }

        Integer getValue() {
            return mInt;
        }
    }
}
