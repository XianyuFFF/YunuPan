package com.logn.yunupan.utils.eventbus;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by OurEDA on 2017/3/29.
 */

public class EventBusInstance {
    private static EventBus busInstance;

    private EventBusInstance(){
        throw  new UnsupportedOperationException("");
    }

    public static EventBus getBusInstance() {
        if(busInstance==null){
            busInstance = EventBus.getDefault();
        }
        return busInstance;
    }
}
