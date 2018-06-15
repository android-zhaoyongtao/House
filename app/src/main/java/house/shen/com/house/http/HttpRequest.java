package house.shen.com.house.http;

import android.app.Activity;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

/**
 * 网络请求都走这里
 * okgo详细文档https://github.com/jeasonlzy/okhttp-OkGo/wiki/OkGo
 */
public class HttpRequest {
    public static HttpRequest getInstance() {
        return Holder.holder;
    }

    private static class Holder {
        private static HttpRequest holder = new HttpRequest();
    }

    public static void get(Activity activity, String url,) {
        OkGo.<LzyResponse<ServerModel>>get(url)//
                .tag(activity)//
//                .headers("header1", "headerValue1")//
//                .params("param1", "paramValue1")//
                .execute(new DialogCallback<LzyResponse<ServerModel>>(this) {
                    @Override
                    public void onSuccess(Response<LzyResponse<ServerModel>> response) {
                        handleResponse(response);
                    }

                    @Override
                    public void onError(Response<LzyResponse<ServerModel>> response) {
                        handleError(response);
                    }
                });
    }

}
