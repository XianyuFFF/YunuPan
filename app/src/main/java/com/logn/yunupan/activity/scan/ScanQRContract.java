package com.logn.yunupan.activity.scan;/**
 * Created by liufengkai on 16/9/25.
 */

import com.logn.yunupan.mvp.BasePresenter;
import com.logn.yunupan.mvp.BaseView;

/**
 * Created by liufengkai on 16/8/16.
 */
class ScanQRContract {

    interface View extends BaseView<Presenter> {

        void intent2Others(String msg);

        void onFailed();
    }

    interface Presenter extends BasePresenter {

        void getSubscribeInfo(String id);

        void getSamplePersonInfo(String id);

        void getClassInfo(String id, String courseNum);
    }

}
