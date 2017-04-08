package com.logn.yunupan.utils.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.logn.yunupan.entity.greendao.DaoMaster;
import com.logn.yunupan.entity.greendao.DaoSession;
import com.logn.yunupan.entity.greendao.FileInfoBean;
import com.logn.yunupan.entity.greendao.FileInfoBeanDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OurEDA on 2017/3/19.
 */

public class DBManagerR {

    private static final String DB_NAME = "db_record";
    private static DBManagerR mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    private DBManagerR(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
    }


    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static DBManagerR getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManagerR.class) {
                if (mInstance == null) {
                    mInstance = new DBManagerR(context);
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
    public void insertFileInfo(FileInfoBean fileInfoBean) {
        FileInfoBeanDao fileInfoBeanDao = getFIfoDaoWrite();
        fileInfoBeanDao.insert(fileInfoBean);
    }

    /**
     * 插入一组数据
     *
     * @param fileInfoBeens
     */
    public void insertFileInfoList(List<FileInfoBean> fileInfoBeens) {
        if (fileInfoBeens == null || fileInfoBeens.isEmpty()) {
            return;
        }
        FileInfoBeanDao fileInfoBeanDao = getFIfoDaoWrite();
        fileInfoBeanDao.insertInTx(fileInfoBeens);
    }


    /**
     * 删除一条数据
     *
     * @param fileInfoBean
     */
    public void deleteFileInfo(FileInfoBean fileInfoBean) {
        FileInfoBeanDao fileInfoBeanDao = getFIfoDaoWrite();
        fileInfoBeanDao.delete(fileInfoBean);
    }

    public void update(FileInfoBean bean) {
        getFIfoDaoWrite().update(bean);
    }

    /**
     * 查询数据列表
     *
     * @return
     */
    public List<FileInfoBean> queryAll() {
        FileInfoBeanDao fileInfoBeanDao = getFIfoDaoRead();
        QueryBuilder<FileInfoBean> qb = fileInfoBeanDao.queryBuilder();
        List<FileInfoBean> list = qb.list();

        return list;
    }

    /**
     * 根据提取码获取数据列表
     *
     * @param file_code
     * @return
     */
    public List<FileInfoBean> queryWithCode(String file_code) {
        FileInfoBeanDao fileInfoBeanDao = getFIfoDaoRead();
        QueryBuilder<FileInfoBean> qb = fileInfoBeanDao.queryBuilder();
//        qb.where(FileInfoBeanDao.Properties.File_code.gt(file_code));
        List<FileInfoBean> list = new ArrayList<>();
        for (FileInfoBean bean : qb.list()) {
            if (bean.getFile_code().equals(file_code)) {
                list.add(bean);
            }
        }

        return list;
    }

    /**
     * 根据MD5查找
     *
     * @param file_md5
     * @return
     */
    public List<FileInfoBean> queryWithMD5(String file_md5) {
        FileInfoBeanDao fileInfoBeanDao = getFIfoDaoRead();
        QueryBuilder<FileInfoBean> qb = fileInfoBeanDao.queryBuilder();
        List<FileInfoBean> list = new ArrayList<>();
        for (FileInfoBean bean : qb.list()) {
            if (bean.getFile_MD5() != null && bean.getFile_MD5().equals(file_md5)) {
                list.add(bean);
            }
        }

        return list;
    }

    public List<FileInfoBean> queryWithPath(String file_path) {
        FileInfoBeanDao fileInfoBeanDao = getFIfoDaoRead();
        QueryBuilder<FileInfoBean> qb = fileInfoBeanDao.queryBuilder();
        List<FileInfoBean> list = new ArrayList<>();
        for (FileInfoBean bean : qb.list()) {
            if (bean.getFile_path().equals(file_path)) {
                list.add(bean);
            }
        }

        return list;
    }


    /**
     * 获取可写的FileInfoBeanDao
     *
     * @return
     */
    private FileInfoBeanDao getFIfoDaoWrite() {
        DaoMaster daoMaster = new DaoMaster(getWriteableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FileInfoBeanDao fileInfoBeanDao = daoSession.getFileInfoBeanDao();
        return fileInfoBeanDao;
    }

    /**
     * 获取可读的FileInfoBeanDao
     *
     * @return
     */
    private FileInfoBeanDao getFIfoDaoRead() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FileInfoBeanDao fileInfoBeanDao = daoSession.getFileInfoBeanDao();
        return fileInfoBeanDao;
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
