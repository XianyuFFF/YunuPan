package com.logn.yunupan.activity.Index;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.logn.yunupan.R;
import com.logn.yunupan.adapter.IndexPageAdapter;
import com.logn.yunupan.utils.StatusBarUtil;
import com.logn.yunupan.views.ViewPagerIndex.view.indicator.Indicator;
import com.logn.yunupan.views.ViewPagerIndex.view.indicator.IndicatorViewPager;
import com.logn.yunupan.views.ViewPagerIndex.view.viewpager.SViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * main page
 */

public class IndexActivity extends AppCompatActivity implements IndexContract.View {
    private static final String TAG = "IndexActivity";


    //viewpager in main page
    private SViewPager indexViewPager;
    //indicator for viewpager of main pager
    private Indicator indexIndicator;
    // combine with indicator and ViewPager
    private IndicatorViewPager indexIndicatorViewPager;


    private IndexContract.Presenter mPresenter;


    public IndexActivity() {
        IndexPresenter presenter = new IndexPresenter(this);
        setPresenter(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.status_bar_color));

        EventBus.getDefault().register(this);

        initView();
    }

    @Subscribe
    public void onEvent(String data) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        indexViewPager = (SViewPager) findViewById(R.id.index_view_pager);
        indexIndicator = (Indicator) findViewById(R.id.index_indicator);
        // 控件链接
        indexIndicatorViewPager = new IndicatorViewPager(indexIndicator, indexViewPager);
        // 初始化接收器
        indexIndicatorViewPager.setAdapter(new IndexPageAdapter(this, getSupportFragmentManager()));
        // 禁止滚动
        indexViewPager.setCanScroll(false);
        // 缓存页数 1
        indexIndicatorViewPager.setPageOffscreenLimit(1);
        // 页面 0
        indexViewPager.setCurrentItem(0);
    }

    @Override
    protected void onStart() {
        super.onStart();


        if (mPresenter == null) mPresenter = new IndexPresenter(this);
        mPresenter.start();
    }

    @Override
    public void setPresenter(IndexContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Context getContext() {
        return this;
    }
}
