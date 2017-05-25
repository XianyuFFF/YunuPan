package com.logn.yunupan.activity.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.logn.yunupan.R;
import com.logn.yunupan.activity.record.RecordActivity;
import com.logn.yunupan.utils.logger.Logger;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by logn on 2017/3/18.
 */

public class WelcomeActivity extends FragmentActivity {

    @BindView(R.id.wel_background)
    LinearLayout welBack;

    private Context context = WelcomeActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Logger.init();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel_cloud);
        ButterKnife.bind(this);


    }

    @Override
    protected void onStart() {
        super.onStart();

        welBack.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this, RecordActivity.class);
                startActivity(intent);

                WelcomeActivity.this.finish();
            }
        }, 2000);
    }
}
