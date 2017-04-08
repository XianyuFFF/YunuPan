package com.logn.yunupan.activity.record;

import com.logn.yunupan.mvp.BasePresenter;
import com.logn.yunupan.mvp.BaseView;

/**
 * Created by OurEDA on 2017/3/18.
 */

public class RecordContract {

    public interface Presenter extends BasePresenter{}

    public interface View extends BaseView<Presenter> {

    }
}
