package com.zhuangfei.expandedittextdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zhuangfei.expandedittext.ExpandEditText;
import com.zhuangfei.expandedittextdemo.tools.FileTools;
import com.zhuangfei.expandedittextdemo.tools.ImageTools;
import com.zhuangfei.expandedittextdemo.tools.ToastTools;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

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

        chooseButton.setOnClickListener(this);
        getTextButton.setOnClickListener(this);
    }

    private void operate() {
        expandEditText.appendText("第一行文字");
        expandEditText.appendDrawable(getResources().getDrawable(R.drawable.img2), "car");

        String text = expandEditText.getText();
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
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
                expandEditText.appendBitmap(bitmap,path);
                break;

        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_choosebutton:
                FileTools.chooseFiles(MainActivity.this,CODE_OPEN_IMAGE);
                break;
            case R.id.id_textbutton:
                ToastTools.show(MainActivity.this,expandEditText.getText());
                break;
        }
    }
}
