package com.logn.yunupan.activity.download;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.logn.yunupan.R;
import com.logn.yunupan.fragment.download.DownloadFragment;
import com.logn.yunupan.fragment.download.DownloadPresenter;
import com.logn.yunupan.utils.ActivityUtils;

/**
 * Created by OurEDA on 2017/3/20.
 */

public class DownloadActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        DownloadFragment fragment = (DownloadFragment) getSupportFragmentManager().findFragmentById(R.id.base_container);

        if (fragment == null) {
            fragment = DownloadFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.base_container);
        }

        DownloadPresenter presenter = new DownloadPresenter(fragment);
    }

}
