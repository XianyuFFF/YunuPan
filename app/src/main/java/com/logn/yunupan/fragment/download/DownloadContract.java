package com.logn.yunupan.fragment.download;

import com.logn.yunupan.entity.greendao.FileDownloadBean;
import com.logn.yunupan.mvp.BasePresenter;
import com.logn.yunupan.mvp.BaseView;

/**
 * Created by OurEDA on 2017/3/21.
 */

public class DownloadContract {

    public interface Presenter extends BasePresenter {
        /**
         * 在要开始上传的时候调用。
         *
         * @param code 文件的提取码
         */
        void start2Prepare(String code);
    }

    public interface View extends BaseView<Presenter> {
        int getPosition(String md5);

        void addItemData(FileDownloadBean bean);

        void updateItem(int position, FileDownloadBean bean);
    }
}
