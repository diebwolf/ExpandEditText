package com.zhuangfei.expandedittext.wrapper;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import com.zhuangfei.expandedittext.BaseEntity;
import com.zhuangfei.expandedittext.EditEntity;
import com.zhuangfei.expandedittext.ExpandEditText;
import com.zhuangfei.expandedittext.TextEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Liu ZhuangFei on 2018/2/27.
 */

public class DefaultImageWrapper extends ImageWrapper {
    @Override
    public void parse(ExpandEditText expandEditText,String str) {
        expandEditText.clear();
        List<BaseEntity> entityList=new ArrayList<>();
        String pattern="!\\[img\\]\\((.*?)\\)";
        Pattern r=Pattern.compile(pattern);
        Matcher matcher=r.matcher(str);
        String nowStr=str;
        int preEnd=0;
        List<String> imageList=new ArrayList<>();
        while (matcher.find()){
            int start=matcher.start();

            int length=matcher.group(0).length();
            String preString=nowStr.substring(preEnd,start);
            preEnd=matcher.end();

            String url=matcher.group(1);

            if(!preString.isEmpty()){
                expandEditText.parseTextEntity(preString);
            }
            expandEditText.parseImageEntity(url);
        }
    }

    @Override
    public String getImageWrapper(String str) {
        return "![img]("+str+")";
    }
}
