package com.demo.weatherapptest.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Forecast {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("date_ts")
    @Expose
    private int dateTs;
    @SerializedName("parts")
    @Expose
    private Parts parts;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDateTs() {
        return dateTs;
    }

    public void setDateTs(int dateTs) {
        this.dateTs = dateTs;
    }

    public Parts getParts() {
        return parts;
    }

    public void setParts(Parts parts) {
        this.parts = parts;
    }

}