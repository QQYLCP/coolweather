package com.coolweather.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coolweather.android.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {

    private Button seachButton;
    private EditText seachEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        TextView title=(TextView)findViewById(R.id.t1);
        title.setShadowLayer(10F,11F,5F,Color.BLACK);
        seachButton = (Button)findViewById(R.id.search_bt);
        seachEdit = (EditText) findViewById(R.id.search_tv);
        seachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this,WeatherActivity.class);
                intent.putExtra("cityname",seachEdit.getText().toString());
                intent.putExtra("position","3");
                startActivity(intent);
            }
        });
    }


}
