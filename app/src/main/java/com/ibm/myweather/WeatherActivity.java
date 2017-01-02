package com.ibm.myweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ibm.myweather.gson.Forecast;
import com.ibm.myweather.gson.Weather;
import com.ibm.myweather.utils.HttpUtil;
import com.ibm.myweather.utils.Utility;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_update_time)
    TextView tvUpdateTime;
    @Bind(R.id.tv_degree_now)
    TextView tvDegreeNow;
    @Bind(R.id.tv_info_now)
    TextView tvInfoNow;
    @Bind(R.id.ll_forecast)
    LinearLayout llForecast;
    @Bind(R.id.tv_aqi)
    TextView tvAqi;
    @Bind(R.id.tv_pm25)
    TextView tvPm25;
    @Bind(R.id.tv_comfort)
    TextView tvComfort;
    @Bind(R.id.tv_car_wash)
    TextView tvCarWash;
    @Bind(R.id.tv_sport)
    TextView tvSport;
    @Bind(R.id.sv_weather)
    ScrollView svWeather;
    @Bind(R.id.iv_backgeround)
    ImageView ivBackgeround;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.btn_show_menu)
    Button btnShowMenu;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private String mWeather_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);

        btnShowMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = preferences.getString("weather", null);
        if (!TextUtils.isEmpty(weatherString)) {
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeather_id = weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {
            svWeather.setVisibility(View.INVISIBLE);
            Intent intent = getIntent();
            mWeather_id = intent.getStringExtra("weather_id");
            requestWeather(mWeather_id);
        }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeather_id);
            }
        });

        String bing_pic = preferences.getString("bing_pic", null);
        if (bing_pic != null) {
            Glide.with(this).load(bing_pic).into(ivBackgeround);
        } else {
            loadBingPic();
        }

    }

    private void loadBingPic() {
        final String requestBing = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBing, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bing_pic = response.body().string();
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                edit.putString("bing_pic", bing_pic);
                edit.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bing_pic).into(ivBackgeround);
                    }
                });
            }
        });
    }

    public void requestWeather(final String weather_id) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weather_id + "&key=bc0418b57b2d4918819d3974ac1285d9";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {

                            showWeatherInfo(weather);
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText).apply();
                            mWeather_id=weather.basic.weatherId;
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败了", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }

    private void showWeatherInfo(Weather weather) {
        tvTitle.setText(weather.basic.cityName);
        tvUpdateTime.setText(weather.basic.update.updateTime.split(" ")[1]);
        tvDegreeNow.setText(weather.now.tmp+"℃");
        tvInfoNow.setText(weather.now.cond.txt);
        llForecast.removeAllViews();
        for (Forecast forecast : weather.daily_forecast) {
            View view = LayoutInflater.from(this).inflate(R.layout.foreast_item, llForecast, false);
            TextView tvData = (TextView) view.findViewById(R.id.tv_data_forecast);
            TextView tvInfo = (TextView) view.findViewById(R.id.tv_info_forecast);
            TextView tvMax = (TextView) view.findViewById(R.id.tv_max_forecast);
            TextView tvMin = (TextView) view.findViewById(R.id.tv_min_forecast);
            tvData.setText(forecast.date);
            tvInfo.setText(forecast.cond.text_d);
            tvMax.setText(forecast.tmp.max);
            tvMin.setText(forecast.tmp.min);
            llForecast.addView(view);
        }
        if (weather.aqi != null) {
            tvAqi.setText(weather.aqi.city.aqi);
            tvPm25.setText(weather.aqi.city.pm25);
        }
        tvComfort.setText(weather.suggestion.comf.txt);
        tvCarWash.setText(weather.suggestion.cw.txt);
        tvSport.setText(weather.suggestion.sport.txt);

        svWeather.setVisibility(View.VISIBLE);

        Intent intent = new Intent(this, AutoDateService.class);
        startService(intent);
    }
}
