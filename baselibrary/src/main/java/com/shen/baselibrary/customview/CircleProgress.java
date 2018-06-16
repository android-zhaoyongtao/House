package com.shen.baselibrary.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.shen.baselibrary.ContextHouse;

public class CircleProgress extends View {
    private int color = 0xffececec;
    private int backColor = 0xffffffff;
    private Paint paint = new Paint();
    private RectF rect = new RectF();

    public CircleProgress(Context context) {
        super(context);
        init();
    }

    public CircleProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleProgress(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (isInEditMode()) {
            return;
        }
        paint.setAntiAlias(true);
        setLayerType(View.LAYER_TYPE_SOFTWARE, paint);
        paint.setShadowLayer(ContextHouse.DP1, 0, 0, 0xFF888888);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int min = Math.min(width, height);
        paint.setColor(backColor);
        paint.setStyle(Paint.Style.FILL);
        //        canvas.drawCircle(width / 2, height / 2, min / 2, paint);
        int redis = (int) (min * 0.3f);
        int left = width / 2 - redis;
        int right = width / 2 + redis;
        int top = height / 2 - redis;
        int bottom = height / 2 + redis;
        float sweep = (float) (440 * (System.currentTimeMillis() % 1500) / 1500) + 20;
        if (sweep > 350) {
            sweep = (440 - sweep) * 3 + 20;
        }
        float start = (System.currentTimeMillis() % 1200) * 360 / 1200;
        rect.set(left, top, right, bottom);
        paint.setColor(color);
        paint.setStrokeWidth(min * 0.1f);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(rect, start, sweep, false, paint);
        invalidate();
    }
}
