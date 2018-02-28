package com.zhuangfei.expandedittextdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zhuangfei.expandedittext.EditEntity;
import com.zhuangfei.expandedittext.ExpandEditText;
import com.zhuangfei.expandedittext.ImageEntity;
import com.zhuangfei.expandedittext.listener.OnExpandImageClickListener;
import com.zhuangfei.expandedittextdemo.tools.FileTools;
import com.zhuangfei.expandedittextdemo.tools.ImageTools;
import com.zhuangfei.expandedittext.utils.InputMethodUtils;
import com.zhuangfei.expandedittextdemo.tools.ToastTools;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,OnExpandImageClickListener{

    ExpandEditText expandEditText;

    Button chooseButton;

    Button getTextButton;

    public static final int CODE_OPEN_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inits();
        operate();
    }

    private void inits() {
        expandEditText = findViewById(R.id.id_edittext);
        chooseButton = findViewById(R.id.id_choosebutton);
        getTextButton=findViewById(R.id.id_textbutton);

        expandEditText.bind(this)
                .setOnExpandImageClickListener(this)
//                .load("![img](/storage/emulated/0/DCIM/Camera/IMG_20180220_201403.jpg)5588\n" +
//                        "dajs\n" +
//                        "zhuangfei\n" +
//                        "\n" +
//                        "is dalao![img](/storage/emulated/0/DCIM/Camera/IMG_20180220_201401.jpg)");
                .setHintText("说些什么吧~");

        chooseButton.setOnClickListener(this);
        getTextButton.setOnClickListener(this);
    }

    private void operate() {
//        expandEditText.appendText("第一行文字");
//        expandEditText.appendDrawable(getResources().getDrawable(R.drawable.img2), "car");
//
//        String text = expandEditText.getText();
//        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

//        for(int i=1;i<=5;i++){
//            EditEntity entity=expandEditText.createEditEntity();
//            entity.getEditText().setText("第"+i+"个EditText");
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {// 取消
            return;
        }
        switch (requestCode) {
            case CODE_OPEN_IMAGE:
                Uri uri = intent.getData();
                String path = ImageTools.getPathFromUri(MainActivity.this, uri);
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                expandEditText.insertBitmap(bitmap,path);
                break;

        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_choosebutton:
                InputMethodUtils.close(MainActivity.this);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0x123);
                    }
                },50);

                break;
            case R.id.id_textbutton:
                Log.i("Text",expandEditText.getText());
                ToastTools.show(MainActivity.this,expandEditText.getText());
                break;
        }
    }

    @Override
    public void onImageClick(View view, ImageEntity imageEntity) {
        ToastTools.show(this,"url:"+imageEntity.getText());
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            FileTools.chooseFiles(MainActivity.this,CODE_OPEN_IMAGE);
        }
    };

}
