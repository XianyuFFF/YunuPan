package com.logn.yunupan.utils.eventbus;

/**
 * Created by OurEDA on 2017/4/6.
 */

public class EventFailedToUpload {
    private String path;
    private String MD5;


    public EventFailedToUpload() {
    }

    /**
     *
     * @param MD5
     * @param path
     */
    public EventFailedToUpload(String MD5, String path) {
        this.path = path;
        this.MD5 = MD5;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMD5() {
        return MD5;
    }

    public void setMD5(String MD5) {
        this.MD5 = MD5;
    }
}
