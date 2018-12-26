package com.coolweather.android;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;

import java.util.ArrayList;
import java.util.List;

public class LocalActivity extends AppCompatActivity {
    public LocationClient mLocationClient;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new LocalActivity.MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());
        initData();
        requestLocation();
    }

    private void initData() {
        showProgressDialog("自动定位中...");
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pDialog.dismiss(); //对话框关闭
            }
        }).start();
    }

    private void showProgressDialog(String title) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage(title + "...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.show();
    }

    private void requestLocation() {
        mLocationClient.start();
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(final BDLocation location) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(LocalActivity.this, WeatherActivity.class);
                    String lat = location.getLatitude() + "";
                    String lon = location.getLongitude() + "";
                    intent.putExtra("lat", lat);
                    intent.putExtra("lon", lon);
                    intent.putExtra("position", "2");
                    startActivity(intent);
                }
            });
        }
    }
}