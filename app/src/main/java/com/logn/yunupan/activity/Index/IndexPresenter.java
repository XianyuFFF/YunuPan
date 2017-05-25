package com.logn.yunupan.activity.Index;

/**
 * Created by OurEDA on 2016/11/20.
 */

public class IndexPresenter implements IndexContract.Presenter {
    private IndexContract.View mView;

    private boolean updateCheck = true;


    public IndexPresenter(IndexContract.View view){
        mView= view;
    }

    @Override
    public void start() {
        if(updateCheck){
            checkUpdate();
        }
    }

    private void checkUpdate(){

    }
}
