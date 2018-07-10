package com.shen.house.post

import android.Manifest
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.tools.PictureFileUtils
import com.shen.baselibrary.base.BaseActivity
import com.shen.baselibrary.helper.FullyGridLayoutManager
import com.shen.baselibrary.utiles.ToastUtile
import com.shen.baselibrary.utiles.requestutiles.PermissionCallBack
import com.shen.baselibrary.utiles.requestutiles.PermissionUtils
import com.shen.house.R
import com.shen.house.customview.spinnerpopupwindow.BaseItem
import com.shen.house.customview.spinnerpopupwindow.BaseSpinerAdapter
import com.shen.house.customview.spinnerpopupwindow.SpinerPopWindow
import com.shen.house.utiles.MessageDialog
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.include_title.*

//发布页面
class PostActivity : BaseActivity() {
    var adapter: GridImageAdapter? = null
    val selectList: List<LocalMedia> = ArrayList()
    val maxSelectNum = 12
    override fun getcontentView(): Int {
        return R.layout.activity_post
    }


    override fun afterInjectView(view: View) {
        titleBack.setOnClickListener { finish() }
        titleText.setText("发布信息")
        recycler.layoutManager = FullyGridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false)
        adapter = GridImageAdapter(this, GridImageAdapter.onAddPicClickListener {
            //+点击事件
            addPic()
        })
        adapter!!.setList(selectList)
        adapter!!.setSelectMax(maxSelectNum)
        recycler.adapter = adapter
        adapter!!.setOnItemClickListener { position, v ->
            if (selectList.size > 0) {
                var media = selectList.get(position)
                var mediaType = PictureMimeType.isPictureType(media.pictureType)
                if (mediaType == 1) {
                    PictureSelector.create(`this`).themeStyle(R.style.picture_default_style).openExternalPreview(position, selectList)
                }

            }
        }
        tvWuZheng.setOnClickListener {
            var lists: ArrayList<BaseItem> = ArrayList()
            for (i in 0..120) {
                lists.add(BaseItem("第${i}个"))
            }
            var baseSpinerAdapter = BaseSpinerAdapter<BaseItem>(`this`, lists, true)

            SpinerPopWindow(`this`).setAdatper(baseSpinerAdapter).setSelect(1)
                    .setItemSelectListener(object : BaseSpinerAdapter.ItemClickCallBack {
                        override fun <T : BaseItem> itemClick(position: Int, item: T) {
                            ToastUtile.showToast("" + item.toString())
                        }

                    })
                    .showPopupWindow(it)
        }
    }

    private fun addPic() {
        PermissionUtils.requestPermission(this, Manifest.permission.CAMERA, object : PermissionCallBack() {
            override fun refusePermissionDonotAskAgain() {
                MessageDialog(`this`, "提示", "授予该应用相机权限后才能上传照片,是否去设置中开启权限?", "前往") {
                    PermissionUtils.toAppSetting(`this`)
                }
            }

            override fun hasPermission() {
                // 进入相册 以下是例子：不需要的api可以不写
                PictureSelector.create(`this`)
                        .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                        .maxSelectNum(maxSelectNum)// 最大图片选择数量
                        .minSelectNum(0)// 最小选择数量
                        .imageSpanCount(4)// 每行显示个数
                        .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                        .previewImage(true)// 是否可预览图片
                        .previewVideo(true)// 是否可预览视频
                        .enablePreviewAudio(true) // 是否可播放音频
                        .isCamera(true)// 是否显示拍照按钮
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                        //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                        .enableCrop(false)// 是否裁剪
                        .compress(true)// 是否压缩
                        .synOrAsy(true)//同步true或异步false 压缩 默认同步
                        //.compressSavePath(getPath())//压缩图片保存地址
                        //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                        .withAspectRatio(3, 2)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                        .isGif(false)// 是否显示gif图片
                        .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                        .circleDimmedLayer(false)// 是否圆形裁剪
                        .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .openClickSound(false)// 是否开启点击声音
                        .selectionMedia(selectList)// 是否传入已选图片
                        //.isDragFrame(false)// 是否可拖动裁剪框(固定)
                        //                        .videoMaxSecond(15)
                        //                        .videoMinSecond(10)
                        //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                        //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                        //.rotateEnabled(true) // 裁剪是否可旋转图片
                        //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                        //.videoQuality()// 视频录制质量 0 or 1
                        //.videoSecond()//显示多少秒以内的视频or音频也可适用
                        //.recordVideoSecond()//录制视频秒数 默认60s
                        .forResult(PictureConfig.CHOOSE_REQUEST)//结果回调onActivityResult code
            }
        })

    }

    fun deleteCache() {//// 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        PictureFileUtils.deleteCacheDirFile(`this`)
    }
}
