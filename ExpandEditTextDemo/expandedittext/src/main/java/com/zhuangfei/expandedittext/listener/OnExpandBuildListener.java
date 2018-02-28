package com.zhuangfei.expandedittext.listener;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuangfei.expandedittext.EditEntity;
import com.zhuangfei.expandedittext.ImageEntity;

import org.w3c.dom.Text;

/**
 * Created by Liu ZhuangFei on 2018/2/28.
 */

public interface OnExpandBuildListener {

    public void onEditBuild(EditText editText);

    public void onTextBuild(TextView textView);

}
