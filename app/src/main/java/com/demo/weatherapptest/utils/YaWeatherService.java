package com.demo.weatherapptest.utils;

import org.json.JSONObject;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface YaWeatherService {

    @Headers("X-Yandex-API-Key: e3131f67-a5d8-4094-a587-0bfe876fb1bc")
    @GET("forecast?lang=ru-RU&hours=true&extra=true")
    JSONObject getWeatherJSON(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("limit") int days
    );

}
