package com.zhuangfei.expandedittext;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
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

import com.zhuangfei.expandedittext.listener.OnExpandImageClickListener;
import com.zhuangfei.expandedittext.utils.BitmapUtils;
import com.zhuangfei.expandedittext.utils.DipUtils;
import com.zhuangfei.expandedittext.utils.EntityUtils;
import com.zhuangfei.expandedittext.utils.InputMethodUtils;
import com.zhuangfei.expandedittext.wrapper.DefaultImageWrapper;
import com.zhuangfei.expandedittext.wrapper.ImageWrapper;
import com.zhuangfei.expandedittext.wrapper.OriginalImageWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liu ZhuangFei on 2018/2/26.
 */

public class ExpandEditText extends LinearLayout {

    private LayoutInflater mInflate;

    private EditStyle editStyle;

    private List<BaseEntity> entityList;

    private Activity activity;

    private OnExpandImageClickListener onExpandImageClickListener;

    private ImageWrapper wrapper;

    private boolean isOpenWrapper = true;

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
    }

    public ExpandEditText bind(Activity activity) {
        this.activity = activity;
        return this;
    }

    public ExpandEditText setOnExpandImageClickListener(OnExpandImageClickListener onExpandImageClickListener) {
        this.onExpandImageClickListener = onExpandImageClickListener;
        return this;
    }

    public ExpandEditText setWrapper(ImageWrapper wrapper) {
        this.wrapper = wrapper;
        return this;
    }

    public ExpandEditText setOpenWrapper(boolean openWrapper) {
        isOpenWrapper = openWrapper;
        return this;
    }

    public List<BaseEntity> getEntityList() {
        return entityList;
    }

    public EditStyle getEditStyle() {
        return editStyle;
    }

    public void clear() {
        removeAllViews();
        entityList.clear();
    }

    public BaseEntity getEntity(int index) {
        if (index < 0 || index >= entityList.size()) return null;
        return entityList.get(index);
    }

    public int indexOfEntity(BaseEntity entity) {
        return entityList.indexOf(entity);
    }

    private void initWrapper() {
        if (isOpenWrapper) {
            if (wrapper == null) wrapper = new DefaultImageWrapper();
        } else {
            wrapper = new OriginalImageWrapper();
        }
    }

    /**
     * 获取文本
     *
     * @return
     */
    public String getText() {
        String text = "";
        initWrapper();
        for (BaseEntity entity : entityList) {
            if (entity.getType() == EntityType.TYPE_IMAGE) {
                text += wrapper.getImageWrapper(entity.getText());
            }else{
                text += entity.getText();
            }
            text+="\n";
        }

        return text;
    }

    public String getText(BaseEntity entity) {
        if (entity == null) return null;
        initWrapper();
        return wrapper.getImageWrapper(entity.getText());
    }

    /**
     * 在光标处插入Bitmap
     *
     * @param bitmap
     */
    public void insertBitmap(Bitmap bitmap) {

    }

    /**
     * 在文尾追加一个Bitmap
     *
     * @param bitmap
     */
    public void appendBitmap(Bitmap bitmap, String replace) {
        createImageEntity(null, bitmap, replace);
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
        createImageEntity(drawable, null, replace);
    }

    /**
     * 追加图片的逻辑封装，drawable、bitmap哪个非空即使用哪种方式
     *
     * @param drawable
     * @param bitmap
     * @param replace
     */
    public void createImageEntity(Drawable drawable, Bitmap bitmap, String replace) {
        getFinalEditEntity();
        View view = mInflate.inflate(R.layout.layout_imageview, null, false);
        addView(view);

        ImageView imageView = view.findViewById(R.id.id_expand_imageview);
        Bitmap newBitmap = null;
        ImageEntity imageEntity = null;
        if (drawable != null) {
            imageView.setImageDrawable(drawable);
            imageEntity = new ImageEntity(replace, drawable, imageView);
        }

        if (bitmap != null) {
            newBitmap = BitmapUtils.zoomAdapter(bitmap, getWidth() - DipUtils.dip2px(getContext(), 40));
            imageView.setImageBitmap(newBitmap);
            imageEntity = new ImageEntity(replace, newBitmap, imageView);
        }

        if (imageEntity != null) {
            entityList.add(imageEntity);
            setOnImageListener(imageEntity);
            createEditEntity();
        }
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
        entity.setText(getText(entity) + text);
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
                resultEntity = createEditEntity();
            }
        } else {
            resultEntity = createEditEntity();
        }
        return resultEntity;
    }

    /**
     * 创建可编辑的实体
     */
    public EditEntity createEditEntity() {
        View view = mInflate.inflate(R.layout.layout_edittext, null, false);
        EditText editText = view.findViewById(R.id.id_expand_edittext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(lp);
        addView(editText);
        EditEntity entity = new EditEntity(editText);
        entityList.add(entity);
        setOnKeyListener(entity);
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
                }
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
        String value = getText(entity);
        EditEntity preEditEntity = EntityUtils.getEditEntity(preEntity);
        String preValue = getText(preEditEntity);

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
