package com.zaaach.citypicker;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaaach.citypicker.adapter.CityListAdapter;
import com.zaaach.citypicker.adapter.InnerListener;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.adapter.decoration.DividerItemDecoration;
import com.zaaach.citypicker.adapter.decoration.SectionItemDecoration;
import com.zaaach.citypicker.db.CitysManager;
import com.zaaach.citypicker.model.CityBean;
import com.zaaach.citypicker.model.HotCityBean;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.view.SideIndexBar;

import java.util.ArrayList;
import java.util.List;

public class CityPickerDialogFragment extends AppCompatDialogFragment implements TextWatcher,
        View.OnClickListener, SideIndexBar.OnIndexTouchedChangedListener, InnerListener {
    private RecyclerView mRecyclerView;
    private View mEmptyView;
    private TextView mOverlayTextView;
    private SideIndexBar mIndexBar;
    private EditText mSearchBox;
    private TextView mCancelBtn;
    private ImageView mClearAllBtn;

    private LinearLayoutManager mLayoutManager;
    private CityListAdapter mAdapter;
    private List<CityBean> mAllCities;
    private List<HotCityBean> mHotCities;
    private List<CityBean> mResults;

    private CitysManager citysManager;

    private boolean enableAnim = false;
    private int mAnimStyle = R.style.DefaultCityPickerAnimation;
    private CityBean mLocatedCity;
    private int locateState;
    private OnPickListener mOnPickListener;

    /**
     * 获取实例
     *
     * @param enable 是否启用动画效果
     * @return
     */
    public static CityPickerDialogFragment newInstance(boolean enable) {
        final CityPickerDialogFragment fragment = new CityPickerDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean("cp_enable_anim", enable);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.CityPickerStyle);

        Bundle args = getArguments();
        if (args != null) {
            enableAnim = args.getBoolean("cp_enable_anim");
        }

        initHotCities();
        initLocatedCity();

        citysManager = new CitysManager(getContext());
        mAllCities = citysManager.getAllCities();
        mAllCities.add(0, mLocatedCity);
        mAllCities.add(1, new HotCityBean("热门城市", "0"));
        mResults = mAllCities;
    }

    private void initLocatedCity() {
        if (mLocatedCity == null) {
            mLocatedCity = new CityBean(getString(R.string.cp_locating), "当前城市", "0");
            locateState = LocateState.LOCATING;
        } else {
            locateState = LocateState.SUCCESS;
        }
    }

    private void initHotCities() {
        if (mHotCities == null || mHotCities.isEmpty()) {
            mHotCities = new ArrayList<>(9);
            mHotCities.add(new HotCityBean("保定", "130600"));
            mHotCities.add(new HotCityBean("北京", "110000"));
            mHotCities.add(new HotCityBean("天津", "120000"));
            mHotCities.add(new HotCityBean("上海", "310000"));
            mHotCities.add(new HotCityBean("广州", "440100"));
            mHotCities.add(new HotCityBean("深圳", "440300"));
            mHotCities.add(new HotCityBean("杭州", "330100"));
            mHotCities.add(new HotCityBean("成都", "510100"));
            mHotCities.add(new HotCityBean("武汉", "420100"));
        }
    }

    public void setLocatedCity(CityBean location) {
        mLocatedCity = location;
    }

    public void setHotCities(List<HotCityBean> data) {
        if (data != null && !data.isEmpty()) {
            this.mHotCities = data;
        }
    }

    @SuppressLint("ResourceType")
    public void setAnimationStyle(@StyleRes int style) {
        this.mAnimStyle = style <= 0 ? R.style.DefaultCityPickerAnimation : style;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mContentView = inflater.inflate(R.layout.cp_dialog_city_picker, container, false);

        mRecyclerView = mContentView.findViewById(R.id.cp_city_recyclerview);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SectionItemDecoration(getActivity(), mAllCities), 0);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()), 1);
        mAdapter = new CityListAdapter(getActivity(), mAllCities, mHotCities, locateState);
        mAdapter.setInnerListener(this);
        mAdapter.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //确保定位城市能正常刷新
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mAdapter.refreshLocationItem();
                }
            }
        });

        mEmptyView = mContentView.findViewById(R.id.cp_empty_view);
        mOverlayTextView = mContentView.findViewById(R.id.cp_overlay);

        mIndexBar = mContentView.findViewById(R.id.cp_side_index_bar);
        mIndexBar.setOverlayTextView(mOverlayTextView).setOnIndexChangedListener(this);

        mSearchBox = mContentView.findViewById(R.id.cp_search_box);
        mSearchBox.addTextChangedListener(this);

        mCancelBtn = mContentView.findViewById(R.id.cp_cancel);
        mClearAllBtn = mContentView.findViewById(R.id.cp_clear_all);
        mCancelBtn.setOnClickListener(this);
        mClearAllBtn.setOnClickListener(this);
        if (locateState != LocateState.SUCCESS) {
            Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
                @Override
                public boolean queueIdle() {
                    locate();
                    return false;
                }
            });
        }
        return mContentView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        if (window != null) {
            window.getDecorView().setPadding(0, 0, 0, 0);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            if (enableAnim) {
                window.setWindowAnimations(mAnimStyle);
            }
        }
        return dialog;
    }

    /**
     * 搜索框监听
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String keyword = s.toString();
        if (TextUtils.isEmpty(keyword)) {
            mClearAllBtn.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.GONE);
            mResults = mAllCities;
            ((SectionItemDecoration) (mRecyclerView.getItemDecorationAt(0))).setData(mResults);
            mAdapter.updateData(mResults);
        } else {
            mClearAllBtn.setVisibility(View.VISIBLE);
            //开始数据库查找
            mResults = citysManager.searchCity(mAllCities, keyword);
            ((SectionItemDecoration) (mRecyclerView.getItemDecorationAt(0))).setData(mResults);
            if (mResults == null || mResults.isEmpty()) {
                mEmptyView.setVisibility(View.VISIBLE);
            } else {
                mEmptyView.setVisibility(View.GONE);
                mAdapter.updateData(mResults);
            }
        }
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cp_cancel) {
            dismiss(-1, null);
        } else if (id == R.id.cp_clear_all) {
            mSearchBox.setText("");
        }
    }

    @Override
    public void onIndexChanged(String index, int position) {
        //滚动RecyclerView到索引位置
        mAdapter.scrollToSection(index);
    }
    //定位完成
    public void locationChanged(final CityBean location, int state) {
        mAdapter.updateLocateState(location, state);
        mRecyclerView.postDelayed(new Runnable() {//选中后再消失,肉眼可见
            @Override
            public void run() {
                 dismiss(0,location);
            }
        }, 100);
    }

    @Override
    public void dismiss(int position, CityBean city) {
        dismiss();
        if (mOnPickListener != null) {
            mOnPickListener.onPick(position, city);
        }
    }

    @Override
    public void locate() {
        if (mOnPickListener != null) {
            mOnPickListener.onLocate();
        }
    }

    public void setOnPickListener(OnPickListener listener) {
        this.mOnPickListener = listener;
    }
}
