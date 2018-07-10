package com.shen.house;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.lzy.okgo.OkGo;
import com.shen.baselibrary.glide.OkHttpUrlLoader;
import com.shen.baselibrary.utiles.ExecutorUtile;
import com.shen.baselibrary.utiles.FileUtils;
import com.shen.baselibrary.utiles.LogUtils;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;

@GlideModule(glideName = "GlideLoader")
public final class GlideConfiguration extends AppGlideModule {
    public static final int CACHESIZE = 100 * 1024 * 1024;

    @Override
    public void applyOptions(@NonNull final Context context, @NonNull GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2)
                .build();
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
        int customMemoryCacheSize = (int) (0.5 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (0.5 * defaultBitmapPoolSize);
        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));
        final DiskLruCacheFactory.CacheDirectoryGetter directoryGetter = new DiskLruCacheFactory.CacheDirectoryGetter() {
            @Override
            public File getCacheDirectory() {
                return new File(FileUtils.getCacheDir(context) + "gilde");
            }
        };
        builder.setDiskCache(new DiskLruCacheFactory(directoryGetter, CACHESIZE) {
            @Override
            public DiskCache build() {
                DiskLruCacheWrapper build = (DiskLruCacheWrapper) super.build();
                try {
                    DiskLruCache diskLruCache = DiskLruCache.open(directoryGetter.getCacheDirectory(), 1, 1, CACHESIZE);
                    Field field = build.getClass().getDeclaredField("diskLruCache");
                    Field service = diskLruCache.getClass().getDeclaredField("executorService");
                    service.setAccessible(true);
                    field.setAccessible(true);
                    service.set(diskLruCache, ExecutorUtile.threadPoolExecutor);
                    field.set(build, diskLruCache);
                } catch (Exception e) {
                    LogUtils.e("GlideConfiguration", e);
                }
                return build;
            }
        });
//        builder.setResizeService(ExecutorUtile.threadPoolExecutor);
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        //1.glide 修改默认加载 使用OKHttp 2。cachekey 修改为 图片路径
        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(OkGo.getInstance().getOkHttpClient());
        registry.replace(GlideUrl.class, InputStream.class, factory);
    }
}
