package com.demo.weatherapptest.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.demo.weatherapptest.R;
import com.demo.weatherapptest.adapters.CitiesWeatherAdapter;
import com.demo.weatherapptest.data.City;
import com.demo.weatherapptest.pojo.WeatherResponse;
import com.demo.weatherapptest.utils.WeatherUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {

    private final static List<City> russianCities = new ArrayList<>();
    private final static List<City> worldCities = new ArrayList<>();

    // init cities
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

    private CompositeDisposable disposables;

    private RecyclerView recyclerViewWeathers;
    private CitiesWeatherAdapter adapter;
    private ProgressBar progressBarLoadingWeathers;

    private RadioGroup radioGroupWeatherIn;
    private RadioGroup radioGroupWeatherWhen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        disposables = new CompositeDisposable();

        recyclerViewWeathers = findViewById(R.id.recyclerViewWeathers);
        recyclerViewWeathers.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CitiesWeatherAdapter();
        recyclerViewWeathers.setAdapter(adapter);
        recyclerViewWeathers.setHasFixedSize(true);
        progressBarLoadingWeathers = findViewById(R.id.progressBarLoadingWeathers);

        updateWeather(russianCities);

        adapter.setOnWeatherClickListener(position -> {
            WeatherResponse chosenWeather = adapter.getWeathers().get(position);
            String name = chosenWeather.getInfo().getName();
            double lat = chosenWeather.getInfo().getLat();
            double lon = chosenWeather.getInfo().getLon();
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("city", new City(name, lat, lon));
            startActivity(intent);
        });

        radioGroupWeatherIn = findViewById(R.id.radioGroupWeatherIn);
        radioGroupWeatherIn.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.radioInRussia :
                    updateWeather(russianCities);
                    break;
                case R.id.radioInWorld :
                    updateWeather(worldCities);
                    break;
                default:
                    break;
            }
        });

        radioGroupWeatherWhen = findViewById(R.id.radioGroupWeatherWhen);
        radioGroupWeatherWhen.setOnCheckedChangeListener((radioGroup, i) -> {
            boolean isTomorrow = false;
            switch (i) {
                case R.id.radioToday :
                    isTomorrow = false;
                    break;
                case R.id.radioTomorrow :
                    isTomorrow = true;
                    break;
            }
            adapter.setWhen(isTomorrow);
        });
    }

    private void updateWeather(List<City> cities) {
        disposables.add(WeatherUtils.loadWeatherList(cities)
                .doOnSubscribe(__ -> progressBarLoadingWeathers.setVisibility(View.VISIBLE))
                .doAfterTerminate(() -> progressBarLoadingWeathers.setVisibility(View.GONE))
                .subscribe(
                        weatherList -> adapter.setWeathers(weatherList),
                        throwable -> Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show()
                )
        );
    }

    @Override
    protected void onDestroy() {
        disposables.dispose();
        super.onDestroy();
    }
}