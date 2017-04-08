package com.logn.yunupan.fragment.download;

import com.logn.yunupan.module.download.DownloadModule;
import com.logn.yunupan.utils.logger.Logger;

/**
 * Created by OurEDA on 2017/3/21.
 */

public class DownloadPresenter implements DownloadContract.Presenter {

    private DownloadContract.View mView;
    private DownloadModule module;

    public DownloadPresenter(DownloadContract.View view) {
        mView = view;
        module = new DownloadModule(mView.getContext());
    }

    @Override
    public void start() {

    }

    @Override
    public void start2Prepare(String code) {
        Logger.e("下载前获取的提取码：-》 " + code);
        module.checkForDownload(code);
    }
}
