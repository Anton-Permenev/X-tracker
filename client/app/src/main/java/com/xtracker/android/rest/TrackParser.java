package com.xtracker.android.rest;

import com.google.gson.annotations.SerializedName;

public class TrackParser {

    @SerializedName("longitude")
    private Float longitude;

    @SerializedName("latitude")
    private Float latitude;

    @SerializedName("speed")
    private Float speed;
}
