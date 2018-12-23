package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

public class Basic {

    @SerializedName("city")
    public String cityName;
    @SerializedName("location")
    public String location;

    @SerializedName("id")
    public String weatherId;
    @SerializedName("cid")
    public String cityId;

    public Update update;

    public class Update {

        @SerializedName("loc")
        public String updateTime;

    }

}
