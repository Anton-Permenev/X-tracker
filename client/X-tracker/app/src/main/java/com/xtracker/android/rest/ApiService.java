package com.xtracker.android.rest;


import com.xtracker.android.objects.Point;
import com.xtracker.android.objects.Track;

import java.sql.Timestamp;
import java.util.List;


import com.xtracker.android.objects.Track;

import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by ASUS on 05/03/2015.
 */
public interface ApiService {
    @GET("users/tracks/{user_id}")
    public List<Track> getTracksList(long user_id,String access_token) {

    }

    @POST("users/tracks/{user_id}")
    public void newUser(String email,String access_token){

    }

    @GET("users/tracks/{user_id}/{track_id}")
    public Track getTrack(long track_id,String access_token) {

    }


    @POST("users/tracks/{user_id}/{track_id}")
    public void addTrack(List<Point> points,Timestamp time_start,Timestamp time_end,String access_token) {

    }

    @POST("users/tracks/{user_id}/{track_id}")
    public void editTrack(long track_id,List<Point> points,Timestamp time_start,Timestamp time_end,String access_token){

    }

    @POST("users/tracks/{user_id}/{track_id}")
    public void deleteTrack(long track_id,String access_token){


    }




}
