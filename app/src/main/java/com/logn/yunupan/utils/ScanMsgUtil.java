package com.logn.yunupan.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 处理扫描（剪切板）出来的信息，基本上就是跳转到新页面
 * <p>
 * Created by ran on 2016/10/30.
 */

public class ScanMsgUtil {

    /**
     * 根据msg信息，判断信息类型
     *
     * @param msg
     * @return
     */
    public static ScanType getType(String msg) {
        if (msg == null) return ScanType.OTHERS;
        if (!getSubscribeID(msg).equals("-1")) {
            return ScanType.SUBSCRIBE;
        } else if (getPersonInfo(msg) != null) {
            return ScanType.PERSON;
        } else if (getClassInfo(msg) != null) {
            return ScanType.CLASS;
        } else if (isURL(msg)) {
            return ScanType.WEB;
        } else {
            return ScanType.OTHERS;
        }
    }

    /**
     * 公众号id在/subscribeID/后面
     *
     * @param msg
     * @return
     */
    public static String getSubscribeID(String msg) {
        int start = msg.lastIndexOf("/subscribeID/");
        if (start == -1) return "-1";
        String id = msg.substring(start + "/subscribeID/".length());
        return id;
    }

    /**
     * 用户id在/personInfo/后面
     *
     * @param msg
     * @return
     */
    public static String getPersonInfo(String msg) {
        int start = msg.lastIndexOf("/personInfo/");
        if (start == -1) return null;
        String info = msg.substring(start + "/personInfo/".length());
        return info;
    }

    /**
     * /classInfo/courseId&courseNumber
     *
     * @param msg
     * @return
     */
    public static String[] getClassInfo(String msg) {
        int start = msg.lastIndexOf("/classInfo/");
        if (start == -1) return null;
        String info = msg.substring(start + "/classInfo/".length());
        int start2 = info.lastIndexOf("&");
        if (start2 == -1) return null;
        String[] result = new String[2];
        result[0] = info.substring(0, start2);
        result[1] = info.substring(start2 + 1);
        return result;
    }

    /**
     * 用正则判断是否为url
     *
     * @param str
     * @return
     */
    private static boolean isURL(String str) {
        if (str == null) return false;
        //转换为小写
        str = str.toLowerCase();
        String regex = "^((https|http|ftp|rtsp|mms)?://)"
                + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
                + "|" // 允许IP和DOMAIN（域名）
                + "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
                + "[a-z]{2,6})" // first level domain- .com or .museum
                + "(:[0-9]{1,4})?" // 端口- :80
                + "((/?)|" // a slash isn't required if there is no file name
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(str);
        return matcher.matches();
    }


    public enum ScanType {
        /**
         * 公众号-<订阅号；功能号>
         */
        SUBSCRIBE,
        /**
         * 用户
         */
        PERSON,
        /**
         * 普通URL
         */
        WEB,
        /**
         * 课程信息
         */
        CLASS,
        /**
         * 其他类型
         */
        OTHERS
    }
}
