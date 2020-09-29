package com.demo.weatherapptest.utils;

import com.demo.weatherapptest.R;
import com.demo.weatherapptest.data.City;
import com.demo.weatherapptest.pojo.WeatherResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WeatherUtils {

    private static YaWeatherFactory yaWeatherFactory = YaWeatherFactory.getInstance();
    private static YaWeatherService yaWeatherService = yaWeatherFactory.getYaWeatherService();

    public static Single<List<WeatherResponse>> loadWeatherList(List<City> cityList) {
        return Observable.fromIterable(cityList)
                .flatMap(city -> loadWeather(city, 3))
                .toList();
    }

    public static Observable<WeatherResponse> loadWeather(City city, int days) {
        return yaWeatherService.getWeather(city.getLat(), city.getLon(), days)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(weatherResponse -> {
                    weatherResponse.getInfo().setName(city.getName());
                    return weatherResponse;
                });
    }

    public static String getFormattedTemp(int temp) {
        String signedTemp = (temp > 0) ? "+" + temp : "" + temp;
        return String.format("%s\u00B0", signedTemp);
    }

    public static int getBackgroundResource(String dayTime) {
        return (dayTime.equals("d")) ? R.drawable.day_background : R.drawable.night_background;
    }

    private static SimpleDateFormat formatter = new SimpleDateFormat("dd.MM", Locale.getDefault());
    public static String getFormattedDate(long unixTime) {
        return formatter.format(new Date(unixTime * 1000L));
    }
}
