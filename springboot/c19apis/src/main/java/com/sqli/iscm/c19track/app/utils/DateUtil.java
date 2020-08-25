package com.sqli.iscm.c19track.app.utils;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateUtil {

    public static SimpleDateFormat DATE_FORMAT_GMT = new SimpleDateFormat("yyyy-MM-dd");

    static {
        DATE_FORMAT_GMT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

}
