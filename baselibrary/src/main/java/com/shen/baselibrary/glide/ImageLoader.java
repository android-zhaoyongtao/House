//package com.shen.baselibrary.glide;
//
//import android.content.Context;
//import android.content.res.ColorStateList;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.ColorFilter;
//import android.graphics.PixelFormat;
//import android.graphics.PorterDuff;
//import android.graphics.Rect;
//import android.graphics.Region;
//import android.graphics.drawable.Drawable;
//import android.support.v4.graphics.drawable.DrawableCompat;
//import android.view.animation.Animation;
//import android.widget.ImageView;
//
//import com.bumptech.glide.DrawableTypeRequest;
//import com.bumptech.glide.GenericRequestBuilder;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.RequestBuilder;
//import com.bumptech.glide.load.DecodeFormat;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.request.animation.GlideAnimation;
//import com.bumptech.glide.request.target.SimpleTarget;
//import com.xin.ContextXin;
//import com.xin.dbm.ui.view.XinImageView;
//
//public class ImageLoader {
//
//    private ImageLoader() {
//    }
//
//    public static final ImageLoader getInstance() {
//        return ImageLoaderHolder.INSTANCE;
//    }
//
//    public void displayImage(Context context, ImageView imageView, String url) {
//        displayImage(context, imageView, url, DecodeFormat.PREFER_RGB_565);
//    }
//
//    public void displayImage(Context context, ImageView imageView, String url, DecodeFormat format) {
//        DefaultImgDrawable imgDrawable = new DefaultImgDrawable(context);
//        displayImage(context, imageView, url, format, imgDrawable, imgDrawable, imgDrawable, null);
//    }
//
//    public void displayImage(Context context, ImageView imageView, String url, DecodeFormat format, Drawable imgDrawable, Animation anim) {
//        displayImage(context, imageView, url, format, imgDrawable, imgDrawable, imgDrawable, anim);
//    }
//
//    public void displayImage(Context context, ImageView imageView, String url, DecodeFormat format, Drawable place, Drawable error, Drawable fallback, Animation anim) {
//        getRequest(context, url, format, place, error, fallback, anim).into(imageView);
//        GlideLoade
//    }
//
//    public RequestBuilder<Drawable> getRequest(Context context, String url, DecodeFormat format, Drawable place, Drawable error, Drawable fallback, Animation anim) {
//        GenericRequestBuilder load = Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL);
//        if (format != null) {
//            load.
//            load = ((DrawableTypeRequest) load).asBitmap().format(format);
//        }
//        if (anim == null) {
//            load = load.dontAnimate();
//        } else {
//            load = load.animate(anim);
//        }
//        load.placeholder(place).error(error).fallback(fallback);
//        return load;
//    }
//
//    public void clear(Context context) {
//        Glide.get(context).clearMemory();
//    }
//
//    public void getBitmap(String uri, final CallBack call) {
//        Glide.with(ContextXin.getContext()).load(uri).asBitmap().into(new SimpleTarget<Bitmap>() {
//
//            @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                call.onBack(resource);
//            }
//        });
//    }
//
//    public interface CallBack {
//        void onBack(Bitmap b);
//    }
//
//    private static class ImageLoaderHolder {
//        private static final ImageLoader INSTANCE = new ImageLoader();
//
//    }
//    static class DefaultImgDrawable extends Drawable {
//
//        private static final OnBoundChangeListener DEFAULT_LISTENER = new OnBoundChangeListener() {
//            @Override
//            public Rect[] onBoundChange(Rect bounds) {
//                Rect[] rects = new Rect[2];
//                rects[0] = bounds;//背景颜色
//                rects[1] = new Rect();
//                float dp50 = ContextXin.DP1 * 50;
//                float dp250 = ContextXin.DP1 * 250;
//                int width = bounds.width();
//                int height = bounds.height();
//                int widthInner = 0;//前景图片的宽度
//                if (width > dp250) {//大图占用了整个 view 的3.5分之一
//                    widthInner = (int) (width / 3.5);
//                } else if (width > dp50) {//小图占用了整个 view 的2.4分支一
//                    widthInner = (int) (width / 2.4);
//                }
//                int heightInner = (int) (widthInner * 0.33);//
//                if (height < heightInner * 2) {//drawable 的高度太低了，就不显示前景色了
//                    widthInner = 0;
//                    heightInner = 0;
//                }
//                rects[1].left = bounds.left + (width - widthInner) / 2;
//                rects[1].right = rects[1].left + widthInner;
//                rects[1].top = bounds.top + (height - heightInner) / 2;
//                rects[1].bottom = rects[1].top + heightInner;
//                return rects;
//            }
//        };
//        private final Drawable[] mDrawables;
//        private OnBoundChangeListener mListener = DEFAULT_LISTENER;
//
//        public DefaultImgDrawable(Context context) {
//            this(context.getResources().getDrawable(R.drawable.bg_image_default_small), context.getResources().getDrawable(R.drawable.ic_empty_logo));
//        }
//
//        public DefaultImgDrawable(Drawable... fore) {
//            mDrawables = fore;
//        }
//
//        public void setListener(OnBoundChangeListener listener) {
//            this.mListener = listener;
//        }
//
//        @Override
//        public void draw(Canvas canvas) {
//            for (Drawable mDrawable : mDrawables) {
//                mDrawable.draw(canvas);
//            }
//        }
//
//        @Override
//        protected void onBoundsChange(Rect bounds) {
//            Rect[] rects = mListener.onBoundChange(bounds);
//            for (int i = 0; i < mDrawables.length; i++) {
//                mDrawables[i].setBounds(rects[i]);
//            }
//        }
//
//        @Override
//        public int getChangingConfigurations() {
//            if (mDrawables.length > 0) {
//                return mDrawables[0].getChangingConfigurations();
//            }
//            return 0;
//        }
//
//        @Override
//        public void setChangingConfigurations(int configs) {
//            for (Drawable mDrawable : mDrawables) {
//                mDrawable.setChangingConfigurations(configs);
//            }
//        }
//
//        @Override
//        public void setDither(boolean dither) {
//            for (Drawable mDrawable : mDrawables) {
//                mDrawable.setDither(dither);
//            }
//        }
//
//        @Override
//        public void setFilterBitmap(boolean filter) {
//            for (Drawable mDrawable : mDrawables) {
//                mDrawable.setFilterBitmap(filter);
//            }
//        }
//
//        @Override
//        public void setAlpha(int alpha) {
//            for (Drawable mDrawable : mDrawables) {
//                mDrawable.setAlpha(alpha);
//            }
//        }
//
//        @Override
//        public void setColorFilter(ColorFilter cf) {
//            for (Drawable mDrawable : mDrawables) {
//                mDrawable.setColorFilter(cf);
//            }
//        }
//
//        @Override
//        public boolean isStateful() {
//            boolean state = false;
//            for (Drawable mDrawable : mDrawables) {
//                state |= mDrawable.isStateful();
//            }
//            return state;
//        }
//
//        @Override
//        public boolean setState(final int[] stateSet) {
//            boolean state = false;
//            for (Drawable mDrawable : mDrawables) {
//                state |= mDrawable.setState(stateSet);
//            }
//            return state;
//        }
//
//        @Override
//        public int[] getState() {
//            if (mDrawables.length > 0) {
//                return mDrawables[0].getState();
//            }
//            return null;
//        }
//
//        @Override
//        public Drawable getCurrent() {
//            if (mDrawables.length > 0) {
//                return mDrawables[0].getCurrent();
//            }
//            return null;
//        }
//
//        @Override
//        public boolean setVisible(boolean visible, boolean restart) {
//            boolean state = super.setVisible(visible, restart);
//            for (Drawable mDrawable : mDrawables) {
//                state |= mDrawable.setVisible(visible, restart);
//            }
//            return state;
//        }
//
//        @Override
//        public int getOpacity() {
//            if (mDrawables.length > 0) {
//                return mDrawables[0].getOpacity();
//            }
//            return PixelFormat.UNKNOWN;
//        }
//
//        @Override
//        public Region getTransparentRegion() {
//            if (mDrawables.length > 0) {
//                return mDrawables[0].getTransparentRegion();
//            }
//            return null;
//        }
//
//        @Override
//        public int getIntrinsicWidth() {
//            if (mDrawables.length > 0) {
//                return mDrawables[0].getIntrinsicWidth();
//            }
//            return 0;
//        }
//
//        @Override
//        public int getIntrinsicHeight() {
//            if (mDrawables.length > 0) {
//                return mDrawables[0].getIntrinsicHeight();
//            }
//            return 0;
//        }
//
//        @Override
//        public int getMinimumWidth() {
//            if (mDrawables.length > 0) {
//                return mDrawables[0].getMinimumWidth();
//            }
//            return 0;
//        }
//
//        @Override
//        public int getMinimumHeight() {
//            if (mDrawables.length > 0) {
//                return mDrawables[0].getMinimumHeight();
//            }
//            return 0;
//        }
//
//        @Override
//        public boolean getPadding(Rect padding) {
//            boolean state = false;
//            for (Drawable mDrawable : mDrawables) {
//                state |= mDrawable.getPadding(padding);
//            }
//            return state;
//        }
//
//        @Override
//        protected boolean onLevelChange(int level) {
//            boolean state = false;
//            for (Drawable mDrawable : mDrawables) {
//                state |= mDrawable.setLevel(level);
//            }
//            return state;
//        }
//
//        @Override
//        public boolean isAutoMirrored() {
//            boolean state = false;
//            for (Drawable mDrawable : mDrawables) {
//                state |= DrawableCompat.isAutoMirrored(mDrawable);
//            }
//            return state;
//        }
//
//        @Override
//        public void setAutoMirrored(boolean mirrored) {
//            for (Drawable mDrawable : mDrawables) {
//                DrawableCompat.setAutoMirrored(mDrawable, mirrored);
//            }
//        }
//
//        @Override
//        public void setTint(int tint) {
//            for (Drawable mDrawable : mDrawables) {
//                DrawableCompat.setTint(mDrawable, tint);
//            }
//        }
//
//        @Override
//        public void setTintList(ColorStateList tint) {
//            for (Drawable mDrawable : mDrawables) {
//                DrawableCompat.setTintList(mDrawable, tint);
//            }
//        }
//
//        @Override
//        public void setTintMode(PorterDuff.Mode tintMode) {
//            for (Drawable mDrawable : mDrawables) {
//                DrawableCompat.setTintMode(mDrawable, tintMode);
//            }
//        }
//
//        @Override
//        public void setHotspot(float x, float y) {
//            for (Drawable mDrawable : mDrawables) {
//                DrawableCompat.setHotspot(mDrawable, x, y);
//            }
//        }
//
//        @Override
//        public void setHotspotBounds(int left, int top, int right, int bottom) {
//            for (Drawable mDrawable : mDrawables) {
//                DrawableCompat.setHotspotBounds(mDrawable, left, top, right, bottom);
//            }
//        }
//
//        public interface OnBoundChangeListener {
//            Rect[] onBoundChange(Rect bounds);
//        }
//    }
//}
