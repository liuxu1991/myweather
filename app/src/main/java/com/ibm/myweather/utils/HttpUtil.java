package com.ibm.myweather.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by 刘旭 on 2016/12/31.
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        okHttpClient.newCall(request).enqueue(callback);

    }
}
