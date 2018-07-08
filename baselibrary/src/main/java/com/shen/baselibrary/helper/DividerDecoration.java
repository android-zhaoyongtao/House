//package com.shen.baselibrary.helper;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.Rect;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.StaggeredGridLayoutManager;
//import android.view.View;
//
//import com.xin.views.R;
//
//
//@SuppressWarnings("SuspiciousNameCombination")
//public class DividerDecoration extends RecyclerView.ItemDecoration {
//    private boolean showParentEdge = true;
//    private int divideColor = 0xffebe9e9;
//    private int divideHeight = 0;
//    private Paint paint;
//    private Rect rect;
//
//    public DividerDecoration(Context rv) {
//        divideHeight = rv.getResources().getDimensionPixelSize(R.dimen.recyclerview_divider);
//        init();
//    }
//
//    public DividerDecoration(Context rv, boolean showParentEdge) {
//        divideHeight = rv.getResources().getDimensionPixelSize(R.dimen.recyclerview_divider);
//        this.showParentEdge = showParentEdge;
//        init();
//    }
//
//    public DividerDecoration(int divideColor, int divideHeight, boolean showParentEdge) {
//        this.divideColor = divideColor;
//        this.divideHeight = divideHeight;
//        this.showParentEdge = showParentEdge;
//        init();
//    }
//
//    /**
//     * rv 的 edge 通过 padding 或者 margin 加进去
//     */
//    public void init() {
//        rect = new Rect();
//        paint = new Paint();
//    }
//
//    /**
//     * 给底部和右侧添加边距
//     */
//    @Override
//    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
//        int position = layoutManager.getPosition(view);
//        int spanCount;
//        if (layoutManager instanceof GridLayoutManager) {
//            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
//        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
//            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
//        } else {
//            spanCount = 1;
//        }
//        if (showParentEdge) {
//            if (spanCount > position) {//第一行
//                outRect.top = divideHeight;
//            }
//            outRect.bottom = divideHeight;
//            float avgDivider = divideHeight * (spanCount + 1f) / spanCount - divideHeight;
//            int spanIndex = position % spanCount;
//            outRect.left = (int) (divideHeight - spanIndex * avgDivider);
//            outRect.right = (int) ((spanIndex + 1) * avgDivider);
//        } else {
//            if (spanCount > position) {//第一行
//                outRect.top = 0;
//            } else {
//                outRect.top = divideHeight;
//            }
//            float avgDivider = divideHeight - divideHeight * (spanCount - 1f) / spanCount;
//            int spanIndex = position % spanCount;
//            outRect.left = (int) (spanIndex * avgDivider);//由于每个 item+decoration 的宽度都是一样的 需要把每一列的分割线总大小分成一样大小
//            outRect.right = (int) (divideHeight - (spanIndex + 1) * avgDivider);
//        }
//    }
//
//    @Override
//    public void onDraw(Canvas c, RecyclerView parent) {
//        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
//        paint.setColor(divideColor);
//        for (int i = 0; i < parent.getChildCount(); i++) {//所有 view 的 right 和 bottom 的线
//            View view = parent.getChildAt(i);
//            int left = view.getLeft();
//            int top = view.getTop();
//            int right = view.getRight();
//            int bottom = view.getBottom();
//            int dbottom = layoutManager.getBottomDecorationHeight(view);
//            int dright = layoutManager.getRightDecorationWidth(view);
//            int dtop = layoutManager.getTopDecorationHeight(view);
//            int dleft = layoutManager.getLeftDecorationWidth(view);
//            rect.bottom = bottom;
//            rect.top = top;
//            rect.left = left - dleft;
//            rect.right = left;
//            c.drawRect(rect, paint);//左边
//            rect.right = right + dright;
//            rect.left = right;
//            c.drawRect(rect, paint);//右边
//            rect.left = left - dleft;
//            rect.top = top - dtop;
//            rect.bottom = top;
//            c.drawRect(rect, paint);//上边
//            rect.top = bottom;
//            rect.bottom = bottom + dbottom;
//            c.drawRect(rect, paint);//下边
//        }
//    }
//}
