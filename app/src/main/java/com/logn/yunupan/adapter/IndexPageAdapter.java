package com.logn.yunupan.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.logn.yunupan.R;
import com.logn.yunupan.fragment.download.DownloadFragment;
import com.logn.yunupan.fragment.upload.UploadFragment;
import com.logn.yunupan.views.ViewPagerIndex.view.indicator.IndicatorViewPager;

public class IndexPageAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
    private String[] tableNames = {"上传", "下载"};

    private int[] tabIcons = {R.drawable.icon_upload_cloud_16, R.drawable.icon_download_cloud};
    private LayoutInflater inflater;

    private Fragment[] pages;

    public IndexPageAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.pages = new Fragment[tableNames.length];
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return tableNames.length;
    }

    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_tab, container, false);
        }
        TextView textView = (TextView) convertView;
        textView.setText(tableNames[position]);
        textView.setTextSize(28);
        return convertView;
    }

    @Override
    public Fragment getFragmentForPage(int position) {
        if (pages[position] == null) {
            switch (position) {
                case 0: {
                    UploadFragment uploadFragment = UploadFragment.newInstance();
                    pages[position] = uploadFragment;
                    return uploadFragment;
                }
                case 1: {
                    DownloadFragment uploadFragment = DownloadFragment.newInstance();
                    pages[position] = uploadFragment;
                    return uploadFragment;
                }
            }
        }
        return pages[position];
    }
}
