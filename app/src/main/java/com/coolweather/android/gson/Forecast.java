package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

public class Forecast {

    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature {

        public String max;

        public String min;

    }

    public class More {

        @SerializedName("txt_d")
        public String info;

    }

    @SerializedName("tmp_max")
    public String tmp_max;
    @SerializedName("tmp_min")
    public String tmp_min;

    @SerializedName("cond_txt_d")
    public String cond_txt_d;



    @SerializedName("cond_code_d")
    public String cond_code_d;
}
