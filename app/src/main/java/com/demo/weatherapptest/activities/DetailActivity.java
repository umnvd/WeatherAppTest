package com.demo.weatherapptest.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.weatherapptest.R;
import com.demo.weatherapptest.adapters.ForecastsWeatherAdapter;
import com.demo.weatherapptest.adapters.HourlyWeatherAdapter;
import com.demo.weatherapptest.data.City;
import com.demo.weatherapptest.pojo.Forecast;
import com.demo.weatherapptest.pojo.Hour;
import com.demo.weatherapptest.pojo.Part;
import com.demo.weatherapptest.pojo.Parts;
import com.demo.weatherapptest.pojo.WeatherResponse;
import com.demo.weatherapptest.utils.SvgUtils;
import com.demo.weatherapptest.utils.WeatherUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class DetailActivity extends AppCompatActivity {

    private CompositeDisposable disposables;
    private ProgressBar progressBarLoadingForecasts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra("city")) {
            finish();
        }
        City city = intent.getParcelableExtra("city");


        disposables = new CompositeDisposable();
        progressBarLoadingForecasts = findViewById(R.id.progressBarLoadingForecasts);
        disposables.add(WeatherUtils.loadWeather(city, 7)
                .doOnSubscribe(__ -> progressBarLoadingForecasts.setVisibility(View.VISIBLE))
                .doAfterTerminate(() -> progressBarLoadingForecasts.setVisibility(View.GONE))
                .subscribe(
                        weatherResponse -> {

                            setWeatherView(weatherResponse);
                        },
                        throwable -> {
                            Toast.makeText(DetailActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        })
        );

    }

    private void setWeatherView(WeatherResponse weatherResponse) {
        setCurrentWeather(weatherResponse);
        setTodayHourlyWeather(weatherResponse);
        setTomorrowWeather(weatherResponse);
        setForecasts(weatherResponse);
    }

    private void setCurrentWeather(WeatherResponse weatherResponse) {
        ImageView imageViewBackground = findViewById(R.id.imageViewBackground);
        TextView textViewName = findViewById(R.id.textViewName);
        TextView textViewCurrentDate = findViewById(R.id.textViewCurrentDate);
        TextView textViewCurrentTemp = findViewById(R.id.textViewCurrentTemp);
        ImageView imageViewCurrentWeatherIcon = findViewById(R.id.imageViewCurrentWeatherIcon);
        TextView textViewCurrentCondition = findViewById(R.id.textViewCurrentCondition);
        TextView textViewCurrentFeels = findViewById(R.id.textViewCurrentFeels);
        TextView textViewCurrentWind = findViewById(R.id.textViewCurrentWind);
        TextView textViewCurrentHumidity = findViewById(R.id.textViewCurrentHumidity);
        TextView textViewCurrentPressure = findViewById(R.id.textViewCurrentPressure);

        String dayTime = weatherResponse.getFact().getDaytime();
        int backgroundResource = WeatherUtils.getBackgroundResource(dayTime);
        String name = weatherResponse.getInfo().getName();
        long offset = weatherResponse.getInfo().getTzinfo().getOffset();
        String date = WeatherUtils.getFormattedDate(weatherResponse.getNow(), offset);
        String temp = WeatherUtils.getFormattedTemp(weatherResponse.getFact().getTemp());
        String icon = weatherResponse.getFact().getIcon();
        String condition = weatherResponse.getFact().getCondition();
        String formattedCondition = WeatherUtils.getFormattedCondition(condition);
        String feelsTemp = WeatherUtils.getFormattedTemp(weatherResponse.getFact().getFeelsLike());
        String feels = String.format("Ощущается как %s", feelsTemp);
        double windSpeed = weatherResponse.getFact().getWindSpeed();
        String windDir = weatherResponse.getFact().getWindDir();
        String formattedWindDir = WeatherUtils.getFormattedWindDir(windDir);
        String wind = String.format("Ветер %s  м/с, %s", windSpeed, formattedWindDir);
        int humidityValue = weatherResponse.getFact().getHumidity();
        String humidity = String.format("Влажность воздуха %s%%", humidityValue);
        int pressureValue = weatherResponse.getFact().getPressureMm();
        String pressure = String.format("Давление %s мм рт. ст.", pressureValue);

        imageViewBackground.setImageResource(backgroundResource);
        textViewName.setText(name);
        textViewCurrentDate.setText(date);
        textViewCurrentTemp.setText(temp);
        SvgUtils.fetchSvg(this, icon, imageViewCurrentWeatherIcon);
        textViewCurrentCondition.setText(formattedCondition);
        textViewCurrentFeels.setText(feels);
        textViewCurrentWind.setText(wind);
        textViewCurrentHumidity.setText(humidity);
        textViewCurrentPressure.setText(pressure);
    }

    private void setTodayHourlyWeather(WeatherResponse weatherResponse) {
        RecyclerView recyclerViewHourlyWeathers = findViewById(R.id.recyclerViewHourlyWeathers);
        recyclerViewHourlyWeathers.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        HourlyWeatherAdapter adapter = new HourlyWeatherAdapter();
        recyclerViewHourlyWeathers.setAdapter(adapter);
        recyclerViewHourlyWeathers.setHasFixedSize(true);

        List<Hour> hours = getRightHours(weatherResponse);
        adapter.setHours(hours);
    }

    private List<Hour> getRightHours(WeatherResponse weatherResponse) {
        long offset = weatherResponse.getInfo().getTzinfo().getOffset();
        long nowDate = weatherResponse.getNow();
        List<Hour> todayHours = weatherResponse.getForecasts().get(0).getHours();
        List<Hour> tomorrowHours = weatherResponse.getForecasts().get(1).getHours();
        todayHours.addAll(tomorrowHours);
        List<Hour> result = new ArrayList<>();
        for (Hour hour : todayHours) {
            if (result.size() == 24) {
                break;
            }
            if (hour.getHourTs() >= nowDate) {
                hour.setHourTs(hour.getHourTs() + offset);
                result.add(hour);
            }
        }
        return result;
    }

    private void setTomorrowWeather(WeatherResponse weatherResponse) {
        View viewNight = findViewById(R.id.layoutWeatherNight);
        View viewMorning = findViewById(R.id.layoutWeatherMorning);
        View viewDay = findViewById(R.id.layoutWeatherDay);
        View viewEvening = findViewById(R.id.layoutWeatherEvening);

        Parts parts = weatherResponse.getForecasts().get(1).getParts();
        setPartWeather(viewMorning, parts.getMorning(), "Утро");
        setPartWeather(viewDay, parts.getDay(), "День");
        setPartWeather(viewEvening, parts.getEvening(), "Вечер");
        // для прогноза на ночь используется Part night следующего дня
        parts = weatherResponse.getForecasts().get(2).getParts();
        setPartWeather(viewNight, parts.getNight(), "Ночь");
    }

    private void setPartWeather(View view, Part part, String partName) {
        TextView textViewPartName = view.findViewById(R.id.textViewPartName);
        ImageView imageViewPartWeatherIcon = view.findViewById(R.id.imageViewPartWeatherIcon);
        TextView textViewPartMaxTemp = view.findViewById(R.id.textViewPartMaxTemp);
        TextView textViewPartMinTemp = view.findViewById(R.id.textViewPartMinTemp);
        TextView textViewPartCondition = view.findViewById(R.id.textViewPartCondition);

        String icon = part.getIcon();
        String maxTemp = WeatherUtils.getFormattedTemp(part.getTempMax());
        String minTemp = WeatherUtils.getFormattedTemp(part.getTempMin());
        String condition = WeatherUtils.getFormattedCondition(part.getCondition());

        textViewPartName.setText(partName);
        SvgUtils.fetchSvg(this, icon, imageViewPartWeatherIcon);
        textViewPartMaxTemp.setText(maxTemp);
        textViewPartMinTemp.setText(minTemp);
        textViewPartCondition.setText(condition);
    }

    private void setForecasts(WeatherResponse weatherResponse) {
        RecyclerView recyclerViewForecastsWeathers = findViewById(R.id.recyclerViewForecastsWeathers);
        recyclerViewForecastsWeathers.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        ForecastsWeatherAdapter adapter = new ForecastsWeatherAdapter();
        recyclerViewForecastsWeathers.setAdapter(adapter);
        recyclerViewForecastsWeathers.setHasFixedSize(true);

        adapter.setWeatherResponse(weatherResponse);
    }


    @Override
    protected void onDestroy() {
        disposables.dispose();
        super.onDestroy();
    }
}