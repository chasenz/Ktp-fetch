package com.ktp.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用工具类
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

    // BASE64解码
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

    // MD5加密
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

    // 正则表达式取group(0)
    public static String getStrByRegex(String originalStr, String regex) {
        String regularStr = null;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(originalStr);
        while (matcher.find()) {
            regularStr = matcher.group(0);
        }
        return regularStr;
    }

    // 正则表达式取group(1)
    public static String getStrByRegex1(String originalStr, String regex) {
        String regularStr = null;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(originalStr);
        while (matcher.find()) {
            regularStr = matcher.group(1);
        }
        return regularStr;
    }

    /**
     * 过滤考题和选项的html标签
     * @param originalStr 形如“<p>事务的四个性质是 &nbsp;<u>”
     */
    public static String getExamByRegex(String originalStr) {
        String regEx_html = "<[^>]+>"; // 去除html标签，如<p>、</u>
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(originalStr);
        originalStr = m_html.replaceAll("");

        String regEx_space = "&nbsp;";// 去除&nbsp;
        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(originalStr);
        originalStr = m_space.replaceAll(" ");

        return  originalStr;
    }

    // 课堂派测试题目类型
    private static Map<String,String> type = new HashMap<String,String>();
    static {
        type.put("1","判断题");
        type.put("2","单选题");
        type.put("3","多选题");
        type.put("4","简答题");
        type.put("5","填空题");
        type.put("6","不定项题");
        type.put("10","段落说明");
    }
    /**
     * 返回类型中文名称
     * @param typeNo 输入1
     * @return 返回判断题
     */
    public static String getExamType(String typeNo){
        // 题目type类型是否存在
        if(type.get(typeNo) != null){
            return type.get(typeNo);
        }else {
            return " ";
        }
    }
}
