package com.logn.yunupan.utils.eventbus;

/**
 * Created by OurEDA on 2017/4/7.
 */

public class EventGetFilePath {
    private String path;

    public EventGetFilePath(String path){
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
