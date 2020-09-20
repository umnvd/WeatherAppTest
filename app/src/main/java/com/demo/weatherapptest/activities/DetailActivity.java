package com.demo.weatherapptest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.demo.weatherapptest.R;
import com.demo.weatherapptest.data.City;
import com.demo.weatherapptest.pojo.WeatherResponse;
import com.demo.weatherapptest.utils.WeatherResponseUtils;

import io.reactivex.disposables.CompositeDisposable;

public class DetailActivity extends AppCompatActivity {

    private CompositeDisposable disposables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        disposables = new CompositeDisposable();

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra("city")) {
            finish();
        } else {
            City city = intent.getParcelableExtra("city");
            disposables.add(WeatherResponseUtils.loadWeather(city)
                    .subscribe(
                            weatherResponse -> setWeatherView(weatherResponse),
                            throwable -> {
                                Toast.makeText(DetailActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                            })
            );
        }
    }

    private void setWeatherView(WeatherResponse weatherResponse) {

    }

    @Override
    protected void onDestroy() {
        disposables.dispose();
        super.onDestroy();
    }
}