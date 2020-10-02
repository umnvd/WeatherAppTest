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

    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM", Locale.getDefault());
    public static String getFormattedDate(long unixTime, long offset) {
        return dateFormatter.format(new Date((unixTime + offset) * 1000L));
    }

    private static SimpleDateFormat hourFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
    public static String getFormattedHour(long hourTs) {
        return hourFormatter.format(new Date(hourTs * 1000L));
    }

    public static String getFormattedCondition(String condition) {
        switch (condition) {
            case "clear":
                return "Ясно";
            case "partly-cloudy":
                return "Малооблачно";
            case "cloudy":
                return "Облачно с\nпрояснениями";
            case "overcast":
                return "Пасмурно";
            case "drizzle":
                return "Морось";
            case "light-rain":
                return "Небольшой\nдождь";
            case "rain":
                return "Дождь";
            case "moderate-rain":
                return "Умеренно\nсильный\nдождь";
            case "heavy-rain":
                return "Сильный\nдождь";
            case "continuous-heavy-rain":
                return "Длительный\nсильный\nдождь";
            case "showers":
                return "Ливень";
            case "wet-snow":
                return "Дождь со\nснегом";
            case "light-snow":
                return "Небольшой\nснег";
            case "snow":
                return "Снег";
            case "snow-showers":
                return "Снегопад";
            case "hail":
                return "Град";
            case "thunderstorm":
                return "Гроза";
            case "thunderstorm-with-rain":
                return "Дождь с\nгрозой";
            case "thunderstorm-with-hail":
                return "Гроза с\nградом";
            default:
                return "";
        }
    }

    public static String getFormattedWindDir(String windDir) {
        switch (windDir) {
            case "nw":
                return "СЗ";
            case "n":
                return "С";
            case "ne":
                return "СВ";
            case "e":
                return "В";
            case "se":
                return "ЮВ";
            case "s":
                return "Ю";
            case "sw":
                return "ЮЗ";
            case "w":
                return "З";
            default:
                return "штиль";
        }
    }
}
