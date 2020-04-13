package com.happinesstonic.networking;

import com.happinesstonic.UrlHolder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AppConfig {

    private static String BASE_URL = UrlHolder.URL_ROOT;


    public static Retrofit getRetrofit() {

        return new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}