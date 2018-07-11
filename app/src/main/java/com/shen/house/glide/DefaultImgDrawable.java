package com.shen.house.glide;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.shen.baselibrary.ContextHouse;
import com.shen.house.R;

public class DefaultImgDrawable extends Drawable {

    private static final OnBoundChangeListener DEFAULT_LISTENER = new OnBoundChangeListener() {
        @Override
        public Rect[] onBoundChange(Rect bounds) {
            Rect[] rects = new Rect[2];
            rects[0] = bounds;//背景颜色
            rects[1] = new Rect();
            float dp50 = ContextHouse.DP1 * 50;
            float dp250 = ContextHouse.DP1 * 250;
            int width = bounds.width();
            int height = bounds.height();
            int widthInner = 0;//前景图片的宽度
            if (width > dp250) {//大图占用了整个 view 的3.5分之一
                widthInner = (int) (width / 3.5);
            } else if (width > dp50) {//小图占用了整个 view 的2.4分支一
                widthInner = (int) (width / 2.4);
            }
            int heightInner = (int) (widthInner * 0.33);//
            if (height < heightInner * 2) {//drawable 的高度太低了，就不显示前景色了
                widthInner = 0;
                heightInner = 0;
            }
            rects[1].left = bounds.left + (width - widthInner) / 2;
            rects[1].right = rects[1].left + widthInner;
            rects[1].top = bounds.top + (height - heightInner) / 2;
            rects[1].bottom = rects[1].top + heightInner;
            return rects;
        }
    };
    private final Drawable[] mDrawables;
    private OnBoundChangeListener mListener = DEFAULT_LISTENER;

    public DefaultImgDrawable(Context context) {
        this(new ColorDrawable(context.getResources().getColor(R.color.bright_grey)));
    }

    public DefaultImgDrawable(Drawable... fore) {
        mDrawables = fore;
    }

    public void setListener(OnBoundChangeListener listener) {
        this.mListener = listener;
    }

    @Override
    public void draw(Canvas canvas) {
        for (Drawable mDrawable : mDrawables) {
            mDrawable.draw(canvas);
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        Rect[] rects = mListener.onBoundChange(bounds);
        for (int i = 0; i < mDrawables.length; i++) {
            mDrawables[i].setBounds(rects[i]);
        }
    }

    @Override
    public int getChangingConfigurations() {
        if (mDrawables.length > 0) {
            return mDrawables[0].getChangingConfigurations();
        }
        return 0;
    }

    @Override
    public void setChangingConfigurations(int configs) {
        for (Drawable mDrawable : mDrawables) {
            mDrawable.setChangingConfigurations(configs);
        }
    }

    @Override
    public void setDither(boolean dither) {
        for (Drawable mDrawable : mDrawables) {
            mDrawable.setDither(dither);
        }
    }

    @Override
    public void setFilterBitmap(boolean filter) {
        for (Drawable mDrawable : mDrawables) {
            mDrawable.setFilterBitmap(filter);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        for (Drawable mDrawable : mDrawables) {
            mDrawable.setAlpha(alpha);
        }
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        for (Drawable mDrawable : mDrawables) {
            mDrawable.setColorFilter(cf);
        }
    }

    @Override
    public boolean isStateful() {
        boolean state = false;
        for (Drawable mDrawable : mDrawables) {
            state |= mDrawable.isStateful();
        }
        return state;
    }

    @Override
    public boolean setState(final int[] stateSet) {
        boolean state = false;
        for (Drawable mDrawable : mDrawables) {
            state |= mDrawable.setState(stateSet);
        }
        return state;
    }

    @Override
    public int[] getState() {
        if (mDrawables.length > 0) {
            return mDrawables[0].getState();
        }
        return null;
    }

    @Override
    public Drawable getCurrent() {
        if (mDrawables.length > 0) {
            return mDrawables[0].getCurrent();
        }
        return null;
    }

    @Override
    public boolean setVisible(boolean visible, boolean restart) {
        boolean state = super.setVisible(visible, restart);
        for (Drawable mDrawable : mDrawables) {
            state |= mDrawable.setVisible(visible, restart);
        }
        return state;
    }

    @Override
    public int getOpacity() {
        if (mDrawables.length > 0) {
            return mDrawables[0].getOpacity();
        }
        return PixelFormat.UNKNOWN;
    }

    @Override
    public Region getTransparentRegion() {
        if (mDrawables.length > 0) {
            return mDrawables[0].getTransparentRegion();
        }
        return null;
    }

    @Override
    public int getIntrinsicWidth() {
        if (mDrawables.length > 0) {
            return mDrawables[0].getIntrinsicWidth();
        }
        return 0;
    }

    @Override
    public int getIntrinsicHeight() {
        if (mDrawables.length > 0) {
            return mDrawables[0].getIntrinsicHeight();
        }
        return 0;
    }

    @Override
    public int getMinimumWidth() {
        if (mDrawables.length > 0) {
            return mDrawables[0].getMinimumWidth();
        }
        return 0;
    }

    @Override
    public int getMinimumHeight() {
        if (mDrawables.length > 0) {
            return mDrawables[0].getMinimumHeight();
        }
        return 0;
    }

    @Override
    public boolean getPadding(Rect padding) {
        boolean state = false;
        for (Drawable mDrawable : mDrawables) {
            state |= mDrawable.getPadding(padding);
        }
        return state;
    }

    @Override
    protected boolean onLevelChange(int level) {
        boolean state = false;
        for (Drawable mDrawable : mDrawables) {
            state |= mDrawable.setLevel(level);
        }
        return state;
    }

    @Override
    public boolean isAutoMirrored() {
        boolean state = false;
        for (Drawable mDrawable : mDrawables) {
            state |= DrawableCompat.isAutoMirrored(mDrawable);
        }
        return state;
    }

    @Override
    public void setAutoMirrored(boolean mirrored) {
        for (Drawable mDrawable : mDrawables) {
            DrawableCompat.setAutoMirrored(mDrawable, mirrored);
        }
    }

    @Override
    public void setTint(int tint) {
        for (Drawable mDrawable : mDrawables) {
            DrawableCompat.setTint(mDrawable, tint);
        }
    }

    @Override
    public void setTintList(ColorStateList tint) {
        for (Drawable mDrawable : mDrawables) {
            DrawableCompat.setTintList(mDrawable, tint);
        }
    }

    @Override
    public void setTintMode(PorterDuff.Mode tintMode) {
        for (Drawable mDrawable : mDrawables) {
            DrawableCompat.setTintMode(mDrawable, tintMode);
        }
    }

    @Override
    public void setHotspot(float x, float y) {
        for (Drawable mDrawable : mDrawables) {
            DrawableCompat.setHotspot(mDrawable, x, y);
        }
    }

    @Override
    public void setHotspotBounds(int left, int top, int right, int bottom) {
        for (Drawable mDrawable : mDrawables) {
            DrawableCompat.setHotspotBounds(mDrawable, left, top, right, bottom);
        }
    }

    public interface OnBoundChangeListener {
        Rect[] onBoundChange(Rect bounds);
    }
}
