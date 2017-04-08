package com.logn.yunupan.activity.upload;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.logn.yunupan.R;
import com.logn.yunupan.activity.record.RecordPresenter;
import com.logn.yunupan.fragment.upload.UploadFragment;
import com.logn.yunupan.fragment.upload.UploadPresenter;
import com.logn.yunupan.utils.ActivityUtils;

/**
 * Created by OurEDA on 2017/3/20.
 */

public class UploadActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        UploadFragment fragment = (UploadFragment) getSupportFragmentManager().findFragmentById(R.id.base_container);

        if (fragment == null) {
            fragment = UploadFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.base_container);
        }

        UploadPresenter presenter = new UploadPresenter(fragment);
    }
}
