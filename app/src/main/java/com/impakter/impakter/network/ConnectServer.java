package com.impakter.impakter.network;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.impakter.impakter.config.WebConfig;

import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Minh Toan
 */

public class ConnectServer {
    private static ServerAPI responseAPI = null;
    private static final int CONNECT_TIMEOUT = 20;

    public static ServerAPI getResponseAPI() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("url", message);
            }
        });

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        httpClient.readTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        httpClient.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);

        Gson gson = new GsonBuilder()
                .setLenient().create();
        if (responseAPI == null) {

            Retrofit.Builder builder = new Retrofit.Builder();
            builder.baseUrl(WebConfig.HOST);
            builder.addConverterFactory(GsonConverterFactory.create(gson));
            builder.client(httpClient.build());
            Retrofit retrofit = builder.build();
            responseAPI = retrofit.create(ServerAPI.class);
        }
        return responseAPI;
    }
}
