package com.shen.house.post

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.view.View
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.shen.baselibrary.base.BaseActivity
import com.shen.baselibrary.helper.FullyGridLayoutManager
import com.shen.baselibrary.utiles.DisplayUtils
import com.shen.baselibrary.utiles.LogUtils
import com.shen.baselibrary.utiles.ToastUtile
import com.shen.baselibrary.utiles.resulttutils.selectpic.SelectPicCallback
import com.shen.baselibrary.utiles.resulttutils.selectpic.SelectPicUtils
import com.shen.house.CityActivity
import com.shen.house.R
import com.shen.house.customview.spinnerpopupwindow.BaseItem
import com.shen.house.customview.spinnerpopupwindow.BaseSpinerAdapter
import com.shen.house.customview.spinnerpopupwindow.SpinerPopWindow
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.include_title.*
import java.io.File

//发布页面
class PostActivity : BaseActivity() {
    val postBean = PostBean();
    var adapter: GridImageAdapter? = null
    override fun getcontentView(): Int {
        return R.layout.activity_post
    }


    override fun afterInjectView(view: View) {
        titleBack.setOnClickListener { finish() }
        titleText.setText("发布信息")
        recycler.layoutManager = FullyGridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false)
        adapter = GridImageAdapter(this, GridImageAdapter.onAddPicClickListener {
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
                    adapter!!.setList(postBean.pics)
                    adapter!!.notifyDataSetChanged()
                }
            })
        })
//        adapter!!.setList(postBean.pics)
        adapter!!.setSelectMax(SelectPicUtils.MAXSELECTNUM)
        recycler.adapter = adapter
        adapter!!.setOnItemClickListener { position, v ->
            if (postBean.pics!!.size > 0) {
                var media = postBean.pics!!.get(position)
                var mediaType = PictureMimeType.isPictureType(media.pictureType)
                if (mediaType == 1) {
                    PictureSelector.create(`this`).themeStyle(R.style.picture_default_style).openExternalPreview(position, postBean.pics)
                }

            }
        }
        layoutWuZheng.setOnClickListener {
            val lists: ArrayList<BaseItem> = ArrayList()
            for (i in 0..120) {
                lists.add(BaseItem("第${i}个"))
            }
            var baseSpinerAdapter = BaseSpinerAdapter<BaseItem>(`this`, lists, false)

            SpinerPopWindow(`this`).setAdatper(baseSpinerAdapter).setSelect(1)
                    .setItemSelectListener(object : BaseSpinerAdapter.ItemClickCallBack {
                        override fun <T : BaseItem> itemClick(position: Int, item: T) {
                            ToastUtile.showToast("" + item.toString())
                            tvWuZheng.setText(item.toString())
                        }

                    })
                    .showPopupWindow(it, it.width)
        }
        btnPost.setOnClickListener { ToastUtile.showToast("fabu发布") }
        layoutQuXian.setOnClickListener { startActivity(Intent(`this`, CityActivity::class.java)) }
        layoutName.setOnClickListener {
            ToastUtile.showToast("小区名称点击")
            edittextName.setText("dianjile")
        }
        layoutShiTing.setOnClickListener {
            ShiTingPopupWindow(`this`).setShiTingCallBack(object : ShiTingPopupWindow.ShiTingCallBack {
                override fun call(shi: Int, shis: String?, ting: Int, tings: String?, wei: Int, weis: String?) {
                    tvShiTing.setText(TextUtils.concat(shis, tings, weis))
                }
            }).showPopupWindow(it, DisplayUtils.dp2px(`this`, 330f))
        }
    }


}
