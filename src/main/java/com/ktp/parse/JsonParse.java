package com.ktp.parse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ktp.bean.examInfo;
import com.ktp.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class JsonParse {
    public static List<examInfo> examParse(String result){
        // 解析result，转为Json类型
        JSONObject resObj = JSON.parseObject(result);
        // 定义变量
        List<examInfo> examList = new ArrayList<examInfo>();
        examInfo bean;
        List<String> optionList;
        // Json数据正常则读取
        if(resObj.getInteger("status") == 1){
            JSONArray listsArray = resObj.getJSONArray("lists");
            for (int i=0; i<listsArray.size(); i++){
                bean = new examInfo();
                optionList = new ArrayList<String>();
                JSONObject examObj = listsArray.getJSONObject(i);
                String no = String.valueOf(i+1);
                String type = Utils.getExamType(examObj.getString("type"));
                String score = examObj.getString("score");
                String title = examObj.getString("title");
                JSONArray optionArray = examObj.getJSONArray("optionList");
                if(optionArray.size()>0){
                    for(int j=0; j<optionArray.size(); j++)
                        optionList.add(optionArray.getJSONObject(j).getString("title"));
                }

                bean.setNo(no);
                bean.setType(type);
                bean.setScore(score);
                bean.setTitle(title);
                bean.setOptionList(optionList);

                examList.add(bean);
            }
        }
        return examList;
    }
}
