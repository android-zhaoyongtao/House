package com.shen.baselibrary.customview.recyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shen.baselibrary.R;
import com.shen.baselibrary.utiles.LogUtils;


public class LoadMoreRecyclerView extends RecyclerView {
    /**
     * item 类型
     */
    public final static int TYPE_NORMAL = 0;
    public final static int TYPE_HEADER = 1000;//头部--支持头部增加一个headerView
    public final static int TYPE_FOOTER = 1001;//底部--往往是loading_more
    public final static int TYPE_LIST = 1002;//代表item展示的模式是list模式
    public final static int TYPE_STAGGER = 1003;//代码item展示模式是网格模式
    public static final int MORE = 0;//未加载
    public static final int MOREING = 1;//正在加载
    public static final int MOREEND = 3;//无数据加载

    private int mLoadMoreState = MORE; //正在加载。加载完成，无数据加载
    private boolean mIsFooterEnable = true;//是否允许加载更多
    private int notifyColor;
    private float notifyTextsize;
    /**
     * 自定义实现了头部和底部加载更多的adapter
     */
    private AutoLoadAdapter mAutoLoadAdapter;
    private final RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            mAutoLoadAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mAutoLoadAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mAutoLoadAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mAutoLoadAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mAutoLoadAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mAutoLoadAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    };
    /**
     * 标记是否正在加载更多，防止再次调用加载更多接口
     */
    private boolean mIsLoadingMore;
    /**
     * 标记加载更多的position
     */
    private int mLoadMorePosition = -1;
    /**
     * 加载更多的监听-业务需要实现加载数据
     */
    private OnLoadMoreListener mListener;
    private CharSequence morefinishhint = "没有更多内容了";
    private boolean isStopScroll = false;

    public LoadMoreRecyclerView(Context context) {
        super(context);
        init();
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        notifyColor = 0x5b5f68;
//        notifyTextsize = getContext().getResources().getDimension(R.dimen.c_13);
        notifyTextsize = 13;
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        isStopScroll = dy > 0;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE && null != mListener && mIsFooterEnable && !mIsLoadingMore && isStopScroll) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition = getLastVisibleItemPosition(layoutManager);
            if (mLoadMoreState == MORE && layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1) {
                mLoadMorePosition = lastVisibleItemPosition;
                LogUtils.e("zoudong", "onScrollStateChanged: " + mLoadMorePosition);
                mLoadMoreState = MOREING;
                mAutoLoadAdapter.notifyItemChanged(mLoadMorePosition);
                mListener.onLoadMore();
            } else {
//                mLoadMoreState = MORE;
//                mAutoLoadAdapter.notifyItemChanged(mLoadMorePosition);
//                mAutoLoadAdapter.notifyItemRemoved(mLoadMorePosition);
            }
        }
    }

    /**
     * 获取最后一条展示的位置
     *
     * @return
     */
    private int getLastVisibleItemPosition(LayoutManager layoutManager) {
        int lastVisibleItemPosition;
        if (layoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
            lastVisibleItemPosition = findMax(into);

        } else {
            lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }
        return lastVisibleItemPosition;
    }

    /**
     * 取数组中最大值
     *
     * @param lastPositions
     * @return
     */
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }

        return max;
    }

    /**
     * 设置正在加载更多
     *
     * @param loadingMore
     */
