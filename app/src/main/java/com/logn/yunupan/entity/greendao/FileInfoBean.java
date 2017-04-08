package com.logn.yunupan.entity.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by OurEDA on 2017/3/19.
 */

@Entity
public class FileInfoBean {

    /**
     * file_name : test.txt
     * file_code : rrr8
     * file_path :
     * file_size : 0
     *
     * file_MD5 : 21178412-3-963826-8528-33-75-74563295
     */

    @Id(autoincrement = true)
    private long file_id;
    private String file_name;
    private String file_code;
    private String file_path;
    private long file_size;
    private long time;
    private boolean file_exist;
    private String file_MD5;

    @Generated(hash = 1252415917)
    public FileInfoBean(long file_id, String file_name, String file_code,
            String file_path, long file_size, long time, boolean file_exist,
            String file_MD5) {
        this.file_id = file_id;
        this.file_name = file_name;
        this.file_code = file_code;
        this.file_path = file_path;
        this.file_size = file_size;
        this.time = time;
        this.file_exist = file_exist;
        this.file_MD5 = file_MD5;
    }

    @Generated(hash = 410787233)
    public FileInfoBean() {
    }

    public long getFile_id() {
        return file_id;
    }

    public void setFile_id(long file_id) {
        this.file_id = file_id;
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

    public boolean getFile_exist() {
        return this.file_exist;
    }
}
