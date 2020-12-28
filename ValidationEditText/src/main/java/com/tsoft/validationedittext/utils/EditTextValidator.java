package com.tsoft.validationedittext.utils;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tsoft.validationedittext.views.ValidationEditText;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tsur Yohananov on 14/10/2020.
 * <p>
 * This class will handle all the validation process,
 * such as Parsing your xml {@link EditTextValidator#setLayout(Activity, int)},<br>
 * adding ValidationEditTexts {@link EditTextValidator#setEts(ValidationEditText...)},
 * the actual validation {@link EditTextValidator#validate()}, etc.
 * </p>
 */
public class EditTextValidator implements ValidationEditText.Listener {
    private static final String TAG = EditTextValidator.class.getSimpleName();
    private final ValidationListener mListener;
    private List<ValidationEditText> mEts;
    private int mCounter = 0;
    private int mFailedCounter = 0;
    private ValidationEditText mEt;

    public EditTextValidator(ValidationListener listener) {
        mListener = listener;
        mEts = new ArrayList<>();
    }

    /**
     * Use this method to add one {@link ValidationEditText} manually
     */
    public void addEditText(ValidationEditText et) {
        mEts.add(et);
    }

    /**
     * Use this method to add a  {@link List} of {@link ValidationEditText}
     */
    public void setEts(List<ValidationEditText> ets) {
        mEts = ets;
    }

    /**
     * Use this method to add an array of {@link ValidationEditText}
     * E.g - setEts(new ValidationEditText(this), new ValidationEditText(this))
     */
    public void setEts(ValidationEditText... ets) {
        mEts.addAll(Arrays.asList(ets));
    }

    /**
     * This method parses your xml layout and initializing the {@link ValidationEditText}s,
     * Plus adding them all to validation process.
     * <p>
     * Use with activity
     *
     * @param a     the hosting Activity
     * @param resId activity's layout resource id  - e.g  R.layout.activity_main
     */
    public void setLayout(Activity a, int resId) {
        try {
            XmlResourceParser parser = a.getResources().getLayout(resId);
            int event = parser.getEventType();
            do {
                int id = getIdAttributeResourceValue(parser);
                if (a.findViewById(id) instanceof ValidationEditText) {
                    ValidationEditText vEt = a.findViewById(id);
                    addEditText(vEt);
                }
                event = parser.next();
            } while (event != XmlPullParser.END_DOCUMENT);
        } catch (Exception e) {
            if (e.getMessage() != null)
                Log.e(TAG, e.getMessage());
        }
    }

    /**
     * This method parses your xml layout and initializing the {@link ValidationEditText}s,
     * Plus adding them all to validation process.
     * <p>
     * Use with Fragment on{@link
     * androidx.fragment.app.Fragment#onViewCreated(View, Bundle)}
     * <p>
     * Or you can use this method with any inflated {@link View}
     * that contains {@link ValidationEditText}
     *
     * @param v     The view mentioned above
     * @param resId fragment's layout resource id  - e.g  R.layout.fragment
     */
    public void setLayout(View v, int resId) throws XmlPullParserException, IOException {
        XmlResourceParser parser = v.getContext()
                .getResources().getLayout(resId);
        int event = parser.getEventType();
        do {
            int id = getIdAttributeResourceValue(parser);
            if (v.findViewById(id) instanceof ValidationEditText) {
                ValidationEditText vEt = v.findViewById(id);
                addEditText(vEt);
            }
            event = parser.next();
        } while (event != XmlPullParser.END_DOCUMENT);
    }

    /**
     * Use this method to start the validation.
     * All added {@link ValidationEditText}
     * will be addressed.
     * Listener provided in {@link  EditTextValidator#EditTextValidator(ValidationListener)}
     * will  be notified with the results.
     * All the {@link ValidationEditText}s ui will changed subsequently
     */
    public void validate() {
        if (mEts != null && !mEts.isEmpty()) {
            mEt = mEts.get(mCounter);
            mEt.setListener(this);
            mEt.validate();
        }
    }

    @Override
    public void onValidated() {
        mCounter++;
        notifyListener();
    }

    @Override
    public void onFailure(String msg) {
        mCounter++;
        mFailedCounter++;
        notifyListener();
    }

    private void notifyListener() {
        if (mCounter >= mEts.size() && mFailedCounter == 0)
            mListener.onValidated();
        if (mCounter >= mEts.size() && mFailedCounter > 0)
            mListener.onFailedToValidate();
        if (mCounter < mEts.size())
            validate();
        else {
            mCounter = 0;
            mFailedCounter = 0;
        }
    }

    private int getIdAttributeResourceValue(XmlResourceParser parser) {
        final int DEFAULT_RETURN_VALUE = 0;
        for (int i = 0; i < parser.getAttributeCount(); i++) {
            String attrName = parser.getAttributeName(i);
            if ("id".equalsIgnoreCase(attrName)) {
                return parser.getAttributeResourceValue(i, DEFAULT_RETURN_VALUE);
            }
        }
        return DEFAULT_RETURN_VALUE;
    }

    /**
     * Implement this interface to get notified with the results.
     */
    public interface ValidationListener {
        void onValidated();

        void onFailedToValidate();
    }
}
