package com.ibm.myweather.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.ibm.myweather.db.City;
import com.ibm.myweather.db.County;
import com.ibm.myweather.db.Province;
import com.ibm.myweather.gson.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 刘旭 on 2016/12/31.
 */

public class Utility {
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(jsonObject.getString("name"));
                    province.setProvinceCode(jsonObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCityResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    City city = new City();
                    city.setCityName(jsonObject.getString("name"));
                    city.setCityCode(jsonObject.getInt("id"));
                    city.setprovinceId(provinceId);
                    city.save();
                }
                    return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCountyResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(jsonObject.getString("name"));
                    county.setWeatherId(jsonObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                    return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Weather handleWeatherResponse(String response){
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
                String weatherContent = jsonArray.getJSONObject(0).toString();
                Weather weather = new Gson().fromJson(weatherContent, Weather.class);
                return weather;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
