package com.logn.yunupan.utils.eventbus;

import com.logn.yunupan.entity.greendao.FileDownloadBean;

/**
 * Created by OurEDA on 2017/3/31.
 */

public class EventProgressD {

    private long bytesWritten;
    private long contentLength;
    private boolean done;
    private int position;
    private FileDownloadBean bean;

    public EventProgressD() {

    }

    public EventProgressD(long bytesWritten, long contentLength, boolean done, int position, FileDownloadBean bean) {
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

    public FileDownloadBean getBean() {
        return bean;
    }

    public void setBean(FileDownloadBean bean) {
        this.bean = bean;
    }
}
