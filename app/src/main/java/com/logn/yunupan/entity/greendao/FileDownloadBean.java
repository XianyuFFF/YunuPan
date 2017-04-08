package com.logn.yunupan.entity.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by OurEDA on 2017/3/19.
 */

@Entity
public class FileDownloadBean {

    @Id(autoincrement = true)
    private long file_id;     //设为主键
    private String download_url;
    private int download_progress;
    private boolean download_hasDown;
    private boolean download_isDownloading;
    private String download_MD5;
    private long download_time;


    private String file_name;
    private String file_code;
    private String file_path;
    private long file_size;
    private long time;
    private boolean file_exist;
    private String file_MD5;

    @Generated(hash = 1941999743)
    public FileDownloadBean(long file_id, String download_url,
            int download_progress, boolean download_hasDown,
            boolean download_isDownloading, String download_MD5, long download_time,
            String file_name, String file_code, String file_path, long file_size,
            long time, boolean file_exist, String file_MD5) {
        this.file_id = file_id;
        this.download_url = download_url;
        this.download_progress = download_progress;
        this.download_hasDown = download_hasDown;
        this.download_isDownloading = download_isDownloading;
        this.download_MD5 = download_MD5;
        this.download_time = download_time;
        this.file_name = file_name;
        this.file_code = file_code;
        this.file_path = file_path;
        this.file_size = file_size;
        this.time = time;
        this.file_exist = file_exist;
        this.file_MD5 = file_MD5;
    }

    @Generated(hash = 1082129065)
    public FileDownloadBean() {
    }

    public long getFile_id() {
        return file_id;
    }

    public void setFile_id(long file_id) {
        this.file_id = file_id;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public int getDownload_progress() {
        return download_progress;
    }

    public void setDownload_progress(int download_progress) {
        this.download_progress = download_progress;
    }

    public boolean isDownload_hasDown() {
        return download_hasDown;
    }

    public void setDownload_hasDown(boolean download_hasDown) {
        this.download_hasDown = download_hasDown;
    }

    public boolean isDownload_isDownloading() {
        return download_isDownloading;
    }

    public void setDownload_isDownloading(boolean download_isDownloading) {
        this.download_isDownloading = download_isDownloading;
    }

    public String getDownload_MD5() {
        return download_MD5;
    }

    public void setDownload_MD5(String download_MD5) {
        this.download_MD5 = download_MD5;
    }

    public long getDownload_time() {
        return download_time;
    }

    public void setDownload_time(long download_time) {
        this.download_time = download_time;
    }


    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_code() {
        return file_code;
    }

    public void setFile_code(String file_code) {
        this.file_code = file_code;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public long getFile_size() {
        return file_size;
    }

    public void setFile_size(long file_size) {
        this.file_size = file_size;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isFile_exist() {
        return file_exist;
    }

    public void setFile_exist(boolean file_exist) {
        this.file_exist = file_exist;
    }

    public String getFile_MD5() {
        return file_MD5;
    }

    public void setFile_MD5(String file_MD5) {
        this.file_MD5 = file_MD5;
    }

    public boolean getDownload_hasDown() {
        return this.download_hasDown;
    }

    public boolean getDownload_isDownloading() {
        return this.download_isDownloading;
    }

    public boolean getFile_exist() {
        return this.file_exist;
    }
}
