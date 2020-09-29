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
        disposables.add(WeatherUtils.loadWeather(city, 5)
                .doOnSubscribe(__ -> progressBarLoadingForecasts.setVisibility(View.VISIBLE))
                .doAfterTerminate(() -> progressBarLoadingForecasts.setVisibility(View.GONE))
                .subscribe(
                        weatherResponse -> setWeatherView(weatherResponse),
                        throwable -> {
                            Toast.makeText(DetailActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        })
        );

    }

    private void setWeatherView(WeatherResponse weatherResponse) {
        setCurrentWeather(weatherResponse);
        setCurrentHourlyWeather(weatherResponse);
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
        String date = WeatherUtils.getFormattedDate(weatherResponse.getNow());
        String temp = WeatherUtils.getFormattedTemp(weatherResponse.getFact().getTemp());
        String icon = weatherResponse.getFact().getIcon();
        String condition = weatherResponse.getFact().getCondition();
        String feelsTemp = WeatherUtils.getFormattedTemp(weatherResponse.getFact().getFeelsLike());
        String feels = String.format("Ощущается как %s", feelsTemp);
        double windSpeed = weatherResponse.getFact().getWindSpeed();
        String windDir = weatherResponse.getFact().getWindDir();
        String wind = String.format("Ветер %s  м/с, направление %s", windSpeed, windDir);
        int humidityValue = weatherResponse.getFact().getHumidity();
        String humidity = String.format("Влажность воздуха %s%%", humidityValue);
        int pressureValue = weatherResponse.getFact().getPressureMm();
        String pressure = String.format("Давление %s мм рт. ст.", pressureValue);

        imageViewBackground.setImageResource(backgroundResource);
        textViewName.setText(name);
        textViewCurrentDate.setText(date);
        textViewCurrentTemp.setText(temp);
        SvgUtils.fetchSvg(this, icon, imageViewCurrentWeatherIcon);
        textViewCurrentCondition.setText(condition);
        textViewCurrentFeels.setText(feels);
        textViewCurrentWind.setText(wind);
        textViewCurrentHumidity.setText(humidity);
        textViewCurrentPressure.setText(pressure);
    }

    private void setCurrentHourlyWeather(WeatherResponse weatherResponse) {
        RecyclerView recyclerViewHourlyWeathers = findViewById(R.id.recyclerViewHourlyWeathers);
        recyclerViewHourlyWeathers.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        HourlyWeatherAdapter adapter = new HourlyWeatherAdapter();
        recyclerViewHourlyWeathers.setAdapter(adapter);
        recyclerViewHourlyWeathers.setHasFixedSize(true);

        List<Hour> hours = getRightHours(weatherResponse);
        adapter.setHours(hours);
    }

    private List<Hour> getRightHours(WeatherResponse weatherResponse) {
        long nowDate = weatherResponse.getNow() + weatherResponse.getInfo().getTzinfo().getOffset();
        List<Hour> todayHours = weatherResponse.getForecasts().get(0).getHours();
        List<Hour> tomorrowHours = weatherResponse.getForecasts().get(1).getHours();
        todayHours.addAll(tomorrowHours);
        List<Hour> result = new ArrayList<>();
        for (Hour hour : todayHours) {
            if (result.size() == 24) {
                break;
            }
            if (hour.getHourTs() >= nowDate) {
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
        setPartWeather(viewNight, parts.getNight(), "Ночь");
        setPartWeather(viewMorning, parts.getMorning(), "Утро");
        setPartWeather(viewDay, parts.getDay(), "День");
        setPartWeather(viewEvening, parts.getEvening(), "Вечер");
    }

    private void setPartWeather(View view, Part part, String partName) {
        TextView textViewPartName = view.findViewById(R.id.textViewPartName);
        ImageView imageViewPartWeatherIcon = view.findViewById(R.id.imageViewPartWeatherIcon);
        TextView textViewPartMaxTemp = view.findViewById(R.id.textViewPartMaxTemp);
        TextView textViewPartMinTemp = view.findViewById(R.id.textViewPartMinTemp);

        String icon = part.getIcon();
        String maxTemp = WeatherUtils.getFormattedTemp(part.getTempMax());
        String minTemp = WeatherUtils.getFormattedTemp(part.getTempMin());

        textViewPartName.setText(partName);
        SvgUtils.fetchSvg(this, icon, imageViewPartWeatherIcon);
        textViewPartMaxTemp.setText(maxTemp);
        textViewPartMinTemp.setText(minTemp);
    }

    private void setForecasts(WeatherResponse weatherResponse) {
        View[] views = new View[3];
        views[0] = findViewById(R.id.layoutWeatherForecast1);
        views[1] = findViewById(R.id.layoutWeatherForecast2);
        views[2] = findViewById(R.id.layoutWeatherForecast3);

        List<Forecast> forecasts = weatherResponse.getForecasts();
        for (int i = 2; i < views.length; i++) {
            View view = views[i];
            TextView textViewShortDate = view.findViewById(R.id.textViewShortDate);
            ImageView imageViewShortWeatherIcon = view.findViewById(R.id.imageViewShortWeatherIcon);
            TextView textViewShortMaxTemp = view.findViewById(R.id.textViewShortMaxTemp);
            TextView textViewShortMinTemp = view.findViewById(R.id.textViewShortMinTemp);

            Forecast forecast = forecasts.get(i);
            String date = WeatherUtils.getFormattedDate(forecast.getDateTs());
            String icon = forecast.getParts().getDay().getIcon();
            int maxTempValue = forecast.getParts().getDay().getTempMax();
            String maxTemp = WeatherUtils.getFormattedTemp(maxTempValue);
            int minTempValue = forecast.getParts().getNight().getTempMin();
            String minTemp = WeatherUtils.getFormattedTemp(minTempValue);

            textViewShortDate.setText(date);
            SvgUtils.fetchSvg(this, icon, imageViewShortWeatherIcon);
            textViewShortMaxTemp.setText(maxTemp);
            textViewShortMinTemp.setText(minTemp);
        }
    }


    @Override
    protected void onDestroy() {
        disposables.dispose();
        super.onDestroy();
    }
}