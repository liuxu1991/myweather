package com.ibm.myweather.gson;

import java.util.List;

/**
 * Created by 刘旭 on 2017/1/1.
 */

public class Weather {
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;
    public List<Forecast> daily_forecast;
}
