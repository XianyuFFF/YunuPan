package com.logn.yunupan.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.logn.yunupan.utils.eventbus.EventBusInstance;
import com.logn.yunupan.utils.eventbus.EventProgressD;
import com.logn.yunupan.utils.greendao.DBManagerD;
import com.logn.yunupan.utils.greendao.DBManagerR;
import com.logn.yunupan.utils.greendao.DBManagerU;
import com.logn.yunupan.utils.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 用于监听上传下载进度，实时更新数据库。
 * Created by OurEDA on 2017/3/31.
 */

public class YunUPanService extends Service {
    private DBManagerR dbr;
    private DBManagerU dbu;
    private DBManagerD dbd;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Logger.e("弱弱的问，能看到我吗？");
        super.onStart(intent, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.e("服务开启");
        dbr = DBManagerR.getInstance(this.getApplicationContext());
        dbu = DBManagerU.getInstance(this.getApplicationContext());
        dbd = DBManagerD.getInstance(this.getApplicationContext());

        EventBusInstance.getBusInstance().register(this);
    }

    @Override
    public void onDestroy() {
        EventBusInstance.getBusInstance().unregister(this);
        super.onDestroy();
        stopSelf();
    }

    ///////////////////////////////////////////////////////////////////////////
    // 上传监听区
    ///////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////
    // 下载监听区
    ///////////////////////////////////////////////////////////////////////////

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getDownloadProgress(EventProgressD event) {

    }

    public void test(String totalBytes) {
        Log.e("download:->", "" + totalBytes);
    }
}
