package com.logn.yunupan.utils;

import com.logn.yunupan.utils.logger.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by OurEDA on 2017/3/29.
 */

public class WIFIWithDLUTUtil {

    private static final String urlstr = "http://upan.oureda.cn/";

    public static boolean connected() {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(urlstr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Logger.e("获取url错误：\t" + e.toString());
            return false;
        }
        try {
            connection = (HttpURLConnection) url.openConnection();

        } catch (IOException e) {
            e.printStackTrace();
            Logger.e("打开连接错误；\t" + e.toString());
            return false;
        }
        connection.setConnectTimeout(5000);

        try {
            if (connection.getResponseCode() == 200) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Logger.e("连接超时；\t" + e.toString());
            return false;
        }

        return false;
    }
}
