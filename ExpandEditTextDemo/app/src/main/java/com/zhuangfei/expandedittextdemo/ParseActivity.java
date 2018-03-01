package com.zhuangfei.expandedittextdemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zhuangfei.expandedittext.ExpandEditText;
import com.zhuangfei.expandedittext.entity.ImageEntity;
import com.zhuangfei.expandedittext.listener.OnExpandImageClickListener;
import com.zhuangfei.expandedittextdemo.tools.ActivityTools;
import com.zhuangfei.expandedittextdemo.tools.ToastTools;

import java.util.List;

public class ParseActivity extends AppCompatActivity implements OnExpandImageClickListener{

    ExpandEditText expandEditText;

    String[] urlArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse);
        inits();

        new CreateTask().execute();
    }

    private void inits() {
        expandEditText = findViewById(R.id.id_parse_edittext);
        expandEditText.setOnExpandImageClickListener(this);

        String[] arr={
                "http://b.hiphotos.baidu.com/zhidao/pic/item/1f178a82b9014a90e7eb9d17ac773912b21bee47.jpg",
                "http://d.hiphotos.baidu.com/zhidao/pic/item/72f082025aafa40fe871b36bad64034f79f019d4.jpg",
                "http://f.hiphotos.baidu.com/zhidao/pic/item/902397dda144ad3464639dc8d1a20cf430ad85a4.jpg",
                "http://i9.download.fd.pchome.net/t_960x600/g1/M00/0B/10/oYYBAFQlOmuIZDQRAALvMZ8mYRIAAB9HAKQEtcAAu9J875.jpg",
                "http://pic1.win4000.com/wallpaper/c/53d715df6274d.jpg",
                "http://images.ali213.net/picfile/pic/2013/03/12/927_hyss%20%281%29.jpg",
                "http://pic1.16pic.com/00/52/38/16pic_5238084_b.jpg",
                "http://img3.duitang.com/uploads/item/201504/11/20150411H1005_Gr4PZ.jpeg",
                "http://t1.niutuku.com/960/37/37-82837.jpg",
                "http://desk.fd.zol-img.com.cn/g5/M00/03/06/ChMkJ1YwYtKISoBIAAaZRhacogwAAEM_wOl-qMABple152.jpg",
                "http://b.hiphotos.baidu.com/zhidao/pic/item/908fa0ec08fa513d2fd87b813b6d55fbb2fbd97a.jpg",
                "http://cdn.duitang.com/uploads/item/201604/01/20160401070646_m3kE2.jpeg",
                "http://img4.duitang.com/uploads/item/201407/18/20140718230003_ZFX2f.jpeg",
                "http://img5.duitang.com/uploads/item/201507/14/20150714202944_jP3rX.thumb.700_0.jpeg",
                "http://img4.duitang.com/uploads/item/201303/14/20130314081619_ehShf.thumb.700_0.jpeg",
                "http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1207/13/c0/12320084_1342149513439.jpg",
                "http://img4.duitang.com/uploads/item/201507/14/20150714202446_VT5P4.jpeg",
                "http://img5.duitang.com/uploads/item/201409/28/20140928221838_sMkEM.jpeg"
        };

        urlArray=arr;
    }

    @Override
    public void onImageClick(View view, ImageEntity imageEntity) {
        ToastTools.show(this,imageEntity.getText());
    }

    class CreateTask extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            String result="图文混排的示例效果如下：\n\n";
            for(int i=0;i<urlArray.length;i++){
                result+="图片地址:"+urlArray[i];
                result+="![img]("+urlArray[i]+")";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            expandEditText.load(s);
        }
    }

    @Override
    public void onBackPressed() {
        ActivityTools.toBackActivityAnim(this,MainActivity.class);
        finish();
    }
}
