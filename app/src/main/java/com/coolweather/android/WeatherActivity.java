package com.coolweather.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coolweather.android.gson.Forecast;
import com.coolweather.android.gson.Lifestyle;
import com.coolweather.android.gson.Weather;
import com.coolweather.android.service.AutoUpdateService;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;

    public SwipeRefreshLayout swipeRefresh;

    private ScrollView weatherLayout;

    private Button navButton;
    private Button localButton;
    private Button searchButton;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    private TextView windText;

    private ImageView bingPicImg;
    private ImageView degree_code;
    private String mWeatherId;
    private String lat;
    private String lon;
    View apilayout;
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
        // 初始化各控件
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);
        localButton = (Button) findViewById(R.id.local_button);
        searchButton = (Button)findViewById(R.id.search_button);
        windText = (TextView) findViewById(R.id.windText);
        degree_code = (ImageView) findViewById(R.id.degree_code);
        apilayout = findViewById(R.id.apilayout);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        final Weather weather;

        if (weatherString != null) {
//             有缓存时直接解析天气数据
            String a = getIntent().getStringExtra("position");
            //点击地位
            if (a.equals("2")){
                lat = getIntent().getStringExtra("lat");
                lon = getIntent().getStringExtra("lon");
                weatherLayout.setVisibility(View.INVISIBLE);
                String weatherUrl = "https://free-api.heweather.com/s6/weather?key=31404c2b55de4f46a157f691d73feecc&location="+lat+","+lon;
                requestWeatherbylat(weatherUrl);
            }
            else if (a.equals("3")){
                String cityname = getIntent().getStringExtra("cityname");
                weatherLayout.setVisibility(View.INVISIBLE);
                String weatherUrl = "https://free-api.heweather.com/s6/weather?key=31404c2b55de4f46a157f691d73feecc&location="+cityname;
                requestWeatherbylat(weatherUrl);
            }
            else{
                if (Utility.handleWeatherResponsebyid(weatherString)==null){
                    weather = Utility.handleWeatherResponse(weatherString);
                }else{
                    weather = Utility.handleWeatherResponsebyid(weatherString);
                }
                //经纬度
                if (weather.basic.cityId!=null && weather.basic.weatherId==null ){
                    mWeatherId = weather.basic.cityId;
                    Log.d("uuuu","ppp");
                    showWeatherInfobylat(weather);
                }else {
                    Log.d("vvv","xx");
                    mWeatherId = weather.basic.weatherId;
                    showWeatherInfobycityid(weather);
                }
            }
        } else {
//             无缓存时去服务器查询天气
            mWeatherId = getIntent().getStringExtra("weather_id");

            if (getIntent().getStringExtra("weather_id")!=null) {
                weatherLayout.setVisibility(View.INVISIBLE);
                requestWeatherbycityid(mWeatherId);
            }else {

                lat = getIntent().getStringExtra("lat");
                lon = getIntent().getStringExtra("lon");
                weatherLayout.setVisibility(View.INVISIBLE);
                Log.d("44444","梵蒂冈跟不上");
                String weatherUrl = "https://free-api.heweather.com/s6/weather?key=31404c2b55de4f46a157f691d73feecc&location="+lat+","+lon;
                requestWeatherbylat(weatherUrl);
            }
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeatherbycityid(mWeatherId);
            }
        });
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        localButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherActivity.this,LocalActivity.class);
                startActivity(intent);
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
        String bingPic = prefs.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }
    }
    /**
     * 根据经纬度请求城市天气信息。
     */
    public void requestWeatherbylat(final String weatherUrl) {

        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            mWeatherId = weather.basic.cityId;
                            showWeatherInfobylat(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败3", Toast.LENGTH_SHORT).show();
                        }
//                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败4", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }
    /**
     * 根据天气id请求城市天气信息。
     */
    public void requestWeatherbycityid(final String weatherId) {
        //ba9079704cc44512bb3af201ef10af15
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=31404c2b55de4f46a157f691d73feecc";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponsebyid(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            mWeatherId = weather.basic.weatherId;
                            showWeatherInfobycityid(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败1", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败2", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }

    /**
     * 根据经纬度处理并展示Weather实体类中的数据。
     */
    private void showWeatherInfobylat(Weather weather) {
        String cityName = weather.basic.location;//*
        String updateTime = weather.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.wcode;
        String wind = weather.now.wind_dir +" "+ weather.now.wind_sc+"级";
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        windText.setText(wind);
        forecastLayout.removeAllViews();
        //天气图标
        String name = "p"+weather.now.cond_code;
        int resID = getResources().getIdentifier(name, "drawable", "com.coolweather.android");
        degree_code.setImageDrawable(getResources().getDrawable(resID));

        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            ImageView pic_img = (ImageView) view.findViewById(R.id.pic_img);
            dateText.setText(forecast.date);
            infoText.setText(forecast.cond_txt_d);
            maxText.setText(forecast.tmp_max+ "℃");
            minText.setText(forecast.tmp_min+ "℃");
            pic_img.setImageDrawable(getResources().getDrawable(R.drawable.local));
            //天气图片
            String pname = "p"+ forecast.cond_code_d;
            int resID1 = getResources().getIdentifier(pname, "drawable", "com.coolweather.android");
            pic_img.setImageDrawable(getResources().getDrawable(resID1));
            forecastLayout.addView(view);
        }
        //设置空气质量不可见，因为没有数据
        apilayout.setVisibility(View.GONE);


        String comfort = "舒适度：" + weather.lifestyleList.get(0).txt;
        String carWash = "洗车指数：" + weather.lifestyleList.get(6).txt;
        String sport = "运行建议：" + weather.lifestyleList.get(3).txt;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }
    /**
     * 根据cityid处理并展示Weather实体类中的数据。
     */
    private void showWeatherInfobycityid(Weather weather) {

        apilayout.setVisibility(View.VISIBLE);
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        String wind = weather.now.wind_dir  +" "+  weather.now.wind_sc+"级";
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        windText.setText(wind);
        //天气图标
        String name = "p"+weather.now.cond_code;
        int resID = getResources().getIdentifier(name, "drawable", "com.coolweather.android");
        degree_code.setImageDrawable(getResources().getDrawable(resID));

        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max+ "℃");
            minText.setText(forecast.temperature.min+ "℃");
            forecastLayout.addView(view);
        }
        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运行建议：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
//        Intent intent = new Intent(this, AutoUpdateService.class);
//        startService(intent);
    }
    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
}
