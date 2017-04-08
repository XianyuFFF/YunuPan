package com.logn.yunupan.utils;

import java.text.DecimalFormat;

/**
 * Created by OurEDA on 2017/3/19.
 */

public class FileSizeUtils {

    private FileSizeUtils() {
        throw new UnsupportedOperationException("u can't get this instance");
    }

    /**
     * 用于将文件大小的单位比特（B）转换为合适的单位
     *
     * @param fileS
     * @return
     */
    public static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }
}
