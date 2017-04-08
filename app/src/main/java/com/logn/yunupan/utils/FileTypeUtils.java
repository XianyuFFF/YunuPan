package com.logn.yunupan.utils;

import com.logn.yunupan.R;
import com.logn.yunupan.data.LocalFile;

import java.io.File;

/**
 * Created by OurEDA on 2016/12/24.
 */

public class FileTypeUtils {


    public static int getBitmapId(String fileName) {
        String type = getType(fileName);
        int icon;
        switch (type) {
            case ".doc":
            case ".docx":
                icon = R.mipmap.icon_myfile_doc;
                break;
            case ".ppt":
            case ".pptx":
                icon = R.mipmap.icon_myfile_ppt;
                break;
            case ".pdf":
                icon = R.mipmap.icon_myfile_pdf;
                break;
            case ".txt":
                icon = R.mipmap.icon_myfile_txt;
                break;
            case ".xls":
                icon = R.mipmap.icon_myfile_xls;
                break;
            case ".apk":
                icon = R.mipmap.icon_myfile_apk;
                break;
            case ".zip":
                icon = R.mipmap.icon_myfile_zip;
                break;
            default:
                icon = R.mipmap.icon_myfile_other;
                if (MediaFile.isAudioFileType(fileName)) {
                    icon = R.mipmap.icon_myfile_video;
                } else if (MediaFile.isImageFileType(fileName)) {
                    icon = R.mipmap.icon_myfile_pic;
                } else if (MediaFile.isVideoFileType(fileName)) {
                    icon = R.mipmap.icon_myfile_video;
                }
                break;
        }
        return icon;
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     * @param file
     */
    public static String getFileType(File file)
    {
        String type="*/*";
        String fName=file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if(dotIndex < 0){
            return type;
        }
    /* 获取文件的后缀名 */
        String end=fName.substring(dotIndex,fName.length()).toLowerCase();
        if(end=="")return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for(int i=0;i<MIME_MapTable.length;i++){
            if(end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    //建立一个MIME类型与文件后缀名的匹配表
    public static final String[][] MIME_MapTable={
            //{后缀名，    MIME类型}
            {".3gp", LocalFile.VIDEO},
            {".apk", LocalFile.ZIP},
            //{".asf",    "video/x-ms-asf"},
            {".avi",    LocalFile.VIDEO},
//            {".bin",    "application/octet-stream"},
            {".bmp",      LocalFile.PHOTO},
            {".c",        LocalFile.DOC},
            {".class",    LocalFile.DOC},
//            {".conf",    "text/plain"},
            {".cpp",    LocalFile.DOC},
            {".doc",    LocalFile.DOC},
//            {".exe",    "application/octet-stream"},
            {".gif",    LocalFile.PHOTO},
//            {".gtar",    "application/x-gtar"},
//            {".gz",        "application/x-gzip"},
            {".h",        LocalFile.DOC},
            {".htm",    LocalFile.HTML},
            {".html",    LocalFile.HTML},
//            {".jar",    "application/java-archive"},
            {".java",    LocalFile.DOC},
            {".jpeg",    LocalFile.PHOTO},
            {".jpg",    LocalFile.PHOTO},
            {".js",      LocalFile.DOC},
//            {".log",    "text/plain"},
//            {".m3u",    "audio/x-mpegurl"},
//            {".m4a",    "audio/mp4a-latm"},
//            {".m4b",    "audio/mp4a-latm"},
//            {".m4p",    "audio/mp4a-latm"},
//            {".m4u",    "video/vnd.mpegurl"},
//            {".m4v",    "video/x-m4v"},
//            {".mov",    "video/quicktime"},
//            {".mp2",    "audio/x-mpeg"},
            {".mp3",    LocalFile.MUSIC},
            {".mp4",    LocalFile.VIDEO},
            {".mpc",    LocalFile.VIDEO},
            {".mpe",    LocalFile.VIDEO},
            {".mpeg",    LocalFile.VIDEO},
            {".mpg",    LocalFile.VIDEO},
            {".mpg4",    LocalFile.VIDEO},
//            {".mpga",    "audio/mpeg"},
//            {".msg",    "application/vnd.ms-outlook"},
//            {".ogg",    "audio/ogg"},
            {".pdf",    LocalFile.PPT_PDF},
            {".png",    LocalFile.PHOTO},
//            {".pps",    "application/vnd.ms-powerpoint"},
            {".ppt",    LocalFile.PPT_PDF},
//            {".prop",    "text/plain"},
            {".rar",    LocalFile.ZIP},
//            {".rc",        "text/plain"},
            {".rmvb",    LocalFile.PPT_PDF},
//            {".rtf",    "application/rtf"},
//            {".sh",        "text/plain"},
//            {".tar",    "application/x-tar"},
//            {".tgz",    "application/x-compressed"},
            {".txt",    LocalFile.DOC},
            {".wav",    LocalFile.MUSIC},
            {".wma",    LocalFile.MUSIC},
            {".wmv",    LocalFile.MUSIC},
            {".wps",    LocalFile.DOC},
            //{".xml",    "text/xml"},
            {".xml",    LocalFile.DOC},
//            {".z",        "application/x-compress"},
            {".zip",    LocalFile.ZIP},
            {"",        "*/*"}
    };



    /**
     * 获取文件的MIME类型
     *
     * @param file 接受一个文件，返回该文件的MIME，如果是目录则返回null
     */
    public static String getMIMEType(File file) {
        String type = getType(file);

        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        if (type != null) {
            boolean found = false;
            for (String[] data : MIME_Table) {
                if (type.equals(data[0])) {
                    type = data[1];
                    found = true;
                    break;
                }
            }
            if (!found)
                type = "*/*";
        }
        return type;
    }

    /**
     * 获取文件的后缀名
     *
     * @param file 接受一个文件，返回该文件的后缀名，如果是目录则返回null
     */
    public static String getType(File file) {
        if (file.isDirectory())
            return null;

        int dotIndex = file.getName().lastIndexOf(".");
        if (dotIndex < 0)
            return "";

        return file.getName().substring(dotIndex).toLowerCase();
    }

    /**
     * 根据文件全名判断文件类型
     *
     * @param fileName
     * @return
     */
    public static String getType(String fileName) {
        int lastdot = fileName.lastIndexOf(".");
        if (lastdot < 0)
            return "";
        return fileName.substring(lastdot).toLowerCase();
    }


    //MIME类型集合，{后缀名，MIME类型}
    public static final String[][] MIME_Table = {
            {".3gp", "video/3gp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };
}
