package com.example.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.example.utills.FontCacheUtils;

/**
 * Created by kittybee on 28/07/17.
 */

public class ButtonMediumText extends AppCompatButton {

    public ButtonMediumText(Context context) {
        super(context);
    }

    public ButtonMediumText(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public ButtonMediumText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCacheUtils.getTypeface("Oswald-Medium.ttf", context);
        setTypeface(customFont);
    }
}