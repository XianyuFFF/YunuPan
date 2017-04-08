package com.logn.yunupan.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by liufengkai on 16/9/17.
 */
public class TimeUtils {
    static final SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z", Locale.CHINA);

    static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.CHINA);

    static final SimpleDateFormat mmDdHhMm = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);

    static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    public enum TimeType {
        HH_MM,
        MM_DD_HH_MM,
        YYYY_MM_DD
    }

    public static Date parseDate(String utc, TimeType type) {
        try {

            switch (type) {
                case HH_MM:
                    return timeFormat.parse(utc);
                case MM_DD_HH_MM:
                    return mmDdHhMm.parse(utc);
                case YYYY_MM_DD:
                    return yyyyMMdd.parse(utc);
                default:
                    return new Date();
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static Date parseUtcStringToDate(String utc) {
        try {
            return utcFormat.parse(utc.replace("Z", " UTC"));
        } catch (ParseException ignored) {
        }
        return new Date();
    }

    public static String getTimeNowString() {
        return getTimeWithLong(System.currentTimeMillis());
    }

    public static String getTimeWithLong(long date) {
        Date fuckDate = new Date(date);
        return yyyyMMdd.format(fuckDate);
    }

//    public static void main(String[] args) {
//        System.out.println(TimeUtils.getTimeWithLong(System.currentTimeMillis()));
//
//    }
}
