package com.zhuangfei.expandedittext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zhuangfei.expandedittext.utils.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liu ZhuangFei on 2018/2/26.
 */

public class ExpandEditText extends LinearLayout {

    LayoutInflater mInflate;

    EditStyle mEditStyle;

    List<BaseEntity> entityList;

    public ExpandEditText(Context context) {
        this(context, null, 0);
    }

    public ExpandEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inits();
    }

    private void inits() {
        mInflate = LayoutInflater.from(getContext());
        entityList=new ArrayList<>();
    }

    public void clear() {
        removeAllViews();
        entityList.clear();

    }

    /**
     * 获取文本
     * @return
     */
    public String getText() {
        String text="";
        for(BaseEntity entity:entityList){
            text+=entity.getText();
        }
        return text;
    }

    /**
     * 在光标处插入Bitmap
     * @param bitmap
     */
    public void insertBitmap(Bitmap bitmap){

    }

    /**
     * 在文尾追加一个Bitmap
     * @param bitmap
     */
    public void appendBitmap(Bitmap bitmap,String replace){
        appendImage(null,bitmap,replace);
    }

    /**
     * 在光标处插入Drawable
     * @param drawable
     */
    public void insertDrawable(Drawable drawable){

    }

    /**
     * 在末尾追加Drawable
     * @param drawable
     */
    public void appendDrawable(Drawable drawable,String replace){
        appendImage(drawable,null,replace);
    }

    /**
     * 追加图片的逻辑封装，drawable、bitmap哪个非空即使用哪种方式
     * @param drawable
     * @param bitmap
     * @param replace
     */
    public void appendImage(Drawable drawable, Bitmap bitmap,String replace){
        getFinalEditEntity();
        View view = mInflate.inflate(R.layout.layout_imageview, null, false);
        ImageView imageView = view.findViewById(R.id.id_expand_imageview);

        if(drawable!=null){
            imageView.setImageDrawable(drawable);
        }
        if(bitmap!=null){
            imageView.setImageBitmap(bitmap);
        }

        addView(view);
        entityList.add(new ImageEntity(replace,drawable,imageView));
        createEditEntity();
    }

    /**
     * 设置指定BaseEntity的文本
     * @param text
     */
    public void setText(String text){
        EditEntity entity=getFinalEditEntity();
        entity.setText(text);
    }


    /**
     * 追加指定BaseEntity的文本
     * @param text
     */
    public void appendText(String text){
        EditEntity entity=getFinalEditEntity();
        entity.setText(entity.getText()+text);
    }

    /**
     * 获取最后的一个可编辑实体，如果不存在，则新建
     * @return
     */
    public EditEntity getFinalEditEntity(){
        int size=entityList.size();
        EditEntity resultEntity;
        if(size>0){
            BaseEntity entity=entityList.get(size-1);
            if(entity.getType()==EntityType.TYPE_EDIT){
                resultEntity= (EditEntity) entity;
            }else{
                resultEntity=createEditEntity();
            }
        }else{
            resultEntity=createEditEntity();
        }
        return resultEntity;
    }

    /**
     * 创建可编辑的实体
     */
    public EditEntity createEditEntity(){
        View view=mInflate.inflate(R.layout.layout_edittext,null,false);
        EditText editText = view.findViewById(R.id.id_expand_edittext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(lp);
        addView(editText);
        EditEntity entity=new EditEntity(editText);
        entityList.add(entity);
        setOnKeyListener(entity);
        return entity;
    }

    public void setOnKeyListener(EditEntity entity){
        final EditEntity tmpEditEntity=entity;
        final EditText tmpEditText=entity.getEditText();
        tmpEditText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN&&i==KeyEvent.KEYCODE_DEL){
                    Log.i("DELETE","点击删除按键");
                    onDeleteEvent(tmpEditEntity);
                }
                return false;
            }
        });
    }

    private void onDeleteEvent(EditEntity entity) {
        int index=entityList.indexOf(entity);
        Log.i("Index",""+index);
        Log.i("Length",entityList.size()+"");
        if(index>0){
            EditText editText=entity.getEditText();
            //执行删除操作
            if(editText!=null&&editText.getText().toString().length()==0){
                BaseEntity preEntity=entityList.get(index-1);

                //1.上一个元素为图片时，直接清除上一个元素即可
                if(preEntity!=null&&EntityUtils.isImageEntity(preEntity)){
                    removeExpandViewAt(index-1);
                }

                //2.上一个元素是EditText时，清除本身，移动鼠标位置至上个元素末尾
                if(preEntity!=null&& EntityUtils.isEditEntity(preEntity)){
                    removeExpandViewAt(index);
                    EditEntity preEditEntity= EntityUtils.getEditEntity(preEntity);
                    EditText preEditText=preEditEntity.getEditText();
                    String preText=preEditText.getText().toString();
                    int start=0;
                    if(!TextUtils.isEmpty(preText)){
                        start=preText.length();
                    }
                    preEditText.setSelection(start,start);
                }
            }
        }
    }

    public void removeExpandViewAt(int index){
        entityList.remove(index);
        removeViewAt(index);
    }
}
