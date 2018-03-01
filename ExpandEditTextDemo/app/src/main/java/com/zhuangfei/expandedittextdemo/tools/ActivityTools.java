package com.zhuangfei.expandedittextdemo.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.WindowManager;

import com.zhuangfei.expandedittextdemo.R;

import java.io.Serializable;

public class ActivityTools {
	/**
	 * 使用默认的入场动画效果
	 * 从context跳转到target页面
	 * @param context
	 * @param target
	 */
	public static void toActivity(Activity context,Class<?> target){
		Intent intent=new Intent(context,target);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.slide2_in, R.anim.slide2_out);//动画
	}

	public static void toBackActivityAnim(Activity context,Class<?> target){
		Intent intent=new Intent(context,target);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);//动画
		context.finish();
	}
}
