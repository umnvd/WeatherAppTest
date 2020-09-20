package com.demo.weatherapptest.utils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YaWeatherFactory {

    private static final String BASE_URL = "https://api.weather.yandex.ru/v2/";
    private static final Object LOCK = new Object();

    private static YaWeatherFactory instance;
    private static Retrofit retrofit;

    private YaWeatherFactory() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    public static YaWeatherFactory getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new YaWeatherFactory();
            }
        }
        return instance;
    }

    public YaWeatherService getYaWeatherService() {
        return retrofit.create(YaWeatherService.class);
    }
}
