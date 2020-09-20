package com.demo.weatherapptest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.demo.weatherapptest.R;
import com.demo.weatherapptest.data.City;
import com.demo.weatherapptest.pojo.WeatherResponse;
import com.demo.weatherapptest.utils.WeatherResponseUtils;
import com.demo.weatherapptest.utils.YaWeatherFactory;
import com.demo.weatherapptest.utils.YaWeatherService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {

    private final static List<City> russianCities = new ArrayList<>();
    private final static List<City> worldCities = new ArrayList<>();

    static {
        russianCities.add(new City("Архангельск", 64.539393, 40.516939));
        russianCities.add(new City("Воронеж", 51.661535, 39.200287));
        russianCities.add(new City("Липецк", 52.610220, 39.594719));
        russianCities.add(new City("Москва", 55.753960, 37.620393));
        russianCities.add(new City("Нижний Новгород", 56.326887, 44.005986));
        russianCities.add(new City("Омск", 54.989342, 73.368212));
        russianCities.add(new City("Санкт-Петербург", 59.939095, 30.315868));
        russianCities.add(new City("Томск", 56.484680, 84.948197));
        russianCities.add(new City("Ульяновск", 54.3107593, 48.3642771));
        russianCities.add(new City("Хабаровск", 48.472584, 135.057732));
    }

    static {
        worldCities.add(new City("Вена", 48.2084900, 16.3720800));
        worldCities.add(new City("Киев", 50.4546600, 30.5238000));
        worldCities.add(new City("Копенгаген", 55.6759400, 12.5655300));
        worldCities.add(new City("Мадрид", 40.4165000, -3.7025600));
        worldCities.add(new City("Минск", 53.9000000, 27.5666700));
        worldCities.add(new City("Мюнхен", 48.1374300, 11.5754900));
        worldCities.add(new City("Сидней", -33.8678500, 151.2073200));
        worldCities.add(new City("Сингапур", 1.2896700, 103.8500700));
        worldCities.add(new City("Токио", 35.6895000, 139.6917100));
        worldCities.add(new City("Торонто", 43.7001100, -79.4163000));
    }

    List<WeatherResponse> weathers;
    Observable<List<WeatherResponse>> observableWeathers;

    private ProgressBar progressBar;
    private CompositeDisposable disposables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        disposables = new CompositeDisposable();

        progressBar = findViewById(R.id.progressBar);

        YaWeatherFactory yaWeatherFactory = YaWeatherFactory.getInstance();
        YaWeatherService yaWeatherService = yaWeatherFactory.getYaWeatherService();

        weathers = new ArrayList<>();

        /*for (City city : russianCities) {
            double lat = city.getLat();
            double lon = city.getLon();
            disposables.add(yaWeatherService.getWeather(lat, lon, 2)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(__ -> progressBar.setVisibility(View.VISIBLE))
                    .doOnTerminate(() -> progressBar.setVisibility(View.GONE))
                    .subscribe(
                            weatherResponse -> {
                                weathers.add(weatherResponse);
                                ((TextView) findViewById(R.id.textTest)).append(weatherResponse.getInfo().getTzinfo().getName() + "\n");
                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    )
            );
        }*/

        /*observableWeathers = new Observable<List<WeatherResponse>>() {
            @Override
            protected void subscribeActual(Observer<? super List<WeatherResponse>> observer) {

            }
        };

        disposables.add(Observable.fromIterable(worldCities)
                .forEach(city -> {
                    disposables.add(yaWeatherService.getWeather(city.getLat(), city.getLon(), 2)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe(__ -> progressBar.setVisibility(View.VISIBLE))
                            .doOnTerminate(() -> progressBar.setVisibility(View.GONE))
                            .subscribe(
                                    weatherResponse -> {
                                        weatherResponse.getInfo().setCityName(city.getName());
                                        weathers.add(weatherResponse);
                                        //((TextView) findViewById(R.id.textTest)).append(weatherResponse.getInfo().getCityName()  + " " + weatherResponse.getFact().getTemp() + "\n");
                                    },
                                    throwable -> Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show()
                            )
                    );
                })
        );*/

        Single<List<WeatherResponse>> weathersTest = WeatherResponseUtils.getWeatherList(russianCities);
        disposables.add(weathersTest
                .subscribe(weatherResponses -> {
                    for (WeatherResponse weatherResponse : weatherResponses) {
                        ((TextView) findViewById(R.id.textTest)).append(weatherResponse.getInfo().getCityName() + " " + weatherResponse.getFact().getTemp() + "\n");
                    }
                    // set responses to adapter
                })
        );
    }

    private void loadData() {

    }


    @Override
    protected void onDestroy() {
        disposables.dispose();
        super.onDestroy();
    }
}