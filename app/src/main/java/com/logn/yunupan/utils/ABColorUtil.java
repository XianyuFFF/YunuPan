package com.logn.yunupan.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;

public class ABColorUtil {
    /**
     * 颜色加深处理
     *
     * @param RGBValues RGB的值，由alpha（透明度）、red（红）、green（绿）、blue（蓝）构成，
     *                  Android中我们一般使用它的16进制，
     *                  例如："#FFAABBCC",最左边到最右每两个字母就是代表alpha（透明度）、
     *                  red（红）、green（绿）、blue（蓝）。每种颜色值占一个字节(8位)，值域0~255
     *                  所以下面使用移位的方法可以得到每种颜色的值，然后每种颜色值减小一下，在合成RGB颜色，颜色就会看起来深一些了
     * @return
     */
    public static int colorBurn(int RGBValues) {
        int alpha = RGBValues >> 24;
        int red = RGBValues >> 16 & 0xFF;
        int green = RGBValues >> 8 & 0xFF;
        int blue = RGBValues & 0xFF;

        red = (int) Math.floor(red * (1 - 0.1));
        green = (int) Math.floor(green * (1 - 0.1));
        blue = (int) Math.floor(blue * (1 - 0.1));

        return Color.argb(alpha, red, green, blue);
    }

    /**
     * 生成Selector的Drawable
     *
     * @param context         context
     * @param colorRes        unpressed Color
     * @param pressedColorRes pressed Color
     * @param cornerRadius    conner R dp !!!!
     * @return Drawable
     */
    public static StateListDrawable listDrawable(Context context,
                                                 @ColorRes int colorRes, @ColorRes int pressedColorRes, int cornerRadius) {
        StateListDrawable drawable = new StateListDrawable();
        GradientDrawable simpleBackGround = new GradientDrawable();
        GradientDrawable pressedBackGround = new GradientDrawable();

        simpleBackGround.setShape(GradientDrawable.RECTANGLE);
        simpleBackGround.setCornerRadius(DisplayUtils.dip2px(context, cornerRadius));
        simpleBackGround.setColor(context.getResources().getColor(colorRes));

        pressedBackGround.setShape(GradientDrawable.RECTANGLE);
        pressedBackGround.setCornerRadius(DisplayUtils.dip2px(context, cornerRadius));
        pressedBackGround.setColor(context.getResources().getColor(pressedColorRes));

        drawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed}, pressedBackGround);
        drawable.addState(new int[]{android.R.attr.focusable}, simpleBackGround);
        drawable.addState(new int[]{}, simpleBackGround);
        return drawable;
    }

    public static StateListDrawable selectorDrawable(Context context,
                                                     @ColorInt int color,
                                                     @ColorInt int pressedColor) {
        StateListDrawable drawable = new StateListDrawable();
        GradientDrawable simpleBackGround = new GradientDrawable();
        GradientDrawable pressedBackGround = new GradientDrawable();

        simpleBackGround.setShape(GradientDrawable.RECTANGLE);
        simpleBackGround.setColor(color);

        pressedBackGround.setShape(GradientDrawable.RECTANGLE);
        pressedBackGround.setColor(pressedColor);

        drawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed}, pressedBackGround);
        drawable.addState(new int[]{android.R.attr.focusable}, simpleBackGround);
        drawable.addState(new int[]{}, simpleBackGround);
        return drawable;
    }
}