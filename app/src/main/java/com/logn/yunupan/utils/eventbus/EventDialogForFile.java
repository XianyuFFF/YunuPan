package com.logn.yunupan.utils.eventbus;

import com.logn.yunupan.entity.greendao.FileInfoBean;

/**
 * Created by OurEDA on 2017/4/9.
 */

public class EventDialogForFile {
    private FileInfoBean bean;

    public EventDialogForFile() {
    }

    public EventDialogForFile(FileInfoBean bean) {
        this.bean = bean;
    }

    public FileInfoBean getBean() {
        return bean;
    }

    public void setBean(FileInfoBean bean) {
        this.bean = bean;
    }
}