//    public void setLoadingMore(boolean loadingMore) {
//        this.mIsLoadingMore = loadingMore;
//        setLoadMoreStatus(MOREING);
//    }
//    public void setLoadingMoreEnd() {
//        setLoadMoreStatus(MOREEND);
//    }
//    public void  setLoadMoreStatus(int status){
//        mLoadMoreState=status;
//        mAutoLoadAdapter.notifyItemRangeChanged(mAutoLoadAdapter.getItemCount()-1,mAutoLoadAdapter.getItemCount());
//    }

    /**
     * 设置加载更多的监听
     *
     * @param listener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mListener = listener;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mAutoLoadAdapter = new AutoLoadAdapter(adapter);
//        super.swapAdapter(mAutoLoadAdapter, true);
        super.setAdapter(mAutoLoadAdapter);// 通过父类方法将自定义的Adapter重新设置进去
        if (adapter.hasObservers()) {
            adapter.unregisterAdapterDataObserver(mDataObserver);
        }

        adapter.registerAdapterDataObserver(mDataObserver);//请看下面分析
    }

    /**
     * 切换layoutManager
     * <p/>
     * 为了保证切换之后页面上还是停留在当前展示的位置，记录下切换之前的第一条展示位置，切换完成之后滚动到该位置
     * 另外切换之后必须要重新刷新下当前已经缓存的itemView，否则会出现布局错乱（俩种模式下的item布局不同），
     * RecyclerView提供了swapAdapter来进行切换adapter并清理老的itemView cache
     *
     * @param layoutManager
     */
    public void switchLayoutManager(LayoutManager layoutManager) {
        int firstVisiblePosition = getFirstVisiblePosition();
//        getLayoutManager().removeAllViews();
        setLayoutManager(layoutManager);
        //super.swapAdapter(mAutoLoadAdapter, true);
        getLayoutManager().scrollToPosition(firstVisiblePosition);
    }

    /**
     * 获取第一条展示的位置
     *
     * @return
     */
    private int getFirstVisiblePosition() {
        int position;
        if (getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        } else if (getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            int[] lastPositions = layoutManager.findFirstVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMinPositions(lastPositions);
        } else {
            position = 0;
        }
        return position;
    }

//    public void notifyDataSetChanged() {
//        getAdapter().notifyDataSetChanged();
//    }

