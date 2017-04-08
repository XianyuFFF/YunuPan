package com.logn.yunupan.mvp;

import android.content.Context;

/**
 * Created by OurEDA on 2016/11/19.
 */

public interface BaseView<T> {

    void setPresenter(T presenter);

    Context getContext();
}
