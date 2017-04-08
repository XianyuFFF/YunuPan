package com.logn.yunupan.adapter.listener;

import android.view.View;

/**
 * Created by OurEDA on 2017/3/21.
 */

public interface OnRecyclerViewItemClickListener {
    /**
     * @param view
     * @param position 根据position在数据库查询相应数据
     */
    void onItemClick(View view, int position);
}
