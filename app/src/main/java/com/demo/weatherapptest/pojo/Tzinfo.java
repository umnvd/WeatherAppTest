package com.demo.weatherapptest.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tzinfo {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("abbr")
    @Expose
    private String abbr;
    @SerializedName("offset")
    @Expose
    private long offset;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

}