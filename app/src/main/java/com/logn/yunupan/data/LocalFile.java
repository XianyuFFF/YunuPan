package com.logn.yunupan.data;

import com.logn.yunupan.utils.FileTypeUtils;
import com.logn.yunupan.utils.TimeUtils;

import java.io.File;

/**
 * Created by qjm3662 on 2016/9/26 0026.
 */

public class LocalFile {
    private String name;
    private String path;
    private long downTime;
    private String type;
    private boolean flag_is_DateNode;
    public static final String PHOTO = "photo";
    public static final String MUSIC = "music";
    public static final String VIDEO = "video";
    public static final String DOC = "doc";
    public static final String HTML = "html";
    public static final String PPT_PDF = "ppt pdf";
    public static final String ZIP = "zip";

    public LocalFile(String name, String path, long downTime, String type){
        this.name = name;
        this.path = path;
        this.downTime = downTime;
        this.type = type;
        this.flag_is_DateNode = false;
    }
    public LocalFile(String name, String path, long downTime){
        this.name = name;
        this.path = path;
        this.downTime = downTime;
        this.type = FileTypeUtils.getFileType(new File(path));
        this.flag_is_DateNode = false;
    }
    public LocalFile(){

    }


    public boolean isFlag_is_DateNode() {
        return flag_is_DateNode;
    }

    public void setFlag_is_DateNode(boolean flag_is_DateNode) {
        this.flag_is_DateNode = flag_is_DateNode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDownTime() {
        return downTime;
    }

    public void setDownTime(long downTime) {
        this.downTime = downTime;
    }

    public String getDownTimeString(){
        return TimeUtils.getTimeWithLong(downTime);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "LocalFile{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", downTime='" + downTime + '\'' +
                ", type='" + type + '\'' +
                ", flag_is_DateNode=" + flag_is_DateNode +
                '}';
    }
}
