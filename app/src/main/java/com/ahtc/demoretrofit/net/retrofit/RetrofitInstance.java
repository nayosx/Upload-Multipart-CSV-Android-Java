package com.ahtc.demoretrofit.net.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import com.ahtc.demoretrofit.util.S;

public class RetrofitInstance {
    private static Retrofit retrofit;
    private static Retrofit retrofitDynamic;
    private static final int SIMPLE_STRING_RESPONSE = 1;

    public static Retrofit getInstance() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1,TimeUnit.MINUTES)
                .readTimeout(1,TimeUnit.MINUTES)
                .build();
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(S.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getInstance(String url, Integer converter) {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1,TimeUnit.MINUTES)
                .writeTimeout(1,TimeUnit.MINUTES)
                .readTimeout(1,TimeUnit.MINUTES)
                .build();
        if (retrofitDynamic ==null) {
            Retrofit.Builder builder = new Retrofit.Builder();
            builder.client(okHttpClient);
            builder.baseUrl(url);
            if (converter == SIMPLE_STRING_RESPONSE) {
                builder.addConverterFactory(ScalarsConverterFactory.create());
            } else {
                builder.addConverterFactory(GsonConverterFactory.create());
            }
            retrofitDynamic = builder.build();
        }
        return retrofitDynamic;
    }
}
