package com.xtracker.android.rest;

import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by ASUS on 05/03/2015.
 */
public interface ApiService {

    @GET("/app/tracks/getTrack")
    public TrackParcel getTrack();

    @FormUrlEncoded
    @POST("/app/tracks/getTrack")
    public void sendTrack(TrackParcel trackParcel);



}
