package com.ktp.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 17-8-10.
 */
public class Utils {

    static Pattern p = Pattern.compile("\\d+");

    private static final String baseDigits = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = baseDigits.length();
    private static final char[] digitsChar = baseDigits.toCharArray();
    private static final int FAST_SIZE = 'z';
    private static final int[] digitsIndex = new int[FAST_SIZE + 1];

    static {
        for (int i = 0; i < FAST_SIZE; i++) {
            digitsIndex[i] = -1;
        }
        //
        for (int i = 0; i < BASE; i++) {
            digitsIndex[digitsChar[i]] = i;
        }
    }

    // 获取字符串中的第一串数字
    public static String getFirstNum(String input) {
        if (StringUtils.isNotBlank(input)) {
            Matcher m = p.matcher(input);
            if (m.find()) {
                input = m.group();
            } else {
                return "0";
            }
        }
        return input;
    }

    // 转换时间格式
    public static Date getDate(String input) {
        Date date;
        date = getTime(input, "yyyy-MM-dd HH:mm");
        return date;
    }

    // 转换时间格式
    public static Date getDate(String input, String formatedDate) {
        Date date;
        date = getTime(input, formatedDate);
        return date;
    }

    // 字符串转换为date类型
    private static Date getTime(String input, String str) {
        Date date = null;
        SimpleDateFormat simp = new SimpleDateFormat(str);
        try {
            date = simp.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private static int getIndex(String s, int pos) {
        char c = s.charAt(pos);
        if (c > FAST_SIZE) {
            throw new IllegalArgumentException("Unknow character for Base62: " + s);
        }
        int index = digitsIndex[c];
        if (index == -1) {
            throw new IllegalArgumentException("Unknow character for Base62: " + s);
        }
        return index;
    }

    public static long decode(String s) {
        long result = 0L;
        long multiplier = 1;
        for (int pos = s.length() - 1; pos >= 0; pos--) {
            int index = getIndex(s, pos);
            result += index * multiplier;
            multiplier *= BASE;
        }
        return result;
    }

    // 获取新浪微博数字id，根据字母id转换为数字id
    public static String getMid(String id) {
        String s1 = id.substring(0, 1);
        String s2 = id.substring(1, 5);
        String s3 = id.substring(5, 9);

        long i1 = decode(s1);
        long i2 = decode(s2);
        long i3 = decode(s3);

        String a1 = "" + i1;
        String a2 = "0000000" + i2;
        String a3 = "0000000" + i3;
        a2 = a2.substring(a2.length() - 7, a2.length());
        a3 = a3.substring(a3.length() - 7, a3.length());

        return a1 + a2 + a3;
    }

    public static String getUnicode(String s, String charset) {
        if (StringUtils.isNotBlank(s)) {
            try {
                byte[] b = s.getBytes(charset);
                String str = "";
                for (int i = 0; i < b.length; i++) {
                    String strTmp = Integer.toHexString(b[i]);
                    if (strTmp.length() >= 2)
                        strTmp = "%" + strTmp.substring(strTmp.length() - 2);
                    str += strTmp;
                }
                return str.toUpperCase();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    public static String md5ByHex(String src) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = src.getBytes();
            md.reset();
            md.update(b);
            byte[] hash = md.digest();
            String hs = "";
            String stmp = "";
            for (int i = 0; i < hash.length; i++) {
                stmp = Integer.toHexString(hash[i] & 0xFF);
                if (stmp.length() == 1)
                    hs = hs + "0" + stmp;
                else {
                    hs = hs + stmp;
                }
            }
            return hs.toUpperCase();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getStrByRegex(String originalStr, String regex) {
        String regularStr = null;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(originalStr);
        while (matcher.find()) {
            regularStr = matcher.group(0);
        }
        return regularStr;
    }

    public static String getStrByRegex1(String originalStr, String regex) {
        String regularStr = null;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(originalStr);
        while (matcher.find()) {
            regularStr = matcher.group(1);
        }
        return regularStr;
    }


    public static String getPhjsHtml(String url) {
        Runtime rt = Runtime.getRuntime();
        String exec = System.getProperty("user.dir") + "\\phantomjs\\bin\\phantomjs.exe \\res\\phantom.js " + url;
        String anti_content = "";
        try {
            Process p = rt.exec(exec);
            InputStream is = p.getInputStream();
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[4096];
            for (int n; (n = is.read(b)) != -1; ) {
                String str = new String(b, 0, n);
                str = str.replace("\r", "").replace("\n", "");
                if (StringUtils.isNotBlank(str)) {
                    out.append(str);
                }
            }
            anti_content = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return anti_content;
    }

}
