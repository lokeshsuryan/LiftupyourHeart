package com.liftupmyheart.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.liftupmyheart.utills.FontCacheUtils;

/**
 * Created by Lokesh Kumar on 13/03/18.
 */

public class CustomTextViewBlack extends AppCompatTextView {

    public CustomTextViewBlack(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public CustomTextViewBlack(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public CustomTextViewBlack(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomTextViewBlack(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCacheUtils.getTypeface("Oswald-Light.ttf", context);
        setTypeface(customFont);
    }
}