package com.example.utills;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by Dhaval Riontech on 6/8/16.
 */
public class FontCacheUtils {
    private static HashMap<String, Typeface> fontCache = new HashMap<>();
    private final String TAG = FontCacheUtils.class.getSimpleName();

    public static Typeface getTypeface(String fontname, Context context) {
        Typeface typeface = null;
        try {
            typeface = fontCache.get(fontname);

            if (typeface == null) {
                try {
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontname);
                } catch (Exception e) {
                    return null;
                }

                fontCache.put(fontname, typeface);
            }
        } catch (Exception e) {

        }

        return typeface;
    }
}
