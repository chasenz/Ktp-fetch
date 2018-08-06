package com.ktp.file;

import com.ktp.bean.examInfo;
import com.ktp.util.Utils;

import java.io.*;
import java.util.List;

public class WriteTxtFile {
    /**
     * 写入TXT，覆盖原内容
     * @param examList
     * @param fileName
     * @return
     * @throws Exception
     */
    public static boolean writeTxtFile(List<examInfo> examList, File fileName)throws Exception{
        RandomAccessFile mm=null;
        boolean flag=false;
        FileOutputStream fileOutputStream=null;
        try {
            if(!fileName.exists()){
                fileName.createNewFile();
            }
            fileOutputStream = new FileOutputStream(fileName);
            for(examInfo ei : examList){
                fileOutputStream.write((ei.getNo()+".\t").getBytes("gbk"));
                fileOutputStream.write((ei.getType()+"\t").getBytes("gbk"));
                fileOutputStream.write((ei.getScore()+"分\t").getBytes("gbk"));
                fileOutputStream.write("\r\n".getBytes("gbk"));
                String newTile = Utils.getExamByRegex(ei.getTitle());
                fileOutputStream.write(newTile.getBytes("gbk"));
                fileOutputStream.write("\r\n".getBytes("gbk"));
                for(String s : ei.getOptionList())
                    fileOutputStream.write((Utils.getExamByRegex(s)+"\r\n").getBytes("gbk"));
            }
            fileOutputStream.close();
            flag=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 写入TXT，追加写入
     * @param filePath
     * @param content
     */
    public static void fileChaseFW(String filePath, String content) {
        try {
            //构造函数中的第二个参数true表示以追加形式写文件
            FileWriter fw = new FileWriter(filePath,true);
            fw.write(content);
            fw.close();
        } catch (IOException e) {
            System.out.println("文件写入失败！" + e);
        }
    }
}
