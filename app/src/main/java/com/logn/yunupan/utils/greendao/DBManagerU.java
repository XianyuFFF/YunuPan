package com.logn.yunupan.utils.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.logn.yunupan.entity.greendao.DaoMaster;
import com.logn.yunupan.entity.greendao.DaoSession;
import com.logn.yunupan.entity.greendao.FileUploadBean;
import com.logn.yunupan.entity.greendao.FileUploadBeanDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OurEDA on 2017/3/19.
 */

public class DBManagerU {

    private static final String DB_NAME = "db_upload";
    private static DBManagerU mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    private DBManagerU(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
    }


    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static DBManagerU getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManagerU.class) {
                if (mInstance == null) {
                    mInstance = new DBManagerU(context);
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
    public void insertFileInfo(FileUploadBean fileInfoBean) {
        FileUploadBeanDao fileUploadBeanDao = getFIfoDaoWrite();
        fileUploadBeanDao.insert(fileInfoBean);
    }

    /**
     * 插入一组数据
     *
     * @param fileInfoBeens
     */
    public void insertFileInfoList(List<FileUploadBean> fileInfoBeens) {
        if (fileInfoBeens == null || fileInfoBeens.isEmpty()) {
            return;
        }
        FileUploadBeanDao fileUploadBeanDao = getFIfoDaoWrite();
        fileUploadBeanDao.insertInTx(fileInfoBeens);
    }


    /**
     * 删除一条数据
     *
     * @param fileInfoBean
     */
    public void deleteFileInfo(FileUploadBean fileInfoBean) {
        FileUploadBeanDao fileUploadBeanDao = getFIfoDaoWrite();
        fileUploadBeanDao.delete(fileInfoBean);
    }

    /**
     * 删除一条数据
     *
     * @param MD5
     */
    public void deleteFileInfoWithMD5(String MD5) {
        FileUploadBeanDao fileUploadBeanDao = getFIfoDaoWrite();
        List<FileUploadBean> beans = queryWithMD5(MD5);
        for (FileUploadBean bean : beans) {
            fileUploadBeanDao.delete(bean);
        }
    }

    /**
     * 根据主键来更新数据
     *
     * @param fileUploadBean
     */
    public void update(FileUploadBean fileUploadBean) {
        getFIfoDaoWrite().update(fileUploadBean);
    }

    /**
     * 查询数据列表
     *
     * @return
     */
    public List<FileUploadBean> queryAll() {
        FileUploadBeanDao fileUploadBeanDao = getFIfoDaoRead();
        QueryBuilder<FileUploadBean> qb = fileUploadBeanDao.queryBuilder();
        List<FileUploadBean> list = qb.list();

        return list;
    }

    /**
     * 根据提取码获取数据列表
     *
     * @param file_code
     * @return
     */
    public List<FileUploadBean> queryWithCode(String file_code) {
        List<FileUploadBean> list = new ArrayList<>();
        for (FileUploadBean bean : queryAll()) {
            if (bean.getFile_code().equals(file_code)) {
                list.add(bean);
            }
        }
        return list;
    }

    /**
     * 根据md5查找记录
     *
     * @param file_md5
     * @return
     */
    public List<FileUploadBean> queryWithMD5(String file_md5) {
        List<FileUploadBean> list = new ArrayList<>();
        for (FileUploadBean bean : queryAll()) {
            if (bean.getFile_MD5().equals(file_md5)) {
                list.add(bean);
            }
        }

        return list;
    }

    /**
     * 获取可写的FileUploadBeanDao
     *
     * @return
     */
    private FileUploadBeanDao getFIfoDaoWrite() {
        DaoMaster daoMaster = new DaoMaster(getWriteableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FileUploadBeanDao fileUploadBeanDao = daoSession.getFileUploadBeanDao();
        return fileUploadBeanDao;
    }

    /**
     * 获取可读的FileUploadBeanDao
     *
     * @return
     */
    private FileUploadBeanDao getFIfoDaoRead() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FileUploadBeanDao fileUploadBeanDao = daoSession.getFileUploadBeanDao();
        return fileUploadBeanDao;
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
