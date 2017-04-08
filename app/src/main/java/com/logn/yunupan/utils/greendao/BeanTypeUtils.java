package com.logn.yunupan.utils.greendao;

import com.logn.yunupan.entity.greendao.FileDownloadBean;
import com.logn.yunupan.entity.greendao.FileInfoBean;
import com.logn.yunupan.entity.greendao.FileUploadBean;

/**
 * Created by OurEDA on 2017/3/29.
 */

public class BeanTypeUtils {
    private BeanTypeUtils() {

    }

    public static FileInfoBean getFromFU(FileUploadBean bean) {
        return new FileInfoBean(bean.getFile_id(), bean.getFile_name(), bean.getFile_code(), bean.getFile_path(),
                bean.getFile_size(), bean.getTime(), bean.getFile_exist(), bean.getFile_MD5());
    }

    public static FileInfoBean getFromFD(FileDownloadBean bean) {
        return new FileInfoBean(bean.getFile_id(), bean.getFile_name(), bean.getFile_code(), bean.getFile_path(),
                bean.getFile_size(), bean.getTime(), bean.getFile_exist(), bean.getFile_MD5());
    }
}
