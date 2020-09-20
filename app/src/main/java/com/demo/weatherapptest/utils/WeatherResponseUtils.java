package com.demo.weatherapptest.utils;

import com.demo.weatherapptest.data.City;
import com.demo.weatherapptest.pojo.WeatherResponse;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WeatherResponseUtils {

    private static YaWeatherFactory yaWeatherFactory = YaWeatherFactory.getInstance();
    private static YaWeatherService yaWeatherService = yaWeatherFactory.getYaWeatherService();

    public static Single<List<WeatherResponse>> getWeatherList(List<City> cityList) {
        return Observable.fromIterable(cityList)
                .flatMap(city -> loadWeather(city))
                .toList();
    }

    private static Observable<WeatherResponse> loadWeather(City city) {
        return yaWeatherService.getWeather(city.getLat(), city.getLon(), 2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(weatherResponse -> {
                    weatherResponse.getInfo().setCityName(city.getName());
                    return weatherResponse;
                });
    }
}
