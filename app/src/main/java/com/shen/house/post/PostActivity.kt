package com.shen.house.post

import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.view.View
import com.google.gson.reflect.TypeToken
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.shen.baselibrary.base.BaseActivity
import com.shen.baselibrary.customview.spinnerpopupwindow.BaseItem
import com.shen.baselibrary.customview.spinnerpopupwindow.BaseSpinerAdapter
import com.shen.baselibrary.customview.spinnerpopupwindow.SpinerPopWindow
import com.shen.baselibrary.helper.FullyGridLayoutManager
import com.shen.baselibrary.utiles.*
import com.shen.baselibrary.utiles.resulttutils.selectpic.SelectPicCallback
import com.shen.baselibrary.utiles.resulttutils.selectpic.SelectPicUtils
import com.shen.house.R
import com.zaaach.citypicker.LocationUtils
import com.zaaach.citypicker.db.CitysManager
import com.zaaach.citypicker.model.AreaBean
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.include_title.*
import java.io.File

//发布页面
class PostActivity : BaseActivity() {
    val postBean = PostBean();
    var imageAdapter: GridImageAdapter? = null
    override fun getcontentView(): Int {
        return R.layout.activity_post
    }


    override fun afterInjectView(view: View) {
        titleText.setText("发布信息")
        initData()
        initListener()
    }

    private fun initData() {

    }

