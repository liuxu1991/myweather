package com.ibm.myweather.gson;

/**
 * Created by 刘旭 on 2017/1/1.
 */

public class AQI {
    public AQICity city;

    public class AQICity {
        public String aqi;
        public String pm25;
    }
}
