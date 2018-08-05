package com.ktp.parse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ktp.bean.examInfo;
import com.ktp.util.Utils;

import java.util.List;

public class JsonParse {
    public static List<examInfo> examParse(String result){
        JSONObject resObj = JSON.parseObject(result);
        if(resObj.getInteger("status") == 1){
            JSONArray listsArray = resObj.getJSONArray("lists");
            for (int i=0; i<listsArray.size(); i++){
                JSONObject examObj = listsArray.getJSONObject(i);
                String no = String.valueOf(i);
                String type = Utils.getExamType(examObj.getString("type"));
                String score = examObj.getString("score");
                String title = examObj
            }
        }
    }
}
