package house.shen.com.house.base

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import house.shen.com.house.ContextHouse
import house.shen.com.house.R

class ContentViewWrap : StatusHandle {
    private var mLoadRoot: FrameLayout;
    private var mErrorRoot: FrameLayout;
    private var mLoadStatus = 0;
    private var mRefreshClick: View.OnClickListener? = null
    private var mMarginTop = (ContextHouse.DP1 * 50).toInt()
    private var cp: View? = null;
    private var tvLoadingDesc: TextView? = null;
    private var noData: View? = null
    private var noNet: View? = null
    private var error: View? = null
    private var serverError: View? = null
    private var vShadow: View? = null

    constructor(activity: Activity) {
        val contentView: View = activity.findViewById(Window.ID_ANDROID_CONTENT)
        if (contentView is FrameLayout) {
            mLoadRoot = contentView;
        } else {
            if (contentView is ViewGroup && contentView.childCount > 0) {
                val contentChild = contentView.getChildAt(0)
                if (contentChild is FrameLayout) {
                    mLoadRoot = contentChild
                } else {
                    mLoadRoot = FrameLayout(activity);
                    contentView.removeView(contentChild)
                    contentView.addView(mLoadRoot, 0);
                    mLoadRoot.addView(contentChild, FrameLayout.LayoutParams(-1, -1));
                    val layoutParams = mLoadRoot.layoutParams
                    layoutParams.height = -1;
                    layoutParams.width = -1;
                }
            } else mLoadRoot = FrameLayout(activity);
        }
        mErrorRoot = mLoadRoot;
    }

    constructor(root: FrameLayout) {
        mLoadRoot = root;
        mErrorRoot = mLoadRoot;
    }

    constructor(loadRoot: FrameLayout, errorRoot: FrameLayout) {
        mLoadRoot = loadRoot;
        mErrorRoot = errorRoot;
    }

    override fun clickError(l: View.OnClickListener) {
        this.mRefreshClick = l
    }

    fun clickError(l: View.OnClickListener, marginLeft: Int, marginTop: Int, marginRight: Int, marginBottom: Int) {
        this.mRefreshClick = l
        this.mMarginTop = marginTop
    }

    override fun onLoading(type: Int, desc: String?) {
        if (cp == null) {
            val load = View.inflate(mLoadRoot.context, R.layout.activity_baseroot, null)
            mLoadRoot.addView(load)
            cp = load.findViewById(R.id.cp)
            tvLoadingDesc = load.findViewById(R.id.tvLoadingDesc) as TextView
        }
        cp?.bringToFront()
        mLoadStatus = type
        cp?.visibility = View.VISIBLE;
        if (desc == null) {
            tvLoadingDesc?.visibility = (View.GONE)
        } else {
            tvLoadingDesc?.visibility = (View.VISIBLE)
            tvLoadingDesc?.text = desc
        }
        when {
            type and StatusHandle.TYPE_UNCLICK == StatusHandle.TYPE_UNCLICK -> cp?.setOnClickListener { }
            type and StatusHandle.TYPE_UNCANCLE == 0 -> cp?.setOnClickListener { cp?.visibility = View.INVISIBLE }
            else -> {
                cp?.setOnClickListener(null)
                cp?.isClickable = false;
            }
        }
    }

    fun getStatusType() = mLoadStatus

    override fun onLoadingFinish(dismissAll: Boolean) {
        this.mLoadStatus = StatusHandle.TYPE_NORMAL
        cp?.visibility = (View.INVISIBLE)
        if (dismissAll) {
            noData?.visibility = (View.GONE)
            noNet?.visibility = (View.GONE)
            error?.visibility = (View.GONE)
            serverError?.visibility = (View.GONE)
        }
    }

    override fun onNoData() {
        if (noData == null) {
            noData = View.inflate(mErrorRoot.context, R.layout.include_nodata, null)
            noData?.setOnClickListener(mRefreshClick)
        }
        if (noData?.parent == null) {
            val layoutParams = FrameLayout.LayoutParams(-1, -1)
            layoutParams.topMargin = mMarginTop
            mErrorRoot.addView(noData, layoutParams)
        }
        noData?.visibility = (View.VISIBLE)
    }

    override fun onNoNetwork() {
        if (noNet == null) {
            noNet = View.inflate(mErrorRoot.context, R.layout.include_nonetwork, null)
            val btn_refresh: Button = noNet?.findViewById(R.id.btn_refresh) as Button
            btn_refresh?.setOnClickListener(mRefreshClick)
            noNet?.setOnClickListener() {}
        }
        if (noNet?.parent == null) {
            val layoutParams = FrameLayout.LayoutParams(-1, -1)
            layoutParams.topMargin = mMarginTop
            mErrorRoot.addView(noNet, layoutParams)
        }
        noNet?.visibility = (View.VISIBLE)
    }

    override fun onError() {
        if (error == null) {
            error = View.inflate(mErrorRoot.context, R.layout.include_error, null)
            val btn_refresh: Button = error?.findViewById(R.id.btn_refresh) as Button
            if (mRefreshClick != null) {
                error?.setOnClickListener(mRefreshClick)
            }
        }
        if (error?.parent == null) {
            val layoutParams = FrameLayout.LayoutParams(-1, -1)
            layoutParams.topMargin = mMarginTop
            mErrorRoot.addView(error, layoutParams)
        }
        error?.setVisibility(View.VISIBLE)
    }

    override fun onServerError() {
        if (serverError == null) {
            serverError = View.inflate(mErrorRoot.context, R.layout.include_server_error, null)
            val btn_refresh: Button = serverError?.findViewById(R.id.btn_refresh) as Button
            if (mRefreshClick != null) {
                btn_refresh?.setOnClickListener(mRefreshClick)
            }
            serverError?.setOnClickListener() {}
        }
        if (serverError?.parent == null) {
            val layoutParams = FrameLayout.LayoutParams(-1, -1)
            layoutParams.topMargin = mMarginTop
            mErrorRoot.addView(serverError, layoutParams)
        }
        serverError?.visibility = (View.VISIBLE)
    }

    fun setShadow(marginTop: Int): View? {
        if (marginTop < 0) {
            if (vShadow != null) {
                mLoadRoot.removeView(vShadow)
                vShadow = null;
            }
        } else {
            fun setParams(marginTop: Int) {
                val params = vShadow?.layoutParams as ViewGroup.MarginLayoutParams
                params.height = (ContextHouse.DP1 * 4).toInt()
                params.topMargin = marginTop
            }
            if (vShadow == null) {
                vShadow = View(mLoadRoot.context);
                mLoadRoot.addView(vShadow)
                setParams(marginTop)
                vShadow?.setBackgroundResource(R.drawable.shadow_top);
            } else {
                setParams(marginTop)
                vShadow?.requestLayout()
            }
        }
        return vShadow
    }


    fun setError(error: View) {
        this.error = error;
    }

    fun setNoNet(noNet: View) {
        this.noNet = noNet;
    }

    fun setNoData(noData: View) {
        this.noData = noData;
    }

    fun removeAllView() {
        mLoadRoot.removeAllViews()
        if (mErrorRoot !== mLoadRoot)
            mErrorRoot.removeAllViews()
    }
}