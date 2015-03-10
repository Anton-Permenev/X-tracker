package com.xtracker.android.rest;

import com.xtracker.android.objects.Track;

import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by ASUS on 05/03/2015.
 */
public interface ApiService {

    @GET("/app/tracks/getTrack")
    public TrackParcer getTrack();

    public Track

    @FormUrlEncoded
    @POST("/app/tracks/getTrack")
    public void sendTrack(TrackParcer trackParcer);



}
