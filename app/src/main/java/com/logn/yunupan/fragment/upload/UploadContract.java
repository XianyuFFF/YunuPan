package com.logn.yunupan.fragment.upload;

import com.logn.yunupan.entity.greendao.FileUploadBean;
import com.logn.yunupan.mvp.BasePresenter;
import com.logn.yunupan.mvp.BaseView;

/**
 * Created by OurEDA on 2017/3/20.
 */

public class UploadContract {
    public interface Presenter extends BasePresenter {
        /**
         * 在要开始上传的时候调用。
         *
         * @param path 文件的路径
         */
        void start2Prepare(String path);
    }

    public interface View extends BaseView<Presenter> {
        int getPosition(String md5);

        void addItemData(FileUploadBean bean);

        void updateItem(int position, FileUploadBean bean);
    }
}
