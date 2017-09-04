package com.shikshitha.admin.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Vinay on 03-09-2017.
 */

public class Conversion {

    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
