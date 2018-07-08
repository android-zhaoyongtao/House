/* Copyright (C) 2013 Sergej Shafarenka, halfbit.de Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file kt in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and
 * limitations under the License. */

package com.shen.baselibrary.customview.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.SectionIndexer;

/**
 * ListView, which is capable to pin section views at its top while the rest is
 * still scrolled.
 */
public class PinnedSectionRecycleView extends RecyclerView {

    // -- inner classes

    // fields used for handling touch events
    private final Rect mTouchRect = new Rect();
    private final PointF mTouchPoint = new PointF();

    // -- class fields
    /**
     * Delegating listener, can be null.
     */
    OnScrollListener mDelegateOnScrollListener;
    /**
     * Shadow for being recycled, can be null.
     */
    PinnedSection mRecycleSection;
    /**
     * shadow instance with a pinned view, can be null.
     */
    PinnedSection mPinnedSection;
    /**
     * Pinned view Y-translation. We use it to stick pinned view to the next
     * section.
     */
    int mTranslateY;
    private int mTouchSlop;
    private View mTouchTarget;
    private MotionEvent mDownEvent;
    // fields used for drawing shadow under a pinned section
    private GradientDrawable mShadowDrawable;
    private int mSectionsDistanceY;
    private Pinnedable mPinable;

