package com.zhuangfei.expandedittext;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;

import com.zhuangfei.expandedittext.listener.OnExpandBuildListener;
import com.zhuangfei.expandedittext.listener.OnExpandImageClickListener;
import com.zhuangfei.expandedittext.utils.BitmapUtils;
import com.zhuangfei.expandedittext.utils.DipUtils;
import com.zhuangfei.expandedittext.utils.EntityUtils;
import com.zhuangfei.expandedittext.utils.InputMethodUtils;
import com.zhuangfei.expandedittext.wrapper.DefaultImageWrapper;
import com.zhuangfei.expandedittext.wrapper.ImageWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liu ZhuangFei on 2018/2/26.
 */

public class ExpandEditText extends LinearLayout {

    private LayoutInflater mInflate;

    private List<BaseEntity> entityList;

    private Activity activity;

    private OnExpandImageClickListener onExpandImageClickListener;

    private OnExpandBuildListener onExpandBuildListener;

    private ImageWrapper wrapper;

    private String hintText = "";

    private boolean isShowModel = false;

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
        entityList = new ArrayList<>();
        addFocusListener();
        createEditEntity(getEntityList().size());
    }

    public ExpandEditText bind(Activity activity) {
        this.activity = activity;
        return this;
    }

    public ExpandEditText setOnExpandImageClickListener(OnExpandImageClickListener onExpandImageClickListener) {
        this.onExpandImageClickListener = onExpandImageClickListener;
        return this;
    }

    public ExpandEditText setOnExpandBuildListener(OnExpandBuildListener onExpandBuildListener) {
        this.onExpandBuildListener = onExpandBuildListener;
        return this;
    }

    public ExpandEditText setWrapper(ImageWrapper wrapper) {
        this.wrapper = wrapper;
        return this;
    }

    public ExpandEditText setHintText(String hintText) {
        this.hintText = hintText;
        changeHint();
        return this;
    }

    public ExpandEditText setShowModel(boolean showModel) {
        isShowModel = showModel;
        return this;
    }

    public List<BaseEntity> getEntityList() {
        return entityList;
    }

    public void clear() {
        removeAllViews();
        entityList.clear();
    }

    public BaseEntity getEntity(int index) {
        if (index < 0 || index >= entityList.size()) return null;
        return entityList.get(index);
    }

    public ImageWrapper getWrapper() {
        if(wrapper==null){
            wrapper=new DefaultImageWrapper();
        }
        return wrapper;
    }

    public String getHintText() {
        return hintText;
    }

    public boolean isShowModel() {
        return isShowModel;
    }

    public int indexOfEntity(BaseEntity entity) {
        return entityList.indexOf(entity);
    }

    /**
     * 将文本解析为图文混合
     *
     * @param text
     */
    public void load(String text) {
        getWrapper().parse(this,text);
    }

    public ImageEntity parseImageEntity(String localPath) {
        View view = mInflate.inflate(R.layout.layout_imageview, null, false);
        addView(view);

        ImageView imageView = view.findViewById(R.id.id_expand_imageview);
        Bitmap bitmap = BitmapFactory.decodeFile(localPath);
        Bitmap newBitmap = BitmapUtils.zoomAdapter(bitmap, getWidth() - DipUtils.dip2px(getContext(), 35));
        imageView.setImageBitmap(newBitmap);
        ImageEntity imageEntity = new ImageEntity(localPath, newBitmap, imageView);
        entityList.add(imageEntity);
        setOnImageListener(imageEntity);
        return imageEntity;
    }

    /**
     * 创建TextView
     */
    public TextEntity parseTextEntity() {
        View view = mInflate.inflate(R.layout.layout_text, null, false);
        TextView textView = view.findViewById(R.id.id_expand_text);
        addView(textView);

        if (onExpandBuildListener != null) {
            onExpandBuildListener.onTextBuild(textView);
        }

        TextEntity textEntity = new TextEntity(textView);
        entityList.add(textEntity);
        return textEntity;
    }

    public TextEntity parseTextEntity(String text) {
        TextEntity textEntity=parseTextEntity();
        textEntity.setText(text);
        return textEntity;
    }

    /**
     * 获取文本
     *
     * @return
     */
    public String getText() {
        String text = "";
        for (BaseEntity entity : entityList) {
            if (entity.getType() == EntityType.TYPE_IMAGE) {
                text += getWrapper().getImageWrapper(entity.getText());
            } else {
                text += entity.getText();
            }
        }

        return text;
    }

    private int indexOfCursor() {
        for (int i = 0; i < entityList.size(); i++) {
            BaseEntity entity = entityList.get(i);
            if (entity.getType() == EntityType.TYPE_EDIT) {
                EditEntity editEntity = EntityUtils.getEditEntity(entity);
                if (editEntity.getEditText().isFocused()) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 在光标处插入Bitmap
     *
     * @param bitmap
     */
    public void insertBitmap(Bitmap bitmap, String replace) {
        int index = indexOfCursor();
        if (index != -1) {
            createImageEntity(null, bitmap, replace, index + 1);
        } else {
            appendBitmap(bitmap, replace);
        }
    }

    /**
     * 在文尾追加一个Bitmap
     *
     * @param bitmap
     */
    public void appendBitmap(Bitmap bitmap, String replace) {
        createImageEntity(null, bitmap, replace, entityList.size());
    }

    /**
     * 在光标处插入Drawable
     *
     * @param drawable
     */
    public void insertDrawable(Drawable drawable) {

    }

    /**
     * 在末尾追加Drawable
     *
     * @param drawable
     */
    public void appendDrawable(Drawable drawable, String replace) {
        createImageEntity(drawable, null, replace, entityList.size());
    }

    /**
     * 追加图片的逻辑封装，drawable、bitmap哪个非空即使用哪种方式
     *
     * @param drawable
     * @param bitmap
     * @param replace
     */
    public ImageEntity createImageEntity(Drawable drawable, Bitmap bitmap, String replace, int index) {
        getFinalEditEntity();
        View view = mInflate.inflate(R.layout.layout_imageview, null, false);
        addView(view, index);

        ImageView imageView = view.findViewById(R.id.id_expand_imageview);
        Bitmap newBitmap = null;
        ImageEntity imageEntity = null;
        if (drawable != null) {
            imageView.setImageDrawable(drawable);
            imageEntity = new ImageEntity(replace, drawable, imageView);
        }

        if (bitmap != null) {
            newBitmap = BitmapUtils.zoomAdapter(bitmap, getWidth() - DipUtils.dip2px(getContext(), 35));
            imageView.setImageBitmap(newBitmap);
            imageEntity = new ImageEntity(replace, newBitmap, imageView);
        }

        if (imageEntity != null) {
            entityList.add(index,imageEntity);
            setOnImageListener(imageEntity);
            createEditEntity(entityList.size());
        }

        EditEntity afterEditEntity=getEditEntityAfter(index);
        changeHint();
        requestEditFocus(afterEditEntity);
        return imageEntity;
    }

    private void setOnImageListener(final ImageEntity imageEntity) {
        if (imageEntity == null) return;
        final ImageView imageView = imageEntity.getImageView();
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onExpandImageClickListener != null) {
                    onExpandImageClickListener.onImageClick(imageView, imageEntity);
                }
            }
        });
    }

    /**
     * 设置指定BaseEntity的文本
     *
     * @param text
     */
    public void setText(String text) {
        EditEntity entity = getFinalEditEntity();
        entity.setText(text);
    }


    /**
     * 追加指定BaseEntity的文本
     *
     * @param text
     */
    public void appendText(String text) {
        EditEntity entity = getFinalEditEntity();
        entity.setText(entity.getText() + text);
    }

    /**
     * 获取最后的一个可编辑实体，如果不存在，则新建
     *
     * @return
     */
    public EditEntity getFinalEditEntity() {
        int size = entityList.size();
        EditEntity resultEntity;
        if (size > 0) {
            BaseEntity entity = entityList.get(size - 1);
            if (entity.getType() == EntityType.TYPE_EDIT) {
                resultEntity = (EditEntity) entity;
            } else {
                resultEntity = createEditEntity(entityList.size());
            }
        } else {
            resultEntity = createEditEntity(entityList.size());
        }
        return resultEntity;
    }

    public EditEntity getEditEntityAfter(int index) {
        int size = entityList.size();
        EditEntity resultEntity=null;
        if(index>=(size-1)&&index>=0){
            resultEntity=createEditEntity(size);
        }else{
            BaseEntity entity=entityList.get(index+1);
            if(entity.getType()==EntityType.TYPE_EDIT){
                resultEntity=EntityUtils.getEditEntity(entity);
            }else{
                resultEntity=createEditEntity(index+1);
            }
        }
        return resultEntity;
    }

    /**
     * 创建可编辑的实体
     */
    public EditEntity createEditEntity(int index) {
        View view = mInflate.inflate(R.layout.layout_edittext, null, false);
        EditText editText = view.findViewById(R.id.id_expand_edittext);
        addView(editText,index);
        if (entityList.size() == 0) {
            editText.setHint(hintText);
        }

        if (onExpandBuildListener != null) {
            onExpandBuildListener.onEditBuild(editText);
        }

        EditEntity entity = new EditEntity(editText);
        entityList.add(index,entity);
        setOnKeyListener(entity);
        changeHint();
        return entity;
    }

    public void setOnKeyListener(EditEntity entity) {
        final EditEntity tmpEditEntity = entity;
        final EditText tmpEditText = entity.getEditText();
        tmpEditText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_DEL) {
                    Log.i("DELETE", "点击删除按键");
                    onDeleteEvent(tmpEditEntity);
                }
                return false;
            }
        });
    }

    private void onDeleteEvent(EditEntity entity) {
        int index = entityList.indexOf(entity);
        Log.i("Index", "" + index);
        Log.i("Length", entityList.size() + "");
        if (index > 0) {
            EditText editText = entity.getEditText();
            int cursorIndex = editText.getSelectionStart();
            //执行删除操作
            if (editText != null && cursorIndex == 0) {
                Log.i("REMOVE", "prepre remove...");
                BaseEntity preEntity = entityList.get(index - 1);

                //1.上一个元素为图片时，直接清除上一个元素即可
                if (preEntity != null && EntityUtils.isImageEntity(preEntity)) {
                    removeExpandViewAt(index - 1);
                }

                //2.上一个元素是EditText时，清除本身，移动鼠标位置至上个元素末尾并将本身文字追加到上个元素末尾
                if (preEntity != null && EntityUtils.isEditEntity(preEntity)) {
                    Log.i("REMOVE", "remove editEntity...");
                    mergeEditEntity(entity, preEntity, index);
                    EditEntity preEditEntity = EntityUtils.getEditEntity(preEntity);
                }
            }
        }

        changeHint();
    }

    public void changeHint() {
        if (getText().isEmpty()) {
            clearHint();
            EditEntity entity = getFirstEditEntity();
            entity.getEditText().setHint(hintText);
        }
    }

    /**
     * 获取第一个可编辑实体，如果不存在，则新建
     *
     * @return
     */
    public EditEntity getFirstEditEntity() {
        int size = entityList.size();
        EditEntity resultEntity = null;
        if (size > 0) {
            boolean isHaveEditEntity = false;
            for (BaseEntity entity : entityList) {
                if (entity.getType() == EntityType.TYPE_EDIT) {
                    resultEntity = (EditEntity) entity;
                    isHaveEditEntity = true;
                    break;
                }
            }
            if (!isHaveEditEntity) resultEntity = createEditEntity(0);
        } else {
            resultEntity = createEditEntity(entityList.size());
        }
        return resultEntity;
    }

    public void clearHint() {
        //清除Hint
        for (BaseEntity entity : entityList) {
            if (entity.getType() == EntityType.TYPE_EDIT) {
                EditEntity editEntity = EntityUtils.getEditEntity(entity);
                editEntity.getEditText().setHint("");
            }
        }
    }

    /**
     * 进行实体的合并
     *
     * @param entity
     * @param preEntity
     * @param entityIndex
     */
    public void mergeEditEntity(EditEntity entity, BaseEntity preEntity, int entityIndex) {
        String value = entity.getText();
        EditEntity preEditEntity = EntityUtils.getEditEntity(preEntity);
        String preValue = preEditEntity.getText();

        removeExpandViewAt(entityIndex);
        int start = 0;
        if (!TextUtils.isEmpty(preValue)) {
            start = preValue.length();
        }

        preEditEntity.setText(preValue + value);

        preEditEntity.getEditText().requestFocus();
        preEditEntity.getEditText().setSelection(start);
    }

    public void removeExpandViewAt(int index) {
        entityList.remove(index);
        removeViewAt(index);
    }

    public void requestFinalEditFocus() {
        EditEntity entity = getFinalEditEntity();
        EditText editText = entity.getEditText();
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.findFocus();
    }

    public void requestEditFocus(EditEntity editEntity){
        EditText editText = editEntity.getEditText();
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.findFocus();
    }

    /**
     * 设置点击空白区域则将最后一个可编辑的区域设为焦点
     */
    public void addFocusListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                requestFinalEditFocus();
                if (activity != null) {
                    InputMethodUtils.open(activity);
                }
            }
        });
    }
}
