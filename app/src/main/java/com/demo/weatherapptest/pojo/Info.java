package com.demo.weatherapptest.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Info {

    private String name;
    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("lon")
    @Expose
    private double lon;
    @SerializedName("tzinfo")
    @Expose
    private Tzinfo tzinfo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public Tzinfo getTzinfo() {
        return tzinfo;
    }

    public void setTzinfo(Tzinfo tzinfo) {
        this.tzinfo = tzinfo;
    }
}