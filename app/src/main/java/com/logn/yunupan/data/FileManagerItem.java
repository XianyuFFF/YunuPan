package com.logn.yunupan.data;

import android.graphics.Bitmap;

import com.logn.yunupan.App;


/**
 * Created by qjm3662 on 2016/11/1 0001.
 */

public class FileManagerItem {
    private String fileName;
    private String filePath;
    private String parentPath;
    private Bitmap icon;
    private boolean isDirectory;
    private boolean isImgOrVid = false;

    public boolean isImgOrVid() {
        return isImgOrVid;
    }

    public void setImgOrVid(boolean imgOrVid) {
        isImgOrVid = imgOrVid;
    }

    public FileManagerItem(){
        icon = App.b_doc;
    }

    public FileManagerItem(boolean isDirectory) {
        this.isDirectory = isDirectory;
        if(isDirectory){
            icon = App.b_directory;
        }else{
            icon = App.b_doc;
        }
    }

    public FileManagerItem(String fileName, String filePath, String parentPath, Bitmap icon, boolean isDirectory, boolean isImgOrVid) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.parentPath = parentPath;
        this.icon = icon;
        this.isDirectory = isDirectory;
        this.isImgOrVid = isImgOrVid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }
}
