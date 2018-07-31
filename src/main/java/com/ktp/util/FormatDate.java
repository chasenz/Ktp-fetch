package com.ktp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FormatDate {

    public static String getDate(String str) {
        SimpleDateFormat simf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simf1 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar nowDate = Calendar.getInstance();
        String date = "";
        Date now = new Date();
        if (str.contains("刚刚")) {
            date = simf.format(now);
        }
        if (str.contains("秒前")) {
            int endIndex = str.indexOf("秒前");
            int minu = Integer.parseInt(str.substring(0, endIndex));
            nowDate.setTime(now);
            nowDate.set(Calendar.SECOND, nowDate.get(Calendar.SECOND) - minu);
            date = simf.format(nowDate.getTime());
        }
        if (str.contains("分钟前")) {
            int endIndex = str.indexOf("分钟前");
            int minu = Integer.parseInt(str.substring(0, endIndex));
            nowDate.setTime(now);
            nowDate.set(Calendar.MINUTE, nowDate.get(Calendar.MINUTE) - minu);
            date = simf.format(nowDate.getTime());
        }
        if (str.contains("今天")) {
            String year = simf1.format(now);
            int beginIndex = str.indexOf("今天");
            String day = str.substring(beginIndex + 2);
            date = year + day + ":00";
            if (day.length() > 2) {
                date = year + day;
            } else if (day.length() <= 2) {
                date = year + day + ":00";
            }
            //System.out.println(year+mouth+day);
        }
        if (str.contains("昨天")) {
            nowDate.setTime(now);
            nowDate.set(Calendar.DATE, nowDate.get(Calendar.DATE) - 1);
            String year = simf1.format(nowDate.getTime());
            int beginIndex = str.indexOf("昨天");
            String day = str.substring(beginIndex + 2);
            if (day.length() > 2) {
                date = year + day;
            } else if (day.length() <= 2) {
                date = year + day + ":00";
            }
//			date= year+" "+day+":00";
        }
        if (str.contains("月") && !str.contains("年")) {
            int endIndex1 = str.indexOf('月');
            String mouth = str.substring(0, endIndex1);
            //	System.out.println(mouth);
            int endIndex2 = str.indexOf('日');
            String day = str.substring(endIndex1 + 1, endIndex2);
            //	System.out.println(day);
            nowDate.setTime(now);
            String year = nowDate.get(Calendar.YEAR) + "";
            String hour = str.substring(endIndex2 + 1);
            if (hour.length() > 2) {
                date = year + "-" + mouth + "-" + day + hour;
            } else if (hour.length() <= 2) {
                date = year + "-" + mouth + "-" + day + hour + ":00";
            }

            //	System.out.println(year);
        }
        if (str.contains("年")) {
            int endIndex1 = str.indexOf('年');
            String year = str.substring(0, endIndex1);
            int endIndex2 = str.indexOf('月');
            String mouth = str.substring(endIndex1 + 1, endIndex2);
            int endIndex3 = str.indexOf('日');
            String day = str.substring(endIndex2 + 1, endIndex3);
            String hour = str.substring(endIndex3 + 1);
            date = year + "-" + mouth + "-" + day + hour + ":00";
        }
        return date;
    }
}
