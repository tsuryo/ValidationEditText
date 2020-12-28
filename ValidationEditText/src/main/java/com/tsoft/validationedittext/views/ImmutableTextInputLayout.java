package com.tsoft.validationedittext.views;

import android.content.Context;
import android.graphics.ColorFilter;
import android.os.Handler;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.material.textfield.TextInputLayout;

/**
 * Created by Tsur Yohananov on 14/10/2020.
 */
class ImmutableTextInputLayout extends TextInputLayout {
    public ImmutableTextInputLayout(Context context) {
        super(context);
    }

    public ImmutableTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImmutableTextInputLayout(Context context, AttributeSet attrs,
                                    int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setError(@Nullable CharSequence error) {
        ColorFilter defColorFilter = getBackgroundDefColorFilter();
        super.setError(error);
        updateBgColorFilter(defColorFilter);
        hideErrorTv(error);
    }

    private void updateBgColorFilter(ColorFilter colorFilter) {
        if (getEditText() != null && getEditText().getBackground() != null)
            getEditText().getBackground().setColorFilter(colorFilter);
    }

    private void hideErrorTv(@Nullable CharSequence error) {
        if (error == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setErrorEnabled(false);
                }
            }, 250);
        }
    }

    @Override
    protected void drawableStateChanged() {
        ColorFilter defaultColorFilter = getBackgroundDefColorFilter();
        super.drawableStateChanged();
        updateBgColorFilter(defaultColorFilter);
    }

    @Nullable
    private ColorFilter getBackgroundDefColorFilter() {
        ColorFilter defaultColorFilter = null;
        if (getEditText() != null && getEditText().getBackground() != null)
            defaultColorFilter = DrawableCompat
                    .getColorFilter(getEditText().getBackground());
        return defaultColorFilter;
    }
}

