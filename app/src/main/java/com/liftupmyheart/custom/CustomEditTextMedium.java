package com.liftupmyheart.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.liftupmyheart.utills.FontCacheUtils;

/**
 * Created by lokeshkumar on 13/03/18.
 */

public class CustomEditTextMedium extends AppCompatEditText {
    public CustomEditTextMedium(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public CustomEditTextMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public CustomEditTextMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomEditTextMedium(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCacheUtils.getTypeface("Oswald-Medium.ttf", context);
        setTypeface(customFont);
    }
}
