package com.logn.yunupan.module.download;

import android.os.Environment;

import com.logn.yunupan.utils.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by OurEDA on 2016/12/22.
 */

public class FileUtil {

    private String SDPATH = "";
    private int file_bytes = 0;

    private OnFilePro pro = null;

    public String getSDPATH() {
        return SDPATH;
    }

    public FileUtil() {
        SDPATH = Environment.getExternalStorageDirectory() + "/";
    }

    /*
     * ��sd���ϴ����ļ�
     */
    public static File creatSDFile(String fileName) throws IOException {
        File file = new File(fileName);
        file.createNewFile();
        return file;
    }

    /**
     * create a dir in the sd
     *
     * @param dirName the path
     * @return the created file
     */
    public static File creatSDDir(String dirName) {
        File dir = new File(dirName);
        dir.mkdir();
        return dir;
    }

    /*
     * �ж��ļ��Ƿ����
     */
    public static boolean isFileExist(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    public File write2SDFromInputStream(String path, String fileName,
                                        InputStream input) {
        File file = null; // �ļ���������
        OutputStream os = null; // �������д�ļ�
        try {
            Logger.e("startUPan");
            creatSDDir(path); // ����Ŀ¼

            file = creatSDFile(path + fileName); // �����ļ�

            os = new FileOutputStream(file);
            if (os == null) {
                Logger.e("fuck_null_1");

            }
            byte buffer[] = new byte[4 * 1024];
            int len = 0;
            while ((len = input.read(buffer)) != -1) {
                // д�����ٶ�����
                file_bytes += len;
                os.write(buffer, 0, len);
                if (pro != null) {
                    pro.onShowPro(file_bytes);
                }
            }
            if (os == null) {
                Logger.e("fuck_null_2");

            }
            os.flush();
            if (os == null) {
                Logger.e("fuck_null_3");

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            try {
//                if (os == null) {
//
//
//                    //os.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            Logger.e("fuck_null_4");
        }
        return file;
    }

    public void setOnFilePro(OnFilePro pro) {
        if (pro != null) {
            this.pro = pro;
        }
    }

    public interface OnFilePro {
        public void onShowPro(int bytes);
    }


}
