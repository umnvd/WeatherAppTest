package com.demo.weatherapptest.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Part {

    @SerializedName("temp_min")
    @Expose
    private int tempMin;
    @SerializedName("temp_max")
    @Expose
    private int tempMax;
    @SerializedName("temp_avg")
    @Expose
    private int tempAvg;
    @SerializedName("feels_like")
    @Expose
    private int feelsLike;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("wind_speed")
    @Expose
    private double windSpeed;

    public int getTempMin() {
        return tempMin;
    }

    public void setTempMin(int tempMin) {
        this.tempMin = tempMin;
    }

    public int getTempMax() {
        return tempMax;
    }

    public void setTempMax(int tempMax) {
        this.tempMax = tempMax;
    }

    public int getTempAvg() {
        return tempAvg;
    }

    public void setTempAvg(int tempAvg) {
        this.tempAvg = tempAvg;
    }

    public int getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(int feelsLike) {
        this.feelsLike = feelsLike;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

}