package com.logn.yunupan.module.download;

import android.os.Environment;
import android.util.Log;


import com.logn.yunupan.entity.greendao.FileDownloadBean;
import com.logn.yunupan.utils.FileSizeUtils;
import com.logn.yunupan.utils.FileUtils;
import com.logn.yunupan.utils.MD5;
import com.logn.yunupan.utils.WIFIWithDLUTUtil;
import com.logn.yunupan.utils.eventbus.EventBusInstance;
import com.logn.yunupan.utils.eventbus.EventFailedToDownload;
import com.logn.yunupan.utils.eventbus.EventToastInfo;
import com.logn.yunupan.utils.eventbus.EventProgressD;
import com.logn.yunupan.utils.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
 * Created by OurEDA on 2016/12/22.
 */

public class FileDownload {

    private static final String BASE_URL = "http://upan.oureda.cn/Home/DownPublicFileByrandName?randName=";

    private int file_length;

    private String file_name;
    private int response_code = -1;

    public void setGetNameOnly(boolean getNameOnly) {
        this.getNameOnly = getNameOnly;
    }

    private boolean getNameOnly = false;

    public int getFile_length() {
        return file_length;
    }

    private URL url = null;
    private FileUtil fileUtiles;

    private OnFilePro pro = null;

    public FileDownload() {
        fileUtiles = new FileUtil();
        file_length = 0;
        fileUtiles.setOnFilePro(new FileUtil.OnFilePro() {

            @Override
            public void onShowPro(int bytes) {
                pro.onShowPro(bytes, file_length);
            }
        });
    }

    /**
     * "http://upan.oureda.cn/Home/DownPublicFileByrandName?randName="
     *
     * @param fileCode 四位提取码
     * @return
     * @throws MalformedURLException
     */
    public String downWithCode(String fileCode) {
        if (!WIFIWithDLUTUtil.connected()) {
            //连接失败
            EventBusInstance.getBusInstance().post(new EventToastInfo("请检查网络，请连接校园网"));
            return "-1";
        }
        InputStream is;
        try {
            FileDownloadBean bean = new FileDownloadBean();
            bean.setFile_id(1);
            bean.setFile_code(fileCode);

            bean.setDownload_progress(0);
            bean.setDownload_hasDown(false);
            bean.setDownload_isDownloading(true);
            bean.setFile_exist(true);
            bean.setFile_name("********");
            bean.setFile_size(0);
            bean.setTime(System.currentTimeMillis());
            //发送事件，记录到数据库，并开始下载。通知实时更新数据库，根据提-取码-查找一遍数据库，没有找到则插入
            EventProgressD event = new EventProgressD();
            event.setBean(bean);
            event.setDone(false);
            event.setContentLength(0);
            event.setPosition(-1);
            EventBusInstance.getBusInstance().post(event);

//            if (file_name != null && !file_name.isEmpty()) {
//                Logger.e("前面以获取文件名：" + file_name);
//            }

            is = getInputStream(BASE_URL + fileCode);

//            if (file_name != null && !file_name.isEmpty()) {
//                Logger.e("后面以获取文件名：" + file_name);
//            }
            Logger.e("开始下载");
            if (is != null) {

                //以获取文件名，
                String path = Environment.getExternalStorageDirectory().getPath() + "/yup/" + file_name;
                bean.setFile_name(file_name);
                bean.setFile_path(path);
                bean.setFile_size(file_length);

                //将输入流写进文件
                File file = new File(path);
                FileUtils.writeFileFromIS(event, file, is, false);
                Logger.e("写文件？？？？？？\n文件名：" + file.getName() + "\n文件大小：" + FileSizeUtils.formatFileSize(file.length()));
                //添加记录

                bean.setDownload_progress(100);
                bean.setDownload_hasDown(true);
                bean.setDownload_isDownloading(false);
                bean.setFile_exist(true);

                bean.setTime(System.currentTimeMillis());
                bean.setDownload_MD5(MD5.getFileMD5(file));

                event.setPosition(-3);
                event.setDone(true);
                EventBusInstance.getBusInstance().post(event);
                return 0 + "";
            } else {
                Logger.e("fuck you");
                //提取码不存在
                //通知删除数据库
                EventBusInstance.getBusInstance().post(new EventFailedToDownload(fileCode));
                return -1 + "";
            }
        } catch (IOException e) {
            e.printStackTrace();
            Logger.e("获取文件流出错：" + e.toString());
            EventBusInstance.getBusInstance().post(new EventFailedToDownload(fileCode));
            return -1 + "";
        }

    }


    public String getNameOnly(String code) {
        InputStream input;
        try {
            url = new URL(BASE_URL + code);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //open the connection with the url.
        HttpURLConnection httpURLConnect = null;
        try {
            httpURLConnect = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //set the request property.
        httpURLConnect.setRequestProperty("referer",
                "http://upan.oureda.cn/Home/Public");
        httpURLConnect.setRequestProperty("UserAgent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; flashget)");

        try {
            httpURLConnect.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, List<String>> headers = httpURLConnect.getHeaderFields();

        if (!headers.containsKey("Content-Disposition")) {  //判断提取码是否有效
            httpURLConnect.disconnect();
            return null;
        }
        file_name = getFileName(headers.get("Content-Disposition").toString());

        httpURLConnect.disconnect();

        return file_name;
    }


    private InputStream getInputStream(String urlStr) throws IOException {
        InputStream input;
        url = new URL(urlStr);

        //open the connection with the url.
        HttpURLConnection httpURLConnect = (HttpURLConnection) url.openConnection();

        //set the request property.
        httpURLConnect.setRequestProperty("referer",
                "http://upan.oureda.cn/Home/Public");
        httpURLConnect.setRequestProperty("UserAgent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; flashget)");

        response_code = httpURLConnect.getResponseCode();
        //根据状态码做判断
        if (response_code == 200) {
            //打开连接
            httpURLConnect.connect();

            Map<String, List<String>> headers = httpURLConnect.getHeaderFields();
            for (String headerKey : headers.keySet()) {
                Log.e("code:" + response_code, headerKey + " : " + headers.get(headerKey).toString());
            }
            if (!headers.containsKey("Content-Disposition")) {
                file_name = "";
                return null;
            }
            file_name = getFileName(headers.get("Content-Disposition").toString());
//            if (isGetNameOnly) {
//                //httpURLConnect.disconnect();
//                Logger.e("断开连接");
//                return null;
//            }
            input = httpURLConnect.getInputStream();
            //获取文件长度
            file_length = httpURLConnect.getContentLength();
            Logger.e("woc_file_length:" + file_length);

            return input;
        } else {
            Logger.e("状态码：" + response_code);
            return null;
        }

    }

    public void setOnFilePro(OnFilePro pro) {
        if (pro != null) {
            this.pro = pro;
        }
    }

    public interface OnFilePro {
        void onShowPro(int bytes, int sum);
    }

    /**
     * 提取文件名
     *
     * @param content
     * @return
     */
    private String getFileName(String content) {
        int s = content.lastIndexOf("=");
        int d = content.lastIndexOf(".");
        int e = content.lastIndexOf("]");

        String name = content.substring(s + 1, d);
        String suffix = content.substring(d + 1, e);
        try {
            name = URLDecoder.decode(name, "utf-8");
        } catch (UnsupportedEncodingException exception) {
            exception.printStackTrace();
        }
        return name + "." + suffix;
    }
}
