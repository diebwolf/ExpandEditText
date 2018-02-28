package com.zhuangfei.expandedittext.wrapper;

/**
 * Created by Liu ZhuangFei on 2018/2/27.
 */

import android.content.Context;

import com.zhuangfei.expandedittext.BaseEntity;
import com.zhuangfei.expandedittext.ExpandEditText;

import java.util.List;

/**
 * 定义图片文字的包裹规则
 */
public abstract class ImageWrapper {

    public abstract void parse(ExpandEditText expandEditText, String str);

    public abstract String getImageWrapper(String str);
}
