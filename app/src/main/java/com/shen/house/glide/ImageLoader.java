package com.shen.house.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.shen.baselibrary.ContextHouse;
import com.shen.baselibrary.utiles.ExecutorUtile;
import com.shen.house.GlideApp;

public class ImageLoader {
    private ImageLoader() {
    }

    public static final ImageLoader getInstance() {
        return ImageLoaderHolder.INSTANCE;
    }

    public void displayImage(Context context, ImageView imageView, String url) {
        displayImage(context, imageView, url, null);
    }

    public void displayImage(Context context, ImageView imageView, String url, DecodeFormat format) {
        DefaultImgDrawable imgDrawable = new DefaultImgDrawable(context);
        displayImage(context, imageView, url, format, imgDrawable, imgDrawable, imgDrawable, null);
    }

    public void displayImage(Context context, ImageView imageView, String url, DecodeFormat format, Drawable imgDrawable, Animation anim) {
        displayImage(context, imageView, url, format, imgDrawable, imgDrawable, imgDrawable, anim);
    }

    public void displayImage(Context context, ImageView imageView, String url, DecodeFormat format, Drawable place, Drawable error, Drawable fallback, Animation anim) {
        if (format != null) {
            GlideApp.with(context).asBitmap().format(format).load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .placeholder(place)//这个有问题
                    .error(error).fallback(fallback).into(imageView);
//            if (anim == null) {
//                load = requests.dontAnimate();
//            } else {
//                load = requests.animate(anim);
//            }
        } else {
            GlideApp.with(context).load(url).transition(DrawableTransitionOptions.withCrossFade()).diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .placeholder(place)
                    .error(error).fallback(fallback)
                    .into(imageView);
        }
    }


    public void clear(Context context) {
        Glide.get(context).clearMemory();
    }

    public void getBitmap(String uri, final CallBack call) {
        GlideApp.with(ContextHouse.getContext()).asBitmap().load(uri).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                call.onBack(resource);
            }
        });
    }

    public interface CallBack {
        void onBack(Bitmap b);
    }

    private static class ImageLoaderHolder {
        private static final ImageLoader INSTANCE = new ImageLoader();

    }

    public void clearCache(final Context context) {
        ExecutorUtile.runInSubThred(new Runnable() {
            @Override
            public void run() {
                try {
                    GlideApp.get(context).clearMemory();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                GlideApp.get(context).clearDiskCache();//由于删除了文件夹，导致清除缓存后 不重启应用 图片无法加载。
            }
        });


    }
}
