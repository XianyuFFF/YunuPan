package com.logn.yunupan.entity.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by OurEDA on 2017/3/19.
 */
@Entity
public class FileUploadBean {

    /**
     * upload_url :
     * upload_progress : 0
     * upload_hasDown : true
     * upload_isUploading : false
     * upload_MD5 : 21178412-3-963826-8528-33-75-74563295
     * upload_time : long
     * upload_file_info : FileInfoBean
     */

    @Id(autoincrement = true)
    private long file_id;     //设为主键
    private String upload_url;
    private int upload_progress;
    private boolean upload_hasDown;
    private boolean upload_isUploading;
    private String upload_MD5;
    private long upload_time;


    private String file_name;
    private String file_code;
    private String file_path;
    private long file_size;
    private long time;
    private boolean file_exist;
    private String file_MD5;

    @Generated(hash = 306507513)
    public FileUploadBean(long file_id, String upload_url, int upload_progress,
            boolean upload_hasDown, boolean upload_isUploading, String upload_MD5,
            long upload_time, String file_name, String file_code, String file_path,
            long file_size, long time, boolean file_exist, String file_MD5) {
        this.file_id = file_id;
        this.upload_url = upload_url;
        this.upload_progress = upload_progress;
        this.upload_hasDown = upload_hasDown;
        this.upload_isUploading = upload_isUploading;
        this.upload_MD5 = upload_MD5;
        this.upload_time = upload_time;
        this.file_name = file_name;
        this.file_code = file_code;
        this.file_path = file_path;
        this.file_size = file_size;
        this.time = time;
        this.file_exist = file_exist;
        this.file_MD5 = file_MD5;
    }

    @Generated(hash = 255913564)
    public FileUploadBean() {
    }

    public long getFile_id() {
        return file_id;
    }

    public void setFile_id(long file_id) {
        this.file_id = file_id;
    }

    public String getUpload_url() {
        return upload_url;
    }

    public void setUpload_url(String upload_url) {
        this.upload_url = upload_url;
    }

    public int getUpload_progress() {
        return upload_progress;
    }

    public void setUpload_progress(int upload_progress) {
        this.upload_progress = upload_progress;
    }

    public boolean isUpload_hasDown() {
        return upload_hasDown;
    }

    public void setUpload_hasDown(boolean upload_hasDown) {
        this.upload_hasDown = upload_hasDown;
    }

    public boolean isUpload_isUploading() {
        return upload_isUploading;
    }

    public void setUpload_isUploading(boolean upload_isUploading) {
        this.upload_isUploading = upload_isUploading;
    }

    public String getUpload_MD5() {
        return upload_MD5;
    }

    public void setUpload_MD5(String upload_MD5) {
        this.upload_MD5 = upload_MD5;
    }

    public long getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(long upload_time) {
        this.upload_time = upload_time;
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

    public boolean getUpload_hasDown() {
        return this.upload_hasDown;
    }

    public boolean getUpload_isUploading() {
        return this.upload_isUploading;
    }

    public boolean getFile_exist() {
        return this.file_exist;
    }
}
