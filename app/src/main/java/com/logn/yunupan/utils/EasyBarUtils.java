package com.logn.yunupan.utils;

import android.app.Activity;
import android.content.Context;

import com.example.easybar.EasyBar;
import com.logn.yunupan.R;

/**
 * Created by qjm3662 on 2017/2/21 0021.
 */

public class EasyBarUtils {
    /**
     * 设置EasyBar的标题以及设置返回监听
     *  @param title
     * @param activity
     * @param flag     1->back  2->finish
     */
    public static EasyBar justSetTitleAndBack(String title, final Context activity, final int flag) {
        final Activity ac;
        if (activity instanceof Activity) {
            ac = (Activity) activity;
        } else {
            return null;
        }
        EasyBar easyBar = (EasyBar) ac.findViewById(R.id.easyBar);
        easyBar.setTitle(title);
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick() {
                if (flag == 1) {
                    ac.onBackPressed();
                } else {
                    ac.finish();
                }
            }

            @Override
            public void onRightIconClick() {

            }
        });
        return easyBar;
    }

}
