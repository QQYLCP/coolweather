package com.coolweather.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SearchActivity extends AppCompatActivity {

    private Button seachButton;
    private EditText seachEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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
