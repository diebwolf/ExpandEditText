package com.zhuangfei.expandedittext;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by Liu ZhuangFei on 2018/2/26.
 */

public class ImageEntity extends BaseEntity{

    private String text;

    private Bitmap bitmap;

    private Drawable drawable;

    private ImageView imageView;

    @Override
    public String getText() {
        return text;
    }

    @Override
    public int getType() {
        return EntityType.TYPE_IMAGE;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public ImageEntity(String text, Bitmap bitmap, ImageView imageView) {
        this.text = text;
        this.bitmap = bitmap;
        this.imageView = imageView;
    }

    public ImageEntity(String text, Drawable drawable, ImageView imageView) {
        this.text = text;
        this.drawable = drawable;
        this.imageView = imageView;
    }


}
