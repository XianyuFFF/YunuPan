package com.logn.yunupan.utils.eventbus;

import com.logn.yunupan.entity.greendao.FileInfoBean;

/**
 * Created by OurEDA on 2017/4/8.
 */

public class EventDelRecord {
    private FileInfoBean bean;

    public EventDelRecord() {

    }

    public EventDelRecord(FileInfoBean bean) {
        this.bean = bean;
    }

    public FileInfoBean getBean() {
        return bean;
    }

    public void setBean(FileInfoBean bean) {
        this.bean = bean;
    }
}
