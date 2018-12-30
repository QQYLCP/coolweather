package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More {

        @SerializedName("txt")
        public String info;

    }

    @SerializedName("cond_txt")
    public String wcode;
    @SerializedName("wind_dir")
    public String wind_dir;
    @SerializedName("wind_sc")
    public String wind_sc;
    @SerializedName("cond_code")
    public String cond_code;
}