    /**
     * Scroll listener which does the magic
     */
    private final OnScrollListener mOnScrollListener = new OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView view, int scrollState) {
            if (mDelegateOnScrollListener != null) { // delegate
                mDelegateOnScrollListener.onScrollStateChanged(view, scrollState);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            if (mDelegateOnScrollListener != null) { // delegate
                mDelegateOnScrollListener.onScrolled(recyclerView, dx, dy);
            }
            int firstVisibleItem = getLayoutManager().findFirstVisibleItemPosition();
            int visibleItemCount = getLayoutManager().findLastVisibleItemPosition() - firstVisibleItem;
            // get expected adapter or fail fast
            Adapter adapter = getAdapter();
            if (adapter == null || visibleItemCount == 0)
                return; // nothing to do

            final boolean isFirstVisibleItemSection = isItemViewTypePinned(mPinable, adapter.getItemViewType(firstVisibleItem));

            if (isFirstVisibleItemSection) {
                View sectionView = getChildAt(0);
                if (sectionView.getTop() == getPaddingTop()) { // view sticks to
                    // the top, no
                    // need for
                    // pinned shadow
                    destroyPinnedShadow();
                } else { // section doesn't stick to the top, make sure we have
                    // a pinned shadow
                    ensureShadowForPosition(firstVisibleItem, firstVisibleItem, visibleItemCount);
                }

            } else { // section is not at the first visible position
                int sectionPosition = findCurrentSectionPosition(firstVisibleItem);
                if (sectionPosition > -1) { // we have section position
                    ensureShadowForPosition(sectionPosition, firstVisibleItem, visibleItemCount);
                } else { // there is no section for the first visible item,
                    // destroy shadow
                    destroyPinnedShadow();
                }
            }
        }

    };
    /**
     * Default change observer.
     */
    private final AdapterDataObserver mDataSetObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            recreatePinnedShadow();
        }

        public void onInvalidated() {
            recreatePinnedShadow();
        }
    };
    private int mShadowHeight;
    private int mLeftClick = -1;
    private int mRightClick = Integer.MAX_VALUE;

    public PinnedSectionRecycleView(Context context) {
        super(context, null);
        initView();
    }

    public PinnedSectionRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PinnedSectionRecycleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    // -- constructors

    private void initView() {
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        setLayoutManager(new LinearLayoutManager(getContext()));
        initShadow(true);
    }

    public void setPinable(Pinnedable mPinable) {
        addOnScrollListener(mOnScrollListener);
        this.mPinable = mPinable;
    }

    public void setShadowVisible(boolean visible) {
        initShadow(visible);
        if (mPinnedSection != null) {
            View v = mPinnedSection.view;
            invalidate(v.getLeft(), v.getTop(), v.getRight(), v.getBottom() + mShadowHeight);
        }
    }

    public void initShadow(boolean visible) {
        if (visible) {
            if (mShadowDrawable == null) {
                mShadowDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0xaaa0a0a0, 0x20a0a0a0, 0x0});
                mShadowHeight = (int) (5 * getResources().getDisplayMetrics().density);
            }
        } else {
            if (mShadowDrawable != null) {
                mShadowDrawable = null;
                mShadowHeight = 0;
            }
        }
    }

    // -- public API methods

    /**
     * Create shadow wrapper with a pinned view for a view at given position
     */
    void createPinnedShadow(int position) {

        // try to recycle shadow
        PinnedSection pinnedShadow = mRecycleSection;
        mRecycleSection = null;

        // create new shadow, if needed
        if (pinnedShadow == null)
            pinnedShadow = new PinnedSection();
        // request new view using recycled view, if such
        Adapter adapter = getAdapter();
        ViewHolder viewHolder = adapter.onCreateViewHolder(this, adapter.getItemViewType(position));
        View pinnedView = viewHolder.itemView;
        adapter.onBindViewHolder(viewHolder, position);
        // read layout parameters
        ViewGroup.LayoutParams layoutParams = pinnedView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = generateDefaultLayoutParams();
            pinnedView.setLayoutParams(layoutParams);
        }

        int heightMode = MeasureSpec.getMode(layoutParams.height);
        int heightSize = MeasureSpec.getSize(layoutParams.height);

        if (heightMode == MeasureSpec.UNSPECIFIED)
            heightMode = MeasureSpec.EXACTLY;

        int maxHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        if (heightSize > maxHeight)
            heightSize = maxHeight;

        // measure & layout
        int ws = MeasureSpec.makeMeasureSpec(getWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY);
        int hs = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
        pinnedView.measure(ws, hs);
        pinnedView.layout(0, 0, pinnedView.getMeasuredWidth(), pinnedView.getMeasuredHeight());
        mTranslateY = 0;

        // initialize pinned shadow
        pinnedShadow.view = pinnedView;
        pinnedShadow.position = position;
        pinnedShadow.id = adapter.getItemId(position);

        // store pinned shadow
        mPinnedSection = pinnedShadow;
    }

    // -- pinned section drawing methods

    /**
     * Destroy shadow wrapper for currently pinned view
     */
    void destroyPinnedShadow() {
        if (mPinnedSection != null) {
            // keep shadow for being recycled later
            mRecycleSection = mPinnedSection;
            mPinnedSection = null;
        }
    }

    /**
     * Makes sure we have an actual pinned shadow for given position.
     */
    void ensureShadowForPosition(int sectionPosition, int firstVisibleItem, int visibleItemCount) {
        if (visibleItemCount < 2) { // no need for creating shadow at all, we
            // have a single visible item
            destroyPinnedShadow();
            return;
        }

        if (mPinnedSection != null && mPinnedSection.position != sectionPosition) { // invalidate
            // shadow,
            // if
            // required
            destroyPinnedShadow();
        }

        if (mPinnedSection == null) { // create shadow, if empty
            createPinnedShadow(sectionPosition);
        }

        // align shadow according to next section position, if needed
        int nextPosition = sectionPosition + 1;
        if (nextPosition < getAdapter().getItemCount()) {
            int nextSectionPosition = findFirstVisibleSectionPosition(nextPosition, visibleItemCount - (nextPosition - firstVisibleItem));
            if (nextSectionPosition > -1) {
                View nextSectionView = getChildAt(nextSectionPosition - firstVisibleItem);
                final int bottom = mPinnedSection.view.getBottom() + getPaddingTop();
                mSectionsDistanceY = nextSectionView.getTop() - bottom;
                if (mSectionsDistanceY < 0) {
                    // next section overlaps pinned shadow, move it up
                    mTranslateY = mSectionsDistanceY;
                } else {
                    // next section does not overlap with pinned, stick to top
                    mTranslateY = 0;
                }
            } else {
                // no other sections are visible, stick to top
                mTranslateY = 0;
                mSectionsDistanceY = Integer.MAX_VALUE;
            }
        }

    }

    int findFirstVisibleSectionPosition(int firstVisibleItem, int visibleItemCount) {
        Adapter adapter = getAdapter();

        if (firstVisibleItem + visibleItemCount >= adapter.getItemCount())
            return -1; // dataset has changed, no candidate

        for (int childIndex = 0; childIndex < visibleItemCount; childIndex++) {
            int position = firstVisibleItem + childIndex;
            int viewType = adapter.getItemViewType(position);
            if (isItemViewTypePinned(mPinable, viewType))
                return position;
        }
        return -1;
    }

    int findCurrentSectionPosition(int fromPosition) {
        Adapter adapter = getAdapter();

        if (fromPosition >= adapter.getItemCount())
            return -1; // dataset has changed, no candidate

        if (adapter instanceof SectionIndexer) {
            // try fast way by asking section indexer
            SectionIndexer indexer = (SectionIndexer) adapter;
            int sectionPosition = indexer.getSectionForPosition(fromPosition);
            int itemPosition = indexer.getPositionForSection(sectionPosition);
            int typeView = adapter.getItemViewType(itemPosition);
            if (isItemViewTypePinned(mPinable, typeView)) {
                return itemPosition;
            } // else, no luck
        }

        // try slow way by looking through to the next section item above
        for (int position = fromPosition; position >= 0; position--) {
            int viewType = adapter.getItemViewType(position);
            if (isItemViewTypePinned(mPinable, viewType))
                return position;
        }
        return -1; // no candidate found
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return (LinearLayoutManager) super.getLayoutManager();
    }

    void recreatePinnedShadow() {
        if (mPinable != null) {
            destroyPinnedShadow();
            Adapter adapter = getAdapter();
            if (adapter != null && adapter.getItemCount() > 0) {
                int firstVisiblePosition = getLayoutManager().findFirstVisibleItemPosition();
                int sectionPosition = findCurrentSectionPosition(firstVisiblePosition);
                if (sectionPosition == -1)
                    return; // no views to pin, exit
                ensureShadowForPosition(sectionPosition, firstVisiblePosition, getLayoutManager().findLastVisibleItemPosition() - firstVisiblePosition);
            }
        }
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        post(new Runnable() {
            @Override
            public void run() { // restore pinned view after configuration
                // change
                recreatePinnedShadow();
            }
        });
    }


    public void setAdapter(Adapter adapter) {

        // assert adapter in debug mode
        // if (BuildConfig.DEBUG && adapter != null) {
        // if (!(adapter instanceof PinnedSectionListAdapter))
        // throw new
        // IllegalArgumentException("Does your adapter implement PinnedSectionListAdapter?");
        // if (adapter.getViewTypeCount() < 2)
        // throw new
        // IllegalArgumentException("Does your adapter handle at least two types"
        // +
        // " of views in getViewTypeCount() method: items and sections?");
        // }

        // unregister observer at old adapter and register on new one
        Adapter oldAdapter = getAdapter();
        if (oldAdapter != null)
            oldAdapter.unregisterAdapterDataObserver(mDataSetObserver);
        if (adapter != null)
            adapter.registerAdapterDataObserver(mDataSetObserver);

        // destroy pinned shadow, if new adapter is not same as old one
        if (oldAdapter != adapter)
            destroyPinnedShadow();

        super.setAdapter(adapter);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mPinnedSection != null) {
            int parentWidth = r - l - getPaddingLeft() - getPaddingRight();
            int shadowWidth = mPinnedSection.view.getWidth();
            if (parentWidth != shadowWidth) {
                recreatePinnedShadow();
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (mPinnedSection != null) {

            // prepare variables
            int pLeft = getPaddingLeft();
            int pTop = getPaddingTop();
            View view = mPinnedSection.view;

            // draw child
            canvas.save();

            int clipHeight = view.getHeight() + (mShadowDrawable == null ? 0 : Math.min(mShadowHeight, mSectionsDistanceY));
            canvas.clipRect(pLeft, pTop, pLeft + view.getWidth(), pTop + clipHeight);

            canvas.translate(pLeft, pTop + mTranslateY);
            drawChild(canvas, mPinnedSection.view, getDrawingTime());

            if (mShadowDrawable != null && mSectionsDistanceY > 0) {
                mShadowDrawable.setBounds(mPinnedSection.view.getLeft(), mPinnedSection.view.getBottom(), mPinnedSection.view.getRight(), mPinnedSection.view.getBottom() + mShadowHeight);
                mShadowDrawable.draw(canvas);
            }

            canvas.restore();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        final float x = ev.getX();
        final float y = ev.getY();
        final int action = ev.getAction();

        if (action == MotionEvent.ACTION_DOWN && mTouchTarget == null && mPinnedSection != null && isPinnedViewTouched(mPinnedSection.view, x, y)) { // create
            // touch
            // target

            // user touched pinned view
            mTouchTarget = mPinnedSection.view;
            mTouchPoint.x = x;
            mTouchPoint.y = y;

            // copy down event for eventually be used later
            mDownEvent = MotionEvent.obtain(ev);
        }

        if (mTouchTarget != null) {
            if (isPinnedViewTouched(mTouchTarget, x, y)) { // forward event to
                // pinned view
                mTouchTarget.dispatchTouchEvent(ev);
            }

            if (action == MotionEvent.ACTION_UP) { // perform onClick on pinned
                // view
                super.dispatchTouchEvent(ev);
                clearTouchTarget();

            } else if (action == MotionEvent.ACTION_CANCEL) { // cancel
                clearTouchTarget();

            } else if (action == MotionEvent.ACTION_MOVE) {
                if (Math.abs(y - mTouchPoint.y) > mTouchSlop) {

                    // cancel sequence on touch target
                    MotionEvent event = MotionEvent.obtain(ev);
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    mTouchTarget.dispatchTouchEvent(event);
                    event.recycle();

                    // provide correct sequence to super class for further
                    // handling
                    super.dispatchTouchEvent(mDownEvent);
                    super.dispatchTouchEvent(ev);
                    clearTouchTarget();

                }
            }

            return true;
        }
        // call super if this was not our pinned view
        return mLeftClick < x && mRightClick > x && super.dispatchTouchEvent(ev);
    }

    private boolean isPinnedViewTouched(View view, float x, float y) {
        view.getHitRect(mTouchRect);

        // by taping top or bottom padding, the list performs on click on a
        // border item.
        // we don't add top padding here to keep behavior consistent.
        mTouchRect.top += mTranslateY;

        mTouchRect.bottom += mTranslateY + getPaddingTop();
        mTouchRect.left += getPaddingLeft();
        mTouchRect.right -= getPaddingRight();
        return mTouchRect.contains((int) x, (int) y);
    }

    // -- touch handling methods

    private void clearTouchTarget() {
        mTouchTarget = null;
        if (mDownEvent != null) {
            mDownEvent.recycle();
            mDownEvent = null;
        }
    }

    private boolean isItemViewTypePinned(Pinnedable adapter, int viewType) {
        return adapter.isItemViewTypePinned(viewType);
    }

    public void setTouchWidth(int leftClick, int rightClick) {
        this.mLeftClick = leftClick;
        this.mRightClick = rightClick;
    }

    /**
     * List adapter to be implemented for being used with PinnedSectionListView
     * adapter.
     */
    public interface Pinnedable {
        /**
         * This method shall return 'true' if views of given type has to be
         * pinned.
         */
        boolean isItemViewTypePinned(int pos);
    }

    /**
     * Wrapper class for pinned section view and its position in the list.
     */
    static class PinnedSection {
        public View view;
        public int position;
        public long id;
    }

}