    private fun initListener() {
        titleBack.setOnClickListener { finish() }
        recycler.layoutManager = FullyGridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false)
        imageAdapter = GridImageAdapter(this, GridImageAdapter.onAddPicClickListener {
            //+点击事件
            //            addPic()
            SelectPicUtils.selectPic(this, false, postBean.pics, object : SelectPicCallback {
                override fun selectPicResult(list: List<LocalMedia>) {
                    postBean.pics = list
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (media in list) {
                        //                    LogUtils.e("图片-----》", media.compressPath)
                        LogUtils.e("图片-----》", "1.${File(media.path).length() / 1024}kb")
                        LogUtils.e("图片-----》", "2.${File(media.compressPath).length() / 1024}kb")

                    }
                    imageAdapter!!.setList(postBean.pics)
                    imageAdapter!!.notifyDataSetChanged()
                }
            })
        })
        //        adapter!!.setList(postBean.pics)
        imageAdapter!!.setSelectMax(SelectPicUtils.MAXSELECTNUM)
        recycler.adapter = imageAdapter
        imageAdapter!!.setOnItemClickListener { position, v ->
            if (postBean.pics!!.size > 0) {
                var media = postBean.pics!!.get(position)
                var mediaType = PictureMimeType.isPictureType(media.pictureType)
                if (mediaType == 1) {
                    PictureSelector.create(`this`).themeStyle(R.style.picture_default_style).openExternalPreview(position, postBean.pics)
                }

            }
        }
        layoutQuXian.setOnClickListener {
            val cuttentCity = LocationUtils.getSPCity()
            if (cuttentCity != null) {
                var areas: List<AreaBean>? = CitysManager(`this`).getAllArea(cuttentCity.areaId)
                if (StringUtils.listSize(areas) > 0) {
                    var quXianAdapter = BaseSpinerAdapter<AreaBean>(`this`, areas, true)
                    SpinerPopWindow(`this`).setAdatper(quXianAdapter).setSelect(postBean.area)
                            .setItemSelectListener(object : BaseSpinerAdapter.ItemClickCallBack<AreaBean> {
                                override fun itemClick(position: Int, item: AreaBean) {
                                    postBean.area = item
                                    tvQuXian.setText(item.text)
                                }
                            })
                            .showPopupWindow(it, it.width)
                } else {
                    ToastUtile.showToast("当前城市无地区信息")
                }
            } else {
                ToastUtile.showToast("当前城市无地区信息")
            }
        }
        layoutShiTing.setOnClickListener {
            ShiTingPopupWindow(`this`).setData(postBean.shiting).setShiTingCallBack(object : ShiTingPopupWindow.ShiTingCallBack {
                override fun call(shi: Int, shis: String?, ting: Int, tings: String?, wei: Int, weis: String?) {
                    postBean.shiting = PostBean.ShiTing(shi, ting, wei)
                    tvShiTing.setText(TextUtils.concat(shis, tings, weis))
                }
            }).showPopupWindow(it, DisplayUtils.dp2px(`this`, 330f))
        }
        layoutChaoXiang.setOnClickListener {
            val chaoxiangs: List<BaseItem>? = AssetsUtils.getObjectFromAssets<ArrayList<BaseItem>>(`this`, "chaoxiang.json", object : TypeToken<ArrayList<BaseItem>>() {}.type)
            if (StringUtils.listSize(chaoxiangs) > 0) {
                var chaoXiangAdapter = BaseSpinerAdapter<BaseItem>(`this`, chaoxiangs, true)
                SpinerPopWindow(`this`).setAdatper(chaoXiangAdapter).setSelect(postBean.chaoxiang).setColunms(2)
                        .setItemSelectListener(object : BaseSpinerAdapter.ItemClickCallBack<BaseItem> {
                            override fun itemClick(position: Int, item: BaseItem) {
                                postBean.chaoxiang = item
                                tvChaoXiang.setText(item.text)
                            }
                        })
                        .showPopupWindow(it)
            }
        }
        layoutDianTi.setOnClickListener {
            DianTiPopupWindow(`this`).setData(postBean.dianti).setDianTiCallBack(object : DianTiPopupWindow.DianTiCallBack {
                override fun call(hasDianTi: Boolean, ti: Int, tis: String?, hu: Int, hus: String?) {
                    postBean.dianti = PostBean.DianTi(hasDianTi, ti, hu)
                    tvDianTi.setText(TextUtils.concat(if (hasDianTi) "有电梯 " else "", tis, hus))
                }
            }).showPopupWindow(it, DisplayUtils.dp2px(`this`, 220f))
        }
        layoutWuZheng.setOnClickListener {
            val wuzhengs = AssetsUtils.getObjectFromAssets<ArrayList<BaseItem>>(`this`, "wuzheng.json", object : TypeToken<ArrayList<BaseItem>>() {}.type)
            var wuZhengAdapter = BaseSpinerAdapter<BaseItem>(`this`, wuzhengs, false)
            SpinerPopWindow(`this`).setAdatper(wuZhengAdapter).setSelect(postBean.wuzhengs)
                    .setPosiButtonClickListener(object : SpinerPopWindow.PosiButtonClickCallBack<BaseItem> {
                        override fun onClick(items: MutableList<BaseItem>) {
                            postBean.wuzhengs = items
                            if (items.isNotEmpty()) {
                                tvWuZheng.setText(items.last()?.text)
                            }
                        }
                    })
                    .showPopupWindow(it)
        }
        layoutZhuangXiu.setOnClickListener {
            val zhuangxius: List<BaseItem>? = AssetsUtils.getObjectFromAssets<ArrayList<BaseItem>>(`this`, "zhuangxiu.json", object : TypeToken<ArrayList<BaseItem>>() {}.type)
            if (StringUtils.listSize(zhuangxius) > 0) {
                var zhuangXiuAdapter = BaseSpinerAdapter<BaseItem>(`this`, zhuangxius, true)
                SpinerPopWindow(`this`).setAdatper(zhuangXiuAdapter).setSelect(postBean.zhuangxiu).setColunms(2)
                        .setItemSelectListener(object : BaseSpinerAdapter.ItemClickCallBack<BaseItem> {
                            override fun itemClick(position: Int, item: BaseItem) {
                                postBean.zhuangxiu = item
                                tvZhuangXiu.setText(item.text)
                            }
                        })
                        .showPopupWindow(it)
            } else {
                ToastUtile.showToast("当前城市无地区信息")
            }
        }
        btnPost.setOnClickListener { ToastUtile.showToast("fabu发布") }

    }


}
