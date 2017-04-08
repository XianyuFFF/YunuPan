package com.logn.yunupan.utils.eventbus;

import com.logn.yunupan.entity.greendao.FileUploadBean;

/**
 * Created by OurEDA on 2017/3/29.
 */

public class EventProgressU {
    private long bytesWritten;
    private long contentLength;
    private boolean done;
    private int position;
    private FileUploadBean bean;

    public EventProgressU() {

    }

    public EventProgressU(long bytesWritten, long contentLength, boolean done, int position, FileUploadBean bean) {
        setBytesWritten(bytesWritten);
        setContentLength(contentLength);
        setDone(done);
        setPosition(position);
        setBean(bean);
    }

    public long getBytesWritten() {
        return bytesWritten;
    }

    public void setBytesWritten(long bytesWritten) {
        this.bytesWritten = bytesWritten;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public FileUploadBean getBean() {
        return bean;
    }

    public void setBean(FileUploadBean bean) {
        this.bean = bean;
    }
}