//    public void notifyItemChanged(int pos) {
//        getAdapter().notifyItemChanged(pos);
//    }

    /**
     * 获得当前展示最小的position
     *
     * @param positions
     * @return
     */
    private int getMinPositions(int[] positions) {
        int size = positions.length;
        int minPosition = Integer.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            minPosition = Math.min(minPosition, positions[i]);
        }
        return minPosition;
    }


    /**
     * 获得最大的位置
     *
     * @param positions
     * @return
     */
    private int getMaxPosition(int[] positions) {
        int size = positions.length;
        int maxPosition = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            maxPosition = Math.max(maxPosition, positions[i]);
        }
        return maxPosition;
    }

    /**
     * 添加头部view
     */

    public void addHeaderView(View headview, ViewHolder mHeadViewHolder) {
        mAutoLoadAdapter.addHeaderView(headview, mHeadViewHolder);
    }

    /**
     * 设置头部view是否展示
     *
     * @param enable
     */
    public void setHeaderEnable(boolean enable) {
        mAutoLoadAdapter.setHeaderEnable(enable);
    }

    /**
     * 设置是否支持自动加载更多
     *
     * @param autoLoadMore
     */
    public void setAutoLoadMoreEnable(boolean autoLoadMore) {
        mIsFooterEnable = autoLoadMore;
    }

    /**
     * 通知更多的数据已经加载
     * <p/>
     * 每次加载完成之后添加了Data数据，用notifyItemRemoved来刷新列表展示，
     * 而不是用notifyDataSetChanged来刷新列表
     *
     * @param hasMore true --加载完成   false ---无数据加载
     */
    public void notifyMoreFinish(boolean hasMore, CharSequence msg) {
        this.morefinishhint = msg;
        notifyMoreFinish(hasMore);
    }

    /**
     * 为了解决 NeededScrollView 中加载更多问题
     *
     * @param hasMore
     */
    public void notifyMoreStart(boolean hasMore) {
        if (hasMore) {
            isStopScroll = true;
            onScrollStateChanged(RecyclerView.SCROLL_STATE_IDLE);
        }

    }

    public void setNotifyTextColor(int color) {
        notifyColor = color;
    }

    public void setNotifyTextSize(float size) {
        notifyTextsize = size;
    }

    public void notifyMoreFinish(boolean hasMore) {
        mLoadMoreState = hasMore ? MORE : MOREEND;
//        if (!hasMore) {
//        }
        if (mLoadMorePosition > 0) {
            mAutoLoadAdapter.notifyItemChanged(mLoadMorePosition);//无法加载
        }
        mIsLoadingMore = false;
    }

    public int getLoadMoreState() {
        return mLoadMoreState;
    }

    /**
     * 加载更多监听
     */
    public interface OnLoadMoreListener {
        /**
         * 加载更多
         */
        void onLoadMore();
    }

    /**
     *
     */
    public class AutoLoadAdapter extends Adapter<ViewHolder> {

        /**
         * 数据adapter
         */
        private Adapter mInternalAdapter;

        private boolean mIsHeaderEnable;
        private View mHeadView;
        private ViewHolder mHeadViewHolder;

        public AutoLoadAdapter(Adapter adapter) {
            super();
            mInternalAdapter = adapter;
            mIsHeaderEnable = false;
        }

        @Override
        public int getItemViewType(int position) {
            int headerPosition = 0;

            if (headerPosition == position && mIsHeaderEnable && mHeadView != null) {
                return TYPE_HEADER;
            }
            //int footerPosition = getItemCount() - 1;
            if (isFooter(position) && mIsFooterEnable) {
                return TYPE_FOOTER;
            }
            /**
             * 这么做保证layoutManager切换之后能及时的刷新上对的布局
             */
            if (getLayoutManager() instanceof LinearLayoutManager) {
                return mInternalAdapter.getItemViewType(position);
            } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
                return mInternalAdapter.getItemViewType(position);
            } else {
                return mInternalAdapter.getItemViewType(position);
            }
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            mInternalAdapter.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (isFooter(position))
                                ? gridManager.getSpanCount() : 1;
                    }
                });
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER) {
                if (mHeadViewHolder != null) {
                    return mHeadViewHolder;
                }
                return new HeaderViewHolder(mHeadView);
            }
            if (viewType == TYPE_FOOTER) {
                return new FooterViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.list_foot_loading, parent, false));
            } else { // type normal
                return mInternalAdapter.onCreateViewHolder(parent, viewType);
            }
        }

        @Override
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            if (!(holder instanceof FooterViewHolder)) {
                mInternalAdapter.onViewAttachedToWindow(holder);
            } else {
                ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
                if (lp != null
                        && lp instanceof StaggeredGridLayoutManager.LayoutParams
                        && (isFooter(holder.getLayoutPosition()))) {
                    StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                    p.setFullSpan(true);
                }
            }


        }

        private boolean isFooter(int position) {
            //return position < getItemCount() && position >= getItemCount() - 1;
            return position == getItemCount() - 1;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (TYPE_FOOTER == getItemViewType(position)) {
                if (holder instanceof FooterViewHolder) {
                    holder.itemView.setVisibility(VISIBLE);
                    if (mLoadMoreState == MOREEND) {
                        ((FooterViewHolder) holder).progressBar.setVisibility(GONE);
                        ((FooterViewHolder) holder).textView.setText(morefinishhint);
                    } else if (mLoadMoreState == MOREING) {
                        ((FooterViewHolder) holder).progressBar.setVisibility(VISIBLE);
                        ((FooterViewHolder) holder).textView.setText("正在努力加载中...");
                    } else {
                        holder.itemView.setVisibility(GONE);
                    }
                }
            } else {
                mInternalAdapter.onBindViewHolder(holder, position);
            }
        }

        public void addHeaderView(View headview, ViewHolder mHeadViewHolder) {
            this.mHeadView = headview;
            this.mHeadViewHolder = mHeadViewHolder;
        }

        /**
         * 需要计算上加载更多和添加的头部俩个
         *
         * @return
         */
        @Override
        public int getItemCount() {
            int count = 0;
            if (mInternalAdapter != null) {
                count = mInternalAdapter.getItemCount();
                if (mIsFooterEnable) count++;
                if (mIsHeaderEnable) count++;
            }
            return count;
        }

        public void setHeaderEnable(boolean enable) {
            mIsHeaderEnable = enable;
        }

        public class FooterViewHolder extends ViewHolder {
            private ProgressBar progressBar;
            private TextView textView;

            public FooterViewHolder(View itemView) {
                super(itemView);
                progressBar = (ProgressBar) itemView.findViewById(R.id.probar);
                textView = (TextView) itemView.findViewById(R.id.tv_hint);
                textView.setTextColor(notifyColor);
                textView.setTextSize(notifyTextsize);
            }
        }

        public class HeaderViewHolder extends ViewHolder {
            public HeaderViewHolder(View itemView) {
                super(itemView);
            }
        }

    }
}