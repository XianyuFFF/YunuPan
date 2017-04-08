package com.logn.yunupan.utils.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.logn.yunupan.entity.greendao.DaoMaster;
import com.logn.yunupan.entity.greendao.DaoSession;
import com.logn.yunupan.entity.greendao.FileDownloadBean;
import com.logn.yunupan.entity.greendao.FileDownloadBeanDao;
import com.logn.yunupan.entity.greendao.FileUploadBean;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OurEDA on 2017/3/19.
 */

public class DBManagerD {

    private static final String DB_NAME = "db_download";
    private static DBManagerD mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    private DBManagerD(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
    }


    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static DBManagerD getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManagerD.class) {
                if (mInstance == null) {
                    mInstance = new DBManagerD(context);
                }
            }
        }
        return mInstance;
    }

    public boolean isEmpty() {
        return queryAll().isEmpty();
    }

    /**
     * 插入一条记录
     *
     * @param fileInfoBean
     */
    public void insertFileInfo(FileDownloadBean fileInfoBean) {
        FileDownloadBeanDao fileDownloadBeanDao = getFIfoDaoWrite();
        fileDownloadBeanDao.insert(fileInfoBean);
    }

    /**
     * 插入一组数据
     *
     * @param fileInfoBeens
     */
    public void insertFileInfoList(List<FileDownloadBean> fileInfoBeens) {
        if (fileInfoBeens == null || fileInfoBeens.isEmpty()) {
            return;
        }
        FileDownloadBeanDao fileDownloadBeanDao = getFIfoDaoWrite();
        fileDownloadBeanDao.insertInTx(fileInfoBeens);
    }


    /**
     * 删除一条数据
     *
     * @param fileInfoBean
     */
    public void deleteFileInfo(FileDownloadBean fileInfoBean) {
        FileDownloadBeanDao fileDownloadBeanDao = getFIfoDaoWrite();
        fileDownloadBeanDao.delete(fileInfoBean);
    }

    /**
     * 删除一条数据，条件为提取码
     *
     * @param code
     */
    public void deleteFileInfoWithCode(String code) {
        if (code == "") {
            return;
        }
        List<FileDownloadBean> beans = queryAll();
        for (FileDownloadBean bean : beans) {
            if (bean.getFile_code() != "" && bean.getFile_code().equals(code)) {
                FileDownloadBeanDao fileDownloadBeanDao = getFIfoDaoWrite();
                fileDownloadBeanDao.delete(bean);
                break;
            }
        }
    }

    /**
     * 查询数据列表
     *
     * @return
     */
    public List<FileDownloadBean> queryAll() {
        FileDownloadBeanDao fileDownloadBeanDao = getFIfoDaoRead();
        QueryBuilder<FileDownloadBean> qb = fileDownloadBeanDao.queryBuilder();
        List<FileDownloadBean> list = qb.list();

        return list;
    }

    /**
     * 根据提取码获取数据列表
     *
     * @param file_code
     * @return
     */
    public List<FileDownloadBean> queryWithCode(String file_code) {
        List<FileDownloadBean> list = new ArrayList<>();
        for (FileDownloadBean bean : queryAll()) {
            if (bean.getFile_code().equals(file_code)) {
                list.add(bean);
            }
        }

        return list;
    }

    /**
     * 根据主键来更新数据
     *
     * @param fileDownloadBean
     */
    public void update(FileDownloadBean fileDownloadBean) {
        getFIfoDaoWrite().update(fileDownloadBean);
    }

    /**
     * 根据md5查找记录
     *
     * @param file_md5
     * @return
     */
    public List<FileDownloadBean> queryWithMD5(String file_md5) {
        List<FileDownloadBean> list = new ArrayList<>();
        for (FileDownloadBean bean : queryAll()) {
            if (bean.getFile_MD5().equals(file_md5)) {
                list.add(bean);
            }
        }

        return list;
    }

    /**
     * 获取可写的FileDownloadBeanDao
     *
     * @return
     */
    private FileDownloadBeanDao getFIfoDaoWrite() {
        DaoMaster daoMaster = new DaoMaster(getWriteableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FileDownloadBeanDao fileDownloadBeanDao = daoSession.getFileDownloadBeanDao();
        return fileDownloadBeanDao;
    }

    /**
     * 获取可读的FileDownloadBeanDao
     *
     * @return
     */
    private FileDownloadBeanDao getFIfoDaoRead() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FileDownloadBeanDao fileDownloadBeanDao = daoSession.getFileDownloadBeanDao();
        return fileDownloadBeanDao;
    }


    /**
     * 获取可读数据库
     *
     * @return
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        }
        return openHelper.getReadableDatabase();
    }

    /**
     * 获取可写数据库
     *
     * @return
     */
    private SQLiteDatabase getWriteableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        }
        return openHelper.getWritableDatabase();
    }
}
