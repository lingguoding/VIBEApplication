package com.hao.common.image;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hao.common.R;

import java.io.File;

/**
 * @Package com.hao.common.utils
 * @作 用:统一图片加载器，当前使用Glide,可替换其他框架
 * @创 建 人: linguoding 邮箱：linggoudingg@gmail.com
 * @日 期: 2017年01月06日  09:41
 */
public class ImageLoader {
    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    private static class ImageLoaderHolder {
        private static final ImageLoader INSTANCE = new ImageLoader();
    }

    private ImageLoader() {
    }

    public static final ImageLoader getInstance() {
        return ImageLoaderHolder.INSTANCE;
    }

    //直接加载网络图片
    public void displayImage(String url, Context context, ImageView imageView, int placeholderImage) {
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .placeholder(placeholderImage)
                .centerCrop()
                .crossFade()
                .into(imageView);
    }

    public void displayImage(String url, Context context, ImageView imageView) {
        displayImage(url, context, imageView, R.mipmap.ic_defualt_loading);
    }

    public void displayLongImage(String url, Context context, ImageView imageView) {
        displayImage(url, context, imageView, R.mipmap.ic_defualt_loading);
    }

    //加载SD卡图片
    public void displayImage(Context context, File file, ImageView imageView) {
        Glide.with(context)
                .load(file)
                .centerCrop()
                .into(imageView);

    }

    //加载SD卡图片并设置大小
    public void displayImage(Context context, File file, ImageView imageView, int width, int height) {
        Glide.with(context)
                .load(file)
                .override(width, height)
                .centerCrop()
                .into(imageView);

    }

    //加载网络图片并设置大小
    public void displayImage(Context context, String url, ImageView imageView, int width, int height) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .override(width, height)
                .crossFade()
                .into(imageView);
    }

    //加载drawable图片
    public void displayImage(Context context, int resId, ImageView imageView) {
        Glide.with(context)
                .load(resourceIdToUri(context, resId))
                .crossFade()
                .into(imageView);
    }

    //加载drawable图片显示为圆形图片
    public void displayCricleImage(Context context, int resId, ImageView imageView) {
        Glide.with(context)
                .load(resourceIdToUri(context, resId))
                .crossFade()
                .transform(new GlideCircleTransform(context))
                .into(imageView);
    }

    //加载网络图片显示为圆形图片
    public void displayCricleImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .transform(new GlideCircleTransform(context))
                .crossFade()
                .into(imageView);
    }

    //加载SD卡图片显示为圆形图片
    public void displayCricleImage(Context context, File file, ImageView imageView) {
        Glide.with(context)
                .load(file)
                //.centerCrop()
                .transform(new GlideCircleTransform(context))
                .into(imageView);

    }

    //将资源ID转为Uri
    public Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FOREWARD_SLASH + resourceId);
    }
}
