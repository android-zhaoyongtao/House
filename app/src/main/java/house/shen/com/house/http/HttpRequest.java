package house.shen.com.house.http;

import android.app.Activity;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.AbsCallback;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 网络请求都走这里
 * okgo详细文档https://github.com/jeasonlzy/okhttp-OkGo/wiki/OkGo
 */
public class HttpRequest {
    public static String request;
    public static String response;

    private static class Holder {
        private static HttpRequest holder = new HttpRequest();
    }

    public static <T> void get(Activity activity, String url, CacheMode cacheMode, AbsCallback callback) {
        OkGo.<LzyResponse<T>>get(url)//
                .tag(activity)
                .cacheMode(cacheMode)
//                .headers("header1", "headerValue1")//
//                .params("param1", "paramValue1")//
                .execute(callback);
    }

    public static <T> void post(Activity activity, String url, Map<String, String> param, AbsCallback callback) {
        OkGo.<LzyResponse<T>>post(url)
                .tag(activity)
//                .cacheMode(cacheMode)
                .params(param)
//                .isMultipart(true)//该方法表示是否强制使用multipart/form-data表单上传，因为该框架在有文件的时候，无论你是否设置这个参数，默认都是multipart/form-data格式上传，但是如果参数中不包含文件，默认使用application/x-www-form-urlencoded格式上传，如果你的服务器要求无论是否有文件，都要使用表单上传，那么可以用这个参数设置为true。
                .execute(callback);
    }

    public static <T> void postUseCache(Activity activity, String url, Map<String, String> param, AbsCallback callback) {
        OkGo.<LzyResponse<T>>post(url)
                .tag(activity)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .params(param)
                .execute(callback);
    }

    public static <T> void postFile(Activity activity, String url, Map<String, String> param, File file, AbsCallback callback) {
        OkGo.<LzyResponse<T>>post(url)
                .tag(activity)
                .params(param)
                .params("file", file)
//                .upFile(file)
                .execute(callback);
    }

    public static <T> void postFile(Activity activity, String url, Map<String, String> param, List<File> files, AbsCallback callback) {
        OkGo.<LzyResponse<T>>post(url)
                .tag(activity)
                .params(param)
                .addFileParams("file", files)
                .execute(callback);
    }

}
