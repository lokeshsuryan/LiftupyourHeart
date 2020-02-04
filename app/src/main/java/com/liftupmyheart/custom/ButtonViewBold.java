package com.liftupmyheart.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.liftupmyheart.utills.FontCacheUtils;

/**
 * Created by lokeshkumar on 13/03/18.
 */

public class ButtonViewBold extends AppCompatButton {
    public ButtonViewBold(Context context) {
        super(context);
    }

    public ButtonViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public ButtonViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCacheUtils.getTypeface("Oswald-DemiBold.ttf", context);
        setTypeface(customFont);
    }
}
