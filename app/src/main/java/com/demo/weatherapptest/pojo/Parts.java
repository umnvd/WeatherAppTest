package com.demo.weatherapptest.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Parts {

    @SerializedName("night")
    @Expose
    private Part night;
    @SerializedName("morning")
    @Expose
    private Part morning;
    @SerializedName("day")
    @Expose
    private Part day;
    @SerializedName("evening")
    @Expose
    private Part evening;

    public Part getNight() {
        return night;
    }

    public void setNight(Part night) {
        this.night = night;
    }

    public Part getMorning() {
        return morning;
    }

    public void setMorning(Part morning) {
        this.morning = morning;
    }

    public Part getDay() {
        return day;
    }

    public void setDay(Part day) {
        this.day = day;
    }

    public Part getEvening() {
        return evening;
    }

    public void setEvening(Part evening) {
        this.evening = evening;
    }

}