package com.zhuangfei.expandedittext.wrapper;

/**
 * Created by Liu ZhuangFei on 2018/2/27.
 */

public class DefaultImageWrapper extends ImageWrapper {
    @Override
    public String getImageWrapper(String str) {
        return "![img]("+str+")";
    }
}
