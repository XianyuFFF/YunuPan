package com.logn.yunupan.utils.eventbus;

/**
 * Created by OurEDA on 2017/4/1.
 */

public class EventFailedToDownload {
    private String code;

    public EventFailedToDownload(){

    }

    public EventFailedToDownload(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
