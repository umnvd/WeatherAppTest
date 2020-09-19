package com.demo.weatherapptest.utils;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

public class YaWeatherFactory {

    private static YaWeatherFactory instance;
    private static Retrofit retrofit;
    private static final Object LOCK = new Object();

    private YaWeatherFactory() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weather.yandex.ru/v2/forecast")
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
