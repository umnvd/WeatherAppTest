package com.demo.weatherapptest.utils;

import com.demo.weatherapptest.pojo.WeatherResponse;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface YaWeatherService {

    String API_KEY = "e3131f67-a5d8-4094-a587-0bfe876fb1bc";

    @Headers("X-Yandex-API-Key: " + API_KEY)
    @GET("forecast?")
    Observable<WeatherResponse> getWeather(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("limit") int days
    );

}
