package com.logn.yunupan.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by OurEDA on 2016/11/22.
 */

public class ToastShort {

    private static Context mContext = null;

    public static void init(Context context) {
        mContext = context;
    }

    public static void showL(String info) {
        if (mContext == null) {
            throw new NullPointerException("u must init first");
        }
        Toast.makeText(mContext, info, Toast.LENGTH_LONG).show();
    }

    public static void showS(String info) {
        if (mContext == null) {
            throw new NullPointerException("u must init first");
        }
        Toast.makeText(mContext, info, Toast.LENGTH_SHORT).show();
    }

    public static void show(Context context, String info) {
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }
}
